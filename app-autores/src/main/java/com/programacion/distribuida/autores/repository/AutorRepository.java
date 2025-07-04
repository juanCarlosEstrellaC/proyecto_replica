package com.programacion.distribuida.autores.repository;

import com.programacion.distribuida.autores.modelo.Autor;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

import java.util.List;

@ApplicationScoped //Indica que será un bean singleton que vive desde que la app inicia hasta que se detiene.
@Transactional
public class AutorRepository implements PanacheRepositoryBase<Autor, Integer> {

    /**
     * Busca los autores asociados a un libro dado su ISBN. (No es propio de panache, es una consulta JPQL)
     *
     * @param isbn El ISBN del libro.
     * @return Una lista de autores asociados al libro.
     */
    public List<Autor> buscarPorLibro(String isbn) {
        // Consulta JPQL para buscar los autores por el ISBN del libro.
        return this.find("SELECT la.autor FROM LibroAutor la WHERE la.id.libroIsbn = ?1", isbn).list();
    }
}
