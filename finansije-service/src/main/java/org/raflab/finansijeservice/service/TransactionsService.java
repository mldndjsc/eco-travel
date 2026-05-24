package org.raflab.finansijeservice.service;

import feign.FeignException;
import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import org.raflab.finansijeservice.client.BookingClient;
import org.raflab.finansijeservice.converter.InvoicesConverter;
import org.raflab.finansijeservice.converter.PaymentMethodsConverter;
import org.raflab.finansijeservice.converter.TaxRatesConverter;
import org.raflab.finansijeservice.converter.TransactionsConverter;
import org.raflab.finansijeservice.dto.request.TaxRatesRequestDto;
import org.raflab.finansijeservice.dto.request.TransactionsRequestDto;
import org.raflab.finansijeservice.dto.response.TaxRatesResponseDto;
import org.raflab.finansijeservice.dto.response.TransactionsResponseDto;
import org.raflab.finansijeservice.messaging.event.TransactionCreatedEvent;
import org.raflab.finansijeservice.messaging.publisher.TransactionEventPublisher;
import org.raflab.finansijeservice.model.Invoices;
import org.raflab.finansijeservice.model.PaymentMethods;
import org.raflab.finansijeservice.model.TaxRates;
import org.raflab.finansijeservice.model.Transactions;
import org.raflab.finansijeservice.repository.InvoicesRepository;
import org.raflab.finansijeservice.repository.PaymentMethodsRepository;
import org.raflab.finansijeservice.repository.TaxRatesRepository;
import org.raflab.finansijeservice.repository.TransactionsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionsService {

    @Autowired
    TransactionsRepository transactionsRepository;

    @Autowired
    InvoicesRepository invoicesRepository;

    @Autowired
    PaymentMethodsRepository paymentMethodsRepository;

    @Autowired
    TaxRatesRepository taxRatesRepository;

    private final BookingClient bookingClient;
    private final TransactionEventPublisher transactionEventPublisher;

    @Transactional
    public void save(TransactionsRequestDto transactionsRequestDto){
        Invoices invoice = resolveInvoice(transactionsRequestDto);
        PaymentMethods paymentMethod = resolvePaymentMethod(transactionsRequestDto);

        Transactions transaction = new Transactions();
        transaction.setAmount(transactionsRequestDto.getAmount());
        transaction.setDate(LocalDateTime.now());
        transaction.setInvoice(invoice);
        transaction.setPaymentMethod(paymentMethod);

        transaction = transactionsRepository.save(transaction);

        TransactionCreatedEvent event = new TransactionCreatedEvent(
                transaction.getId(),
                transactionsRequestDto.getInvoiceRequestDto().getBookingId(),
                transactionsRequestDto.getInvoiceRequestDto().getInvoiceNumber(),
                transactionsRequestDto.getAmount(),
                transactionsRequestDto.getPaymentMethodsRequestDto().getMethodName(),
                LocalDateTime.now()
        );
        transactionEventPublisher.publishTransactionCreated(event);
    }

    private Invoices resolveInvoice(TransactionsRequestDto transactionsRequestDto) {
        String invoiceNumber = transactionsRequestDto.getInvoiceRequestDto().getInvoiceNumber();
        Invoices existingInvoice = invoicesRepository.findInvoicesByInvoiceNumber(invoiceNumber);
        if (existingInvoice != null) {
            return existingInvoice;
        }

        TaxRates taxRates = taxRatesRepository
                .findByTaxName(transactionsRequestDto.getInvoiceRequestDto().getTaxRatesRequest().getTaxName())
                .orElseGet(() -> taxRatesRepository.save(
                        TaxRatesConverter.toTaxRates(transactionsRequestDto.getInvoiceRequestDto().getTaxRatesRequest())
                ));

        Invoices invoice = InvoicesConverter.toInvoices(transactionsRequestDto.getInvoiceRequestDto());
        invoice.setTaxRates(taxRates);
        if (transactionsRequestDto.getInvoiceRequestDto().getTotal() == null) {
            invoice.setTotal_amount(invoice.getSubtotal() + ((invoice.getSubtotal() / 100) * taxRates.getPercentage()));
        } else {
            invoice.setTotal_amount(transactionsRequestDto.getInvoiceRequestDto().getTotal());
        }

        return invoicesRepository.save(invoice);
    }

    private PaymentMethods resolvePaymentMethod(TransactionsRequestDto transactionsRequestDto) {
        String methodName = transactionsRequestDto.getPaymentMethodsRequestDto().getMethodName();
        PaymentMethods existingMethod = paymentMethodsRepository.findPaymentMethodsByMethodName(methodName);
        if (existingMethod != null) {
            return existingMethod;
        }

        return paymentMethodsRepository.save(
                PaymentMethodsConverter.toPaymentMethods(transactionsRequestDto.getPaymentMethodsRequestDto())
        );
    }

    public void updateById(TransactionsRequestDto transactionsRequestDto,Long id){

        if(transactionsRepository.existsById(id)){
            Transactions transactions = transactionsRepository.findById(id).get();
            transactions.setAmount(transactionsRequestDto.getAmount());
            transactions.setInvoice(InvoicesConverter.toInvoices(transactionsRequestDto.getInvoiceRequestDto()));
            transactions.setPaymentMethod(PaymentMethodsConverter.toPaymentMethods(transactionsRequestDto.getPaymentMethodsRequestDto()));
            transactionsRepository.save(transactions);
        }else {
            transactionsRepository.save(TransactionsConverter.toTransactions(transactionsRequestDto));
        }
    }

    public List<TransactionsResponseDto> findAll(){
        return TransactionsConverter.toTransacationsResponseList(transactionsRepository.findAll());
    }
    public TransactionsResponseDto findById(Long id){
        return TransactionsConverter.toTransactionsResponseDto(transactionsRepository.findById(id).get());
    }
    public void deleteById( Long id) {
        transactionsRepository.deleteById(id);
    }

    @Transactional
    @CircuitBreaker(name = "bookingServiceCB", fallbackMethod = "fallbackProcessPayment")
    @RateLimiter(name = "rezervacijeServiceRateLimit", fallbackMethod = "fallbackProcessPaymentRateLimit")
    @Retry(name ="bookingServiceCB" )
    public void processPayment(TransactionsRequestDto dto) {

        Invoices invoice = invoicesRepository.findInvoicesByInvoiceNumber(dto.getInvoiceRequestDto().getInvoiceNumber());



        Transactions transaction = TransactionsConverter.toTransactions(dto);
        transaction.setInvoice(invoice);

        PaymentMethods method = paymentMethodsRepository.findPaymentMethodsByMethodName(dto.getPaymentMethodsRequestDto().getMethodName());
        if(method == null){
            throw new RuntimeException("Nije pronadjena metoda placanja za transakciju.");
        }
        transaction.setPaymentMethod(method);

        transactionsRepository.save(transaction);

        Double totalPaidSoFar = transactionsRepository.sumPaidAmountByInvoiceId(invoice.getId());
        if (totalPaidSoFar == null) {
            totalPaidSoFar = 0.0;
        }

        if (totalPaidSoFar >= invoice.getTotal_amount()) {
            invoice.setStatus("PAID");
            invoicesRepository.save(invoice);

            bookingClient.updateBookingStatus(invoice.getBookingId(), "CONFIRMED");

        } else {
            invoice.setStatus("PARTIALLY_PAID");
            invoicesRepository.save(invoice);
        }
    }

    public void fallbackProcessPayment(TransactionsRequestDto dto, Throwable t) {
        if (t instanceof CallNotPermittedException) {
            System.err.println("CRITICAL (Circuit Breaker OPEN): Sistem je pod zaštitom. Uplata " + dto.getAmount() + " je sačuvana lokalno, ali Bookings servis nije obavešten. Potrebna je naknadna (asinhrona) sinhronizacija.");
            return;
        }

        if (t instanceof FeignException.NotFound) {
            System.err.println(" ERROR (Nepostojeća rezervacija): Uplata je evidentirana, ali rezervacija ne postoji u Bookings servisu! Potrebna je intervencija administratora za eventualni povraćaj novca.");
            return;
        }

        System.err.println(" UPOZORENJE (Mrežni pad): Uplata je uspešno primljena");
    }

    public void fallbackProcessPaymentRateLimit(TransactionsRequestDto dto, Throwable t) {
        throw new ResponseStatusException(
                HttpStatus.TOO_MANY_REQUESTS,
                "Previse zahteva prema Bookings servisu. Obrada placanja trenutno nije moguca."
        );
    }

    public List<TransactionsResponseDto> findTransactionsByDateAndPaymentMethods(LocalDateTime startDate,LocalDateTime endDate,String methodName) {

        return TransactionsConverter.toTransacationsResponseList(transactionsRepository.findTransactionsByDateAndMethod(startDate,endDate,methodName));
    }
}

