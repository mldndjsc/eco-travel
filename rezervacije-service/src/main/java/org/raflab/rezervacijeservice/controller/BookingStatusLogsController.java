package org.raflab.rezervacijeservice.controller;

import jakarta.validation.Valid;
import org.raflab.rezervacijeservice.dto.request.BookingStatusLogsRequestDto;
import org.raflab.rezervacijeservice.dto.request.BookingsRequestDto;
import org.raflab.rezervacijeservice.dto.response.BookingsResponseDto;
import org.raflab.rezervacijeservice.dto.response.BookingsStatusLogsResponseDto;
import org.raflab.rezervacijeservice.service.BookingStatusLogsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//@CrossOrigin(origins = "http://localhost:8080")
@RestController
@RequestMapping("/api/rezervacije")
@RefreshScope
public class BookingStatusLogsController {

    @Autowired
    BookingStatusLogsService bookingStatusLogsService;

    @PostMapping(path = "/bookingstatuslogs/add")
    public void addBookingStatusLogs(@RequestBody @Valid BookingStatusLogsRequestDto bookingStatusLogsRequestDto){
        bookingStatusLogsService.save(bookingStatusLogsRequestDto);
    }

    @PutMapping(path = "/bookingstatuslogs/update/{id}")
    public void updateBookingStatusLogs(@RequestBody @Valid BookingStatusLogsRequestDto bookingStatusLogsRequestDto,@PathVariable Long id){
        bookingStatusLogsService.updateById(bookingStatusLogsRequestDto,id);
    }
    @GetMapping(path = "/bookingstatuslogs/{id}")
    public BookingsStatusLogsResponseDto findBookingStatusLogsById(@PathVariable Long id){
        return bookingStatusLogsService.findById(id);
    }
    @GetMapping(path = "/bookingstatuslogs/all")
    public List<BookingsStatusLogsResponseDto> getAllBookingStatusLogs(){
        return bookingStatusLogsService.findAll();
    }
    @DeleteMapping(path = "/bookingstatuslogs/delete/{id}")
    public void deleteBookingStatusLogsById(@PathVariable Long id) {
        bookingStatusLogsService.deleteById(id);
    }
}
