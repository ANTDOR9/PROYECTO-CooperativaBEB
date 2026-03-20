package com.cooperativabeb.view;

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

        JLabel lblTitulo = new JLabel("Generacion de Reportes PDF", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitulo.setForeground(new Color(100, 149, 237));
        lblTitulo.setBorder(new EmptyBorder(0, 0, 20, 0));

        // Panel de botones
        JPanel panelReportes = new JPanel(new GridLayout(2, 2, 20, 20));
        panelReportes.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(100, 149, 237), 1),
            "Seleccione el reporte a generar",
            TitledBorder.LEFT, TitledBorder.TOP,
            new Font("Segoe UI", Font.BOLD, 12),
            new Color(100, 149, 237)));

        panelReportes.add(crearBotonReporte(
            "Reporte de Clientes",
            "Lista completa de clientes registrados",
            new Color(100, 149, 237), "CLIENTES"));

        panelReportes.add(crearBotonReporte(
            "Reporte de Cuentas",
            "Cuentas de ahorro con saldos",
            new Color(80, 200, 120), "CUENTAS"));

        panelReportes.add(crearBotonReporte(
            "Reporte de Transacciones",
            "Ultimas 100 transacciones del sistema",
            new Color(255, 180, 50), "TRANSACCIONES"));

        panelReportes.add(crearBotonReporte(
            "Reporte de Planes",
            "Planes de inversion activos y vencidos",
            new Color(200, 100, 200), "PLANES"));

        // Estado
        lblEstado = new JLabel("Seleccione un reporte para generar", SwingConstants.CENTER);
        lblEstado.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        lblEstado.setBorder(new EmptyBorder(15, 0, 0, 0));

        add(lblTitulo, BorderLayout.NORTH);
        add(panelReportes, BorderLayout.CENTER);
        add(lblEstado, BorderLayout.SOUTH);
    }

    private JPanel crearBotonReporte(String titulo, String descripcion,
                                      Color color, String tipo) {
        JPanel panel = new JPanel(new BorderLayout(8, 8));
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(color, 2),
            new EmptyBorder(15, 15, 15, 15)));

        JLabel lblTit = new JLabel(titulo);
        lblTit.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblTit.setForeground(color);

        JLabel lblDesc = new JLabel("<html>" + descripcion + "</html>");
        lblDesc.setFont(new Font("Segoe UI", Font.PLAIN, 11));

        JButton btnGenerar = new JButton("Generar PDF");
        btnGenerar.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnGenerar.setBackground(color);
        btnGenerar.setForeground(Color.WHITE);
        btnGenerar.setFocusPainted(false);
        btnGenerar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnGenerar.setPreferredSize(new Dimension(130, 36));

        btnGenerar.addActionListener(e -> generarReporte(tipo, color));

        panel.add(lblTit, BorderLayout.NORTH);
        panel.add(lblDesc, BorderLayout.CENTER);
        panel.add(btnGenerar, BorderLayout.SOUTH);
        return panel;
    }

    private void generarReporte(String tipo, Color color) {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        chooser.setDialogTitle("Selecciona la carpeta donde guardar el PDF");

        int result = chooser.showSaveDialog(this);
        if (result != JFileChooser.APPROVE_OPTION) return;

        String ruta = chooser.getSelectedFile().getAbsolutePath();
        lblEstado.setText("Generando reporte...");
        lblEstado.setForeground(new Color(255, 180, 50));

        SwingWorker<String, Void> worker = new SwingWorker<>() {
            @Override
            protected String doInBackground() throws Exception {
                return switch (tipo) {
                    case "CLIENTES"      -> ReportePDF.generarReporteClientes(ruta);
                    case "CUENTAS"       -> ReportePDF.generarReporteCuentas(ruta);
                    case "TRANSACCIONES" -> ReportePDF.generarReporteTransacciones(ruta);
                    case "PLANES"        -> ReportePDF.generarReportePlanes(ruta);
                    default -> throw new Exception("Tipo desconocido");
                };
            }

            @Override
            protected void done() {
                try {
                    String archivo = get();
                    lblEstado.setText("PDF generado: " + new File(archivo).getName());
                    lblEstado.setForeground(new Color(80, 200, 120));

                    int abrir = JOptionPane.showConfirmDialog(
                        ReportesPanel.this,
                        "PDF generado exitosamente.\n" + archivo +
                        "\n\n¿Desea abrir la carpeta?",
                        "Reporte generado",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.INFORMATION_MESSAGE);

                    if (abrir == JOptionPane.YES_OPTION) {
                        Desktop.getDesktop().open(new File(ruta));
                    }
                } catch (Exception e) {
                    lblEstado.setText("Error al generar el reporte");
                    lblEstado.setForeground(Color.RED);
                    JOptionPane.showMessageDialog(ReportesPanel.this,
                        "Error: " + e.getMessage(), "Error",
                        JOptionPane.ERROR_MESSAGE);
                }
            }
        };
        worker.execute();
    }
}
