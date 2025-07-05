package com.programacion.distribuida.libros.rest;

import com.programacion.distribuida.libros.clients.AutorRestClient;
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

        var libroDto = new LibroDto();
        modelMapper.map(libro.get(), libroDto);
        var autores = autorRestClient.buscarPorLibro(isbn)
                .stream()
                .map(autorDto -> autorDto.getNombre())
                .toList();

        libroDto.setAutores(autores);
        return Response.ok(libroDto).build();
    }

}
