package org.example.gestion_psicologia.repository.citarepository;


import org.example.gestion_psicologia.model.Cita;
import org.example.gestion_psicologia.model.Estudiante;
import org.example.gestion_psicologia.model.Psicologo;
import org.example.gestion_psicologia.model.enums.EstadoCita;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ICitaRepository {
    Optional<Cita> findById(Long id);
    List<Cita> findAll();
    List<Cita> findByEstudiante(Estudiante estudiante);
    List<Cita> findByPsicologo(Psicologo psicologo);
    List<Cita> findByFechaHora(LocalDateTime inicio, LocalDateTime fin);
    List<Cita> findByEstado(EstadoCita estado);
    void save(Cita cita);
    void update(Cita cita);
    void delete(Cita cita);
}