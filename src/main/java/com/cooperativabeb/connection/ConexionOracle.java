package com.cooperativabeb.connection;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ConexionOracle {

    private static ConexionOracle instancia;
    private static Properties props = new Properties();

    private static String URL;
    private static String USUARIO;
    private static String PASSWORD;

    private ConexionOracle() {
        cargarPropiedades();
    }

    public static ConexionOracle getInstancia() {
        if (instancia == null) {
            instancia = new ConexionOracle();
        }
        return instancia;
    }

    private void cargarPropiedades() {
        try (InputStream input = getClass().getClassLoader()
                .getResourceAsStream("db.properties")) {
            if (input == null) {
                System.err.println("No se encontro db.properties");
                return;
            }
            props.load(input);
            URL      = props.getProperty("db.url");
            USUARIO  = props.getProperty("db.user");
            PASSWORD = props.getProperty("db.password");
        } catch (IOException e) {
            System.err.println("Error cargando db.properties: " + e.getMessage());
        }
    }

    public Connection getConexion() throws SQLException {
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            Connection conn = DriverManager.getConnection(URL, USUARIO, PASSWORD);
            System.out.println("Conexion exitosa a Oracle");
            return conn;
        } catch (ClassNotFoundException e) {
            throw new SQLException("Driver Oracle no encontrado: " + e.getMessage());
        }
    }

    public void cerrarConexion(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
                System.out.println("Conexion cerrada correctamente");
            } catch (SQLException e) {
                System.err.println("Error al cerrar conexion: " + e.getMessage());
            }
        }
    }

    public boolean probarConexion() {
        try (Connection conn = getConexion()) {
            return conn != null && !conn.isClosed();
        } catch (SQLException e) {
            System.err.println("Error de conexion: " + e.getMessage());
            return false;
        }
    }
}
