package com.svalero.vuelosapi.dto;

import com.svalero.vuelosapi.domain.Passenger;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TicketPassengerOutDto {
    private Long id;
    private String seatNumber;
    private LocalDate issuing;
    private boolean premium;
    private Long flightId;
    private Passenger passenger;


}
