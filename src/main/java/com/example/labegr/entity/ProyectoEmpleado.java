package com.example.labegr.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "proyecto_empleado")
public class ProyectoEmpleado {
    @EmbeddedId
    private ProyectoEmpleadoId id = new ProyectoEmpleadoId();

    @ManyToOne
    @MapsId("proyectoId")
    @JoinColumn(name = "proyecto_id")
    private Proyecto proyecto;

    @ManyToOne
    @MapsId("empleadoId")
    @JoinColumn(name = "empleado_id")
    private Empleado empleado;

    private String rol;
}
