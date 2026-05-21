package org.raflab.finansijeservice.converter;

import org.raflab.finansijeservice.dto.request.InvoicesRequestDto;
import org.raflab.finansijeservice.dto.request.PaymentMethodsRequestDto;
import org.raflab.finansijeservice.dto.response.InvoicesResponseDto;
import org.raflab.finansijeservice.dto.response.PaymentMethodsResponseDto;
import org.raflab.finansijeservice.model.Invoices;
import org.raflab.finansijeservice.model.PaymentMethods;

import java.util.ArrayList;
import java.util.List;

public class PaymentMethodsConverter {

    public static PaymentMethods toPaymentMethods(PaymentMethodsRequestDto paymentMethodsRequestDto) {
        PaymentMethods paymentMethods = new PaymentMethods();
        paymentMethods.setMethodName(paymentMethodsRequestDto.getMethodName());
        paymentMethods.setIsActive(paymentMethodsRequestDto.getIsActive());


        return paymentMethods;
    }

    public static PaymentMethods updateToPaymentMethods(PaymentMethodsRequestDto paymentMethodsRequestDto,Long id) {
        PaymentMethods paymentMethods = new PaymentMethods();
        paymentMethods.setId(id);
        paymentMethods.setMethodName(paymentMethodsRequestDto.getMethodName());
        paymentMethods.setIsActive(paymentMethodsRequestDto.getIsActive());


        return paymentMethods;
    }

    public static PaymentMethodsResponseDto toPaymentMethodsResponse(PaymentMethods paymentMethods) {
        PaymentMethodsResponseDto paymentMethodsResponseDto = new PaymentMethodsResponseDto();
        paymentMethodsResponseDto.setId(paymentMethods.getId());
        paymentMethodsResponseDto.setMethodName(paymentMethods.getMethodName());
        paymentMethodsResponseDto.setIsActive(paymentMethods.getIsActive());


        return paymentMethodsResponseDto;
    }

    public static List<PaymentMethodsResponseDto> toPaymentMethodsResponseList(List<PaymentMethods> paymentMethodsList) {
        List<PaymentMethodsResponseDto> paymentMethodsResponseDtoList = new ArrayList<>();
        for (PaymentMethods paymentMethods : paymentMethodsList) {
            paymentMethodsResponseDtoList.add(toPaymentMethodsResponse(paymentMethods));
        }
        return paymentMethodsResponseDtoList;
    }
}
