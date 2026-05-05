package org.raflab.finansijeservice.service;

import org.raflab.finansijeservice.converter.InvoicesConverter;

import org.raflab.finansijeservice.converter.PaymentMethodsConverter;
import org.raflab.finansijeservice.dto.request.PaymentMethodsRequestDto;
import org.raflab.finansijeservice.dto.response.InvoicesResponseDto;
import org.raflab.finansijeservice.dto.response.PaymentMethodsResponseDto;
import org.raflab.finansijeservice.model.PaymentMethods;
import org.raflab.finansijeservice.repository.PaymentMethodsRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PaymentMethodsService {

    PaymentMethodsRepository paymentMethodsRepository;

    public void save(PaymentMethodsRequestDto paymentMethodsRequestDto){
        paymentMethodsRepository.save(PaymentMethodsConverter.toPaymentMethods(paymentMethodsRequestDto));
    }

    public void updateById(PaymentMethodsRequestDto paymentMethodsRequestDto,Long id){

        if(paymentMethodsRepository.existsById(id)){
            PaymentMethods  paymentMethods = paymentMethodsRepository.findById(id).get();
            paymentMethods.setMethodName(paymentMethodsRequestDto.getMethodName());
            paymentMethods.setIsActive(paymentMethodsRequestDto.getIsActive());
            paymentMethodsRepository.save(paymentMethods);
        }
        else{
            paymentMethodsRepository.save(PaymentMethodsConverter.toPaymentMethods(paymentMethodsRequestDto));
        }
    }

    public List<PaymentMethodsResponseDto> findAll(){
        return PaymentMethodsConverter.toPaymentMethodsResponseList(paymentMethodsRepository.findAll());
    }
    public PaymentMethodsResponseDto findById(Long id){
        return PaymentMethodsConverter.toPaymentMethodsResponse(paymentMethodsRepository.findById(id).get());
    }
    public void deleteById( Long id) {
        paymentMethodsRepository.deleteById(id);
    }

}
