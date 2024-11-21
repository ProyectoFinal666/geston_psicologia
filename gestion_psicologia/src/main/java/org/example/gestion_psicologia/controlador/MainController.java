package org.example.gestion_psicologia.controlador;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.Parent;
import org.example.gestion_psicologia.repository.citarepository.CitaRepositoryImpl;
import org.example.gestion_psicologia.repository.citarepository.ICitaRepository;
import org.example.gestion_psicologia.repository.estudianterepository.EstudianteRepositoryImpl;
import org.example.gestion_psicologia.repository.estudianterepository.IEstudianteRepository;
import org.example.gestion_psicologia.repository.psicologorepository.IPsicologoRepository;
import org.example.gestion_psicologia.repository.psicologorepository.PsicologoRepositoryImpl;
import org.example.gestion_psicologia.repository.horariodisponiblerepository.IHorarioDisponibleRepository;
import org.example.gestion_psicologia.repository.horariodisponiblerepository.HorarioDisponibleRepositoryImpl;
import org.example.gestion_psicologia.util.JPAUtil;
import javax.persistence.EntityManager;

public class MainController {
    @FXML
    private TabPane mainTabPane;

    private final EntityManager entityManager;
    private final IEstudianteRepository estudianteRepository;
    private final IPsicologoRepository psicologoRepository;
    private final IHorarioDisponibleRepository horarioRepository;
    private final ICitaRepository citaRepository;

    public MainController() {
        this.entityManager = JPAUtil.getEntityManager();
        this.estudianteRepository = new EstudianteRepositoryImpl(entityManager);
        this.psicologoRepository = new PsicologoRepositoryImpl(entityManager);
        this.horarioRepository = new HorarioDisponibleRepositoryImpl(entityManager);
        this.citaRepository = new CitaRepositoryImpl(entityManager);
    }

    @FXML
    private void initialize() {
        crearPestañas();
    }

    private void crearPestañas() {
        try {
            // Pestaña de Estudiantes
            Tab estudiantesTab = new Tab("Estudiantes");
            FXMLLoader estudiantesLoader = new FXMLLoader(getClass().getResource("/view/EstudiantesView.fxml"));
            Parent estudiantesView = estudiantesLoader.load();
            EstudiantesController estudiantesController = estudiantesLoader.getController();
            estudiantesController.setEstudianteRepository(estudianteRepository);
            estudiantesTab.setContent(estudiantesView);

            // Pestaña de Psicólogos
            Tab psicologosTab = new Tab("Psicólogos");
            FXMLLoader psicologosLoader = new FXMLLoader(getClass().getResource("/view/PsicologosView.fxml"));
            Parent psicologosView = psicologosLoader.load();
            PsicologosController psicologosController = psicologosLoader.getController();
            psicologosController.setRepositories(psicologoRepository, horarioRepository);
            psicologosTab.setContent(psicologosView);

            // Pestaña de Citas
            Tab citasTab = new Tab("Citas");
            FXMLLoader citasLoader = new FXMLLoader(getClass().getResource("/view/CitasView.fxml"));
            Parent citasView = citasLoader.load();
            CitasController citasController = citasLoader.getController();
            citasController.setRepositories(citaRepository, estudianteRepository, psicologoRepository);
            citasTab.setContent(citasView);

            // Agregar todas las pestañas
            mainTabPane.getTabs().addAll(estudiantesTab, psicologosTab, citasTab);

        } catch (Exception e) {
            e.printStackTrace();
            mostrarError("Error al cargar las vistas", e.getMessage());
        }
    }

    private void mostrarError(String titulo, String mensaje) {
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.ERROR);
        alert.setTitle(titulo);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    // Método para limpiar recursos
    public void cleanup() {
        if (entityManager != null) {
            entityManager.close();
        }
    }
}