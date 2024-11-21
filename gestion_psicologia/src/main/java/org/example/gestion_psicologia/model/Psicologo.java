package org.example.gestion_psicologia.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;


import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import lombok.Data;
import lombok.ToString;
import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

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
@Table(name = "psicologos")
public class Psicologo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    @Column(name = "id_psicologo", nullable = false, unique = true)
    private String idPsicologo;

    @Column(nullable = false)
    private String especialidad;

    @OneToMany(mappedBy = "psicologo", cascade = CascadeType.ALL)
    private List<HorarioDisponible> horariosDisponibles;

    @OneToMany(mappedBy = "psicologo", cascade = CascadeType.ALL)
    private List<Cita> citas;
}
