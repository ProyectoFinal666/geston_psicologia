package org.example.gestion_psicologia.repository.psicologorepository;



import org.example.gestion_psicologia.model.Psicologo;

import java.util.List;
import java.util.Optional;

public interface IPsicologoRepository {
    Optional<Psicologo> findById(Long id);
    List<Psicologo> findAll();
    Optional<Psicologo> findByIdPsicologo(String idPsicologo);
    List<Psicologo> findByEspecialidad(String especialidad);
    void save(Psicologo psicologo);
    void update(Psicologo psicologo);
    void delete(Psicologo psicologo);
}