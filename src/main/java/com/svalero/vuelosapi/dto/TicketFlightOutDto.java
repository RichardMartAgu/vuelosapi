package com.svalero.vuelosapi.dto;

import com.svalero.vuelosapi.domain.Flight;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TicketFlightOutDto {
    private Long id;
    private String seatNumber;
    private LocalDate issuing;
    private boolean premium;
    private Long passengerId;
    private Flight flight;
}
