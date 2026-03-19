package com.cooperativabeb.dao;

import com.cooperativabeb.connection.ConexionOracle;
import com.cooperativabeb.model.ProductoFinanciero;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductoFinancieroDAO {

    private ConexionOracle conexion = ConexionOracle.getInstancia();

    public List<ProductoFinanciero> listarActivos() {
        List<ProductoFinanciero> lista = new ArrayList<>();
        String sql = "SELECT * FROM PRODUCTO_FINANCIERO WHERE estado='ACTIVO' ORDER BY tipo, nombre";
        try (Connection conn = conexion.getConexion();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) lista.add(mapear(rs));
        } catch (SQLException e) {
            System.err.println("Error listarActivos: " + e.getMessage());
        }
        return lista;
    }

    public List<ProductoFinanciero> listarPorTipo(String tipo) {
        List<ProductoFinanciero> lista = new ArrayList<>();
        String sql = "SELECT * FROM PRODUCTO_FINANCIERO WHERE tipo=? AND estado='ACTIVO'";
        try (Connection conn = conexion.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, tipo);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) lista.add(mapear(rs));
        } catch (SQLException e) {
            System.err.println("Error listarPorTipo: " + e.getMessage());
        }
        return lista;
    }

    private ProductoFinanciero mapear(ResultSet rs) throws SQLException {
        return new ProductoFinanciero(
            rs.getInt("id_producto"),
            rs.getString("nombre"),
            rs.getString("tipo"),
            rs.getString("descripcion"),
            rs.getDouble("tasa_base"),
            rs.getDouble("monto_minimo"),
            rs.getDouble("monto_maximo"),
            rs.getInt("plazo_meses"),
            rs.getString("estado")
        );
    }
}
