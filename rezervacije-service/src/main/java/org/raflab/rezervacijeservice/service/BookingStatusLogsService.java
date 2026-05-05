package org.raflab.rezervacijeservice.service;

import org.raflab.rezervacijeservice.converter.BookingStatusLogsConverter;
import org.raflab.rezervacijeservice.converter.BookingsConverter;
import org.raflab.rezervacijeservice.dto.request.BookingStatusLogsRequestDto;
import org.raflab.rezervacijeservice.dto.response.BookingsStatusLogsResponseDto;
import org.raflab.rezervacijeservice.model.BookingStatusLogs;
import org.raflab.rezervacijeservice.model.Bookings;
import org.raflab.rezervacijeservice.repository.BookingStatusLogsRepository;
import org.raflab.rezervacijeservice.repository.BookingsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class BookingStatusLogsService {

    @Autowired
    BookingStatusLogsRepository bookingStatusLogsRepository;

    @Autowired
    BookingsRepository bookingsRepository;

    public void save(BookingStatusLogsRequestDto bookingStatusLogsRequestDto) {
        bookingStatusLogsRepository.save(BookingStatusLogsConverter.toBookingStatusLogs(bookingStatusLogsRequestDto));
    }

    public void updateById(BookingStatusLogsRequestDto bookingStatusLogsRequestDto, Long id) {
        if (bookingStatusLogsRepository.existsById(id)) {
            BookingStatusLogs bookingsStatusLogs = bookingStatusLogsRepository.findById(id).get();
            bookingsStatusLogs.setOldStatus(bookingStatusLogsRequestDto.getOldStatus());
            bookingsStatusLogs.setNewStatus(bookingStatusLogsRequestDto.getNewStatus());
            bookingsStatusLogs.setChangedAt(bookingStatusLogsRequestDto.getChangedAt());
            bookingsStatusLogs.setBooking(BookingsConverter.toBooking(bookingStatusLogsRequestDto.getBookingsRequestDto()));
            bookingStatusLogsRepository.save(bookingsStatusLogs);
        } else {
            bookingStatusLogsRepository.save(BookingStatusLogsConverter.toBookingStatusLogs(bookingStatusLogsRequestDto));
        }
    }

    public List<BookingsStatusLogsResponseDto> findAll() {
        return BookingStatusLogsConverter.toBookingResponseDtoList(bookingStatusLogsRepository.findAll());
    }

    public BookingsStatusLogsResponseDto findById(Long id) {
        return BookingStatusLogsConverter.toBookingStatusLogsResponseDto(bookingStatusLogsRepository.findById(id).get());
    }

    public void deleteById(Long id) {
        bookingStatusLogsRepository.deleteById(id);
    }

    public void logStatusChange(Long bookingId, String oldStatus, String newStatus) {
        Bookings booking = bookingsRepository.findById(bookingId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Booking sa ID " + bookingId + " ne postoji."));

        BookingStatusLogs log = new BookingStatusLogs();
        log.setBooking(booking);
        log.setOldStatus(oldStatus);
        log.setNewStatus(newStatus);
        log.setChangedAt(LocalDateTime.now());

        bookingStatusLogsRepository.save(log);
    }
}
