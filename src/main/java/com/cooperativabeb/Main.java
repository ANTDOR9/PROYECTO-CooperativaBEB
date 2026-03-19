package com.cooperativabeb;

import com.formdev.flatlaf.FlatDarkLaf;
import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            FlatDarkLaf.setup();
            JFrame frame = new JFrame("Cooperativa BEB");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(400, 300);
            frame.setLocationRelativeTo(null);
            JLabel label = new JLabel(
                    "Proyecto configurado correctamente",
                    SwingConstants.CENTER
            );
            frame.add(label);
            frame.setVisible(true);
        });
    }
}