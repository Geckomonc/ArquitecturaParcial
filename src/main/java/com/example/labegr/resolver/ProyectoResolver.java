package com.example.labegr.resolver;

import com.example.labegr.entity.EmpleadoInput;
import com.example.labegr.entity.Proyecto;
import com.example.labegr.service.ProyectoService;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.time.LocalDate;
import java.util.List;

@Controller
public class ProyectoResolver {

    private final ProyectoService proyectoService;

    public ProyectoResolver(ProyectoService proyectoService) {
        this.proyectoService = proyectoService;
    }

    @MutationMapping
    public Proyecto addProyecto(@Argument String nombre,
                                @Argument String descripcion,
                                @Argument String fechaInicio,
                                @Argument String fechaFin,
                                @Argument String estado,
                                @Argument List<EmpleadoInput> empleados) {

        LocalDate inicio = LocalDate.parse(fechaInicio);
        LocalDate fin = LocalDate.parse(fechaFin);
        return proyectoService.addProyecto(nombre, descripcion, inicio, fin, estado, empleados);
    }

    @QueryMapping
    public List<Proyecto> proyectosPorCedulaEmpleado(@Argument String cedula) {
        return proyectoService.getProyectosPorCedulaEmpleado(cedula);
    }
}

