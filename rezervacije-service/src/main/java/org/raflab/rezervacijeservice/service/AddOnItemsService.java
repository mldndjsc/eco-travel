package org.raflab.rezervacijeservice.service;

import org.raflab.rezervacijeservice.converter.AddOnItemsConverter;
import org.raflab.rezervacijeservice.converter.BookingsConverter;
import org.raflab.rezervacijeservice.dto.request.AddOnItemsRequestDto;
import org.raflab.rezervacijeservice.dto.response.AddOnItemsResponseDto;
import org.raflab.rezervacijeservice.model.AddOnItems;
import org.raflab.rezervacijeservice.model.Bookings;
import org.raflab.rezervacijeservice.repository.AddOnItemsRepository;
import org.raflab.rezervacijeservice.repository.BookingsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AddOnItemsService {

    @Autowired
    AddOnItemsRepository addOnItemsRepository;
    @Autowired
    BookingsRepository bookingsRepository;

    public void save(AddOnItemsRequestDto addOnItemsRequestDto){
        addOnItemsRepository.save(AddOnItemsConverter.toAddOnItems(addOnItemsRequestDto));
    }

    public void updateById(AddOnItemsRequestDto addOnItemsRequestDto,Long id){
        if(addOnItemsRepository.existsById(id)){

            AddOnItems addOnItems = addOnItemsRepository.findById(id).get();
            addOnItems.setServiceName(addOnItemsRequestDto.getServiceName());
            addOnItems.setPrice(addOnItemsRequestDto.getPrice());
            addOnItems.setBooking(BookingsConverter.toBooking(addOnItemsRequestDto.getBookingRequestDto()));
        }else{
            addOnItemsRepository.save(AddOnItemsConverter.toAddOnItems(addOnItemsRequestDto));
        }
    }

    public List<AddOnItemsResponseDto> findAll(){
        return AddOnItemsConverter.toAddOnItemsResponseDtoList(addOnItemsRepository.findAll());
    }
    public AddOnItemsResponseDto findById(Long id){
        return AddOnItemsConverter.toAddOnItemsResponseDto(addOnItemsRepository.findById(id).get());
    }
    public void deleteById( Long id) {
        addOnItemsRepository.deleteById(id);
    }
}
