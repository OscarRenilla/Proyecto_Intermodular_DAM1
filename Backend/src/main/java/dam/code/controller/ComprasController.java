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

public class ComprasController {

    private Usuario usuario;
    private RelojService service;

    // Tabla catálogo
    @FXML private TableView<Reloj> tablaRelojs;
    @FXML private TableColumn<Reloj, Integer> colCatId;
    @FXML private TableColumn<Reloj, String> colCatNombre;
    @FXML private TableColumn<Reloj, String> colCatModelo;
    @FXML private TableColumn<Reloj, String> colCatDescripcion;
    @FXML private TableColumn<Reloj, Integer> colCatStock;
    @FXML private TableColumn<Reloj, Integer> colCatPrecio;

    // Tabla mis compras
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
        lblUsuario.setText("Usuario: " + usuario.getNombre());
    }

    public void setRelojService(RelojService service) throws RelojException {
        this.service = service;
        cargarTablas();
    }

    @FXML
    private void initialize() {
        // Catálogo
        colCatId.setCellValueFactory(c -> new SimpleIntegerProperty(c.getValue().getId()).asObject());
        colCatNombre.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getNombre()));
        colCatModelo.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getModelo()));
        colCatDescripcion.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getDescripcion()));
        colCatStock.setCellValueFactory(c -> new SimpleIntegerProperty(c.getValue().getStock()).asObject());
        colCatPrecio.setCellValueFactory(c -> new SimpleIntegerProperty(c.getValue().getPrecio()).asObject());

        // Mis compras
        colId.setCellValueFactory(c -> new SimpleIntegerProperty(c.getValue().getId()).asObject());
        colNombre.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getNombre()));
        colModelo.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getModelo()));
        colDescripcion.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getDescripcion()));
        colStock.setCellValueFactory(c -> new SimpleIntegerProperty(c.getValue().getStock()).asObject());
        colPrecio.setCellValueFactory(c -> new SimpleIntegerProperty(c.getValue().getPrecio()).asObject());

        tablaRelojs.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);
        tablaCompras.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);

        // Doble clic en catálogo para comprar
        tablaRelojs.setRowFactory(tv -> {
            TableRow<Reloj> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && !row.isEmpty()) {
                    Reloj reloj = row.getItem();
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Comprar");
                    alert.setHeaderText("¿Quieres comprar este reloj?");
                    alert.setContentText(reloj.getNombre() + " - " + reloj.getModelo());
                    alert.showAndWait().ifPresent(response -> {
                        if (response == ButtonType.OK) {
                            try {
                                service.addCompra(usuario.getId(), reloj);
                                cargarTablas();
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

    private void cargarTablas() throws RelojException {
        tablaRelojs.setItems(service.obtenerRelojs());
        tablaCompras.setItems(service.obtenerRelojsPorUsuario(usuario.getId()));
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
                    Stage stage = (Stage) tablaCompras.getScene().getWindow();
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