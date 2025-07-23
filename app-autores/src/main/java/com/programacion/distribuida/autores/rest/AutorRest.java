package com.programacion.distribuida.autores.rest;

import com.programacion.distribuida.autores.modelo.Autor;
import com.programacion.distribuida.autores.repository.AutorRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Path("/autores")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@ApplicationScoped
@Transactional
public class AutorRest {
    // No necesario el @Transacctional, basta que esté en el Repository

    @Inject
    AutorRepository autorRepository;

    @ConfigProperty(name = "quarkus.http.port")
    Integer puerto;

    @GET
    @Path("/{id}")
    public Response buscarPorId(@PathParam("id") Integer id) {
        var autor = autorRepository.findByIdOptional(id);
        return autor.isEmpty()
                ? Response.status(Response.Status.NOT_FOUND).build()
                : Response.ok(autor.get()).build();
    }

    @GET
    public Response buscarTodos() {
        var autores = autorRepository.findAll().list();
        return autores.isEmpty()
                ? Response.status(Response.Status.NOT_FOUND).build()
                : Response.ok(autores).build();
    }

    @GET
    @Path("/libro/{isbn}")
    public Response buscarPorLibro(@PathParam("isbn") String isbn) {

        //simularErrores();

        var autores = autorRepository.buscarPorLibro(isbn);
        var autoresYPuerto = listaAutoresYPuerto(autores);

        return autores.isEmpty()
                ? Response.status(Response.Status.NOT_FOUND).build()
                : Response.ok(autoresYPuerto).build();
    }

    @POST
    public Response guardar(Autor autor) {
        if (autor == null) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        autorRepository.persist(autor);
        return Response.status(Response.Status.CREATED).entity(autor).build();
    }

    @PUT
    @Path("/{id}")
    public Response actualizar(@PathParam("id") Integer id, Autor autor) {
        if (autor == null) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        var autorExistente = autorRepository.findByIdOptional(id);
        if (autorExistente.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        autor.setId(id);
        autorRepository.getEntityManager().merge(autor);
        return Response.ok(autor).build();
    }

    @DELETE
    @Path("/{id}")
    public Response eliminar(@PathParam("id") Integer id) {
        var autorExistente = autorRepository.findByIdOptional(id);
        if (autorExistente.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        autorRepository.delete(autorExistente.get());
        return Response.noContent().build();
    }

    /**
     * Simulación de errores
     * Se usa un AtomicInteger para contar los intentos de llamada al endpoint, ya
     * que AtomicInteger permite un acceso
     * seguro en entornos concurrentes. Este es un entorno concurrente porque el
     * microservicio puede recibir múltiples
     * solicitudes al mismo tiempo, y cada solicitud incrementa el contador de
     * intentos.
     * AtomicInteger garantiza que las operaciones de incremento sean atómicas y
     * seguras entre hilos.
     */
    AtomicInteger contador = new AtomicInteger();

    private void simularErrores() {
        // Simulación de errores: De 5 llamadas, 4 son fallas y 1 éxito.
        int intento = contador.getAndIncrement();
        if (intento % 5 != 0) {
            String mensaje = String.format("Intento %d, generando error.", intento);
            System.out.println(mensaje);
            throw new RuntimeException(mensaje);
        }
    }

    private List<Autor> listaAutoresYPuerto(List<Autor> autores) {
        var autoresInfo = autores.stream()
                .map(autor -> {
                    var copia = new Autor();
                    copia.setId(autor.getId());
                    copia.setNombre(String.format("%s (Puerto: %d)", autor.getNombre(), puerto));
                    copia.setVersion(autor.getVersion());
                    return copia;
                }).toList();
        return autoresInfo;
    }

}
