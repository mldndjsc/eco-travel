package org.raflab.rezervacijeservice.controller;

import jakarta.validation.Valid;
import org.raflab.rezervacijeservice.dto.request.AddOnItemsRequestDto;
import org.raflab.rezervacijeservice.dto.request.BookingsRequestDto;
import org.raflab.rezervacijeservice.dto.response.AddOnItemsResponseDto;
import org.raflab.rezervacijeservice.dto.response.BookingsResponseDto;
import org.raflab.rezervacijeservice.service.BookingsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//@CrossOrigin(origins = "http://localhost:8080")
@RestController
@RequestMapping("/api/rezervacije")
@RefreshScope
public class BookingsController {

    @Autowired
    BookingsService bookingsService;

    @PostMapping(path = "/bookings/add")
    public void addBoking(@RequestBody @Valid BookingsRequestDto bookingsRequestDto){
        bookingsService.save(bookingsRequestDto);
    }
    @PutMapping(path = "/bookings/update/{id}")
    public void updateBookingById(@RequestBody @Valid BookingsRequestDto bookingsRequestDto,@PathVariable Long id){
        bookingsService.updateById(bookingsRequestDto,id);
    }
    @PostMapping(path = "/bookings/{id}/cancel")
    public Double cancelBookingAndRefundById(@PathVariable Long id){
        return bookingsService.cancelBookingAndRefund(id);
    }
    @GetMapping(path = "/bookings/{id}")
    public BookingsResponseDto findBookingById(@PathVariable Long id){
        return bookingsService.findById(id);
    }
    @GetMapping(path = "/bookings/all")
    public List<BookingsResponseDto> getAllBookings(){
        return bookingsService.findAll();
    }
    @DeleteMapping(path = "/bookings/delete/{id}")
    public void deleteBookingById(@PathVariable Long id) {
        bookingsService.deleteById(id);
    }

    @PutMapping("/bookings/{id}/status")
    public void updateBookingStatus(@PathVariable Long id, @RequestParam String status){
        bookingsService.updateBookingStatus(id,status);
    };

    @PutMapping(path = "/bookings/{id}/cancel-unpaid")
    public void cancelBookingDueToNonPayment(@PathVariable Long id){
        bookingsService.cancelBookingDueToNonPayment(id);
    }

    @GetMapping(path = "/bookings/search")
    public List<BookingsResponseDto> searchBookings(
            @RequestParam(name = "guestName") String guestName,
            @RequestParam(name = "status", defaultValue = "CONFIRMED") String status) {

        return bookingsService.searchBookingsByGuestAndStatus(guestName, status);
    }
}
