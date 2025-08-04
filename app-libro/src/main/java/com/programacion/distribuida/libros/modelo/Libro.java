package com.programacion.distribuida.libros.modelo;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name = "libros")
public class Libro {

    @Id
    // No se usa @GeneratedValue porque el ISBN es un identificador string único, no autogenerado.
    @Column(length = 128)
    private String isbn;

    @Column(length = 128)
    private String titulo;

    @Column(precision = 12, scale = 2)
    private BigDecimal precio;

    private Integer version = 1;

    @OneToOne(mappedBy = "libro")
    private Inventario inventario;
    
}
