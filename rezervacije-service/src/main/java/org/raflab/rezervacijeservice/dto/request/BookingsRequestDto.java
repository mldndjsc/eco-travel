package org.raflab.rezervacijeservice.dto.request;

import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.raflab.rezervacijeservice.model.CancellationPolicies;
import org.raflab.rezervacijeservice.model.Guests;

import java.time.LocalDate;

@Data
public class BookingsRequestDto {

    @NotNull(message = "Morate uneti Id jedinicije za koju zelite booking")
    private Integer unitId;
    @NotNull(message = "Morate uneti osobu koja hoce da rezervise jedinicu(booking)")
    private GuestsRequestDto guestsRequestDto;
    @NotNull(message = "Morate izabrati politiku otkazivanja")
    private CancellationPoliciesRequestDto policyRequestDto;
    @NotNull(message = "Morate uneti pocetni datum bookinga")
    private LocalDate startDate;
    @NotNull(message = "Morate uneti krajnji datum bookinga")
    private LocalDate endDate;
    @NotNull(message = "Morate uneti cenu bookinga")
    private Double totalPrice;
    private String status;
}
