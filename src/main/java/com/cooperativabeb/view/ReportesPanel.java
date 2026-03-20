package com.cooperativabeb.view;

import com.cooperativabeb.report.ReporteExcel;
import com.cooperativabeb.report.ReportePDF;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.io.File;

public class ReportesPanel extends JPanel {

    private JLabel lblEstado;

    public ReportesPanel() {
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel lblTitulo = new JLabel("Generacion de Reportes", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitulo.setForeground(new Color(100, 149, 237));
        lblTitulo.setBorder(new EmptyBorder(0, 0, 20, 0));

        JPanel panelReportes = new JPanel(new GridLayout(4, 2, 15, 15));
        panelReportes.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(100, 149, 237), 1),
            "Seleccione el reporte y formato",
            TitledBorder.LEFT, TitledBorder.TOP,
            new Font("Segoe UI", Font.BOLD, 12),
            new Color(100, 149, 237)));

        // Fila 1 — Clientes
        panelReportes.add(crearBoton("Clientes — PDF", new Color(100, 149, 237), "PDF", "CLIENTES"));
        panelReportes.add(crearBoton("Clientes — Excel", new Color(34, 139, 34), "EXCEL", "CLIENTES"));

        // Fila 2 — Cuentas
        panelReportes.add(crearBoton("Cuentas — PDF", new Color(100, 149, 237), "PDF", "CUENTAS"));
        panelReportes.add(crearBoton("Cuentas — Excel", new Color(34, 139, 34), "EXCEL", "CUENTAS"));

        // Fila 3 — Transacciones
        panelReportes.add(crearBoton("Transacciones — PDF", new Color(100, 149, 237), "PDF", "TRANSACCIONES"));
        panelReportes.add(crearBoton("Transacciones — Excel", new Color(34, 139, 34), "EXCEL", "TRANSACCIONES"));

        // Fila 4 — Planes
        panelReportes.add(crearBoton("Planes de Inversion — PDF", new Color(100, 149, 237), "PDF", "PLANES"));
        panelReportes.add(crearBoton("Planes de Inversion — Excel", new Color(34, 139, 34), "EXCEL", "PLANES"));

        lblEstado = new JLabel("Seleccione un reporte para generar", SwingConstants.CENTER);
        lblEstado.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        lblEstado.setBorder(new EmptyBorder(15, 0, 0, 0));

        add(lblTitulo, BorderLayout.NORTH);
        add(panelReportes, BorderLayout.CENTER);
        add(lblEstado, BorderLayout.SOUTH);
    }

    private JButton crearBoton(String texto, Color color, String formato, String tipo) {
        JButton btn = new JButton(texto);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(color.darker(), 1),
            new EmptyBorder(10, 15, 10, 15)));
        btn.addActionListener(e -> generarReporte(tipo, formato));
        return btn;
    }

    private void generarReporte(String tipo, String formato) {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        chooser.setDialogTitle("Selecciona la carpeta destino");
        if (chooser.showSaveDialog(this) != JFileChooser.APPROVE_OPTION) return;

        String ruta = chooser.getSelectedFile().getAbsolutePath();
        lblEstado.setText("Generando " + formato + "...");
        lblEstado.setForeground(new Color(255, 180, 50));

        SwingWorker<String, Void> worker = new SwingWorker<>() {
            @Override
            protected String doInBackground() throws Exception {
                if (formato.equals("PDF")) {
                    return switch (tipo) {
                        case "CLIENTES"      -> ReportePDF.generarReporteClientes(ruta);
                        case "CUENTAS"       -> ReportePDF.generarReporteCuentas(ruta);
                        case "TRANSACCIONES" -> ReportePDF.generarReporteTransacciones(ruta);
                        case "PLANES"        -> ReportePDF.generarReportePlanes(ruta);
                        default -> throw new Exception("Tipo desconocido");
                    };
                } else {
                    return switch (tipo) {
                        case "CLIENTES"      -> ReporteExcel.generarReporteClientes(ruta);
                        case "CUENTAS"       -> ReporteExcel.generarReporteCuentas(ruta);
                        case "TRANSACCIONES" -> ReporteExcel.generarReporteTransacciones(ruta);
                        case "PLANES"        -> ReporteExcel.generarReportePlanes(ruta);
                        default -> throw new Exception("Tipo desconocido");
                    };
                }
            }

            @Override
            protected void done() {
                try {
                    String archivo = get();
                    lblEstado.setText(formato + " generado: " + new File(archivo).getName());
                    lblEstado.setForeground(new Color(80, 200, 120));
                    int abrir = JOptionPane.showConfirmDialog(ReportesPanel.this,
                        "Reporte generado exitosamente.\n" + archivo +
                        "\n\n¿Abrir la carpeta?", "Listo",
                        JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE);
                    if (abrir == JOptionPane.YES_OPTION)
                        Desktop.getDesktop().open(new File(ruta));
                } catch (Exception e) {
                    lblEstado.setText("Error al generar el reporte");
                    lblEstado.setForeground(Color.RED);
                    JOptionPane.showMessageDialog(ReportesPanel.this,
                        "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        };
        worker.execute();
    }
}
