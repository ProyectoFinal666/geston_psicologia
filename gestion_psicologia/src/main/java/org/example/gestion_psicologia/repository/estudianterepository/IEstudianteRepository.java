package org.example.gestion_psicologia.repository.estudianterepository;




import org.example.gestion_psicologia.model.Estudiante;

import java.util.List;
import java.util.Optional;

public interface IEstudianteRepository {
    Optional<Estudiante> findById(Long id);
    List<Estudiante> findAll();
    Optional<Estudiante> findByCodigoEstudiante(String codigo);
    void save(Estudiante estudiante);
    void update(Estudiante estudiante);
    void delete(Estudiante estudiante);
}
