package org.example.gestion_psicologia.controlador;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import org.example.gestion_psicologia.model.HorarioDisponible;
import org.example.gestion_psicologia.model.Psicologo;
import org.example.gestion_psicologia.repository.horariodisponiblerepository.IHorarioDisponibleRepository;
import org.example.gestion_psicologia.repository.psicologorepository.IPsicologoRepository;

import java.time.DayOfWeek;
import java.time.LocalTime;
import javafx.util.StringConverter;

public class PsicologosController {
    private IPsicologoRepository psicologoRepository;
    private IHorarioDisponibleRepository horarioRepository;

    @FXML private TableView<Psicologo> psicologosTable;
    @FXML private TableColumn<Psicologo, Integer> idColumn;
    @FXML private TableColumn<Psicologo, String> nombreColumn;
    @FXML private TableColumn<Psicologo, String> idPsicologoColumn;
    @FXML private TableColumn<Psicologo, String> especialidadColumn;

    @FXML private TableView<HorarioDisponible> horariosTable;
    @FXML private TableColumn<HorarioDisponible, DayOfWeek> diaColumn;
    @FXML private TableColumn<HorarioDisponible, LocalTime> horaInicioColumn;
    @FXML private TableColumn<HorarioDisponible, LocalTime> horaFinColumn;

    @FXML private TextField nombreField;
    @FXML private TextField idPsicologoField;
    @FXML private TextField especialidadField;
    @FXML private ComboBox<DayOfWeek> diaCombo;
    @FXML private ComboBox<String> horaInicioCombo;
    @FXML private ComboBox<String> horaFinCombo;

    private ObservableList<Psicologo> psicologos;
    private ObservableList<HorarioDisponible> horarios;

    public PsicologosController() {
        this.psicologos = FXCollections.observableArrayList();
        this.horarios = FXCollections.observableArrayList();
    }

    public void setRepositories(IPsicologoRepository psicologoRepository,
                                IHorarioDisponibleRepository horarioRepository) {
        this.psicologoRepository = psicologoRepository;
        this.horarioRepository = horarioRepository;
        Platform.runLater(this::cargarPsicologos);
    }

    @FXML
    private void initialize() {
        psicologosTable.setEditable(true);
        configurarTablas();
        configurarCombos();

        psicologosTable.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldSelection, newSelection) -> {
                    if (newSelection != null) {
                        cargarHorarios(newSelection);
                        mostrarDatosPsicologo(newSelection);
                    }
                });
    }

    private void configurarTablas() {
        // Configurar columnas de psicólogos
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));

        nombreColumn.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        nombreColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        nombreColumn.setOnEditCommit(event -> {
            Psicologo psicologo = event.getRowValue();
            psicologo.setNombre(event.getNewValue());
            actualizarPsicologo(psicologo);
        });

        idPsicologoColumn.setCellValueFactory(new PropertyValueFactory<>("idPsicologo"));
        idPsicologoColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        idPsicologoColumn.setOnEditCommit(event -> {
            Psicologo psicologo = event.getRowValue();
            psicologo.setIdPsicologo(event.getNewValue());
            actualizarPsicologo(psicologo);
        });

        especialidadColumn.setCellValueFactory(new PropertyValueFactory<>("especialidad"));
        especialidadColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        especialidadColumn.setOnEditCommit(event -> {
            Psicologo psicologo = event.getRowValue();
            psicologo.setEspecialidad(event.getNewValue());
            actualizarPsicologo(psicologo);
        });

        // Configurar columnas de horarios
        diaColumn.setCellValueFactory(new PropertyValueFactory<>("dia"));
        horaInicioColumn.setCellValueFactory(new PropertyValueFactory<>("horaInicio"));
        horaFinColumn.setCellValueFactory(new PropertyValueFactory<>("horaFin"));
    }

    private void actualizarPsicologo(Psicologo psicologo) {
        try {
            psicologoRepository.update(psicologo);
            cargarPsicologos();
            mostrarExito("Actualización exitosa", "El psicólogo ha sido actualizado correctamente");
        } catch (Exception e) {
            mostrarError("Error al actualizar psicólogo", e.getMessage());
            cargarPsicologos();
        }
    }

    private void configurarCombos() {
        diaCombo.setItems(FXCollections.observableArrayList(
                DayOfWeek.MONDAY,
                DayOfWeek.TUESDAY,
                DayOfWeek.WEDNESDAY,
                DayOfWeek.THURSDAY,
                DayOfWeek.FRIDAY
        ));

        ObservableList<String> horas = FXCollections.observableArrayList();
        for (int i = 8; i <= 12; i++) {
            horas.add(String.format("%02d:00", i));
        }
        for (int i = 14; i <= 17; i++) {
            horas.add(String.format("%02d:00", i));
        }

        horaInicioCombo.setItems(horas);
        horaFinCombo.setItems(horas);
    }

    private void cargarPsicologos() {
        if (psicologoRepository != null) {
            psicologos.clear();
            psicologos.addAll(psicologoRepository.findAll());
            psicologosTable.setItems(psicologos);
        }
    }

    private void cargarHorarios(Psicologo psicologo) {
        if (horarioRepository != null) {
            horarios.clear();
            horarios.addAll(horarioRepository.findByPsicologo(psicologo));
            horariosTable.setItems(horarios);
        }
    }

    private void mostrarDatosPsicologo(Psicologo psicologo) {
        nombreField.setText(psicologo.getNombre());
        idPsicologoField.setText(psicologo.getIdPsicologo());
        especialidadField.setText(psicologo.getEspecialidad());
    }

    @FXML
    private void handleRegistrar() {
        if (psicologoRepository == null) {
            mostrarError("Error", "No se ha inicializado el repositorio");
            return;
        }

        try {
            Psicologo psicologo = new Psicologo();
            psicologo.setNombre(nombreField.getText());
            psicologo.setIdPsicologo(idPsicologoField.getText());
            psicologo.setEspecialidad(especialidadField.getText());

            psicologoRepository.save(psicologo);
            cargarPsicologos();
            limpiarCampos();
            mostrarExito("Registro exitoso", "El psicólogo ha sido registrado correctamente");
        } catch (Exception e) {
            mostrarError("Error al guardar psicólogo", e.getMessage());
        }
    }

    @FXML
    private void handleEliminar() {
        if (psicologoRepository == null) {
            mostrarError("Error", "No se ha inicializado el repositorio");
            return;
        }

        Psicologo psicologoSeleccionado = psicologosTable.getSelectionModel().getSelectedItem();
        if (psicologoSeleccionado == null) {
            mostrarError("Error", "Debe seleccionar un psicólogo para eliminar");
            return;
        }

        try {
            psicologoRepository.delete(psicologoSeleccionado);
            cargarPsicologos();
            limpiarCampos();
            mostrarExito("Eliminación exitosa", "El psicólogo ha sido eliminado correctamente");
        } catch (Exception e) {
            mostrarError("Error al eliminar psicólogo", e.getMessage());
        }
    }

    @FXML
    private void handleAgregarHorario() {
        if (horarioRepository == null) {
            mostrarError("Error", "No se ha inicializado el repositorio de horarios");
            return;
        }

        Psicologo psicologoSeleccionado = psicologosTable.getSelectionModel().getSelectedItem();
        if (psicologoSeleccionado == null) {
            mostrarError("Error", "Debe seleccionar un psicólogo");
            return;
        }

        try {
            LocalTime horaInicio = LocalTime.parse(horaInicioCombo.getValue());
            LocalTime horaFin = LocalTime.parse(horaFinCombo.getValue());
            DayOfWeek dia = diaCombo.getValue();

            if (dia == null) {
                mostrarError("Error", "Debe seleccionar un día");
                return;
            }

            if (horaInicio.isAfter(horaFin)) {
                mostrarError("Error", "La hora de inicio debe ser anterior a la hora de fin");
                return;
            }

            if (!esHorarioValido(horaInicio) || !esHorarioValido(horaFin)) {
                mostrarError("Error", "Las horas deben estar entre 8:00-12:00 o 14:00-17:00");
                return;
            }

            HorarioDisponible horario = new HorarioDisponible();
            horario.setPsicologo(psicologoSeleccionado);
            horario.setDia(dia);
            horario.setHoraInicio(horaInicio);
            horario.setHoraFin(horaFin);

            horarioRepository.save(horario);
            cargarHorarios(psicologoSeleccionado);
            limpiarCamposHorario();
            mostrarExito("Horario agregado", "El horario ha sido agregado correctamente");
        } catch (Exception e) {
            mostrarError("Error al guardar horario", e.getMessage());
        }
    }

    private boolean esHorarioValido(LocalTime hora) {
        return (hora.isAfter(LocalTime.of(7, 59)) && hora.isBefore(LocalTime.of(12, 1))) ||
                (hora.isAfter(LocalTime.of(13, 59)) && hora.isBefore(LocalTime.of(17, 1)));
    }

    @FXML
    private void handleLimpiar() {
        limpiarCampos();
    }

    private void limpiarCampos() {
        nombreField.clear();
        idPsicologoField.clear();
        especialidadField.clear();
        limpiarCamposHorario();
    }

    private void limpiarCamposHorario() {
        diaCombo.setValue(null);
        horaInicioCombo.setValue(null);
        horaFinCombo.setValue(null);
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