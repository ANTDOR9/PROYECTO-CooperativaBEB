package com.cooperativabeb.dao;

import com.cooperativabeb.connection.ConexionOracle;
import com.cooperativabeb.model.Cliente;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClienteDAO {

    private ConexionOracle conexion = ConexionOracle.getInstancia();

    public List<Cliente> listarTodos() {
        List<Cliente> lista = new ArrayList<>();
        String sql = "SELECT * FROM CLIENTE ORDER BY apellidos";
        try (Connection conn = conexion.getConexion();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                lista.add(mapear(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error listarTodos: " + e.getMessage());
        }
        return lista;
    }

    public Cliente buscarPorId(int id) {
        String sql = "SELECT * FROM CLIENTE WHERE id_cliente = ?";
        try (Connection conn = conexion.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapear(rs);
        } catch (SQLException e) {
            System.err.println("Error buscarPorId: " + e.getMessage());
        }
        return null;
    }

    public Cliente buscarPorDni(String dni) {
        String sql = "SELECT * FROM CLIENTE WHERE dni = ?";
        try (Connection conn = conexion.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, dni);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapear(rs);
        } catch (SQLException e) {
            System.err.println("Error buscarPorDni: " + e.getMessage());
        }
        return null;
    }

    public boolean insertar(Cliente c) {
        String sql = "INSERT INTO CLIENTE VALUES (seq_cliente.NEXTVAL,?,?,?,?,?,?,?,?,SYSDATE)";
        try (Connection conn = conexion.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, c.getDni());
            ps.setString(2, c.getNombres());
            ps.setString(3, c.getApellidos());
            ps.setString(4, c.getTelefono());
            ps.setString(5, c.getEmail());
            ps.setDate(6, c.getFechaNacimiento() != null ?
                new java.sql.Date(c.getFechaNacimiento().getTime()) : null);
            ps.setString(7, c.getDireccion());
            ps.setString(8, c.getEstado() != null ? c.getEstado() : "ACTIVO");
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error insertar: " + e.getMessage());
            return false;
        }
    }

    public boolean actualizar(Cliente c) {
        String sql = "UPDATE CLIENTE SET nombres=?, apellidos=?, telefono=?, " +
                     "email=?, direccion=?, estado=? WHERE id_cliente=?";
        try (Connection conn = conexion.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, c.getNombres());
            ps.setString(2, c.getApellidos());
            ps.setString(3, c.getTelefono());
            ps.setString(4, c.getEmail());
            ps.setString(5, c.getDireccion());
            ps.setString(6, c.getEstado());
            ps.setInt(7, c.getIdCliente());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error actualizar: " + e.getMessage());
            return false;
        }
    }

    public boolean eliminar(int id) {
        String sql = "UPDATE CLIENTE SET estado='INACTIVO' WHERE id_cliente=?";
        try (Connection conn = conexion.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error eliminar: " + e.getMessage());
            return false;
        }
    }

    public List<Cliente> buscarPorNombre(String nombre) {
        List<Cliente> lista = new ArrayList<>();
        String sql = "SELECT * FROM CLIENTE WHERE UPPER(nombres || ' ' || apellidos) " +
                     "LIKE UPPER(?) ORDER BY apellidos";
        try (Connection conn = conexion.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, "%" + nombre + "%");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) lista.add(mapear(rs));
        } catch (SQLException e) {
            System.err.println("Error buscarPorNombre: " + e.getMessage());
        }
        return lista;
    }

    private Cliente mapear(ResultSet rs) throws SQLException {
        return new Cliente(
            rs.getInt("id_cliente"),
            rs.getString("dni"),
            rs.getString("nombres"),
            rs.getString("apellidos"),
            rs.getString("telefono"),
            rs.getString("email"),
            rs.getDate("fecha_nacimiento"),
            rs.getString("direccion"),
            rs.getString("estado"),
            rs.getDate("fecha_registro")
        );
    }
}
