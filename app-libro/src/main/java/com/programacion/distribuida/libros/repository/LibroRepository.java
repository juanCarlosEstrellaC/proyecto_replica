package com.programacion.distribuida.libros.repository;

import com.programacion.distribuida.libros.modelo.Libro;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

@ApplicationScoped
@Transactional
public class LibroRepository implements PanacheRepositoryBase<Libro, String> {

}
