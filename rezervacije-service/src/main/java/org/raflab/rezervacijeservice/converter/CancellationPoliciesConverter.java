package org.raflab.rezervacijeservice.converter;

import org.raflab.rezervacijeservice.dto.request.CancellationPoliciesRequestDto;
import org.raflab.rezervacijeservice.dto.response.CancellationPoliciesResponseDto;
import org.raflab.rezervacijeservice.model.CancellationPolicies;

import java.util.ArrayList;
import java.util.List;

public class CancellationPoliciesConverter {

    public static CancellationPolicies toCancellationPolicies(CancellationPoliciesRequestDto cancellationPoliciesRequestDto){
        CancellationPolicies  cancellationPolicies = new CancellationPolicies();
        cancellationPolicies.setPolicyName(cancellationPoliciesRequestDto.getPolicyName());
        cancellationPolicies.setRefundPercentage(cancellationPoliciesRequestDto.getRefundPercentage());

        return cancellationPolicies;
    }

    public static CancellationPolicies updateToCancellationPolicies(CancellationPoliciesRequestDto cancellationPoliciesRequestDto,Long id){
        CancellationPolicies  cancellationPolicies = new CancellationPolicies();
        cancellationPolicies.setId(id);
        cancellationPolicies.setPolicyName(cancellationPoliciesRequestDto.getPolicyName());
        cancellationPolicies.setRefundPercentage(cancellationPoliciesRequestDto.getRefundPercentage());

        return cancellationPolicies;
    }

    public static CancellationPoliciesResponseDto toCancellationPoliciesResponseDto(CancellationPolicies cancellationPolicies){
        CancellationPoliciesResponseDto cancellationPoliciesResponseDto = new CancellationPoliciesResponseDto();

        cancellationPoliciesResponseDto.setPolicyName(cancellationPolicies.getPolicyName());
        cancellationPoliciesResponseDto.setRefundPercentage(cancellationPolicies.getRefundPercentage());

        return cancellationPoliciesResponseDto;
    }

    public static List<CancellationPoliciesResponseDto> toCancellationPoliciesResponseDtoList(List<CancellationPolicies> cancellationPoliciesList){

        List<CancellationPoliciesResponseDto> cancellationPoliciesResponseDtoList = new ArrayList<>();

        for(CancellationPolicies cancellationPolicies : cancellationPoliciesList){
            cancellationPoliciesResponseDtoList.add(toCancellationPoliciesResponseDto(cancellationPolicies));
        }
        return cancellationPoliciesResponseDtoList;
    }
}
