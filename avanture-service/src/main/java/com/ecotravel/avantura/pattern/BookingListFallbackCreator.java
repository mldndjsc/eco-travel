package com.ecotravel.avantura.pattern;

import com.ecotravel.avantura.dto.external.BookingExternalDTO;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
public class BookingListFallbackCreator extends FallbackResponseCreator<List<BookingExternalDTO>> {

    @Override
    public List<BookingExternalDTO> createFallbackResponse() {
        return Collections.emptyList();
    }
}
