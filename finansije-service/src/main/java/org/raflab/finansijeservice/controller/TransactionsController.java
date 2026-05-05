package org.raflab.finansijeservice.controller;

import jakarta.validation.Valid;
import org.raflab.finansijeservice.dto.request.TaxRatesRequestDto;
import org.raflab.finansijeservice.dto.request.TransactionsRequestDto;
import org.raflab.finansijeservice.dto.response.TaxRatesResponseDto;
import org.raflab.finansijeservice.dto.response.TransactionsResponseDto;
import org.raflab.finansijeservice.service.TransactionsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@CrossOrigin(origins = "http://localhost:8080")
@RestController
@RequestMapping("/api/finansije")
@RefreshScope
public class TransactionsController {

    @Autowired
    TransactionsService transactionsService;

    @PostMapping(path = "/transactions/add")
    public void addTransactions(@RequestBody @Valid TransactionsRequestDto transactionsRequestDto){
        transactionsService.save(transactionsRequestDto);
    }

    @PutMapping(path = "/transactions/update/{id}")
    public void updateTransactions(@RequestBody @Valid TransactionsRequestDto transactionsRequestDto,@PathVariable Long id){
        transactionsService.updateById(transactionsRequestDto,id);
    }

    @GetMapping(path = "/transactions/{id}")
    public TransactionsResponseDto findTransactionsById(@PathVariable Long id){
        return transactionsService.findById(id);
    }
    @GetMapping(path = "/transactions/all")
    public List<TransactionsResponseDto> getAllTransactions(){
        return transactionsService.findAll();
    }
    @DeleteMapping(path = "/transactions/delete/{id}")
    public void deleteTransactionsById(@PathVariable Long id) {
        transactionsService.deleteById(id);
    }

    @PostMapping("/transactions/pay")
    public void payInvoice(@RequestBody TransactionsRequestDto requestDto) {

        transactionsService.processPayment(requestDto);
    }

    @GetMapping(path = "/transactions/search")
    public List<TransactionsResponseDto> searchTransactions(@RequestParam LocalDateTime startDate, @RequestParam LocalDateTime endDate, @RequestParam String methodName) {
        return transactionsService.findTransactionsByDateAndPaymentMethods(startDate,endDate,methodName);
    }
}
