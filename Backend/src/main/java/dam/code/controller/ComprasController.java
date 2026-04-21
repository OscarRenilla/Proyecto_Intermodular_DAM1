package dam.code.controller;

import dam.code.exception.RelojException;
import dam.code.models.Reloj;
import dam.code.models.Usuario;
import dam.code.service.RelojService;
import dam.code.service.UsuarioService;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class ComprasController {

    private Usuario usuario;
    private RelojService service;


    @FXML private TableView<Reloj> tablaCompras;
    @FXML private TableColumn<Reloj, Integer> colId;
    @FXML private TableColumn<Reloj, String> colNombre;
    @FXML private TableColumn<Reloj, String> colModelo;
    @FXML private TableColumn<Reloj, String> colDescripcion;
    @FXML private TableColumn<Reloj, Integer> colStock;
    @FXML private TableColumn<Reloj, Integer> colPrecio;

    @FXML private Label lblUsuario;


    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
        lblUsuario.setText("Usuario" + usuario.getNombre());
    }

    public void setRelojService(RelojService service) throws RelojException {
        this.service = service;

        tablaCompras.setItems(service.obtenerRelojsPorUsuario(usuario.getId()));
    }

    @FXML
    private void initialize() {
        prefWidthColumns();

        colId.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getId()).asObject());
        colNombre.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNombre()));
        colModelo.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getModelo()));
        colDescripcion.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDescripcion()));
        colStock.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getStock()).asObject());
        colPrecio.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getPrecio()).asObject());

        setVisualizacion();
    }

    private void prefWidthColumns() {
        tablaCompras.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);

        colId.prefWidthProperty().bind(tablaCompras.widthProperty().multiply(0.05));
        colNombre.prefWidthProperty().bind(tablaCompras.widthProperty().multiply(0.35));
        colModelo.prefWidthProperty().bind(tablaCompras.widthProperty().multiply(0.30));
        colDescripcion.prefWidthProperty().bind(tablaCompras.widthProperty().multiply(0.10));
        colStock.prefWidthProperty().bind(tablaCompras.widthProperty().multiply(0.15));
        colPrecio.prefWidthProperty().bind(tablaCompras.widthProperty().multiply(0.05));
    }

    private void setVisualizacion() {
        tablaCompras.setRowFactory(tv -> {
            TableRow<Reloj> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2) {
                    Reloj reloj = row.getItem();
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Compras");
                    alert.setHeaderText("Comprar reloj");
                    alert.setContentText("¿Quieres comprar el reloj: " + reloj.getModelo() + " de " + reloj.getNombre() + "?");
                    alert.showAndWait().ifPresent(response -> {
                        if (response == ButtonType.OK) {
                            try {
                                service.addCompra(usuario.getId(), reloj);
                                tablaCompras.setItems(service.obtenerRelojsPorUsuario(usuario.getId()));
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
    public void relojs() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Relojes_view.fxml"));

            Parent root = loader.load();

            RelojController controller = loader.getController();
            controller.setUsuario(usuario);
            controller.setRelojService(service);

            Stage stage = (Stage) lblUsuario.getScene().getWindow();
            stage.setResizable(false);
            stage.setWidth(800);
            stage.setHeight(600);
            stage.setScene(new Scene(root));

        }catch (Exception e) {
            mostrarError(e.getMessage());
        }
    }

    private void mostrarError(String mensaje) {
        Alert alerta = new Alert(Alert.AlertType.ERROR);
        alerta.setTitle("Error");
        alerta.setContentText(mensaje);
        alerta.showAndWait();
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

                Stage stage = (Stage) tablaCompras.getScene().getWindow();
                stage.setResizable(false);
                stage.setWidth(400);
                stage.setHeight(600);
                stage.setScene(new Scene(root));
            } catch (Exception e) {
                mostrarError(e.getMessage());
            }
        });
    }

}
