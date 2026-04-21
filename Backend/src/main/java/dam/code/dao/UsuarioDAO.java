package dam.code.dao;

import dam.code.exceptions.UsuarioException;
import dam.code.models.Usuario;

public interface UsuarioDAO {
    void registrar(Usuario usuario, String password) throws UsuarioException;
    Usuario login(String email, String password) throws UsuarioException;
}