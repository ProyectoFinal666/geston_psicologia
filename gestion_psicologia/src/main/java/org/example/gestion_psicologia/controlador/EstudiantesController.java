package org.example.gestion_psicologia.controlador;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import org.example.gestion_psicologia.model.Estudiante;
import org.example.gestion_psicologia.repository.estudianterepository.IEstudianteRepository;
import javafx.util.StringConverter;

public class EstudiantesController {
    private IEstudianteRepository estudianteRepository;

    @FXML private TableView<Estudiante> estudiantesTable;
    @FXML private TableColumn<Estudiante, Integer> idColumn;
    @FXML private TableColumn<Estudiante, String> nombreColumn;
    @FXML private TableColumn<Estudiante, String> codigoColumn;
    @FXML private TableColumn<Estudiante, String> correoColumn;
    @FXML private TableColumn<Estudiante, String> telefonoColumn;

    @FXML private TextField nombreField;
    @FXML private TextField codigoField;
    @FXML private TextField correoField;
    @FXML private TextField telefonoField;

    private ObservableList<Estudiante> estudiantes;

    public EstudiantesController() {
        this.estudiantes = FXCollections.observableArrayList();
    }

    public void setEstudianteRepository(IEstudianteRepository estudianteRepository) {
        this.estudianteRepository = estudianteRepository;
        Platform.runLater(this::cargarEstudiantes);
    }

    @FXML
    private void initialize() {
        estudiantesTable.setEditable(true);
        configurarTabla();

        estudiantesTable.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldSelection, newSelection) -> {
                    if (newSelection != null) {
                        mostrarDatosEstudiante(newSelection);
                    }
                });
    }

    private void configurarTabla() {
        // Configurar columnas editables
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));

        nombreColumn.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        nombreColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        nombreColumn.setOnEditCommit(event -> {
            Estudiante estudiante = event.getRowValue();
            estudiante.setNombre(event.getNewValue());
            actualizarEstudiante(estudiante);
        });

        codigoColumn.setCellValueFactory(new PropertyValueFactory<>("codigoEstudiante"));
        codigoColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        codigoColumn.setOnEditCommit(event -> {
            Estudiante estudiante = event.getRowValue();
            estudiante.setCodigoEstudiante(event.getNewValue());
            actualizarEstudiante(estudiante);
        });

        correoColumn.setCellValueFactory(new PropertyValueFactory<>("correo"));
        correoColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        correoColumn.setOnEditCommit(event -> {
            Estudiante estudiante = event.getRowValue();
            estudiante.setCorreo(event.getNewValue());
            actualizarEstudiante(estudiante);
        });

        telefonoColumn.setCellValueFactory(new PropertyValueFactory<>("telefono"));
        telefonoColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        telefonoColumn.setOnEditCommit(event -> {
            Estudiante estudiante = event.getRowValue();
            estudiante.setTelefono(event.getNewValue());
            actualizarEstudiante(estudiante);
        });
    }

    private void actualizarEstudiante(Estudiante estudiante) {
        try {
            estudianteRepository.update(estudiante);
            cargarEstudiantes();
            mostrarExito("Actualización exitosa", "El estudiante ha sido actualizado correctamente");
        } catch (Exception e) {
            mostrarError("Error al actualizar estudiante", e.getMessage());
            cargarEstudiantes(); // Recargar datos originales en caso de error
        }
    }

    private void cargarEstudiantes() {
        if (estudianteRepository == null) {
            mostrarError("Error", "EstudianteRepository no ha sido inicializado");
            return;
        }
        estudiantes.clear();
        estudiantes.addAll(estudianteRepository.findAll());
        estudiantesTable.setItems(estudiantes);
    }

    private void mostrarDatosEstudiante(Estudiante estudiante) {
        nombreField.setText(estudiante.getNombre());
        codigoField.setText(estudiante.getCodigoEstudiante());
        correoField.setText(estudiante.getCorreo());
        telefonoField.setText(estudiante.getTelefono());
    }

    @FXML
    private void handleRegistrar() {
        if (estudianteRepository == null) {
            mostrarError("Error", "EstudianteRepository no ha sido inicializado");
            return;
        }

        try {
            Estudiante estudiante = new Estudiante();
            estudiante.setNombre(nombreField.getText());
            estudiante.setCodigoEstudiante(codigoField.getText());
            estudiante.setCorreo(correoField.getText());
            estudiante.setTelefono(telefonoField.getText());

            estudianteRepository.save(estudiante);
            cargarEstudiantes();
            limpiarCampos();
            mostrarExito("Registro exitoso", "El estudiante ha sido registrado correctamente");
        } catch (Exception e) {
            mostrarError("Error al guardar estudiante", e.getMessage());
        }
    }

    @FXML
    private void handleEliminar() {
        if (estudianteRepository == null) {
            mostrarError("Error", "EstudianteRepository no ha sido inicializado");
            return;
        }

        Estudiante estudianteSeleccionado = estudiantesTable.getSelectionModel().getSelectedItem();
        if (estudianteSeleccionado == null) {
            mostrarError("Error", "Debe seleccionar un estudiante para eliminar");
            return;
        }

        try {
            estudianteRepository.delete(estudianteSeleccionado);
            cargarEstudiantes();
            limpiarCampos();
            mostrarExito("Eliminación exitosa", "El estudiante ha sido eliminado correctamente");
        } catch (Exception e) {
            mostrarError("Error al eliminar estudiante", e.getMessage());
        }
    }

    @FXML
    private void handleLimpiar() {
        limpiarCampos();
    }

    private void limpiarCampos() {
        nombreField.clear();
        codigoField.clear();
        correoField.clear();
        telefonoField.clear();
    }

    private void mostrarError(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titulo);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    private void mostrarExito(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}