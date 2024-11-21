package org.example.gestion_psicologia.repository.horariodisponiblerepository;


import org.example.gestion_psicologia.model.HorarioDisponible;
import org.example.gestion_psicologia.model.Psicologo;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public interface IHorarioDisponibleRepository {
    Optional<HorarioDisponible> findById(Long id);
    List<HorarioDisponible> findAll();
    List<HorarioDisponible> findByPsicologo(Psicologo psicologo);
    List<HorarioDisponible> findByDia(DayOfWeek dia);
    List<HorarioDisponible> findByPsicologoAndDia(Psicologo psicologo, DayOfWeek dia);
    List<HorarioDisponible> findDisponiblesByRangoHorario(LocalTime inicio, LocalTime fin);
    void save(HorarioDisponible horario);
    void update(HorarioDisponible horario);
    void delete(HorarioDisponible horario);
}