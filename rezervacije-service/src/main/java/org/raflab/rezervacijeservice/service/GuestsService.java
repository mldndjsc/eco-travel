package org.raflab.rezervacijeservice.service;

import org.raflab.rezervacijeservice.converter.CancellationPoliciesConverter;
import org.raflab.rezervacijeservice.converter.GuestsConverter;
import org.raflab.rezervacijeservice.dto.request.CancellationPoliciesRequestDto;
import org.raflab.rezervacijeservice.dto.request.GuestsRequestDto;
import org.raflab.rezervacijeservice.dto.response.CancellationPoliciesResponseDto;
import org.raflab.rezervacijeservice.dto.response.GuestsResponseDto;
import org.raflab.rezervacijeservice.model.Guests;
import org.raflab.rezervacijeservice.repository.GuestsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GuestsService {

    @Autowired
    GuestsRepository guestsRepository;

    public void save(GuestsRequestDto guestsRequestDto){
        guestsRepository.save(GuestsConverter.toGuests(guestsRequestDto));
    }

    public void updateById(GuestsRequestDto guestsRequestDto,Long id){
        if(guestsRepository.existsById(id)){

            Guests guests = guestsRepository.findById(id).get();
            guests.setFirstName(guestsRequestDto.getFirstName());
            guests.setLastName(guestsRequestDto.getLastName());
            guests.setEmail(guestsRequestDto.getEmail());

            guestsRepository.save(guests);
        }else{
            guestsRepository.save(GuestsConverter.toGuests(guestsRequestDto));

        }
    }

    public List<GuestsResponseDto> findAll(){
        return GuestsConverter.toGuestsResponseDtoList(guestsRepository.findAll());
    }
    public GuestsResponseDto findById(Long id){
        return GuestsConverter.toGuestsResponseDto(guestsRepository.findById(id).get());
    }
    public void deleteById( Long id) {
        guestsRepository.deleteById(id);
    }
}
