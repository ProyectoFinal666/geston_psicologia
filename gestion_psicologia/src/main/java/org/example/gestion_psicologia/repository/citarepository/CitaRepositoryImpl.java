package org.example.gestion_psicologia.repository.citarepository;



import org.example.gestion_psicologia.model.Cita;
import org.example.gestion_psicologia.model.Estudiante;
import org.example.gestion_psicologia.model.Psicologo;
import org.example.gestion_psicologia.model.enums.EstadoCita;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class CitaRepositoryImpl implements ICitaRepository {
    private final EntityManager entityManager;

    public CitaRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public Optional<Cita> findById(Long id) {
        return Optional.ofNullable(entityManager.find(Cita.class, id));
    }

    @Override
    public List<Cita> findAll() {
        TypedQuery<Cita> query = entityManager.createQuery(
                "SELECT c FROM Cita c", Cita.class);
        return query.getResultList();
    }

    @Override
    public List<Cita> findByEstudiante(Estudiante estudiante) {
        TypedQuery<Cita> query = entityManager.createQuery(
                "SELECT c FROM Cita c WHERE c.estudiante = :estudiante",
                Cita.class);
        query.setParameter("estudiante", estudiante);
        return query.getResultList();
    }

    @Override
    public List<Cita> findByPsicologo(Psicologo psicologo) {
        TypedQuery<Cita> query = entityManager.createQuery(
                "SELECT c FROM Cita c WHERE c.psicologo = :psicologo",
                Cita.class);
        query.setParameter("psicologo", psicologo);
        return query.getResultList();
    }

    @Override
    public List<Cita> findByFechaHora(LocalDateTime inicio, LocalDateTime fin) {
        TypedQuery<Cita> query = entityManager.createQuery(
                "SELECT c FROM Cita c WHERE c.fechaHora BETWEEN :inicio AND :fin",
                Cita.class);
        query.setParameter("inicio", inicio);
        query.setParameter("fin", fin);
        return query.getResultList();
    }

    @Override
    public List<Cita> findByEstado(EstadoCita estado) {
        TypedQuery<Cita> query = entityManager.createQuery(
                "SELECT c FROM Cita c WHERE c.estado = :estado",
                Cita.class);
        query.setParameter("estado", estado);
        return query.getResultList();
    }

    @Override
    public void save(Cita cita) {
        entityManager.getTransaction().begin();
        entityManager.persist(cita);
        entityManager.getTransaction().commit();
    }

    @Override
    public void update(Cita cita) {
        entityManager.getTransaction().begin();
        entityManager.merge(cita);
        entityManager.getTransaction().commit();
    }

    @Override
    public void delete(Cita cita) {
        entityManager.getTransaction().begin();
        entityManager.remove(cita);
        entityManager.getTransaction().commit();
    }
}