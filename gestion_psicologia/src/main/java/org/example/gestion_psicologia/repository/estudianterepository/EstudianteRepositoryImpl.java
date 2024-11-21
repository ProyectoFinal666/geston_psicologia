package org.example.gestion_psicologia.repository.estudianterepository;


import org.example.gestion_psicologia.model.Estudiante;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

public class EstudianteRepositoryImpl implements IEstudianteRepository {
    private final EntityManager entityManager;

    public EstudianteRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public Optional<Estudiante> findById(Long id) {
        return Optional.ofNullable(entityManager.find(Estudiante.class, id));
    }

    @Override
    public List<Estudiante> findAll() {
        TypedQuery<Estudiante> query = entityManager.createQuery(
                "SELECT e FROM Estudiante e", Estudiante.class);
        return query.getResultList();
    }

    @Override
    public Optional<Estudiante> findByCodigoEstudiante(String codigo) {
        TypedQuery<Estudiante> query = entityManager.createQuery(
                "SELECT e FROM Estudiante e WHERE e.codigoEstudiante = :codigo",
                Estudiante.class);
        query.setParameter("codigo", codigo);
        return query.getResultList().stream().findFirst();
    }

    @Override
    public void save(Estudiante estudiante) {
        entityManager.getTransaction().begin();
        entityManager.persist(estudiante);
        entityManager.getTransaction().commit();
    }

    @Override
    public void update(Estudiante estudiante) {
        entityManager.getTransaction().begin();
        entityManager.merge(estudiante);
        entityManager.getTransaction().commit();
    }

    @Override
    public void delete(Estudiante estudiante) {
        entityManager.getTransaction().begin();
        entityManager.remove(estudiante);
        entityManager.getTransaction().commit();
    }
}