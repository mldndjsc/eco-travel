package com.ecotravel.avantura.config;

import com.ecotravel.avantura.exception.BusinessException;
import com.ecotravel.avantura.exception.ResourceNotFoundException;
import feign.Response;
import feign.codec.ErrorDecoder;

public class FeignErrorDecoder implements ErrorDecoder {

    private final ErrorDecoder defaultDecoder = new Default();

    @Override
    public Exception decode(String methodKey, Response response) {
        return switch (response.status()) {
            case 400 -> new BusinessException("Los zahtev ka eksternom servisu [" + methodKey + "]");
            case 404 -> new ResourceNotFoundException("Resurs nije pronadjen u eksternom servisu [" + methodKey + "]");
            case 503 -> new BusinessException("Eksterni servis nije dostupan [" + methodKey + "]");
            default -> defaultDecoder.decode(methodKey, response);
        };
    }
}
