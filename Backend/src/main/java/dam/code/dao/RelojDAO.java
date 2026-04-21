package dam.code.dao;

import dam.code.exception.RelojException;
import dam.code.models.Reloj;

import java.util.List;

public interface RelojDAO {
    void registrar(Reloj reloj) throws RelojException;
    List<Reloj> listar() throws RelojException;
    List<Reloj> obtenerRelojsPorUsuario(int idUsuario) throws RelojException;
    void comprar(int idUsuario, int idReloj) throws RelojException;
}