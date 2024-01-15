package com.svalero.vuelosapi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AirplaneOutDto {

    private long id;

    private String model;

    private LocalDate manufacturingDate;

    private int passengerCapacity;

    private float maxSpeed;

    private boolean active;
    private long airlineId;

}
