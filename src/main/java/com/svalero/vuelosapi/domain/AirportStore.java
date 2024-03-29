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
@Entity(name = "airportService")
public class AirportStore {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @NotNull(message = "El nombre es obligatorio")
    @NotBlank(message = "El nombre no puede estar vacío")
    @Size(max = 25, message = "El nombre no puede tener más de 25 caracteres")
    @Column
    private String name;
    @Column
    @NotNull(message = "El tipo de tienda es obligatorio")
    private String type;
    @Column
    @Positive
    private int telephone;
    @Column
    @Min(value = 0, message = "La media del beneficio debe ser mayor que cero")
    @NotNull(message = "La media del beneficio es obligatoria")
    private float averageProfit;
    @Column
    private LocalDate openingDay;
    @Column
    @NotNull(message = "ESi la tienda esta abierta es obligatorio")
    private boolean open;

    @ManyToOne
    @NotNull(message = "El Id del aeropuerto asociado es obligatorio")
    @JoinColumn(name = "airport_id")
    private Airport airport;

}