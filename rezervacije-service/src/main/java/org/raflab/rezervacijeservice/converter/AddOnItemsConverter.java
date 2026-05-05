package org.raflab.rezervacijeservice.converter;

import org.raflab.rezervacijeservice.dto.request.AddOnItemsRequestDto;
import org.raflab.rezervacijeservice.dto.response.AddOnItemsResponseDto;
import org.raflab.rezervacijeservice.model.AddOnItems;

import java.util.ArrayList;
import java.util.List;

public class AddOnItemsConverter {

    public static AddOnItems toAddOnItems(AddOnItemsRequestDto addOnItemsRequestDto) {

        AddOnItems addOnItems = new AddOnItems();
        addOnItems.setBooking(BookingsConverter.toBooking(addOnItemsRequestDto.getBookingRequestDto()));
        addOnItems.setPrice(addOnItemsRequestDto.getPrice());
        addOnItems.setServiceName(addOnItemsRequestDto.getServiceName());
        return addOnItems;
    }

    public static AddOnItems updateToAddOnItems(AddOnItemsRequestDto addOnItemsRequestDto,Long id) {

        AddOnItems addOnItems = new AddOnItems();
        addOnItems.setId(id);
        addOnItems.setBooking(BookingsConverter.toBooking(addOnItemsRequestDto.getBookingRequestDto()));
        addOnItems.setPrice(addOnItemsRequestDto.getPrice());
        addOnItems.setServiceName(addOnItemsRequestDto.getServiceName());
        return addOnItems;
    }

    public static AddOnItemsResponseDto toAddOnItemsResponseDto(AddOnItems addOnItems) {
        AddOnItemsResponseDto addOnItemsResponseDto = new AddOnItemsResponseDto();
        addOnItemsResponseDto.setBookingsResponseDto(BookingsConverter.toBookingResponseDto(addOnItems.getBooking()));
        addOnItemsResponseDto.setPrice(addOnItems.getPrice());
        addOnItemsResponseDto.setServiceName(addOnItems.getServiceName());

        return addOnItemsResponseDto;
    }

    public static List<AddOnItemsResponseDto> toAddOnItemsResponseDtoList(List<AddOnItems> addOnItemsList) {
        List<AddOnItemsResponseDto> addOnItemsResponseDtoList = new ArrayList<>();
        for (AddOnItems addOnItems : addOnItemsList) {
            addOnItemsResponseDtoList.add(toAddOnItemsResponseDto(addOnItems));
        }
        return addOnItemsResponseDtoList;
    }
}
