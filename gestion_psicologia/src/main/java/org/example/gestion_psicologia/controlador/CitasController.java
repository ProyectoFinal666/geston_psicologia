package org.example.gestion_psicologia.controlador;

import javafx.application.Platform;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.StringConverter;
import org.example.gestion_psicologia.model.*;
import org.example.gestion_psicologia.model.enums.EstadoCita;
import org.example.gestion_psicologia.repository.citarepository.ICitaRepository;
import org.example.gestion_psicologia.repository.estudianterepository.IEstudianteRepository;
import org.example.gestion_psicologia.repository.psicologorepository.IPsicologoRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.concurrent.CompletableFuture;

public class CitasController {
    @FXML private TableView<Cita> citasTable;
    @FXML private TableColumn<Cita, Integer> idColumn;
    @FXML private TableColumn<Cita, Estudiante> estudianteColumn;
    @FXML private TableColumn<Cita, Psicologo> psicologoColumn;
    @FXML private TableColumn<Cita, LocalDateTime> fechaColumn;
    @FXML private TableColumn<Cita, LocalTime> horaColumn;
    @FXML private TableColumn<Cita, EstadoCita> estadoColumn;

    @FXML private ComboBox<Estudiante> estudianteCombo;
    @FXML private ComboBox<Psicologo> psicologoCombo;
    @FXML private DatePicker fechaPicker;
    @FXML private ComboBox<LocalTime> horaCombo;
    @FXML private ComboBox<EstadoCita> estadoCombo;

    private ICitaRepository citaRepository;
    private IEstudianteRepository estudianteRepository;
    private IPsicologoRepository psicologoRepository;
    private ObservableList<Cita> citas;
    private ObservableList<Estudiante> estudiantes;
    private ObservableList<Psicologo> psicologos;
    private ObservableList<LocalTime> horas;
    private volatile boolean isLoading = false;

    public CitasController() {
        this.citas = FXCollections.observableArrayList();
        this.estudiantes = FXCollections.observableArrayList();
        this.psicologos = FXCollections.observableArrayList();
        this.horas = FXCollections.observableArrayList();
    }

    @FXML
    private void initialize() {
        configurarTablaEditable();
        configurarCombos();

        citasTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null && !isLoading) {
                cargarDetallesCita(newSelection);
            }
        });
    }

    private void configurarTablaEditable() {
        citasTable.setEditable(true);

        idColumn.setCellValueFactory(new PropertyValueFactory<>("idCita"));

        estudianteColumn.setCellValueFactory(new PropertyValueFactory<>("estudiante"));
        estudianteColumn.setCellFactory(ComboBoxTableCell.forTableColumn(
                new StringConverter<Estudiante>() {
                    @Override
                    public String toString(Estudiante estudiante) {
                        return estudiante == null ? null : estudiante.getNombre();
                    }

                    @Override
                    public Estudiante fromString(String string) {
                        return null;
                    }
                },
                estudiantes
        ));
        estudianteColumn.setOnEditCommit(event -> {
            if (!isLoading) {
                Cita cita = event.getRowValue();
                cita.setEstudiante(event.getNewValue());
                actualizarCita(cita);
            }
        });

        psicologoColumn.setCellValueFactory(new PropertyValueFactory<>("psicologo"));
        psicologoColumn.setCellFactory(ComboBoxTableCell.forTableColumn(
                new StringConverter<Psicologo>() {
                    @Override
                    public String toString(Psicologo psicologo) {
                        return psicologo == null ? null : psicologo.getNombre();
                    }

                    @Override
                    public Psicologo fromString(String string) {
                        return null;
                    }
                },
                psicologos
        ));
        psicologoColumn.setOnEditCommit(event -> {
            if (!isLoading) {
                Cita cita = event.getRowValue();
                cita.setPsicologo(event.getNewValue());
                actualizarCita(cita);
            }
        });

        fechaColumn.setCellValueFactory(new PropertyValueFactory<>("fechaHora"));
        fechaColumn.setCellFactory(column -> new TableCell<Cita, LocalDateTime>() {
            private final DatePicker datePicker = new DatePicker();

            {
                datePicker.setOnAction(event -> {
                    if (!isLoading) {
                        Cita cita = getTableRow().getItem();
                        if (cita != null) {
                            LocalDateTime nuevaFecha = LocalDateTime.of(
                                    datePicker.getValue(),
                                    cita.getFechaHora().toLocalTime()
                            );
                            cita.setFechaHora(nuevaFecha);
                            actualizarCita(cita);
                        }
                    }
                });
            }

            @Override
            protected void updateItem(LocalDateTime item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setGraphic(null);
                } else {
                    datePicker.setValue(item.toLocalDate());
                    setGraphic(datePicker);
                }
            }
        });

        horaColumn.setCellValueFactory(cellData ->
                new SimpleObjectProperty<>(cellData.getValue().getFechaHora().toLocalTime()));
        horaColumn.setCellFactory(ComboBoxTableCell.forTableColumn(
                new StringConverter<LocalTime>() {
                    @Override
                    public String toString(LocalTime time) {
                        if (time == null) return "";
                        return String.format("%02d:00", time.getHour());
                    }

                    @Override
                    public LocalTime fromString(String string) {
                        if (string == null || string.isEmpty()) return null;
                        return LocalTime.parse(string);
                    }
                },
                horas
        ));
        horaColumn.setOnEditCommit(event -> {
            if (!isLoading) {
                Cita cita = event.getRowValue();
                cita.setFechaHora(LocalDateTime.of(
                        cita.getFechaHora().toLocalDate(),
                        event.getNewValue()
                ));
                actualizarCita(cita);
            }
        });

        estadoColumn.setCellValueFactory(new PropertyValueFactory<>("estado"));
        estadoColumn.setCellFactory(ComboBoxTableCell.forTableColumn(EstadoCita.values()));
        estadoColumn.setOnEditCommit(event -> {
            if (!isLoading) {
                Cita cita = event.getRowValue();
                cita.setEstado(event.getNewValue());
                actualizarCita(cita);
            }
        });
    }

    private void configurarCombos() {
        horas.clear();
        for (int i = 8; i <= 12; i++) {
            horas.add(LocalTime.of(i, 0));
        }
        for (int i = 14; i <= 17; i++) {
            horas.add(LocalTime.of(i, 0));
        }
        horaCombo.setItems(horas);

        horaCombo.setConverter(new StringConverter<LocalTime>() {
            @Override
            public String toString(LocalTime time) {
                if (time == null) return "";
                return String.format("%02d:00", time.getHour());
            }

            @Override
            public LocalTime fromString(String string) {
                if (string == null || string.isEmpty()) return null;
                return LocalTime.parse(string);
            }
        });

        estadoCombo.setItems(FXCollections.observableArrayList(EstadoCita.values()));

        estudianteCombo.setConverter(new StringConverter<Estudiante>() {
            @Override
            public String toString(Estudiante estudiante) {
                return estudiante == null ? null : estudiante.getNombre();
            }

            @Override
            public Estudiante fromString(String string) {
                return null;
            }
        });

        psicologoCombo.setConverter(new StringConverter<Psicologo>() {
            @Override
            public String toString(Psicologo psicologo) {
                return psicologo == null ? null : psicologo.getNombre();
            }

            @Override
            public Psicologo fromString(String string) {
                return null;
            }
        });
    }

    private void cargarDetallesCita(Cita cita) {
        estudianteCombo.setValue(cita.getEstudiante());
        psicologoCombo.setValue(cita.getPsicologo());
        fechaPicker.setValue(cita.getFechaHora().toLocalDate());
        horaCombo.setValue(cita.getFechaHora().toLocalTime());
        estadoCombo.setValue(cita.getEstado());
    }

    private void actualizarCita(Cita cita) {
        if (isLoading) return;

        isLoading = true;
        CompletableFuture.runAsync(() -> {
            try {
                citaRepository.update(cita);
                Platform.runLater(() -> {
                    cargarDatos();
                    isLoading = false;
                });
            } catch (Exception e) {
                Platform.runLater(() -> {
                    mostrarError("Error al actualizar cita", e.getMessage());
                    isLoading = false;
                });
            }
        });
    }

    public void setRepositories(ICitaRepository citaRepository,
                                IEstudianteRepository estudianteRepository,
                                IPsicologoRepository psicologoRepository) {
        this.citaRepository = citaRepository;
        this.estudianteRepository = estudianteRepository;
        this.psicologoRepository = psicologoRepository;
        Platform.runLater(this::cargarDatos);
    }

    private void cargarDatos() {
        if (isLoading || estudianteRepository == null || psicologoRepository == null || citaRepository == null) {
            return;
        }

        isLoading = true;
        CompletableFuture.runAsync(() -> {
            try {
                var estudiantesList = estudianteRepository.findAll();
                var psicologosList = psicologoRepository.findAll();
                var citasList = citaRepository.findAll();

                Platform.runLater(() -> {
                    estudiantes.clear();
                    estudiantes.addAll(estudiantesList);
                    estudianteCombo.setItems(estudiantes);

                    psicologos.clear();
                    psicologos.addAll(psicologosList);
                    psicologoCombo.setItems(psicologos);

                    citas.clear();
                    citas.addAll(citasList);
                    citasTable.setItems(citas);
                    isLoading = false;
                });
            } catch (Exception e) {
                Platform.runLater(() -> {
                    mostrarError("Error", "Error al cargar datos: " + e.getMessage());
                    isLoading = false;
                });
            }
        });
    }

    @FXML
    private void handleAgendar() {
        if (!validarCamposRegistro() || isLoading) {
            return;
        }

        isLoading = true;
        CompletableFuture.runAsync(() -> {
            try {
                Cita cita = new Cita();
                cita.setEstudiante(estudianteCombo.getValue());
                cita.setPsicologo(psicologoCombo.getValue());
                cita.setFechaHora(LocalDateTime.of(fechaPicker.getValue(), horaCombo.getValue()));
                cita.setEstado(EstadoCita.PROGRAMADA);

                citaRepository.save(cita);

                Platform.runLater(() -> {
                    cargarDatos();
                    limpiarCampos();
                    mostrarExito("Cita agendada", "La cita ha sido agendada exitosamente");
                    isLoading = false;
                });
            } catch (Exception e) {
                Platform.runLater(() -> {
                    mostrarError("Error al guardar cita", e.getMessage());
                    isLoading = false;
                });
            }
        });
    }

    private boolean validarCamposRegistro() {
        if (estudianteCombo.getValue() == null ||
                psicologoCombo.getValue() == null ||
                fechaPicker.getValue() == null ||
                horaCombo.getValue() == null) {
            mostrarError("Error", "Todos los campos son requeridos");
            return false;
        }

        if (!esHorarioValido(horaCombo.getValue())) {
            mostrarError("Error", "La hora debe estar entre 8:00-12:00 o 14:00-17:00");
            return false;
        }

        return true;
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
        estudianteCombo.setValue(null);
        psicologoCombo.setValue(null);
        fechaPicker.setValue(null);
        horaCombo.setValue(null);
        estadoCombo.setValue(null);
        citasTable.getSelectionModel().clearSelection();
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