package com.cooperativabeb.util;

import java.awt.*;

public class Tema {

    // Paleta negro + dorado
    public static final Color NEGRO_FONDO      = new Color(10,  10,  10);
    public static final Color NEGRO_PANEL      = new Color(15,  15,  15);
    public static final Color NEGRO_CARD       = new Color(26,  26,  26);
    public static final Color NEGRO_HOVER      = new Color(32,  32,  32);
    public static final Color NEGRO_BORDE      = new Color(42,  42,  42);
    public static final Color NEGRO_HEADER     = new Color(17,  17,  17);

    public static final Color DORADO_PRINCIPAL = new Color(201, 168, 76);
    public static final Color DORADO_CLARO     = new Color(220, 190, 110);
    public static final Color DORADO_OSCURO    = new Color(160, 130, 50);
    public static final Color DORADO_SUTIL     = new Color(201, 168, 76, 60);

    public static final Color TEXTO_PRINCIPAL  = new Color(220, 220, 220);
    public static final Color TEXTO_SECUNDARIO = new Color(140, 140, 140);
    public static final Color TEXTO_SUTIL      = new Color(80,  80,  80);

    public static final Color VERDE_EXITO      = new Color(76,  175, 80);
    public static final Color ROJO_ERROR       = new Color(220, 80,  80);
    public static final Color AMARILLO_ALERTA  = new Color(255, 180, 50);

    // Fuentes
    public static final Font FUENTE_TITULO     = new Font("Segoe UI", Font.BOLD, 22);
    public static final Font FUENTE_SUBTITULO  = new Font("Segoe UI", Font.BOLD, 15);
    public static final Font FUENTE_NORMAL     = new Font("Segoe UI", Font.PLAIN, 13);
    public static final Font FUENTE_PEQUEÑA    = new Font("Segoe UI", Font.PLAIN, 11);
    public static final Font FUENTE_TABLA      = new Font("Segoe UI", Font.PLAIN, 13);
    public static final Font FUENTE_HEADER     = new Font("Segoe UI", Font.BOLD, 12);

    // Botón primario dorado
    public static void aplicarBotonPrimario(javax.swing.JButton btn) {
        btn.setBackground(DORADO_PRINCIPAL);
        btn.setForeground(NEGRO_FONDO);
        btn.setFont(FUENTE_HEADER);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setOpaque(true);
    }

    // Botón secundario borde dorado
    public static void aplicarBotonSecundario(javax.swing.JButton btn) {
        btn.setBackground(NEGRO_CARD);
        btn.setForeground(DORADO_PRINCIPAL);
        btn.setFont(FUENTE_NORMAL);
        btn.setFocusPainted(false);
        btn.setBorder(javax.swing.BorderFactory.createLineBorder(DORADO_OSCURO, 1));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setOpaque(true);
    }

    // Botón peligro rojo
    public static void aplicarBotonPeligro(javax.swing.JButton btn) {
        btn.setBackground(NEGRO_CARD);
        btn.setForeground(ROJO_ERROR);
        btn.setFont(FUENTE_NORMAL);
        btn.setFocusPainted(false);
        btn.setBorder(javax.swing.BorderFactory.createLineBorder(ROJO_ERROR, 1));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setOpaque(true);
    }

    // Campo de texto
    public static void aplicarCampo(javax.swing.JTextField campo) {
        campo.setBackground(NEGRO_CARD);
        campo.setForeground(TEXTO_PRINCIPAL);
        campo.setCaretColor(DORADO_PRINCIPAL);
        campo.setBorder(javax.swing.BorderFactory.createCompoundBorder(
            javax.swing.BorderFactory.createLineBorder(NEGRO_BORDE, 1),
            javax.swing.BorderFactory.createEmptyBorder(6, 10, 6, 10)));
        campo.setFont(FUENTE_NORMAL);
    }

    // Tabla premium
    public static void aplicarTabla(javax.swing.JTable tabla) {
        tabla.setBackground(NEGRO_CARD);
        tabla.setForeground(TEXTO_PRINCIPAL);
        tabla.setSelectionBackground(DORADO_OSCURO);
        tabla.setSelectionForeground(NEGRO_FONDO);
        tabla.setGridColor(NEGRO_BORDE);
        tabla.setRowHeight(32);
        tabla.setFont(FUENTE_TABLA);
        tabla.setShowHorizontalLines(true);
        tabla.setShowVerticalLines(false);

        // Header dorado
        tabla.getTableHeader().setBackground(NEGRO_HEADER);
        tabla.getTableHeader().setForeground(DORADO_PRINCIPAL);
        tabla.getTableHeader().setFont(FUENTE_HEADER);
        tabla.getTableHeader().setBorder(
            javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, DORADO_OSCURO));
    }
}
