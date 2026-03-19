package com.cooperativabeb.dao;

import com.cooperativabeb.connection.ConexionOracle;
import com.cooperativabeb.model.PlanInversion;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PlanInversionDAO {

    private ConexionOracle conexion = ConexionOracle.getInstancia();

    public List<PlanInversion> listarTodos() {
        List<PlanInversion> lista = new ArrayList<>();
        String sql = "SELECT * FROM PLAN_INVERSION ORDER BY fecha_inicio DESC";
        try (Connection conn = conexion.getConexion();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) lista.add(mapear(rs));
        } catch (SQLException e) {
            System.err.println("Error listarTodos: " + e.getMessage());
        }
        return lista;
    }

    public List<PlanInversion> listarPorCliente(int idCliente) {
        List<PlanInversion> lista = new ArrayList<>();
        String sql = "SELECT * FROM PLAN_INVERSION WHERE id_cliente=? ORDER BY fecha_inicio DESC";
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

    public boolean contratar(int idCliente, int idProducto, int idAsesor, double monto) {
        String sql = "{call sp_contratar_plan(?,?,?,?)}";
        try (Connection conn = conexion.getConexion();
             CallableStatement cs = conn.prepareCall(sql)) {
            cs.setInt(1, idCliente);
            cs.setInt(2, idProducto);
            cs.setInt(3, idAsesor);
            cs.setDouble(4, monto);
            cs.execute();
            return true;
        } catch (SQLException e) {
            System.err.println("Error contratar: " + e.getMessage());
            return false;
        }
    }

    public boolean cancelar(int idPlan) {
        String sql = "UPDATE PLAN_INVERSION SET estado='CANCELADO' WHERE id_plan=?";
        try (Connection conn = conexion.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idPlan);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error cancelar: " + e.getMessage());
            return false;
        }
    }

    private PlanInversion mapear(ResultSet rs) throws SQLException {
        return new PlanInversion(
            rs.getInt("id_plan"),
            rs.getInt("id_cliente"),
            rs.getInt("id_producto"),
            rs.getInt("id_asesor"),
            rs.getDouble("monto_invertido"),
            rs.getDouble("tasa_pactada"),
            rs.getInt("plazo_meses"),
            rs.getDate("fecha_inicio"),
            rs.getDate("fecha_vencimiento"),
            rs.getString("estado")
        );
    }
}
