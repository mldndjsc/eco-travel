package org.raflab.rezervacijeservice.converter;

import org.raflab.rezervacijeservice.dto.request.AddOnItemsRequestDto;
import org.raflab.rezervacijeservice.dto.request.BookingsRequestDto;
import org.raflab.rezervacijeservice.dto.response.AddOnItemsResponseDto;
import org.raflab.rezervacijeservice.dto.response.BookingsResponseDto;
import org.raflab.rezervacijeservice.model.AddOnItems;
import org.raflab.rezervacijeservice.model.Bookings;

import java.util.ArrayList;
import java.util.List;

public class BookingsConverter {

    public static Bookings toBooking(BookingsRequestDto bookingsRequestDto) {

        Bookings bookings = new Bookings();
        bookings.setStartDate(bookingsRequestDto.getStartDate());
        bookings.setEndDate(bookingsRequestDto.getEndDate());
        bookings.setGuest(GuestsConverter.toGuests(bookingsRequestDto.getGuestsRequestDto()));
        bookings.setStatus(bookingsRequestDto.getStatus());
        bookings.setPolicy(CancellationPoliciesConverter.toCancellationPolicies(bookingsRequestDto.getPolicyRequestDto()));
        bookings.setUnitId(bookingsRequestDto.getUnitId());
        return bookings;
    }

    public static Bookings updateToBooking(BookingsRequestDto bookingsRequestDto,Long id) {

        Bookings bookings = new Bookings();
        bookings.setId(id);
        bookings.setStartDate(bookingsRequestDto.getStartDate());
        bookings.setEndDate(bookingsRequestDto.getEndDate());
        bookings.setGuest(GuestsConverter.toGuests(bookingsRequestDto.getGuestsRequestDto()));
        bookings.setStatus(bookingsRequestDto.getStatus());
        bookings.setPolicy(CancellationPoliciesConverter.toCancellationPolicies(bookingsRequestDto.getPolicyRequestDto()));
        bookings.setUnitId(bookingsRequestDto.getUnitId());

        return bookings;

    }

    public static BookingsResponseDto toBookingResponseDto(Bookings bookings) {
        return new BookingsResponseDto.BookingsResponseDtoBuilder()
                .unitId(bookings.getUnitId())
                .guestsResponseDto(GuestsConverter.toGuestsResponseDto(bookings.getGuest()))
                .policyResponseDto(CancellationPoliciesConverter.toCancellationPoliciesResponseDto(bookings.getPolicy()))
                .startDate(bookings.getStartDate())
                .endDate(bookings.getEndDate())
                .totalPrice(bookings.getTotalPrice())
                .status(bookings.getStatus())
                .build();
    }


    public static List<BookingsResponseDto> toBookingResponseDtoList(List<Bookings> bookingsList) {
        List<BookingsResponseDto> bookingsResponseDtoList = new ArrayList<>();
        for (Bookings bookings : bookingsList) {
            bookingsResponseDtoList.add(toBookingResponseDto(bookings));
        }
        return bookingsResponseDtoList;
    }
}
