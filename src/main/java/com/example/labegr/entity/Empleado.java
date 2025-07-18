package com.example.labegr.entity;
import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "empleado")
public class Empleado {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private String apellido;

    @Column(unique = true)
    private String cedula;

    @OneToMany(mappedBy = "empleado", cascade = CascadeType.ALL)
    private List<ProyectoEmpleado> proyectosAsignados;
}
