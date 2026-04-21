package dam.code.controller;

import dam.code.exceptions.RelojException;
import dam.code.models.Reloj;
import dam.code.models.Usuario;
import dam.code.service.RelojService;
import dam.code.service.UsuarioService;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class RelojController {

    private Usuario usuario;
    private RelojService service;

    @FXML private Label lblUsuario;

    @FXML private TextField txtNombre;
    @FXML private TextField txtModelo;
    @FXML private TextField txtDescripcion;
    @FXML private TextField txtStock;
    @FXML private TextField txtPrecio;

    @FXML private TableView<Reloj> tablaRelojs;
    @FXML private TableColumn<Reloj, Integer> colId;
    @FXML private TableColumn<Reloj, String> colNombre;
    @FXML private TableColumn<Reloj, String> colModelo;
    @FXML private TableColumn<Reloj, String> colDescripcion;
    @FXML private TableColumn<Reloj, Integer> colStock;
    @FXML private TableColumn<Reloj, Integer> colPrecio;

    private Reloj relojSeleccionado = null;

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
        lblUsuario.setText("Admin: " + usuario.getNombre());
    }

    public void setRelojService(RelojService service) throws RelojException {
        this.service = service;
        tablaRelojs.setItems(service.obtenerRelojs());
    }

    @FXML
    private void initialize() {
        colId.setCellValueFactory(c -> new SimpleIntegerProperty(c.getValue().getId()).asObject());
        colNombre.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getNombre()));
        colModelo.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getModelo()));
        colDescripcion.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getDescripcion()));
        colStock.setCellValueFactory(c -> new SimpleIntegerProperty(c.getValue().getStock()).asObject());
        colPrecio.setCellValueFactory(c -> new SimpleIntegerProperty(c.getValue().getPrecio()).asObject());

        prefWidthColumns();

        // Al seleccionar una fila, rellenar los campos para editar
        tablaRelojs.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                relojSeleccionado = newVal;
                txtNombre.setText(newVal.getNombre());
                txtModelo.setText(newVal.getModelo());
                txtDescripcion.setText(newVal.getDescripcion());
                txtStock.setText(String.valueOf(newVal.getStock()));
                txtPrecio.setText(String.valueOf(newVal.getPrecio()));
            }
        });
    }

    private void prefWidthColumns() {
        tablaRelojs.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);
        colId.prefWidthProperty().bind(tablaRelojs.widthProperty().multiply(0.05));
        colNombre.prefWidthProperty().bind(tablaRelojs.widthProperty().multiply(0.25));
        colModelo.prefWidthProperty().bind(tablaRelojs.widthProperty().multiply(0.25));
        colDescripcion.prefWidthProperty().bind(tablaRelojs.widthProperty().multiply(0.25));
        colStock.prefWidthProperty().bind(tablaRelojs.widthProperty().multiply(0.10));
        colPrecio.prefWidthProperty().bind(tablaRelojs.widthProperty().multiply(0.10));
    }

    @FXML
    public void addReloj() {
        try {
            if (!validarCampos()) {
                mostrarError("Todos los campos son obligatorios");
                return;
            }
            Reloj reloj = new Reloj(
                    txtNombre.getText(),
                    txtModelo.getText(),
                    txtDescripcion.getText(),
                    Integer.parseInt(txtStock.getText()),
                    Integer.parseInt(txtPrecio.getText())
            );
            service.agregarReloj(reloj);
            tablaRelojs.setItems(service.obtenerRelojs());
            limpiarCampos();
        } catch (RelojException e) {
            mostrarError(e.getMessage());
        } catch (NumberFormatException e) {
            mostrarError("Stock y precio deben ser números válidos.");
        }
    }

    @FXML
    public void editarReloj() {
        if (relojSeleccionado == null) {
            mostrarError("Selecciona un reloj de la tabla para editar.");
            return;
        }
        try {
            if (!validarCampos()) {
                mostrarError("Todos los campos son obligatorios");
                return;
            }
            relojSeleccionado.setNombre(txtNombre.getText());
            relojSeleccionado.setModelo(txtModelo.getText());
            relojSeleccionado.setDescripcion(txtDescripcion.getText());
            relojSeleccionado.setStock(Integer.parseInt(txtStock.getText()));
            relojSeleccionado.setPrecio(Integer.parseInt(txtPrecio.getText()));

            service.editarReloj(relojSeleccionado);
            tablaRelojs.setItems(service.obtenerRelojs());
            limpiarCampos();
            relojSeleccionado = null;
        } catch (RelojException e) {
            mostrarError(e.getMessage());
        } catch (NumberFormatException e) {
            mostrarError("Stock y precio deben ser números válidos.");
        }
    }

    @FXML
    public void eliminarReloj() {
        Reloj seleccionado = tablaRelojs.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            mostrarError("Selecciona un reloj de la tabla para eliminar.");
            return;
        }
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Eliminar reloj");
        confirm.setHeaderText("¿Seguro que quieres eliminar este reloj?");
        confirm.setContentText(seleccionado.getNombre() + " - " + seleccionado.getModelo());
        confirm.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    service.eliminarReloj(seleccionado.getId());
                    tablaRelojs.setItems(service.obtenerRelojs());
                    limpiarCampos();
                    relojSeleccionado = null;
                } catch (RelojException e) {
                    mostrarError(e.getMessage());
                }
            }
        });
    }

    private void limpiarCampos() {
        txtNombre.clear();
        txtModelo.clear();
        txtDescripcion.clear();
        txtStock.clear();
        txtPrecio.clear();
        tablaRelojs.getSelectionModel().clearSelection();
    }

    private boolean validarCampos() {
        return !txtNombre.getText().isBlank()
                && !txtModelo.getText().isBlank()
                && !txtDescripcion.getText().isBlank()
                && !txtStock.getText().isBlank()
                && !txtPrecio.getText().isBlank();
    }

    @FXML
    public void cerrarSesion() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Cerrar Sesión");
        alert.setHeaderText("¿Seguro que desea cerrar sesión?");
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Inicio_view.fxml"));
                    Parent root = loader.load();
                    InicioController controller = loader.getController();
                    controller.setUsuarioService(new UsuarioService());
                    Stage stage = (Stage) txtNombre.getScene().getWindow();
                    stage.setResizable(false);
                    stage.setWidth(400);
                    stage.setHeight(600);
                    stage.setScene(new Scene(root));
                } catch (Exception e) {
                    mostrarError(e.getMessage());
                }
            }
        });
    }

    private void mostrarError(String mensaje) {
        Alert alerta = new Alert(Alert.AlertType.ERROR);
        alerta.setTitle("Error");
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
}