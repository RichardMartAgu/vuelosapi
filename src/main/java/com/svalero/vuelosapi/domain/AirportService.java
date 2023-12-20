package com.svalero.vuelosapi.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "airport")
public class AirportService {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @NotNull(message = "El nombre es obligatorio")
    @NotBlank(message = "El nombre no puede estar vacío")
    @Size(max = 25, message = "El nombre no puede tener más de 25 caracteres")
    @Column
    private String name;
    @NotNull(message = "La ciudad es obligatoria")
    @Column
    private String city;
    @NotNull(message = "El año de fundación es obligatoria")
    @Column
    private LocalDate foundationYear;
    @Column
    private float latitude;
    @Column
    private float longitude;
    @Column
    private boolean active;

}