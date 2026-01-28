package com.programacion.distribuida.autores.modelo;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "autores_libros")
public class AutorLibro {

    @EmbeddedId
    private AutorLibroId id;

    @ManyToOne
    @MapsId("autorId")
    @JoinColumn(name = "autor_id")
    private Autor autor;
}
