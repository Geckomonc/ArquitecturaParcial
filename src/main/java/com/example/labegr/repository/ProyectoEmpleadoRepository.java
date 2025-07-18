package com.example.labegr.repository;

import com.example.labegr.entity.ProyectoEmpleado;
import com.example.labegr.entity.ProyectoEmpleadoId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProyectoEmpleadoRepository extends JpaRepository<ProyectoEmpleado, ProyectoEmpleadoId> {
    List<ProyectoEmpleado> findByEmpleado_Cedula(String cedula);
}
