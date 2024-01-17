package com.svalero.vuelosapi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class AirportPatchDto {
    private String field;
    private String name;

}
