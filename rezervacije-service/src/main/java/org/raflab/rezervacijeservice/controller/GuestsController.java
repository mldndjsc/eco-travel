package org.raflab.rezervacijeservice.controller;

import jakarta.validation.Valid;
import org.raflab.rezervacijeservice.dto.request.CancellationPoliciesRequestDto;
import org.raflab.rezervacijeservice.dto.request.GuestsRequestDto;
import org.raflab.rezervacijeservice.dto.response.CancellationPoliciesResponseDto;
import org.raflab.rezervacijeservice.dto.response.GuestsResponseDto;
import org.raflab.rezervacijeservice.service.GuestsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//@CrossOrigin(origins = "http://localhost:8080")
@RestController
@RequestMapping("/api/rezervacije")
@RefreshScope
public class GuestsController {

    @Autowired
    GuestsService guestsService;

    @PostMapping(path = "/guests/add")
    public void addGuest(@RequestBody @Valid GuestsRequestDto guestsRequestDto){
         guestsService.save(guestsRequestDto);
    }

    @PutMapping(path = "/guests/update/{id}")
    public void updateGuestById(@RequestBody @Valid GuestsRequestDto guestsRequestDto,@PathVariable Long id){
        guestsService.updateById(guestsRequestDto,id);
    }

    @GetMapping(path = "/guests/{id}")
    public GuestsResponseDto findGuestById(@PathVariable Long id){
        return guestsService.findById(id);
    }
    @GetMapping(path = "/guests/all")
    public List<GuestsResponseDto> getAllGuests(){
        return guestsService.findAll();
    }
    @DeleteMapping(path = "/guests/delete/{id}")
    public void deleteGuestById(@PathVariable Long id) {
        guestsService.deleteById(id);
    }
}
