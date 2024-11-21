package org.example.gestion_psicologia.repository.psicologorepository;

import org.example.gestion_psicologia.model.Psicologo;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

public class PsicologoRepositoryImpl implements IPsicologoRepository {
    private final EntityManager entityManager;

    public PsicologoRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public Optional<Psicologo> findById(Long id) {
        return Optional.ofNullable(entityManager.find(Psicologo.class, id));
    }

    @Override
    public List<Psicologo> findAll() {
        TypedQuery<Psicologo> query = entityManager.createQuery(
                "SELECT p FROM Psicologo p", Psicologo.class);
        return query.getResultList();
    }

    @Override
    public Optional<Psicologo> findByIdPsicologo(String idPsicologo) {
        TypedQuery<Psicologo> query = entityManager.createQuery(
                "SELECT p FROM Psicologo p WHERE p.idPsicologo = :idPsicologo",
                Psicologo.class);
        query.setParameter("idPsicologo", idPsicologo);
        return query.getResultList().stream().findFirst();
    }

    @Override
    public List<Psicologo> findByEspecialidad(String especialidad) {
        TypedQuery<Psicologo> query = entityManager.createQuery(
                "SELECT p FROM Psicologo p WHERE p.especialidad = :especialidad",
                Psicologo.class);
        query.setParameter("especialidad", especialidad);
        return query.getResultList();
    }

    @Override
    public void save(Psicologo psicologo) {
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            transaction.begin();
            entityManager.persist(psicologo);
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw new RuntimeException("Error al guardar el psicólogo", e);
        }
    }

    @Override
    public void update(Psicologo psicologo) {
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            transaction.begin();
            entityManager.merge(psicologo);
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw new RuntimeException("Error al actualizar el psicólogo", e);
        }
    }

    @Override
    public void delete(Psicologo psicologo) {
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            transaction.begin();
            entityManager.remove(psicologo);
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw new RuntimeException("Error al eliminar el psicólogo", e);
        }
    }
}