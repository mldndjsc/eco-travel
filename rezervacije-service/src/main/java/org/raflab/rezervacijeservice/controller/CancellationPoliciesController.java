package org.raflab.rezervacijeservice.controller;

import jakarta.validation.Valid;
import org.raflab.rezervacijeservice.dto.request.BookingStatusLogsRequestDto;
import org.raflab.rezervacijeservice.dto.request.CancellationPoliciesRequestDto;
import org.raflab.rezervacijeservice.dto.response.BookingsStatusLogsResponseDto;
import org.raflab.rezervacijeservice.dto.response.CancellationPoliciesResponseDto;
import org.raflab.rezervacijeservice.service.CancellationPoliciesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:8080")
@RestController
@RequestMapping("/api/rezervacije")
@RefreshScope
public class CancellationPoliciesController {

    @Autowired
    CancellationPoliciesService cancellationPoliciesService;


    @PostMapping(path = "/cancellationpolicies/add")
    public void addCancellationPolicies(@RequestBody @Valid CancellationPoliciesRequestDto cancellationPoliciesRequestDto){
        cancellationPoliciesService.save(cancellationPoliciesRequestDto);
    }

    @PutMapping(path = "/cancellationpolicies/update/{id}")
    public void updateCancellationPolicies(@RequestBody @Valid CancellationPoliciesRequestDto cancellationPoliciesRequestDto, @PathVariable Long id){
        cancellationPoliciesService.updateById(cancellationPoliciesRequestDto,id);
    }

    @GetMapping(path = "/cancellationpolicies/{id}")
    public CancellationPoliciesResponseDto findCancellationPoliciesById(@PathVariable Long id){
        return cancellationPoliciesService.findById(id);
    }
    @GetMapping(path = "/cancellationpolicies/all")
    public List<CancellationPoliciesResponseDto> getAllCancellationPolicies(){
        return cancellationPoliciesService.findAll();
    }
    @DeleteMapping(path = "/cancellationpolicies/delete/{id}")
    public void deleteCancellationPoliciesById(@PathVariable Long id) {
        cancellationPoliciesService.deleteById(id);
    }
}
