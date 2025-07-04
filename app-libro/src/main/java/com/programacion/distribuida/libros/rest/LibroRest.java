package com.programacion.distribuida.libros.rest;

import com.programacion.distribuida.libros.repository.LibroRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/libros")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@ApplicationScoped
public class LibroRest {

    @Inject
    private LibroRepository libroRepository;

    @GET
    @Path("/{isbn}")
    public Response buscarPorIsbn(@PathParam("isbn") String isbn) {
        var libro = libroRepository.findByIdOptional(isbn);
        if (libro.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(libro).build();
    }
}
