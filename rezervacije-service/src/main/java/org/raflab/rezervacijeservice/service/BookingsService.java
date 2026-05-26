package org.raflab.rezervacijeservice.service;

import org.raflab.rezervacijeservice.converter.BookingsConverter;
import org.raflab.rezervacijeservice.converter.CancellationPoliciesConverter;
import org.raflab.rezervacijeservice.converter.GuestsConverter;
import org.raflab.rezervacijeservice.dto.request.BookingsRequestDto;
import org.raflab.rezervacijeservice.dto.response.BookingsResponseDto;
import org.raflab.rezervacijeservice.messaging.publisher.BookingMaintenancePublisher;
import org.raflab.rezervacijeservice.model.Bookings;
import org.raflab.rezervacijeservice.model.CancellationPolicies;
import org.raflab.rezervacijeservice.model.Guests;
import org.raflab.rezervacijeservice.observer.BookingPublisher;
import org.raflab.rezervacijeservice.observer.BookingSubscriber;
import org.raflab.rezervacijeservice.repository.BookingsRepository;
import org.raflab.rezervacijeservice.repository.CancellationPoliciesRepository;
import org.raflab.rezervacijeservice.repository.GuestsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.raflab.shared.events.BookingCreatedMaintenanceEvent;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class BookingsService implements BookingPublisher {

    @Autowired
    BookingsRepository bookingsRepository;
    @Autowired
    GuestsRepository guestsRepository;
    @Autowired
    CancellationPoliciesRepository cancellationPoliciesRepository;

    private final List<BookingSubscriber> observers = new ArrayList<>();
    private final BookingMaintenancePublisher bookingMaintenancePublisher;

    @Autowired
    public BookingsService(List<BookingSubscriber> observers,
                           BookingMaintenancePublisher bookingMaintenancePublisher) {
        this.observers.addAll(observers);
        this.bookingMaintenancePublisher = bookingMaintenancePublisher;
    }

    @Override
    public void addObserver(BookingSubscriber observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(BookingSubscriber observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers(Long bookingId, String oldStatus, String newStatus) {
        for (BookingSubscriber observer : observers) {
            observer.update(bookingId, oldStatus, newStatus);
        }
    }

    public void save(BookingsRequestDto bookingsRequestDto) {
        validateBookingSave(bookingsRequestDto);

        Bookings bookings = BookingsConverter.toBooking(bookingsRequestDto);
        Optional<Guests> guest = guestsRepository.findByEmail(bookingsRequestDto.getGuestsRequestDto().getEmail());
        Optional<CancellationPolicies> cancellationPolicies =
                cancellationPoliciesRepository.findByPolicyName(bookingsRequestDto.getPolicyRequestDto().getPolicyName());

        if (cancellationPolicies.isPresent()) {
            bookings.setPolicy(cancellationPolicies.get());
        } else {
            CancellationPolicies newPolicy =
                    CancellationPoliciesConverter.toCancellationPolicies(bookingsRequestDto.getPolicyRequestDto());
            cancellationPoliciesRepository.save(newPolicy);
            bookings.setPolicy(newPolicy);
        }

        if (guest.isPresent()) {
            bookings.setGuest(guest.get());
        } else {
            Guests newGuest = GuestsConverter.toGuests(bookingsRequestDto.getGuestsRequestDto());
            guestsRepository.save(newGuest);
            bookings.setGuest(newGuest);
        }

        Bookings savedBooking = bookingsRepository.save(bookings);

        bookingMaintenancePublisher.publishBookingCreated(
                new BookingCreatedMaintenanceEvent(
                        savedBooking.getId(),
                        savedBooking.getUnitId().longValue(),
                        savedBooking.getStartDate(),
                        savedBooking.getEndDate(),
                        savedBooking.getStatus(),
                        LocalDateTime.now()
                )
        );
    }

    public void updateById(BookingsRequestDto bookingsRequestDto, Long id) {
        if (bookingsRepository.findById(id).isPresent()) {
            Bookings bookings = bookingsRepository.findById(id).get();
            bookings.setGuest(GuestsConverter.toGuests(bookingsRequestDto.getGuestsRequestDto()));
            bookings.setStartDate(bookingsRequestDto.getStartDate());
            bookings.setEndDate(bookingsRequestDto.getEndDate());
            bookings.setStatus(bookingsRequestDto.getStatus());
            bookings.setUnitId(bookingsRequestDto.getUnitId());
            bookings.setPolicy(CancellationPoliciesConverter.toCancellationPolicies(bookingsRequestDto.getPolicyRequestDto()));
            bookings.setTotalPrice(bookingsRequestDto.getTotalPrice());
            bookingsRepository.save(bookings);
        } else {
            bookingsRepository.save(BookingsConverter.toBooking(bookingsRequestDto));
        }
    }

    public List<BookingsResponseDto> findAll() {
        return BookingsConverter.toBookingResponseDtoList(bookingsRepository.findAll());
    }

    public BookingsResponseDto findById(Long id) {
        Bookings booking = bookingsRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Booking sa ID " + id + " ne postoji."));
        return BookingsConverter.toBookingResponseDto(booking);
    }

    public void deleteById(Long id) {
        bookingsRepository.deleteById(id);
    }

    public Double cancelBookingAndRefund(Long id) {
        Bookings booking = bookingsRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Booking sa ID " + id + " ne postoji."));

        CancellationPolicies policy = booking.getPolicy();
        int refundPercentage = policy.getRefundPercentage();
        double refundAmount = booking.getTotalPrice() * refundPercentage / 100;

        booking.setStatus("CANCELLED");
        bookingsRepository.save(booking);

        return refundAmount;
    }

    public void updateBookingStatus(Long id, String status) {
        if (bookingsRepository.existsById(id)) {
            Bookings booking = bookingsRepository.findById(id).get();
            booking.setStatus(status);
            bookingsRepository.save(booking);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Booking sa ID " + id + " ne postoji.");
        }
    }

    public void markPaymentRecorded(Long id) {
        if (bookingsRepository.existsById(id)) {
            Bookings booking = bookingsRepository.findById(id).get();
            String oldStatus = booking.getStatus();
            String newStatus = "PAYMENT_RECORDED";

            booking.setStatus(newStatus);
            bookingsRepository.save(booking);
            notifyObservers(id, oldStatus, newStatus);
            return;
        }

        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Booking sa ID " + id + " ne postoji.");
    }

    public void cancelBookingDueToNonPayment(Long id) {
        Bookings booking = bookingsRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Booking sa ID " + id + " ne postoji."));

        if ("CONFIRMED".equals(booking.getStatus())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Potvrdjena rezervacija ne moze biti otkazana kao neplacena.");
        }

        String oldStatus = booking.getStatus();
        booking.setStatus("CANCELLED");
        bookingsRepository.save(booking);

        notifyObservers(id, oldStatus, "CANCELLED");
    }

    public List<BookingsResponseDto> searchBookingsByGuestAndStatus(String searchTerm, String status) {
        List<Bookings> bookings = bookingsRepository.searchGuestsBookings(searchTerm, status);
        return BookingsConverter.toBookingResponseDtoList(bookings);
    }

    private void validateBookingSave(BookingsRequestDto bookingsRequestDto) {
        if (bookingsRequestDto.getGuestsRequestDto() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Ne postoji gost za dati booking.");
        }
        if (bookingsRequestDto.getStartDate() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Ne postoji pocetni datum.");
        }
        if (bookingsRequestDto.getEndDate() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Ne postoji krajnji datum.");
        }
        if (bookingsRequestDto.getStatus() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Nema statusa rezervacije.");
        }
        if (bookingsRequestDto.getUnitId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Nema unit-a za koji je rezervacija.");
        }
        if (bookingsRequestDto.getPolicyRequestDto() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Dodajte politiku otkazivanja.");
        }
        if (bookingsRequestDto.getTotalPrice() == null || bookingsRequestDto.getTotalPrice() <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Ukupan iznos za rezervaciju nije validan.");
        }
    }
}
