package com.example.labegr.entity;

import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProyectoEmpleadoId implements Serializable {

    private Long proyectoId;
    private Long empleadoId;

    // equals y hashCode obligatorios
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ProyectoEmpleadoId that)) return false;
        return Objects.equals(proyectoId, that.proyectoId) && Objects.equals(empleadoId, that.empleadoId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(proyectoId, empleadoId);
    }
}

