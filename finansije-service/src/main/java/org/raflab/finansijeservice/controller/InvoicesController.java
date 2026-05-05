package org.raflab.finansijeservice.controller;

import jakarta.validation.Valid;
import org.raflab.finansijeservice.dto.request.InvoicesRequestDto;
import org.raflab.finansijeservice.dto.response.InvoicesResponseDto;
import org.raflab.finansijeservice.service.InvoicesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:8080")
@RestController
@RequestMapping("/api/finansije")
@RefreshScope
public class InvoicesController {

    @Value("${custom.welcome-message}")
    private String message;
    @GetMapping("/invoices/welcome")
    public String getMessage() { return message; }

    @Autowired
    InvoicesService invoicesService;


    @PostMapping(path = "/invoices/add")
    public void addInvoice(@RequestBody @Valid InvoicesRequestDto invoicesRequestDto){
        invoicesService.save(invoicesRequestDto);
    }

    @PutMapping(path = "/invoices/update/{id}")
    public void updateInvoice(@RequestBody @Valid InvoicesRequestDto invoicesRequestDto,@PathVariable Long id){
        invoicesService.updateById(invoicesRequestDto,id);
    }

    @GetMapping(path = "/invoices/{id}")
    public InvoicesResponseDto findInvoicesById(@PathVariable Long id){
        return invoicesService.findById(id);
    }
    @GetMapping(path = "/invoices/all")
    public List<InvoicesResponseDto> getAllInvoices(){
        return invoicesService.findAll();
    }
    @DeleteMapping(path = "/invoices/delete/{id}")
    public void deleteInvoicesById(@PathVariable Long id) {
        invoicesService.deleteById(id);
    }

    @PutMapping("/invoices/{id}/cancel-unpaid")
    public ResponseEntity<String> cancelUnpaidInvoice(@PathVariable Long id) {

        invoicesService.cancelUnpaidInvoice(id);

        return ResponseEntity.ok("Račun i rezervacija su uspešno otkazani.");
    }

}
