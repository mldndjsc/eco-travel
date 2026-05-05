package org.raflab.rezervacijeservice.converter;

import org.raflab.rezervacijeservice.dto.request.GuestsRequestDto;
import org.raflab.rezervacijeservice.dto.response.GuestsResponseDto;
import org.raflab.rezervacijeservice.model.Guests;

import java.util.ArrayList;
import java.util.List;

public class GuestsConverter {

    public static Guests toGuests(GuestsRequestDto guestsRequestDto) {

        Guests guests = new Guests();
        guests.setEmail(guestsRequestDto.getEmail());
        guests.setFirstName(guestsRequestDto.getFirstName());
        guests.setLastName(guestsRequestDto.getLastName());

        return guests;
    }

    public static Guests updateToGuests(GuestsRequestDto guestsRequestDto,Long id) {

        Guests guests = new Guests();
        guests.setId(id);
        guests.setEmail(guestsRequestDto.getEmail());
        guests.setFirstName(guestsRequestDto.getFirstName());
        guests.setLastName(guestsRequestDto.getLastName());

        return guests;
    }
    public static GuestsResponseDto toGuestsResponseDto(Guests guests) {
        GuestsResponseDto guestsResponseDto = new GuestsResponseDto();

        guestsResponseDto.setEmail(guests.getEmail());
        guestsResponseDto.setFirstName(guests.getFirstName());
        guestsResponseDto.setLastName(guests.getLastName());

        return guestsResponseDto;
    }

    public static List<GuestsResponseDto>  toGuestsResponseDtoList(List<Guests> guestsList) {

        List<GuestsResponseDto> guestsResponseDtoList = new ArrayList<>();

        for (Guests guests : guestsList) {
            guestsResponseDtoList.add(toGuestsResponseDto(guests));
        }
        return guestsResponseDtoList;
    }
}
