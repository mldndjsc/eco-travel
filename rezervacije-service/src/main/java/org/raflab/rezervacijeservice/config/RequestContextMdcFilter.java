package org.raflab.rezervacijeservice.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

@Component
public class RequestContextMdcFilter extends OncePerRequestFilter {

    private static final String TRACE_ID_HEADER = "X-Trace-Id";
    private static final String AUTHENTICATED_USER_HEADER = "X-Authenticated-User";
    private static final String USER_ROLE_HEADER = "X-User-Role";

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String traceId = Optional.ofNullable(request.getHeader(TRACE_ID_HEADER))
                .filter(value -> !value.isBlank())
                .orElse("N/A");
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
}
