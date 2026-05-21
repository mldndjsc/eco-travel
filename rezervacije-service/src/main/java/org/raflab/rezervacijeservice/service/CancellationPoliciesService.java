package org.raflab.rezervacijeservice.service;

import org.raflab.rezervacijeservice.converter.BookingStatusLogsConverter;
import org.raflab.rezervacijeservice.converter.CancellationPoliciesConverter;
import org.raflab.rezervacijeservice.dto.request.BookingStatusLogsRequestDto;
import org.raflab.rezervacijeservice.dto.request.CancellationPoliciesRequestDto;
import org.raflab.rezervacijeservice.dto.response.BookingsStatusLogsResponseDto;
import org.raflab.rezervacijeservice.dto.response.CancellationPoliciesResponseDto;
import org.raflab.rezervacijeservice.model.CancellationPolicies;
import org.raflab.rezervacijeservice.repository.CancellationPoliciesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CancellationPoliciesService {

    @Autowired
    CancellationPoliciesRepository cancellationPoliciesRepository;

    public void save(CancellationPoliciesRequestDto cancellationPoliciesRequestDto){
        cancellationPoliciesRepository.save(CancellationPoliciesConverter.toCancellationPolicies(cancellationPoliciesRequestDto));
    }

    public void updateById(CancellationPoliciesRequestDto cancellationPoliciesRequestDto,Long id){
        if(cancellationPoliciesRepository.existsById(id)){

            CancellationPolicies cancellationPolicies = cancellationPoliciesRepository.findById(id).get();
            cancellationPolicies.setPolicyName(cancellationPolicies.getPolicyName());
            cancellationPolicies.setRefundPercentage(cancellationPolicies.getRefundPercentage());

            cancellationPoliciesRepository.save(cancellationPolicies);

        }else{
            cancellationPoliciesRepository.save(CancellationPoliciesConverter.toCancellationPolicies(cancellationPoliciesRequestDto));

        }
    }

    public List<CancellationPoliciesResponseDto> findAll(){
        return CancellationPoliciesConverter.toCancellationPoliciesResponseDtoList(cancellationPoliciesRepository.findAll());
    }
    public CancellationPoliciesResponseDto findById(Long id){
        return CancellationPoliciesConverter.toCancellationPoliciesResponseDto(cancellationPoliciesRepository.findById(id).get());
    }
    public void deleteById( Long id) {
        cancellationPoliciesRepository.deleteById(id);
    }
}
