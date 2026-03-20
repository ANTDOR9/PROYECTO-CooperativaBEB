package com.cooperativabeb.dao;

import com.cooperativabeb.connection.ConexionOracle;
import com.cooperativabeb.model.UsuarioSistema;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDAO {

    private ConexionOracle conexion = ConexionOracle.getInstancia();

    public UsuarioSistema autenticar(String username, String password) {
        String sql = "SELECT * FROM USUARIO_SISTEMA WHERE username=? AND password=? AND estado='ACTIVO'";
        try (Connection conn = conexion.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                // Actualizar ultimo acceso
                actualizarUltimoAcceso(rs.getInt("id_usuario"));
                return mapear(rs);
            }
        } catch (SQLException e) {
            System.err.println("Error autenticar: " + e.getMessage());
        }
        return null;
    }

    private void actualizarUltimoAcceso(int idUsuario) {
        String sql = "UPDATE USUARIO_SISTEMA SET ultimo_acceso=SYSDATE WHERE id_usuario=?";
        try (Connection conn = conexion.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idUsuario);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error actualizarUltimoAcceso: " + e.getMessage());
        }
    }

    public List<UsuarioSistema> listarTodos() {
        List<UsuarioSistema> lista = new ArrayList<>();
        String sql = "SELECT * FROM USUARIO_SISTEMA ORDER BY rol, username";
        try (Connection conn = conexion.getConexion();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) lista.add(mapear(rs));
        } catch (SQLException e) {
            System.err.println("Error listarTodos: " + e.getMessage());
        }
        return lista;
    }

    public boolean insertar(UsuarioSistema u) {
        String sql = "INSERT INTO USUARIO_SISTEMA VALUES (seq_usuario.NEXTVAL,?,?,?,?,?,?,?,SYSDATE,NULL)";
        try (Connection conn = conexion.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, u.getUsername());
            ps.setString(2, u.getPassword());
            ps.setString(3, u.getRol());
            if (u.getIdCliente() > 0) ps.setInt(4, u.getIdCliente());
            else ps.setNull(4, Types.NUMBER);
            ps.setString(5, u.getNombreCompleto());
            ps.setString(6, u.getEmail());
            ps.setString(7, u.getEstado() != null ? u.getEstado() : "ACTIVO");
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error insertar: " + e.getMessage());
            return false;
        }
    }

    public boolean actualizar(UsuarioSistema u) {
        String sql = "UPDATE USUARIO_SISTEMA SET nombre_completo=?, email=?, rol=?, estado=?" +
                     (u.getPassword() != null && !u.getPassword().isEmpty() ?
                     ", password=?" : "") + " WHERE id_usuario=?";
        try (Connection conn = conexion.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, u.getNombreCompleto());
            ps.setString(2, u.getEmail());
            ps.setString(3, u.getRol());
            ps.setString(4, u.getEstado());
            if (u.getPassword() != null && !u.getPassword().isEmpty()) {
                ps.setString(5, u.getPassword());
                ps.setInt(6, u.getIdUsuario());
            } else {
                ps.setInt(5, u.getIdUsuario());
            }
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error actualizar: " + e.getMessage());
            return false;
        }
    }

    public boolean desactivar(int idUsuario) {
        String sql = "UPDATE USUARIO_SISTEMA SET estado='INACTIVO' WHERE id_usuario=?";
        try (Connection conn = conexion.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idUsuario);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error desactivar: " + e.getMessage());
            return false;
        }
    }

    public boolean existeUsername(String username) {
        String sql = "SELECT COUNT(*) FROM USUARIO_SISTEMA WHERE username=?";
        try (Connection conn = conexion.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1) > 0;
        } catch (SQLException e) {
            System.err.println("Error existeUsername: " + e.getMessage());
        }
        return false;
    }

    private UsuarioSistema mapear(ResultSet rs) throws SQLException {
        UsuarioSistema u = new UsuarioSistema(
            rs.getInt("id_usuario"),
            rs.getString("username"),
            rs.getString("password"),
            rs.getString("rol"),
            rs.getInt("id_cliente"),
            rs.getString("nombre_completo"),
            rs.getString("email"),
            rs.getString("estado")
        );
        u.setFechaCreacion(rs.getDate("fecha_creacion"));
        u.setUltimoAcceso(rs.getDate("ultimo_acceso"));
        return u;
    }
}
