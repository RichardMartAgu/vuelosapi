package com.svalero.vuelosapi.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AirportStoreOutDto {
    private long id;

    private String name;

    private String type;

    private int telephone;

    private float averageProfit;

    private LocalDate openingDay;

    private boolean open;
    private long airportId;
}
