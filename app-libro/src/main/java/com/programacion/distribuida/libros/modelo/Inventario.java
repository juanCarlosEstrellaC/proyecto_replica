package com.programacion.distribuida.libros.modelo;

import jakarta.json.bind.annotation.JsonbTransient;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "inventario")
public class Inventario {

    @Id
    @OneToOne
    @JsonbTransient  //TODO: Use esto para evitar bucle infinito en serialización JSON
    @JoinColumn(name = "isbn")
    private Libro libro;

    @Column(name = "vendidos")
    private Integer vendido;

    @Column(name = "suministrados")
    private Integer suministrado;
}
