package com.svalero.vuelosapi.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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

    @NotBlank(message = "El modelo no puede estar vacío")
    @Size(max = 25, message = "El modelo no puede tener más de 25 caracteres")
    @NotNull(message = "El modelo es obligatorio")
    @Column
    private String model;
    @NotNull(message = "El la fecha de manufacturación es obligatoria")
    @Column
    private LocalDate manufacturingDate;
    @Min(value = 0, message = "La capacidad debe ser mayor que cero")
    @Column
    @Positive
    private int passengerCapacity;
    @Column
    @Positive
    private float maxSpeed;
    @Column
    private boolean active;

    @ManyToOne
    @NotNull(message = "El Id de la aerolínea asociada es obligatorio")
    @JoinColumn(name = "airline_id")
    private Airline airline;

}