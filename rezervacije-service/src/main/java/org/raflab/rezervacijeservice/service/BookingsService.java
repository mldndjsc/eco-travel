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
import org.raflab.shared.events.MaintenanceWorkOrderCreatedFeedbackEvent;

import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class BookingsService implements BookingPublisher {

    @Autowired
    BookingsRepository bookingsRepository;
    @Autowired
    GuestsRepository guestsRepository;
    @Autowired
    CancellationPoliciesRepository cancellationPoliciesRepository;
    @Autowired
    BookingStatusLogsService bookingStatusLogsService;

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
        log.info("Kreiranje nove rezervacije: gost={}, unitId={}, od={}, do={}, cijena={}",
                bookingsRequestDto.getGuestsRequestDto().getEmail(),
                bookingsRequestDto.getUnitId(),
                bookingsRequestDto.getStartDate(),
                bookingsRequestDto.getEndDate(),
                bookingsRequestDto.getTotalPrice());

        validateBookingSave(bookingsRequestDto);

        Bookings bookings = BookingsConverter.toBooking(bookingsRequestDto);
        Optional<Guests> guest = guestsRepository.findByEmail(bookingsRequestDto.getGuestsRequestDto().getEmail());
        Optional<CancellationPolicies> cancellationPolicies =
                cancellationPoliciesRepository.findByPolicyName(bookingsRequestDto.getPolicyRequestDto().getPolicyName());

        if (cancellationPolicies.isPresent()) {
            bookings.setPolicy(cancellationPolicies.get());
        } else {
            log.info("Politika otkazivanja '{}' ne postoji — kreiranje nove.",
                    bookingsRequestDto.getPolicyRequestDto().getPolicyName());
            CancellationPolicies newPolicy =
                    CancellationPoliciesConverter.toCancellationPolicies(bookingsRequestDto.getPolicyRequestDto());
            cancellationPoliciesRepository.save(newPolicy);
            bookings.setPolicy(newPolicy);
        }

        if (guest.isPresent()) {
            bookings.setGuest(guest.get());
        } else {
            log.info("Gost '{}' ne postoji u bazi — kreiranje novog gosta.",
                    bookingsRequestDto.getGuestsRequestDto().getEmail());
            Guests newGuest = GuestsConverter.toGuests(bookingsRequestDto.getGuestsRequestDto());
            guestsRepository.save(newGuest);
            bookings.setGuest(newGuest);
        }

        Bookings savedBooking = bookingsRepository.save(bookings);
        log.info("Rezervacija uspjesno kreirana: bookingId={}, status={}", savedBooking.getId(), savedBooking.getStatus());

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
        log.info("Trazenje rezervacije: bookingId={}", id);
        Bookings booking = bookingsRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Rezervacija nije pronadjena: bookingId={}", id);
                    return new ResponseStatusException(HttpStatus.NOT_FOUND, "Booking sa ID " + id + " ne postoji.");
                });
        log.info("Rezervacija pronadjena: bookingId={}, status={}, gost={}",
                booking.getId(), booking.getStatus(), booking.getGuest().getEmail());
        return BookingsConverter.toBookingResponseDto(booking);
    }

    public void deleteById(Long id) {
        log.info("Brisanje rezervacije: bookingId={}", id);
        bookingsRepository.deleteById(id);
        log.info("Rezervacija obrisana: bookingId={}", id);
    }

    public Double cancelBookingAndRefund(Long id) {
        log.info("Otkazivanje rezervacije sa povracajem: bookingId={}", id);
        Bookings booking = bookingsRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Rezervacija za otkazivanje nije pronadjena: bookingId={}", id);
                    return new ResponseStatusException(HttpStatus.NOT_FOUND, "Booking sa ID " + id + " ne postoji.");
                });

        CancellationPolicies policy = booking.getPolicy();
        int refundPercentage = policy.getRefundPercentage();
        double refundAmount = booking.getTotalPrice() * refundPercentage / 100;

        booking.setStatus("CANCELLED");
        bookingsRepository.save(booking);

        log.info("Rezervacija otkazana: bookingId={}, politika={}, povracaj={}%, iznos={}EUR",
                id, policy.getPolicyName(), refundPercentage, refundAmount);
        return refundAmount;
    }

    public void updateBookingStatus(Long id, String status) {
        if (bookingsRepository.existsById(id)) {
            Bookings booking = bookingsRepository.findById(id).get();
            String oldStatus = booking.getStatus();
            booking.setStatus(status);
            bookingsRepository.save(booking);
            log.info("Status rezervacije promijenjen: bookingId={}, {} -> {}", id, oldStatus, status);
        } else {
            log.error("Azuriranje statusa nije uspjelo — rezervacija ne postoji: bookingId={}", id);
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
            log.info("Placanje evidentirano za rezervaciju: bookingId={}, {} -> {}", id, oldStatus, newStatus);
            notifyObservers(id, oldStatus, newStatus);
            return;
        }

        log.error("Evidencija placanja nije uspjela — rezervacija ne postoji: bookingId={}", id);
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Booking sa ID " + id + " ne postoji.");
    }

    public void cancelBookingDueToNonPayment(Long id) {
        Bookings booking = bookingsRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Otkazivanje zbog neplacanja nije uspjelo — rezervacija ne postoji: bookingId={}", id);
                    return new ResponseStatusException(HttpStatus.NOT_FOUND, "Booking sa ID " + id + " ne postoji.");
                });

        if ("CONFIRMED".equals(booking.getStatus())) {
            log.error("Pokusaj otkazivanja vec potvrdjene rezervacije zbog neplacanja: bookingId={}", id);
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Potvrdjena rezervacija ne moze biti otkazana kao neplacena.");
        }

        String oldStatus = booking.getStatus();
        booking.setStatus("CANCELLED");
        bookingsRepository.save(booking);

        log.warn("Rezervacija otkazana zbog neplacanja: bookingId={}, prethodni status={}", id, oldStatus);
        notifyObservers(id, oldStatus, "CANCELLED");
    }

    public void recordMaintenanceWorkOrderCreated(MaintenanceWorkOrderCreatedFeedbackEvent event) {
        Bookings booking = bookingsRepository.findById(event.getBookingId())
                .orElseThrow(() -> {
                    log.error("RabbitMQ feedback — rezervacija ne postoji: bookingId={}", event.getBookingId());
                    return new ResponseStatusException(HttpStatus.NOT_FOUND,
                            "Booking sa ID " + event.getBookingId() + " ne postoji.");
                });

        bookingStatusLogsService.logStatusChange(
                booking.getId(),
                booking.getStatus(),
                "MAINTENANCE_WORK_ORDER_CREATED"
        );

        log.info("RabbitMQ feedback primljen: bookingId={}, workOrderId={}, scheduledFor={}",
                booking.getId(), event.getWorkOrderId(), event.getScheduledFor());
    }

    public List<BookingsResponseDto> searchBookingsByGuestAndStatus(String searchTerm, String status) {
        List<Bookings> bookings = bookingsRepository.searchGuestsBookings(searchTerm, status);
        return BookingsConverter.toBookingResponseDtoList(bookings);
    }

    private void validateBookingSave(BookingsRequestDto bookingsRequestDto) {
        if (bookingsRequestDto.getGuestsRequestDto() == null) {
            log.error("Validacija rezervacije pala: nedostaje gost");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Ne postoji gost za dati booking.");
        }
        if (bookingsRequestDto.getStartDate() == null) {
            log.error("Validacija rezervacije pala: nedostaje pocetni datum");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Ne postoji pocetni datum.");
        }
        if (bookingsRequestDto.getEndDate() == null) {
            log.error("Validacija rezervacije pala: nedostaje krajnji datum");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Ne postoji krajnji datum.");
        }
        if (bookingsRequestDto.getStatus() == null) {
            log.error("Validacija rezervacije pala: nedostaje status");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Nema statusa rezervacije.");
        }
        if (bookingsRequestDto.getUnitId() == null) {
            log.error("Validacija rezervacije pala: nedostaje unitId");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Nema unit-a za koji je rezervacija.");
        }
        if (bookingsRequestDto.getPolicyRequestDto() == null) {
            log.error("Validacija rezervacije pala: nedostaje politika otkazivanja");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Dodajte politiku otkazivanja.");
        }
        if (bookingsRequestDto.getTotalPrice() == null || bookingsRequestDto.getTotalPrice() <= 0) {
            log.error("Validacija rezervacije pala: nevalidan ukupan iznos={}", bookingsRequestDto.getTotalPrice());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Ukupan iznos za rezervaciju nije validan.");
        }
    }
}