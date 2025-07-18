package com.example.labegr.service;

import com.example.labegr.entity.*;
import com.example.labegr.repository.EmpleadoRepository;
import com.example.labegr.repository.ProyectoEmpleadoRepository;
import com.example.labegr.repository.ProyectoRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class ProyectoService {

    private final ProyectoRepository proyectoRepository;
    private final EmpleadoRepository empleadoRepository;
    private final ProyectoEmpleadoRepository proyectoEmpleadoRepository;

    public ProyectoService(ProyectoRepository proyectoRepository, EmpleadoRepository empleadoRepository,
                           ProyectoEmpleadoRepository proyectoEmpleadoRepository) {
        this.proyectoRepository = proyectoRepository;
        this.empleadoRepository = empleadoRepository;
        this.proyectoEmpleadoRepository = proyectoEmpleadoRepository;
    }

    public Proyecto addProyecto(String nombre, String descripcion, LocalDate fechaInicio, LocalDate fechaFin, String estado,
                                List<EmpleadoInput> empleadosInput) {

        Proyecto proyecto = new Proyecto();
        proyecto.setNombre(nombre);
        proyecto.setDescripcion(descripcion);
        proyecto.setFechaInicio(fechaInicio);
        proyecto.setFechaFin(fechaFin);
        proyecto.setEstado(estado);

        List<ProyectoEmpleado> asignaciones = new ArrayList<>();

        for (EmpleadoInput input : empleadosInput) {
            Empleado empleado = empleadoRepository.findByCedula(input.getCedula())
                    .orElseGet(() -> {
                        Empleado nuevo = new Empleado();
                        nuevo.setNombre(input.getNombre());
                        nuevo.setApellido(input.getApellido());
                        nuevo.setCedula(input.getCedula());
                        return empleadoRepository.save(nuevo);
                    });

            ProyectoEmpleado pe = new ProyectoEmpleado();
            pe.setEmpleado(empleado);
            pe.setProyecto(proyecto);
            pe.setRol(input.getRol());
            pe.setId(new ProyectoEmpleadoId(null, null)); // se completará al guardar
            asignaciones.add(pe);
        }

        proyecto.setEmpleadosAsignados(asignaciones);
        return proyectoRepository.save(proyecto);
    }

    public List<Proyecto> getProyectosPorCedulaEmpleado(String cedula) {
        List<ProyectoEmpleado> asignaciones = proyectoEmpleadoRepository.findByEmpleado_Cedula(cedula);
        List<Proyecto> proyectos = new ArrayList<>();
        for (ProyectoEmpleado pe : asignaciones) {
            proyectos.add(pe.getProyecto());
        }
        return proyectos;
    }
}

