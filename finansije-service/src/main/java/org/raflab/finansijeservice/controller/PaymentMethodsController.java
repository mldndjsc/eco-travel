package org.raflab.finansijeservice.controller;

import jakarta.validation.Valid;
import org.raflab.finansijeservice.dto.request.InvoicesRequestDto;
import org.raflab.finansijeservice.dto.request.PaymentMethodsRequestDto;
import org.raflab.finansijeservice.dto.response.InvoicesResponseDto;
import org.raflab.finansijeservice.dto.response.PaymentMethodsResponseDto;
import org.raflab.finansijeservice.service.PaymentMethodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//@CrossOrigin(origins = "http://localhost:8080")
@RestController
@RequestMapping("/api/finansije")
@RefreshScope
public class PaymentMethodsController {

    @Autowired
    PaymentMethodsService paymentMethodsService;

    @PostMapping(path = "/paymentmethods/add")
    public void addPaymentMethods(@RequestBody @Valid PaymentMethodsRequestDto paymentMethodsRequestDto){
        paymentMethodsService.save(paymentMethodsRequestDto);
    }

    @PutMapping(path = "/paymentmethods/update/{id}")
    public void updatePaymentMethods(@RequestBody @Valid PaymentMethodsRequestDto paymentMethodsRequestDto,@PathVariable  Long id){
        paymentMethodsService.updateById(paymentMethodsRequestDto,id);
    }

    @GetMapping(path = "/paymentmethods/{id}")
    public PaymentMethodsResponseDto findPaymentMethodsById(@PathVariable Long id){
        return paymentMethodsService.findById(id);
    }
    @GetMapping(path = "/paymentmethods/all")
    public List<PaymentMethodsResponseDto> getAllPaymentMethods(){
        return paymentMethodsService.findAll();
    }
    @DeleteMapping(path = "/paymentmethods/delete/{id}")
    public void deletePaymentMethodsById(@PathVariable Long id) {
        paymentMethodsService.deleteById(id);
    }
}
