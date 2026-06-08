package com.ecotravel.avantura.config;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.List;

public class FeignHeaderInterceptor implements RequestInterceptor {

    private static final List<String> PROPAGATED_HEADERS = List.of(
            "X-API-KEY",
            "X-Trace-Id",
            "X-User-Role",
            "X-Authenticated-User",
            "traceparent",
            "tracestate"
    );

    @Override
    public void apply(RequestTemplate template) {
        ServletRequestAttributes attributes =
            (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes == null) {
            return;
        }
        HttpServletRequest request = attributes.getRequest();
        for (String header : PROPAGATED_HEADERS) {
            String value = request.getHeader(header);
            if (value != null && !value.isBlank()) {
                template.header(header, value);
            }
        }
    }
}
