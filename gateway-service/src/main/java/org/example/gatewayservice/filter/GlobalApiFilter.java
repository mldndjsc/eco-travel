package org.example.gatewayservice.filter;

import org.example.gatewayservice.dto.AuthResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;

@Component
public class GlobalApiFilter implements GlobalFilter, Ordered {

    private static final Logger log = LoggerFactory.getLogger(GlobalApiFilter.class);
    private static final String TRACE_ID_HEADER = "X-Trace-Id";
    private static final String W3C_TRACEPARENT_HEADER = "traceparent";
    private static final String RESPONSE_TIME_HEADER = "X-Internal-Duration-Ms";
    private static final String ECO_STATUS_HEADER = "X-Eco-Status";
    private final WebClient.Builder webClientBuilder;
    private final String authServiceUrl;

    public GlobalApiFilter(
            WebClient.Builder webClientBuilder,
            @Value("${AUTH_SERVICE_URL:http://localhost:8086}/api/auth/validate") String authServiceUrl
    ) {
        this.webClientBuilder = webClientBuilder;
        this.authServiceUrl = authServiceUrl;
    }

    private Mono<Void> continueChain(ServerWebExchange exchange, GatewayFilterChain chain, String traceId, long startTime) {
        exchange.getResponse().beforeCommit(() -> {
            long duration = System.currentTimeMillis() - startTime;
            exchange.getResponse().getHeaders().set(TRACE_ID_HEADER, traceId);
            exchange.getResponse().getHeaders().set(RESPONSE_TIME_HEADER, String.valueOf(duration));
            exchange.getResponse().getHeaders().set(ECO_STATUS_HEADER, "GREEN-OPTIMIZED");
            log.info(
                    "Gateway handled {} {} with status {} in {} ms [traceId={}]",
                    exchange.getRequest().getMethod(),
                    exchange.getRequest().getURI().getPath(),
                    exchange.getResponse().getStatusCode(),
                    duration,
                    traceId
            );
            return Mono.empty();
        });

        return chain.filter(exchange);
    }

    private Mono<Void> rejectRequest(ServerWebExchange exchange, HttpStatus status, String message, String traceId, long startTime) {
        long duration = System.currentTimeMillis() - startTime;

        exchange.getResponse().setStatusCode(status);
        exchange.getResponse().getHeaders().set(TRACE_ID_HEADER, traceId);
        exchange.getResponse().getHeaders().set(RESPONSE_TIME_HEADER, String.valueOf(duration));
        exchange.getResponse().getHeaders().set(ECO_STATUS_HEADER, "GREEN-OPTIMIZED");
        exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);

        log.warn(
                "Gateway rejected {} {} with status {} in {} ms [traceId={}, reason={}]",
                exchange.getRequest().getMethod(),
                exchange.getRequest().getURI().getPath(),
                status.value(),
                duration,
                traceId,
                message
        );

        String responseBody = String.format(
                "{\"timestamp\":\"%s\",\"status\":%d,\"error\":\"%s\",\"message\":\"%s\",\"traceId\":\"%s\",\"path\":\"%s\"}",
                java.time.LocalDateTime.now(),
                status.value(),
                escapeJson(status.name()),
                escapeJson(message),
                escapeJson(traceId),
                escapeJson(exchange.getRequest().getURI().getPath())
        );

        return exchange.getResponse().writeWith(
                Mono.just(exchange.getResponse().bufferFactory()
                        .wrap(responseBody.getBytes(java.nio.charset.StandardCharsets.UTF_8)))
        );
    }

    private String resolveTraceId(ServerWebExchange exchange) {
        String traceparent = exchange.getRequest().getHeaders().getFirst(W3C_TRACEPARENT_HEADER);
        String traceparentTraceId = extractTraceIdFromTraceparent(traceparent);
        if (traceparentTraceId != null) {
            return traceparentTraceId;
        }
        String existing = exchange.getRequest().getHeaders().getFirst(TRACE_ID_HEADER);
        if (existing != null && !existing.isBlank()) {
            String normalized = existing.replace("-", "");
            if (normalized.matches("[0-9a-fA-F]{32}")) {
                return normalized.toLowerCase();
            }
        }
        return newTraceId();
    }

    private String resolveTraceparent(ServerWebExchange exchange, String traceId) {
        String existing = exchange.getRequest().getHeaders().getFirst(W3C_TRACEPARENT_HEADER);
        if (extractTraceIdFromTraceparent(existing) != null) {
            return existing;
        }
        return "00-" + traceId + "-" + newSpanId() + "-01";
    }

    private String extractTraceIdFromTraceparent(String traceparent) {
        if (traceparent == null || traceparent.isBlank()) {
            return null;
        }
        String[] parts = traceparent.split("-");
        if (parts.length >= 2 && parts[1].matches("[0-9a-fA-F]{32}")) {
            return parts[1].toLowerCase();
        }
        return null;
    }

    private String newTraceId() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    private String newSpanId() {
        return UUID.randomUUID().toString().replace("-", "").substring(0, 16);
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String path = exchange.getRequest().getURI().getPath();
        HttpMethod method = exchange.getRequest().getMethod();
        String traceId = resolveTraceId(exchange);
        String traceparent = resolveTraceparent(exchange, traceId);
        long startTime = System.currentTimeMillis();
        ServerWebExchange traceAwareExchange = exchange.mutate()
                .request(builder -> builder
                        .header(TRACE_ID_HEADER, traceId)
                        .header(W3C_TRACEPARENT_HEADER, traceparent))
                .build();

        if (path.contains("/v3/api-docs") || path.contains("/swagger-ui") || path.contains("/auth") || path.contains("webjars")) {
            return continueChain(traceAwareExchange, chain, traceId, startTime);
        }

        String apiKey = exchange.getRequest().getHeaders().getFirst("X-API-KEY");
        if (apiKey == null || apiKey.isBlank()) {
            return rejectRequest(traceAwareExchange, HttpStatus.UNAUTHORIZED, "Missing X-API-KEY header", traceId, startTime);
        }

        return webClientBuilder.build().get()
                .uri(authServiceUrl)
                .header("X-API-KEY", apiKey)
                .header(TRACE_ID_HEADER, traceId)
                .header(W3C_TRACEPARENT_HEADER, traceparent)
                .retrieve()
                .onStatus(status -> status.is4xxClientError(), response -> Mono.error(new ResponseStatusException(HttpStatus.UNAUTHORIZED, "API key is not valid")))
                .onStatus(status -> status.is5xxServerError(), response -> Mono.error(new ResponseStatusException(HttpStatus.BAD_GATEWAY, "Auth service is unavailable")))
                .bodyToMono(AuthResponse.class)
                .flatMap(authResponse -> {

                    if (!hasPermission(authResponse.getRoles(), path, method)) {
                        return rejectRequest(traceAwareExchange, HttpStatus.FORBIDDEN, "User does not have permission for this route", traceId, startTime);
                    }

                    String userRolesStr = authResponse.getRoles() != null ? String.join(",", authResponse.getRoles()) : "";
                    ServerWebExchange mutatedExchange = traceAwareExchange.mutate()
                            .request(builder -> builder
                                    .header(TRACE_ID_HEADER, traceId)
                                    .header(W3C_TRACEPARENT_HEADER, traceparent)
                                    .header("X-User-Role", userRolesStr)
                                    .header("X-Authenticated-User", authResponse.getUsername()))
                            .build();

                    return continueChain(mutatedExchange, chain, traceId, startTime);
                })
                .onErrorResume(ResponseStatusException.class, ex -> rejectRequest(
                        traceAwareExchange,
                        HttpStatus.valueOf(ex.getStatusCode().value()),
                        ex.getReason() != null ? ex.getReason() : "Authorization failed",
                        traceId,
                        startTime
                ))
                .onErrorResume(ex -> rejectRequest(
                        traceAwareExchange,
                        HttpStatus.BAD_GATEWAY,
                        "Gateway could not validate the caller identity",
                        traceId,
                        startTime
                ));
    }

    private boolean hasPermission(List<String> roles, String path, HttpMethod method) {
        if (roles == null) {
            return false;
        }
        if (roles.contains("GUEST") && method != null && (method.matches("POST") || method.matches("PUT") || method.matches("DELETE"))) {
            return false;
        }
        if (path.contains("/admin") && !roles.contains("ADMIN")) {
            return false;
        }
        return true;
    }

    @Override
    public int getOrder() {
        return -1;
    }

    private String escapeJson(String value) {
        if (value == null) {
            return "";
        }
        return value.replace("\\", "\\\\").replace("\"", "\\\"");
    }
}
