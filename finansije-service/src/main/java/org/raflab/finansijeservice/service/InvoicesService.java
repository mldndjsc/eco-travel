package org.raflab.finansijeservice.service;

import feign.FeignException;
import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.RequestNotPermitted;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import lombok.RequiredArgsConstructor;
import org.raflab.finansijeservice.client.BookingClient;
import org.raflab.finansijeservice.converter.InvoicesConverter;
import org.raflab.finansijeservice.converter.TaxRatesConverter;
import org.raflab.finansijeservice.dto.request.InvoicesRequestDto;
import org.raflab.finansijeservice.dto.response.BookingResponseDto;
import org.raflab.finansijeservice.dto.response.InvoicesResponseDto;
import org.raflab.finansijeservice.model.Invoices;
import org.raflab.finansijeservice.model.TaxRates;
import org.raflab.finansijeservice.repository.InvoicesRepository;
import org.raflab.finansijeservice.repository.TaxRatesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class InvoicesService {

    @Autowired
    InvoicesRepository invoicesRepository;

    @Autowired
    TaxRatesRepository taxRatesRepository;

    private final BookingClient bookingClient;

    @CircuitBreaker(name = "bookingServiceCB", fallbackMethod = "fallbackSaveInvoice")
    @RateLimiter(name = "rezervacijeServiceRateLimit", fallbackMethod = "fallbackSaveInvoiceRateLimit")
    public void save(InvoicesRequestDto invoicesRequestDto) {
        log.info("Kreiranje fakture: broj={}, bookingId={}, subtotal={}",
                invoicesRequestDto.getInvoiceNumber(),
                invoicesRequestDto.getBookingId(),
                invoicesRequestDto.getSubtotal());

        validateNewInvoiceData(invoicesRequestDto);

        Invoices invoices = InvoicesConverter.toInvoices(invoicesRequestDto);

        Optional<TaxRates> taxRates = taxRatesRepository.findByTaxName(invoicesRequestDto.getTaxRatesRequest().getTaxName());

        if (taxRates.isPresent()) {
            invoices.setTaxRates(taxRates.get());
        } else {
            log.info("Poreska stopa '{}' ne postoji — kreiranje nove.", invoicesRequestDto.getTaxRatesRequest().getTaxName());
            TaxRates newTaxRates = TaxRatesConverter.toTaxRates(invoicesRequestDto.getTaxRatesRequest());
            taxRatesRepository.save(newTaxRates);
            invoices.setTaxRates(newTaxRates);
        }
        if (invoicesRequestDto.getTotal() == null) {
            invoices.setTotal_amount(invoicesRequestDto.getSubtotal() + ((invoicesRequestDto.getSubtotal() / 100) * invoices.getTaxRates().getPercentage()));
        }

        // Verifikujemo da rezervacija postoji; bookingId iz zahteva ostaje izvor istine.
        bookingClient.findBookingById(invoicesRequestDto.getBookingId());

        invoicesRepository.save(invoices);
        log.info("Faktura uspjesno kreirana: broj={}, ukupno={}, status={}",
                invoices.getInvoiceNumber(), invoices.getTotal_amount(), invoices.getStatus());
    }

    public void updateById(InvoicesRequestDto invoicesRequestDto, Long id) {

        validateNewInvoiceData(invoicesRequestDto);

        if (invoicesRepository.existsById(id)) {
            Invoices invoices = invoicesRepository.findById(id).get();
            invoices.setInvoiceNumber(invoicesRequestDto.getInvoiceNumber());
            invoices.setStatus(invoicesRequestDto.getStatus());
            invoices.setSubtotal(invoicesRequestDto.getSubtotal());
            invoices.setBookingId(invoicesRequestDto.getBookingId());
            invoices.setTaxRates(TaxRatesConverter.toTaxRates(invoicesRequestDto.getTaxRatesRequest()));
            if (invoicesRequestDto.getTotal() == null) {
                invoices.setTotal_amount(invoicesRequestDto.getSubtotal() + ((invoicesRequestDto.getSubtotal() / 100) * invoices.getTaxRates().getPercentage()));
            } else {
                invoices.setTotal_amount(invoicesRequestDto.getTotal());
            }
            invoicesRepository.save(invoices);

        } else {
            invoicesRepository.save(InvoicesConverter.toInvoices(invoicesRequestDto));

        }
    }

    public List<InvoicesResponseDto> findAll() {
        return InvoicesConverter.toInvoicesResponseList(invoicesRepository.findAll());
    }

    public InvoicesResponseDto findById(Long id) {
        log.info("Trazenje fakture: invoiceId={}", id);
        Invoices invoice = invoicesRepository.findById(id).orElseThrow(() -> {
            log.error("Faktura nije pronadjena: invoiceId={}", id);
            return new ResponseStatusException(HttpStatus.NOT_FOUND, "Faktura sa ID " + id + " ne postoji.");
        });
        return InvoicesConverter.toInvoicesResponse(invoice);
    }

    public void deleteById(Long id) {
        log.info("Brisanje fakture: invoiceId={}", id);
        invoicesRepository.deleteById(id);
        log.info("Faktura obrisana: invoiceId={}", id);
    }

    public void markReservationFeedbackReceived(String invoiceNumber) {
        Invoices invoice = invoicesRepository.findInvoicesByInvoiceNumber(invoiceNumber);
        if (invoice == null) {
            log.error("RabbitMQ feedback — faktura nije pronadjena: broj={}", invoiceNumber);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Faktura sa brojem " + invoiceNumber + " ne postoji u finansije servisu.");
        }

        invoice.setStatus("RESERVATION_PAYMENT_RECORDED");
        invoicesRepository.save(invoice);
        log.info("RabbitMQ feedback primljen: faktura={} azurirana na RESERVATION_PAYMENT_RECORDED", invoiceNumber);
    }

    @CircuitBreaker(name = "bookingServiceCB", fallbackMethod = "fallbackCancelUnpaidInvoice")
    @RateLimiter(name = "rezervacijeServiceRateLimit", fallbackMethod = "fallbackCancelUnpaidInvoiceRateLimit")
    public void cancelUnpaidInvoice(Long invoiceId) {
        log.warn("Storniranje neplacene fakture: invoiceId={}", invoiceId);
        Invoices invoice = invoicesRepository.findById(invoiceId)
                .orElseThrow(() -> {
                    log.error("Storniranje nije uspjelo — faktura ne postoji: invoiceId={}", invoiceId);
                    return new ResponseStatusException(HttpStatus.NOT_FOUND, "Faktura sa ID " + invoiceId + " ne postoji u bazi.");
                });

        validateInvoiceCancellation(invoice);

        invoice.setStatus("CANCELLED");
        invoicesRepository.save(invoice);
        log.warn("Faktura stornirana: invoiceId={}, broj={}, bookingId={}",
                invoiceId, invoice.getInvoiceNumber(), invoice.getBookingId());

        bookingClient.cancelBookingDueToNonPayment(invoice.getBookingId());
    }

    public void fallbackSaveInvoice(InvoicesRequestDto dto, Throwable t) {
        if (t instanceof ResponseStatusException) {
            throw (ResponseStatusException) t;
        }

        if (t instanceof CallNotPermittedException) {
            log.error("Circuit Breaker OPEN — kreiranje fakture blokirano: bookingId={}", dto.getBookingId());
            throw new ResponseStatusException(
                    HttpStatus.SERVICE_UNAVAILABLE,
                    "Sistem je preopterecen. Zastitni osigurac je aktivan. Faktura nije sacuvana, pokusajte ponovo kasnije."
            );
        }

        if (t instanceof FeignException.NotFound) {
            log.error("Kreiranje fakture palo — rezervacija ne postoji: bookingId={}", dto.getBookingId());
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Nije moguce kreirati fakturu. Rezervacija sa ID " + dto.getBookingId() + " ne postoji."
            );
        }

        log.error("Komunikacija sa Bookings servisom pala pri kreiranju fakture: {}", t.getMessage());
        throw new ResponseStatusException(
                HttpStatus.SERVICE_UNAVAILABLE,
                "Greska u komunikaciji: mreza ka Bookings servisu je trenutno nedostupna. Detalji: " + t.getMessage()
        );
    }

    public void fallbackSaveInvoiceRateLimit(InvoicesRequestDto dto, Throwable t) {
        if (t instanceof ResponseStatusException ex) {
            throw ex;
        }
        if (t instanceof RequestNotPermitted) {
            throw new ResponseStatusException(
                    HttpStatus.TOO_MANY_REQUESTS,
                    "Previse zahteva prema Bookings servisu. Faktura nije sacuvana, pokusajte ponovo kasnije."
            );
        }

        if (t instanceof RuntimeException runtimeException) {
            throw runtimeException;
        }

        throw new RuntimeException(t);
    }

    public void fallbackCancelUnpaidInvoice(Long invoiceId, Throwable t) {

        System.out.println("RATE LIMIT fallback -> " + t.getClass().getName());
        System.out.println("CB fallback -> " + t.getClass().getName());
        if (t instanceof ResponseStatusException) {
            throw (ResponseStatusException) t;
        }

        if (t instanceof CallNotPermittedException) {
            throw new ResponseStatusException(
                    HttpStatus.SERVICE_UNAVAILABLE,
                    "Faktura je stornirana lokalno, ali je circuit breaker blokirao otkazivanje rezervacije u Bookings servisu."
            );
        }

        if (t instanceof FeignException.NotFound) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Faktura je stornirana, ali rezervacija nije pronadjena u Bookings servisu."
            );
        }

        throw new ResponseStatusException(
                HttpStatus.SERVICE_UNAVAILABLE,
                "Faktura je stornirana, ali komunikacija sa Bookings servisom za otkazivanje rezervacije nije uspela."
        );
    }

    public void fallbackCancelUnpaidInvoiceRateLimit(Long invoiceId, Throwable t) {

        System.out.println("RATE LIMIT fallback -> " + t.getClass().getName());
        System.out.println("CB fallback -> " + t.getClass().getName());
        if (t instanceof ResponseStatusException ex) {
            throw ex;
        }
        if (t instanceof RequestNotPermitted) {
            throw new ResponseStatusException(
                    HttpStatus.TOO_MANY_REQUESTS,
                    "Previse zahteva prema Bookings servisu. Faktura nije sacuvana, pokusajte ponovo kasnije."
            );
        }
        throw new ResponseStatusException(
                HttpStatus.SERVICE_UNAVAILABLE,
                "Bookings servis trenutno nije dostupan."
        );
    }

    private void validateNewInvoiceData(InvoicesRequestDto dto) {
        if (dto.getSubtotal() == null || dto.getSubtotal() <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Osnovica (subtotal) mora biti pozitivna vrednost.");
        }

        if (dto.getTaxRatesRequest() != null) {
            double taxPercentage = dto.getTaxRatesRequest().getPercentage();
            if (taxPercentage < 0 || taxPercentage > 100) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Poreska stopa mora biti izmedju 0% i 100%.");
            }
        }
    }

    private void validateInvoiceCancellation(Invoices invoice) {
        if ("PAID".equals(invoice.getStatus()) || "PARTIALLY_PAID".equals(invoice.getStatus())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Faktura " + invoice.getInvoiceNumber() + " ima evidentirane uplate i ne moze biti stornirana na ovaj nacin.");
        }
    }
}
