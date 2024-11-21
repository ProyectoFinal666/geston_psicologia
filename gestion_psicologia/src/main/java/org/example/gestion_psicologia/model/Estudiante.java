package org.example.gestion_psicologia.model;


import lombok.Data;


import javax.persistence.*;
import java.util.List;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import lombok.ToString;
import java.util.ArrayList;


import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import javax.persistence.*;
import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import javax.persistence.*;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "estudiantes")
public class Estudiante {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    @Column(name = "codigo_estudiante", nullable = false, unique = true)
    private String codigoEstudiante;

    @Column(nullable = false)
    private String correo;

    @Column(nullable = false)
    private String telefono;

    @OneToMany(mappedBy = "estudiante", cascade = CascadeType.ALL)
    private List<Cita> citas;
}