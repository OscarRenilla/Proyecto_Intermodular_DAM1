package dam.code.controller;

import dam.code.exception.RelojException;
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

import java.time.format.DateTimeParseException;

public class RelojController {
    private Usuario usuario;
    private RelojService service;

    @FXML
    private Label lblUsuario;

    @FXML
    private TextField txtNombre;
    @FXML
    private TextField txtModelo;
    @FXML
    private TextField txtDescripcion;
    @FXML
    private TextField txtStock;
    @FXML
    private TextField txtPrecio;

    @FXML
    private TableView<Reloj> tablaRelojs;
    @FXML
    private TableColumn<Reloj, Integer> colId;
    @FXML
    private TableColumn<Reloj, String> colNombre;
    @FXML
    private TableColumn<Reloj, String> colModelo;
    @FXML
    private TableColumn<Reloj, String> colDescripcion;
    @FXML
    private TableColumn<Reloj, Integer> colStock;
    @FXML
    private TableColumn<Reloj, Integer> colPrecio;

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
        lblUsuario.setText("Usuario:" + usuario.getNombre());
    }

    public void setRelojService(RelojService service) throws RelojException {
        this.service = service;
        tablaRelojs.setItems(service.obtenerRelojs());
    }

    @FXML
    private void initialize() {
        prefWidthColumns();
        colId.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getId()).asObject());
        colNombre.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNombre()));
        colModelo.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getModelo()));
        colDescripcion.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDescripcion()));
        colStock.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getStock()).asObject());
        colStock.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getPrecio()).asObject());

        txtStock.setEditable(true);
    }

    private void prefWidthColumns() {
        tablaRelojs.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);

        colId.prefWidthProperty().bind(tablaRelojs.widthProperty().multiply(0.05));
        colNombre.prefWidthProperty().bind(tablaRelojs.widthProperty().multiply(0.35));
        colModelo.prefWidthProperty().bind(tablaRelojs.widthProperty().multiply(0.30));
        colDescripcion.prefWidthProperty().bind(tablaRelojs.widthProperty().multiply(0.15));
        colStock.prefWidthProperty().bind(tablaRelojs.widthProperty().multiply(0.15));

        setCompra();
    }

    private void setCompra() {
        tablaRelojs.setRowFactory(tv -> {
            TableRow<Reloj> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2) {
                    Reloj reloj = row.getItem();
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Compras");
                    alert.setHeaderText("Añador compra");
                    alert.setContentText("¿Quieres añadir una compra al reloj " + reloj.getModelo() + " de " + reloj.getNombre() + "?");
                    alert.showAndWait().ifPresent(response -> {
                        if (response == ButtonType.OK) {
                            try {
                                service.addCompra(usuario.getId(), reloj);
                                tablaRelojs.setItems(service.obtenerRelojs());
                            } catch (RelojException e) {
                                mostrarError(e.getMessage());
                            }
                        }
                    });
                }
            });
            return row;
        });
    }


    @FXML
    public void addReloj() {
        try {
            if (!validarCampos()) throw new RelojException("Todos los campos son obligatorios");
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
        } catch (RelojException | DateTimeParseException e) {
            mostrarError(e.getMessage());
        } catch (NumberFormatException e) {
            mostrarError("EL precio tiene que ser un número válido.");

        }
    }

    private void limpiarCampos() {
        txtNombre.clear();
        txtModelo.clear();
        txtDescripcion.clear();
        txtStock.clear();
        txtPrecio.clear();
    }

    private boolean validarCampos() {
        return !txtNombre.getText().isBlank()
                && !txtModelo.getText().isBlank()
                && !txtDescripcion.getText().isBlank()
                && txtStock.getText().isBlank()
                && txtPrecio.getText().isBlank();
    }

    @FXML
    public void compras() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Compras_view.fxml"));
            Parent root = loader.load();
            ComprasController controller = loader.getController();
            controller.setUsuario(usuario);
            controller.setRelojService(service);

            Stage stage = (Stage) txtNombre.getScene().getWindow();
            stage.setResizable(false);
            stage.setWidth(800);
            stage.setHeight(600);
            stage.setScene(new Scene(root));
        } catch (Exception e) {
            mostrarError(e.getMessage());
        }
    }

    @FXML
    public void cerrarSesion() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Cerrar Sesion");
        alert.setHeaderText("¿Seguro que desea cerrar sesión?");
        alert.setContentText("Se cerrara la sesion actual");
        alert.showAndWait().ifPresent(response -> {
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
        });
    }

    private void mostrarError(String mensaje) {
        Alert alerta = new Alert(Alert.AlertType.ERROR);
        alerta.setTitle("Error");
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
}