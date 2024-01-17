package com.svalero.vuelosapi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AirplanePatchDto {
    private String field;
    private int maxSpeed;
}


