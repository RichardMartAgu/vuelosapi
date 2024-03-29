package com.svalero.vuelosapi.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;
import java.util.List;

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
    @Positive
    private int telephone;
    @NotNull(message = "El el año es obligatorio")
    @Column
    private LocalDate foundationYear;
    @Min(value = 0, message = "La flota debe ser mayor que cero")
    @NotNull(message = "La flota es obligatoria")
    @Column
    @Positive
    private int fleet;
    @Column
    private float onTime;
    @Column
    private boolean active;
    @ToString.Exclude
    @OneToMany(mappedBy = "airline")
    @JsonIgnore
    private List<Airplane> airplanes;

}