package com.cooperativabeb;

import com.cooperativabeb.connection.ConexionOracle;
import com.formdev.flatlaf.FlatDarkLaf;
import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            FlatDarkLaf.setup();

            ConexionOracle conexion = ConexionOracle.getInstancia();
            boolean conectado = conexion.probarConexion();

            JFrame frame = new JFrame("Cooperativa BEB");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(450, 200);
            frame.setLocationRelativeTo(null);

            String mensaje = conectado
                ? "Conexion a Oracle exitosa!"
                : "Error: No se pudo conectar a Oracle";

            JLabel label = new JLabel(mensaje, SwingConstants.CENTER);
            label.setFont(label.getFont().deriveFont(16f));
            frame.add(label);
            frame.setVisible(true);
        });
    }
}
