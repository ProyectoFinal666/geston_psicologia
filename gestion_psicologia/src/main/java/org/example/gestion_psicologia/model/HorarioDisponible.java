package org.example.gestion_psicologia.model;


import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import javax.persistence.*;
import java.time.LocalTime;
import java.time.DayOfWeek;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "horarios_disponibles")
public class HorarioDisponible {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "psicologo_id", nullable = false)
    private Psicologo psicologo;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DayOfWeek dia;

    @Column(name = "hora_inicio", nullable = false)
    private LocalTime horaInicio;

    @Column(name = "hora_fin", nullable = false)
    private LocalTime horaFin;
}
