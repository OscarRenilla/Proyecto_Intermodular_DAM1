package dam.code.service;

import dam.code.dao.RelojDAO;
import dam.code.dao.impl.RelojDAOImpl;

import dam.code.exception.RelojException;
import dam.code.models.Reloj;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.time.LocalDate;

public class RelojService {

    private final RelojDAO relojDAO = new RelojDAOImpl();

    public ObservableList<Reloj> obtenerRelojs() {
        return FXCollections.observableArrayList(relojDAO.listar());
    }

    public void addVisualizacion(int idUsuario, Reloj reloj) throws RelojException {
        relojDAO.comprar(idUsuario, reloj.getId());

    }

    public void agregarReloj(Reloj reloj) {
        validar_reloj(reloj);

        relojDAO.registrar(reloj);
    }

    private void validar_reloj(Reloj reloj) throws RelojException {
        if (reloj.getNombre().length() < 2) {
            throw new RelojException("El nombre del reloj es muy corto");
        }
        if (reloj.getModelo().length() < 2) {
            throw new RelojException("El modelo del reloj es muy corto");
        }
        if (reloj.getDescripcion().length() < 2) {
            throw new RelojException("La descripción del reloj es muy corta");
        }
        if (reloj.getStock() < 0){
            throw new RelojException("El stock no puede ser negativo");
        }
        if (reloj.getPrecio() < 0){
            throw new RelojException("El precio no puede ser negativo");
        }
    }

    public ObservableList<Reloj> obtenerRelojsPorUsuario(int idUsuario) throws RelojException {
        return FXCollections.observableArrayList(relojDAO.obtenerRelojsPorUsuario(idUsuario));
    }
}
