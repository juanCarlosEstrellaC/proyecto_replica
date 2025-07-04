package com.programacion.distribuida.autores.modelo;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "libros_autores")
public class LibroAutor {

    @EmbeddedId
    private LibroAutorId id;

    @ManyToOne
    @MapsId("autorId")
    @JoinColumn(name = "autor_id")
    private Autor autor;
}
