package com.svalero.vuelosapi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TicketOutDto {
    private Long id;
    private String seatNumber;
    private LocalDate issuing;
    private int baggage;
    private boolean premium;
    private Long passengerId;
    private Long flightId;
}
