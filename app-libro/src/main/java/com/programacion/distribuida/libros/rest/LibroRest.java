package com.programacion.distribuida.libros.rest;

import com.programacion.distribuida.libros.clients.AutorRestClient;
import com.programacion.distribuida.libros.modelo.Libro;
import com.programacion.distribuida.libros.modelo.dto.LibroDto;
import com.programacion.distribuida.libros.repository.LibroRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.modelmapper.ModelMapper;

@Path("/libros")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@ApplicationScoped
public class LibroRest {

    @Inject
    private LibroRepository libroRepository;

    @Inject
    private ModelMapper modelMapper;

    @Inject
    @RestClient
    private AutorRestClient autorRestClient;

    @GET
    @Path("/{isbn}")
    public Response buscarPorIsbn(@PathParam("isbn") String isbn) {
        var libro = libroRepository.findByIdOptional(isbn);
        if (libro.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        var libroDto = convertirALibroDto(libro.get());
        return Response.ok(libroDto).build();
    }

    @GET
    public Response buscarTodos() {
        var libros = libroRepository.findAll().list();
        if (libros.isEmpty()) {
            return Response.status(Response.Status.NO_CONTENT).build();
        }
        var librosDto = libros.stream()
                .map(libro -> convertirALibroDto(libro))
                .toList();
        return Response.ok(librosDto).build();
    }

    private LibroDto convertirALibroDto(Libro libro) {
        var libroDto = new LibroDto();
        modelMapper.map(libro, libroDto);
       var autores = autorRestClient.buscarPorLibro(libro.getIsbn())
                .stream()
                .map(autorDto -> autorDto.getNombre())
                .toList();
        libroDto.setAutores(autores);
        return libroDto;
    }

}
