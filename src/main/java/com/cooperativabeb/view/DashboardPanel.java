package com.cooperativabeb.view;

import com.cooperativabeb.connection.ConexionOracle;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.sql.*;

public class DashboardPanel extends JPanel {

    private ConexionOracle conexion = ConexionOracle.getInstancia();
    private JLabel lblClientesActivos, lblCuentasActivas;
    private JLabel lblTotalAhorros, lblPlanesActivos;
    private JLabel lblTotalInversiones, lblTransHoy;

    public DashboardPanel() {
        initComponents();
        cargarDatos();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(15, 15, 15, 15));

        JPanel panelTarjetas = new JPanel(new GridLayout(2, 3, 12, 12));
        panelTarjetas.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(100, 149, 237), 1),
            "Resumen general", TitledBorder.LEFT, TitledBorder.TOP,
            new Font("Segoe UI", Font.BOLD, 12), new Color(100, 149, 237)));

        lblClientesActivos  = crearTarjeta(panelTarjetas, "Clientes activos",    new Color(100, 149, 237));
        lblCuentasActivas   = crearTarjeta(panelTarjetas, "Cuentas activas",     new Color(80,  200, 120));
        lblTotalAhorros     = crearTarjeta(panelTarjetas, "Total en ahorros",    new Color(255, 180, 50));
        lblPlanesActivos    = crearTarjeta(panelTarjetas, "Planes activos",      new Color(200, 100, 200));
        lblTotalInversiones = crearTarjeta(panelTarjetas, "Total invertido",     new Color(100, 200, 220));
        lblTransHoy         = crearTarjeta(panelTarjetas, "Transacciones hoy",   new Color(220, 100, 100));

        JPanel panelGraficos = new JPanel(new GridLayout(1, 2, 12, 0));
        panelGraficos.add(crearGraficoBarras());
        panelGraficos.add(crearGraficoPie());

        JButton btnRefrescar = new JButton("Refrescar dashboard");
        btnRefrescar.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnRefrescar.setBackground(new Color(100, 149, 237));
        btnRefrescar.setForeground(Color.WHITE);
        btnRefrescar.setFocusPainted(false);
        btnRefrescar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnRefrescar.setPreferredSize(new Dimension(0, 36));
        btnRefrescar.addActionListener(e -> {
            removeAll();
            initComponents();
            cargarDatos();
            revalidate();
            repaint();
        });

        JPanel panelSur = new JPanel(new BorderLayout());
        panelSur.add(btnRefrescar, BorderLayout.CENTER);

        add(panelTarjetas, BorderLayout.NORTH);
        add(panelGraficos, BorderLayout.CENTER);
        add(panelSur, BorderLayout.SOUTH);
    }

    private JLabel crearTarjeta(JPanel parent, String titulo, Color color) {
        JPanel tarjeta = new JPanel(new BorderLayout(5, 5));
        tarjeta.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(color, 2),
            new EmptyBorder(10, 14, 10, 14)));

        JLabel lblTitulo = new JLabel(titulo);
        lblTitulo.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblTitulo.setForeground(color);

        JLabel lblValor = new JLabel("...");
        lblValor.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblValor.setForeground(Color.WHITE);
        lblValor.setHorizontalAlignment(SwingConstants.RIGHT);

        tarjeta.add(lblTitulo, BorderLayout.NORTH);
        tarjeta.add(lblValor, BorderLayout.CENTER);
        parent.add(tarjeta);
        return lblValor;
    }

    private JPanel crearGraficoBarras() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        try (Connection conn = conexion.getConexion()) {
            String sql = "SELECT tipo_cuenta, SUM(saldo) FROM CUENTA_AHORRO " +
                         "WHERE estado='ACTIVA' GROUP BY tipo_cuenta";
            ResultSet rs = conn.createStatement().executeQuery(sql);
            while (rs.next())
                dataset.addValue(rs.getDouble(2), "Saldo", rs.getString(1));
        } catch (SQLException e) {
            dataset.addValue(0, "Saldo", "Sin datos");
        }

        JFreeChart chart = ChartFactory.createBarChart(
            "Saldo por tipo de cuenta", "Tipo", "Saldo (S/.)", dataset);

        chart.setBackgroundPaint(new Color(30, 30, 46));
        chart.getTitle().setPaint(Color.WHITE);
        chart.getLegend().setBackgroundPaint(new Color(30, 30, 46));
        chart.getLegend().setItemPaint(Color.WHITE);

        CategoryPlot plot = chart.getCategoryPlot();
        plot.setBackgroundPaint(new Color(40, 40, 60));
        plot.setDomainGridlinePaint(new Color(80, 80, 100));
        plot.setRangeGridlinePaint(new Color(80, 80, 100));
        plot.getDomainAxis().setLabelPaint(Color.WHITE);
        plot.getDomainAxis().setTickLabelPaint(Color.WHITE);
        plot.getRangeAxis().setLabelPaint(Color.WHITE);
        plot.getRangeAxis().setTickLabelPaint(Color.WHITE);

        BarRenderer renderer = (BarRenderer) plot.getRenderer();
        renderer.setSeriesPaint(0, new Color(100, 149, 237));
        renderer.setMaximumBarWidth(0.3);

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(100, 149, 237), 1),
            "Saldos por tipo de cuenta", TitledBorder.LEFT, TitledBorder.TOP,
            new Font("Segoe UI", Font.BOLD, 12), new Color(100, 149, 237)));
        panel.add(new ChartPanel(chart), BorderLayout.CENTER);
        return panel;
    }

    private JPanel crearGraficoPie() {
        DefaultPieDataset dataset = new DefaultPieDataset();
        try (Connection conn = conexion.getConexion()) {
            String sql = "SELECT tipo, SUM(monto) FROM TRANSACCION " +
                         "WHERE estado='EXITOSA' GROUP BY tipo";
            ResultSet rs = conn.createStatement().executeQuery(sql);
            while (rs.next())
                dataset.setValue(rs.getString(1), rs.getDouble(2));
        } catch (SQLException e) {
            dataset.setValue("Sin datos", 1);
        }

        JFreeChart chart = ChartFactory.createPieChart(
            "Transacciones por tipo", dataset, true, true, false);

        chart.setBackgroundPaint(new Color(30, 30, 46));
        chart.getTitle().setPaint(Color.WHITE);
        chart.getLegend().setBackgroundPaint(new Color(30, 30, 46));
        chart.getLegend().setItemPaint(Color.WHITE);

        PiePlot plot = (PiePlot) chart.getPlot();
        plot.setBackgroundPaint(new Color(40, 40, 60));
        plot.setOutlinePaint(new Color(100, 149, 237));
        plot.setLabelBackgroundPaint(new Color(50, 50, 70));
        plot.setLabelPaint(Color.WHITE);
        plot.setLabelOutlinePaint(null);
        plot.setLabelShadowPaint(null);
        plot.setSectionPaint("DEPOSITO",      new Color(80,  200, 120));
        plot.setSectionPaint("RETIRO",        new Color(220, 100, 100));
        plot.setSectionPaint("APERTURA",      new Color(100, 149, 237));
        plot.setSectionPaint("TRANSFERENCIA", new Color(255, 180, 50));
        plot.setSectionPaint("INTERES",       new Color(200, 100, 200));

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(100, 149, 237), 1),
            "Distribucion de transacciones", TitledBorder.LEFT, TitledBorder.TOP,
            new Font("Segoe UI", Font.BOLD, 12), new Color(100, 149, 237)));
        panel.add(new ChartPanel(chart), BorderLayout.CENTER);
        return panel;
    }

    private void cargarDatos() {
        try (Connection conn = conexion.getConexion()) {
            ResultSet rs = conn.createStatement().executeQuery(
                "SELECT clientes_activos, cuentas_activas, total_ahorros, " +
                "planes_activos, total_inversiones, transacciones_hoy FROM v_dashboard");
            if (rs.next()) {
                lblClientesActivos.setText(String.valueOf(rs.getInt(1)));
                lblCuentasActivas.setText(String.valueOf(rs.getInt(2)));
                lblTotalAhorros.setText(String.format("S/. %.0f", rs.getDouble(3)));
                lblPlanesActivos.setText(String.valueOf(rs.getInt(4)));
                lblTotalInversiones.setText(String.format("S/. %.0f", rs.getDouble(5)));
                lblTransHoy.setText(String.valueOf(rs.getInt(6)));
            }
        } catch (SQLException e) {
            System.err.println("Error dashboard: " + e.getMessage());
        }
    }
}
