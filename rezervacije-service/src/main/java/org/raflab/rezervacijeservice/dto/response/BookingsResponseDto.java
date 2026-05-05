package org.raflab.rezervacijeservice.dto.response;

import jakarta.persistence.ManyToOne;
import lombok.Data;
import org.raflab.rezervacijeservice.model.CancellationPolicies;
import org.raflab.rezervacijeservice.model.Guests;

import java.time.LocalDate;

@Data
public class BookingsResponseDto {

    private final Integer unitId;
    private final GuestsResponseDto guestsResponseDto;
    private final CancellationPoliciesResponseDto policyResponseDto;
    private final LocalDate startDate;
    private final LocalDate endDate;
    private final Double totalPrice;
    private final String status;

    private BookingsResponseDto(BookingsResponseDtoBuilder builder) {
        this.unitId = builder.unitId;
        this.guestsResponseDto = builder.guestsResponseDto;
        this.policyResponseDto = builder.policyResponseDto;
        this.startDate = builder.startDate;
        this.endDate = builder.endDate;
        this.totalPrice = builder.totalPrice;
        this.status = builder.status;
    }

    public Integer getUnitId() { return unitId; }
    public GuestsResponseDto getGuestsResponseDto() { return guestsResponseDto; }
    public CancellationPoliciesResponseDto getPolicyResponseDto() { return policyResponseDto; }
    public LocalDate getStartDate() { return startDate; }
    public LocalDate getEndDate() { return endDate; }
    public Double getTotalPrice() { return totalPrice; }
    public String getStatus() { return status; }

    public static class BookingsResponseDtoBuilder {
        private Integer unitId;
        private GuestsResponseDto guestsResponseDto;
        private CancellationPoliciesResponseDto policyResponseDto;
        private LocalDate startDate;
        private LocalDate endDate;
        private Double totalPrice;
        private String status;

        public BookingsResponseDtoBuilder unitId(Integer unitId) {
            this.unitId = unitId;
            return this;
        }

        public BookingsResponseDtoBuilder guestsResponseDto(GuestsResponseDto guestsResponseDto) {
            this.guestsResponseDto = guestsResponseDto;
            return this;
        }

        public BookingsResponseDtoBuilder policyResponseDto(CancellationPoliciesResponseDto policyResponseDto) {
            this.policyResponseDto = policyResponseDto;
            return this;
        }

        public BookingsResponseDtoBuilder startDate(LocalDate startDate) {
            this.startDate = startDate;
            return this;
        }

        public BookingsResponseDtoBuilder endDate(LocalDate endDate) {
            this.endDate = endDate;
            return this;
        }

        public BookingsResponseDtoBuilder totalPrice(Double totalPrice) {
            this.totalPrice = totalPrice;
            return this;
        }

        public BookingsResponseDtoBuilder status(String status) {
            this.status = status;
            return this;
        }

        public BookingsResponseDto build() {
            return new BookingsResponseDto(this);
        }
    }
}
