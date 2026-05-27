package org.raflab.finansijeservice.converter;

import org.raflab.finansijeservice.dto.request.TransactionsRequestDto;
import org.raflab.finansijeservice.dto.response.TransactionsResponseDto;
import org.raflab.finansijeservice.model.Transactions;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class TransactionsConverter {

    public static Transactions toTransactions(TransactionsRequestDto transactionsRequestDto) {
        Transactions transactions = new Transactions();
        transactions.setAmount(transactionsRequestDto.getAmount());
        transactions.setDate(LocalDateTime.now());

        return transactions;
    }

    public static Transactions updateToTransactions(TransactionsRequestDto transactionsRequestDto,Long id) {
        Transactions transactions = new Transactions();
        transactions.setId(id);
        transactions.setAmount(transactionsRequestDto.getAmount());
        transactions.setDate(LocalDateTime.now());

        return transactions;
    }

    public static TransactionsResponseDto toTransactionsResponseDto(Transactions transactions) {
        TransactionsResponseDto transactionsResponseDto = new TransactionsResponseDto();
        transactionsResponseDto.setAmount(transactions.getAmount());
        transactionsResponseDto.setDate(transactions.getDate());
        transactionsResponseDto.setInvoiceResponseDto(InvoicesConverter.toInvoicesResponse(transactions.getInvoice()));
        transactionsResponseDto.setPaymentMethodResponseDto(PaymentMethodsConverter.toPaymentMethodsResponse(transactions.getPaymentMethod()));


        return transactionsResponseDto;
    }

    public static List<TransactionsResponseDto> toTransacationsResponseList(List<Transactions> transactionsList) {
        List<TransactionsResponseDto> transactionsResponseDtoList = new ArrayList<>();
        for (Transactions transactions : transactionsList) {
            transactionsResponseDtoList.add(toTransactionsResponseDto(transactions));
        }
        return transactionsResponseDtoList;
    }
}
