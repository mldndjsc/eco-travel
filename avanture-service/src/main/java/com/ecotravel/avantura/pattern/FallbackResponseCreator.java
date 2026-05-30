package com.ecotravel.avantura.pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class FallbackResponseCreator<T> {

    protected final Logger log = LoggerFactory.getLogger(getClass());

    public abstract T createFallbackResponse();

    public T getFallbackResponse(Exception e) {
        log.warn("[FALLBACK] Eksterni servis nije dostupan. Razlog: {}. Vracam podrazumevani odgovor.", e.getMessage());
        return createFallbackResponse();
    }
}
