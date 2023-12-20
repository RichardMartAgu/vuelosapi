package com.svalero.vuelosapi.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "airplanes")
public class Airplane {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @NotNull(message = "El nombre es obligatorio")
    @NotBlank(message = "El nombre no puede estar vacío")
    @Size(max = 25, message = "El nombre no puede tener más de 25 caracteres")

    @NotNull(message = "El modelo es obligatorio")
    @Column
    private String model;
    @NotNull(message = "El la fecha de construcción es obligatoria")

    @Column
    private LocalDate manufacturingDate;
    @Min(value = 0, message = "La capacidad debe ser mayor que cero")
    @Column
    private int passengerCapacity;
    @Column
    private float maxSpeed;
    @Column
    private boolean active;

}