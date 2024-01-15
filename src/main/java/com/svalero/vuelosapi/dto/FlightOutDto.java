package com.svalero.vuelosapi.dto;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FlightOutDto {

    private long id;
    private String name;
    private LocalDate departureDate;
    private int gate;
    private float duration;
    private boolean departure;
    private long  airportId;
}
