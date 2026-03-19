package com.cooperativabeb.dao;

import com.cooperativabeb.connection.ConexionOracle;
import com.cooperativabeb.model.Transaccion;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TransaccionDAO {

    private ConexionOracle conexion = ConexionOracle.getInstancia();

    public List<Transaccion> listarPorCuenta(int idCuenta) {
        List<Transaccion> lista = new ArrayList<>();
        String sql = "SELECT * FROM TRANSACCION WHERE id_cuenta=? " +
                     "ORDER BY fecha_transaccion DESC";
        try (Connection conn = conexion.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idCuenta);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) lista.add(mapear(rs));
        } catch (SQLException e) {
            System.err.println("Error listarPorCuenta: " + e.getMessage());
        }
        return lista;
    }

    public List<Transaccion> listarUltimas(int cantidad) {
        List<Transaccion> lista = new ArrayList<>();
        String sql = "SELECT * FROM (SELECT * FROM TRANSACCION " +
                     "ORDER BY fecha_transaccion DESC) WHERE ROWNUM <= ?";
        try (Connection conn = conexion.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, cantidad);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) lista.add(mapear(rs));
        } catch (SQLException e) {
            System.err.println("Error listarUltimas: " + e.getMessage());
        }
        return lista;
    }

    private Transaccion mapear(ResultSet rs) throws SQLException {
        return new Transaccion(
            rs.getInt("id_transaccion"),
            rs.getInt("id_cuenta"),
            rs.getInt("id_plan"),
            rs.getString("tipo"),
            rs.getDouble("monto"),
            rs.getDouble("saldo_anterior"),
            rs.getDouble("saldo_posterior"),
            rs.getDate("fecha_transaccion"),
            rs.getString("descripcion"),
            rs.getString("estado")
        );
    }
}
