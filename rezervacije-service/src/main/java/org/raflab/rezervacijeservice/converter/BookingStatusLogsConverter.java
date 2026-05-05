package org.raflab.rezervacijeservice.converter;

import org.raflab.rezervacijeservice.dto.request.BookingStatusLogsRequestDto;
import org.raflab.rezervacijeservice.dto.request.BookingsRequestDto;
import org.raflab.rezervacijeservice.dto.response.BookingsResponseDto;
import org.raflab.rezervacijeservice.dto.response.BookingsStatusLogsResponseDto;
import org.raflab.rezervacijeservice.model.BookingStatusLogs;
import org.raflab.rezervacijeservice.model.Bookings;

import java.util.ArrayList;
import java.util.List;

public class BookingStatusLogsConverter {

    public static BookingStatusLogs toBookingStatusLogs(BookingStatusLogsRequestDto bookingStatusLogsRequestDto) {

        BookingStatusLogs bookingStatusLogs = new BookingStatusLogs();
        bookingStatusLogs.setBooking(BookingsConverter.toBooking(bookingStatusLogsRequestDto.getBookingsRequestDto()));
        bookingStatusLogs.setOldStatus(bookingStatusLogsRequestDto.getOldStatus());
        bookingStatusLogs.setNewStatus(bookingStatusLogsRequestDto.getNewStatus());
        bookingStatusLogs.setChangedAt(bookingStatusLogsRequestDto.getChangedAt());
        return bookingStatusLogs;
    }

    public static BookingStatusLogs updateToBookingStatusLogs(BookingStatusLogsRequestDto bookingStatusLogsRequestDto,Long id) {

        BookingStatusLogs bookingStatusLogs = new BookingStatusLogs();
        bookingStatusLogs.setId(id);
        bookingStatusLogs.setBooking(BookingsConverter.toBooking(bookingStatusLogsRequestDto.getBookingsRequestDto()));
        bookingStatusLogs.setOldStatus(bookingStatusLogsRequestDto.getOldStatus());
        bookingStatusLogs.setNewStatus(bookingStatusLogsRequestDto.getNewStatus());
        bookingStatusLogs.setChangedAt(bookingStatusLogsRequestDto.getChangedAt());
        return bookingStatusLogs;
    }

    public static BookingsStatusLogsResponseDto toBookingStatusLogsResponseDto(BookingStatusLogs bookingStatusLogs) {
        BookingsStatusLogsResponseDto bookingsStatusLogsResponseDto = new BookingsStatusLogsResponseDto();
        bookingsStatusLogsResponseDto.setBookingsResponseDto(BookingsConverter.toBookingResponseDto(bookingStatusLogs.getBooking()));
        bookingsStatusLogsResponseDto.setOldStatus(bookingStatusLogs.getOldStatus());
        bookingsStatusLogsResponseDto.setNewStatus(bookingStatusLogs.getNewStatus());


        return bookingsStatusLogsResponseDto;
    }

    public static List<BookingsStatusLogsResponseDto> toBookingResponseDtoList(List<BookingStatusLogs> bookingStatusLogsList) {
        List<BookingsStatusLogsResponseDto> bookingsStatusLogsResponseDtoList = new ArrayList<>();
        for (BookingStatusLogs bookingStatusLogs : bookingStatusLogsList) {
            bookingsStatusLogsResponseDtoList.add(toBookingStatusLogsResponseDto(bookingStatusLogs));
        }
        return bookingsStatusLogsResponseDtoList;
    }
}
