package com.svalero.vuelosapi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AirlineDto {
        private String name;
        private int telephone;
        private LocalDate foundationYear;
        private int fleet;
        private float onTime;
        private boolean active;
}
