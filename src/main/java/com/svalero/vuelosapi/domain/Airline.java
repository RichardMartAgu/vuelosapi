package com.svalero.vuelosapi.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
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
@Entity(name = "airlines")
public class Airline {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @NotNull(message = "El nombre es obligatorio")
    @NotBlank(message = "El nombre no puede estar vacío")
    @Size(max = 25, message = "El nombre no puede tener más de 25 caracteres")
    @Column
    private String name;
    @NotNull(message = "El teléfono es obligatorio")
    @Column
    private int telephone;
    @NotNull(message = "El el año es obligatorio")
    @Column
    private LocalDate foundationYear;
    @Min(value = 0, message = "La flota debe ser mayor que cero")
    @NotNull(message = "La flota es obligatoria")
    @Column
    private int fleet;
    @Column
    private float onTime;
    @Column
    private boolean active;



}