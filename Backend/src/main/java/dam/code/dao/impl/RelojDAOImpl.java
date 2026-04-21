package dam.code.dao.impl;

import dam.code.config.DatabaseConfig;
import dam.code.dao.RelojDAO;
import dam.code.exceptions.RelojException;
import dam.code.models.Reloj;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RelojDAOImpl implements RelojDAO {

    @Override
    public void registrar(Reloj reloj) throws RelojException {
        String sql = "INSERT INTO relojs (nombre, modelo, descripcion, stock, precio) VALUES (?, ?, ?, ?, ?)";

        try (Connection conexion = DatabaseConfig.getConnection();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setString(1, reloj.getNombre());
            ps.setString(2, reloj.getModelo());
            ps.setString(3, reloj.getDescripcion());
            ps.setInt(4, reloj.getStock());
            ps.setDouble(5, reloj.getPrecio());

            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RelojException(e.getMessage());
        }
    }

    @Override
    public List<Reloj> listar() throws RelojException {
        List<Reloj> relojs = new ArrayList<>();
        String sql = "SELECT * FROM relojs";

        try (Connection con = DatabaseConfig.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                relojs.add(new Reloj(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getString("modelo"),
                        rs.getString("descripcion"),
                        rs.getInt("stock"),
                        rs.getInt("precio")
                ));
            }

        } catch (SQLException e) {
            throw new RelojException(e.getMessage());
        }
        return relojs;
    }

    @Override
    public List<Reloj> obtenerRelojsPorUsuario(int idUsuario) throws RelojException {
        List<Reloj> relojs = new ArrayList<>();
        String sql = """
                SELECT p.id, p.nombre, p.modelo, p.descripcion, p.stock, p.precio,
                    COUNT(v.id_reloj) AS compras
                FROM relojs p
                INNER JOIN compras v ON p.id = v.id_reloj
                WHERE v.id_usuario = ?
                GROUP BY p.id, p.nombre, p.modelo, p.descripcion, p.stock, p.precio
                """;

        try (Connection conexion = DatabaseConfig.getConnection();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setInt(1, idUsuario);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Reloj reloj = new Reloj(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getString("modelo"),
                        rs.getString("descripcion"),
                        rs.getInt("stock"),
                        rs.getInt("precio")
                );
                reloj.setCompras(rs.getInt("compras"));
                relojs.add(reloj);
            }
        } catch (SQLException e) {
            throw new RelojException(e.getMessage());
        }
        return relojs;
    }

    @Override
    public void comprar(int idUsuario, int idReloj) throws RelojException {
        String sqlCheck = "SELECT stock FROM relojs WHERE id = ?";
        String sqlCompra = "INSERT INTO compras (id_usuario, id_reloj) VALUES (?, ?)";
        String sqlStock = "UPDATE relojs SET stock = stock - 1 WHERE id = ?";

        try (Connection con = DatabaseConfig.getConnection()) {
            con.setAutoCommit(false);

            try (PreparedStatement psCheck = con.prepareStatement(sqlCheck)) {
                psCheck.setInt(1, idReloj);
                ResultSet rs = psCheck.executeQuery();
                if (rs.next()) {
                    int stock = rs.getInt("stock");
                    if (stock <= 0) {
                        throw new RelojException("No hay stock disponible para este reloj.");
                    }
                } else {
                    throw new RelojException("El reloj no existe.");
                }
            }

            try (PreparedStatement psCompra = con.prepareStatement(sqlCompra)) {
                psCompra.setInt(1, idUsuario);
                psCompra.setInt(2, idReloj);
                psCompra.executeUpdate();
            }

            try (PreparedStatement psStock = con.prepareStatement(sqlStock)) {
                psStock.setInt(1, idReloj);
                psStock.executeUpdate();
            }

            con.commit();

        } catch (SQLException e) {
            throw new RelojException(e.getMessage());
        }
    }

    @Override
    public void actualizar(Reloj reloj) throws RelojException {
        String sql = "UPDATE relojs SET nombre=?, modelo=?, descripcion=?, stock=?, precio=? WHERE id=?";
        try (Connection con = DatabaseConfig.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, reloj.getNombre());
            ps.setString(2, reloj.getModelo());
            ps.setString(3, reloj.getDescripcion());
            ps.setInt(4, reloj.getStock());
            ps.setInt(5, reloj.getPrecio());
            ps.setInt(6, reloj.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RelojException(e.getMessage());
        }
    }

    @Override
    public void eliminar(int id) throws RelojException {
        String sql = "DELETE FROM relojs WHERE id=?";
        try (Connection con = DatabaseConfig.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RelojException(e.getMessage());
        }
    }
}