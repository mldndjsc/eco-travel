package org.raflab.rezervacijeservice.dto.response;

import lombok.Data;

@Data
public class CancellationPoliciesResponseDto {

    private String policyName;
    private int refundPercentage;
}
