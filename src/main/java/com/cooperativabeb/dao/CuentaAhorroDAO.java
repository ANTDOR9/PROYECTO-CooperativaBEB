package com.cooperativabeb.dao;

import com.cooperativabeb.connection.ConexionOracle;
import com.cooperativabeb.model.CuentaAhorro;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CuentaAhorroDAO {

    private ConexionOracle conexion = ConexionOracle.getInstancia();

    public List<CuentaAhorro> listarPorCliente(int idCliente) {
        List<CuentaAhorro> lista = new ArrayList<>();
        String sql = "SELECT * FROM CUENTA_AHORRO WHERE id_cliente=? ORDER BY fecha_apertura DESC";
        try (Connection conn = conexion.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idCliente);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) lista.add(mapear(rs));
        } catch (SQLException e) {
            System.err.println("Error listarPorCliente: " + e.getMessage());
        }
        return lista;
    }

    public List<CuentaAhorro> listarTodas() {
        List<CuentaAhorro> lista = new ArrayList<>();
        String sql = "SELECT * FROM CUENTA_AHORRO ORDER BY fecha_apertura DESC";
        try (Connection conn = conexion.getConexion();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) lista.add(mapear(rs));
        } catch (SQLException e) {
            System.err.println("Error listarTodas: " + e.getMessage());
        }
        return lista;
    }

    public CuentaAhorro buscarPorNro(String nroCuenta) {
        String sql = "SELECT * FROM CUENTA_AHORRO WHERE nro_cuenta=?";
        try (Connection conn = conexion.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nroCuenta);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapear(rs);
        } catch (SQLException e) {
            System.err.println("Error buscarPorNro: " + e.getMessage());
        }
        return null;
    }

    public boolean depositar(int idCuenta, double monto, String descripcion) {
        String sql = "{call sp_deposito(?,?,?)}";
        try (Connection conn = conexion.getConexion();
             CallableStatement cs = conn.prepareCall(sql)) {
            cs.setInt(1, idCuenta);
            cs.setDouble(2, monto);
            cs.setString(3, descripcion);
            cs.execute();
            return true;
        } catch (SQLException e) {
            System.err.println("Error depositar: " + e.getMessage());
            return false;
        }
    }

    public boolean retirar(int idCuenta, double monto, String descripcion) {
        String sql = "{call sp_retiro(?,?,?)}";
        try (Connection conn = conexion.getConexion();
             CallableStatement cs = conn.prepareCall(sql)) {
            cs.setInt(1, idCuenta);
            cs.setDouble(2, monto);
            cs.setString(3, descripcion);
            cs.execute();
            return true;
        } catch (SQLException e) {
            System.err.println("Error retirar: " + e.getMessage());
            return false;
        }
    }

    private CuentaAhorro mapear(ResultSet rs) throws SQLException {
        return new CuentaAhorro(
            rs.getInt("id_cuenta"),
            rs.getString("nro_cuenta"),
            rs.getInt("id_cliente"),
            rs.getInt("id_asesor"),
            rs.getDouble("saldo"),
            rs.getDouble("tasa_interes"),
            rs.getString("tipo_cuenta"),
            rs.getString("estado"),
            rs.getDate("fecha_apertura"),
            rs.getDate("fecha_cierre")
        );
    }
}
