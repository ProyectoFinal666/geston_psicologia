package org.example.gestion_psicologia.repository.horariodisponiblerepository;


import org.example.gestion_psicologia.model.HorarioDisponible;
import org.example.gestion_psicologia.model.Psicologo;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public class HorarioDisponibleRepositoryImpl implements IHorarioDisponibleRepository {
    private final EntityManager entityManager;

    public HorarioDisponibleRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public Optional<HorarioDisponible> findById(Long id) {
        return Optional.ofNullable(entityManager.find(HorarioDisponible.class, id));
    }

    @Override
    public List<HorarioDisponible> findAll() {
        TypedQuery<HorarioDisponible> query = entityManager.createQuery(
                "SELECT h FROM HorarioDisponible h", HorarioDisponible.class);
        return query.getResultList();
    }

    @Override
    public List<HorarioDisponible> findByPsicologo(Psicologo psicologo) {
        TypedQuery<HorarioDisponible> query = entityManager.createQuery(
                "SELECT h FROM HorarioDisponible h WHERE h.psicologo = :psicologo",
                HorarioDisponible.class);
        query.setParameter("psicologo", psicologo);
        return query.getResultList();
    }

    @Override
    public List<HorarioDisponible> findByDia(DayOfWeek dia) {
        TypedQuery<HorarioDisponible> query = entityManager.createQuery(
                "SELECT h FROM HorarioDisponible h WHERE h.dia = :dia",
                HorarioDisponible.class);
        query.setParameter("dia", dia);
        return query.getResultList();
    }

    @Override
    public List<HorarioDisponible> findByPsicologoAndDia(Psicologo psicologo, DayOfWeek dia) {
        TypedQuery<HorarioDisponible> query = entityManager.createQuery(
                "SELECT h FROM HorarioDisponible h WHERE h.psicologo = :psicologo AND h.dia = :dia",
                HorarioDisponible.class);
        query.setParameter("psicologo", psicologo);
        query.setParameter("dia", dia);
        return query.getResultList();
    }

    @Override
    public List<HorarioDisponible> findDisponiblesByRangoHorario(LocalTime inicio, LocalTime fin) {
        TypedQuery<HorarioDisponible> query = entityManager.createQuery(
                "SELECT h FROM HorarioDisponible h WHERE h.horaInicio >= :inicio AND h.horaFin <= :fin",
                HorarioDisponible.class);
        query.setParameter("inicio", inicio);
        query.setParameter("fin", fin);
        return query.getResultList();
    }

    @Override
    public void save(HorarioDisponible horario) {
        entityManager.getTransaction().begin();
        entityManager.persist(horario);
        entityManager.getTransaction().commit();
    }

    @Override
    public void update(HorarioDisponible horario) {
        entityManager.getTransaction().begin();
        entityManager.merge(horario);
        entityManager.getTransaction().commit();
    }

    @Override
    public void delete(HorarioDisponible horario) {
        entityManager.getTransaction().begin();
        entityManager.remove(horario);
        entityManager.getTransaction().commit();
    }
}