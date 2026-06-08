package com.ecotravel.vodici.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.MDC;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class TraceIdFilter extends OncePerRequestFilter {

    private static final String TRACE_ID_HEADER = "X-Trace-Id";
    private static final String W3C_TRACEPARENT_HEADER = "traceparent";
    private static final String AUTHENTICATED_USER_HEADER = "X-Authenticated-User";
    private static final String USER_ROLE_HEADER = "X-User-Role";

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String traceId = resolveTraceId(request);
        String authenticatedUser = Optional.ofNullable(request.getHeader(AUTHENTICATED_USER_HEADER))
                .filter(value -> !value.isBlank())
                .orElse("anonymous");
        String userRole = Optional.ofNullable(request.getHeader(USER_ROLE_HEADER))
                .filter(value -> !value.isBlank())
                .orElse("unknown");

        MDC.put("traceId", traceId);
        MDC.put("authenticatedUser", authenticatedUser);
        MDC.put("userRole", userRole);

        request.setAttribute(TRACE_ID_HEADER, traceId);
        response.setHeader(TRACE_ID_HEADER, traceId);

        try {
            filterChain.doFilter(request, response);
        } finally {
            MDC.clear();
        }
    }

    private String resolveTraceId(HttpServletRequest request) {
        return Optional.ofNullable(extractTraceIdFromTraceparent(request.getHeader(W3C_TRACEPARENT_HEADER)))
                .or(() -> Optional.ofNullable(request.getHeader(TRACE_ID_HEADER)).filter(value -> !value.isBlank()))
                .orElse("N/A");
    }

    private String extractTraceIdFromTraceparent(String traceparent) {
        if (traceparent == null || traceparent.isBlank()) {
            return null;
        }
        String[] parts = traceparent.split("-");
        if (parts.length >= 2 && parts[1].matches("[0-9a-fA-F]{32}")) {
            return parts[1];
        }
        return null;
    }
}
