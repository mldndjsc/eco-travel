package org.raflab.rezervacijeservice.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.raflab.rezervacijeservice.dto.request.AddOnItemsRequestDto;
import org.raflab.rezervacijeservice.dto.response.AddOnItemsResponseDto;
import org.raflab.rezervacijeservice.service.AddOnItemsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:8080")
@RestController
@RequestMapping("/api/rezervacije")
@RefreshScope
public class AddOnItemsController {

    @Autowired
    AddOnItemsService addOnItemsService;

    @PostMapping(path = "/addonitems/add")
    public void addAddOnItems(@RequestBody @Valid AddOnItemsRequestDto addOnItemsRequestDto){
        addOnItemsService.save(addOnItemsRequestDto);
    }
    @GetMapping(path = "/addonitems/{id}")
    public AddOnItemsResponseDto findAddOnItemsById(@PathVariable Long id){
        return addOnItemsService.findById(id);
    }
    @GetMapping(path = "/addonitems/all")
    public List<AddOnItemsResponseDto> getAllAddOnItems(){
        return addOnItemsService.findAll();
    }
    @DeleteMapping(path = "/addonitems/delete/{id}")
    public void deleteAddOnItemsById(@PathVariable Long id) {
        addOnItemsService.deleteById(id);
    }
    @PutMapping(path="/addonitems/update/{id}")
    public void updateAddOnItems(@RequestBody@Valid AddOnItemsRequestDto addOnItemsRequestDto, @PathVariable Long id){
        addOnItemsService.updateById(addOnItemsRequestDto,id);
    }
}
