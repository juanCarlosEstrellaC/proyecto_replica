package com.programacion.distribuida.controller;

import com.programacion.distribuida.modelo.Autor;
import com.programacion.distribuida.repository.AutorRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.config.ConfigProvider;

@Path("/autores")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@ApplicationScoped
public class AutorController {
    // No necesario el @Transacctional, basta que esté en el Repository

    @Inject
    AutorRepository autorRepository;

    @GET
    @Path("/{id}")
    public Response buscarPorId(@PathParam("id") Integer id) {
        var autor = autorRepository.findByIdOptional(id);
        if (autor.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(autor.get()).build();
    }

    @GET
    public Response buscarTodos() {
        var autores = autorRepository.findAll().list();
        return Response.ok(autores).build();
    }

    @GET
    @Path("/libro/{isbn}")
    public Response buscarPorLibro(@PathParam("isbn") String isbn) {

        // Falta simulación de errores

        var puerto = ConfigProvider.getConfig().getValue("quarkus.http.port", Integer.class);
        var autores = autorRepository.buscarPorLibro(isbn);
        if (autores.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND).build();
        } else {
            var autoresInfo = autores.stream()
                    .map(autor -> {
                        var copia = new Autor();
                        copia.setId(autor.getId());
                        copia.setNombre(String.format("%s (Puerto: %d)", autor.getNombre(), puerto));
                        copia.setVersion(autor.getVersion());
                        return copia;
                    }).toList();
            return Response.ok(autoresInfo).build();
        }
    }
}
