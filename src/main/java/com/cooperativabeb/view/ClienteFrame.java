package com.cooperativabeb.view;

import com.cooperativabeb.dao.CuentaAhorroDAO;
import com.cooperativabeb.dao.TransaccionDAO;
import com.cooperativabeb.model.CuentaAhorro;
import com.cooperativabeb.model.Transaccion;
import com.cooperativabeb.report.ReporteExcel;
import com.cooperativabeb.report.ReportePDF;
import com.cooperativabeb.util.Tema;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.File;
import java.net.URL;
import java.util.List;

public class ClienteFrame extends JFrame {

    private CuentaAhorroDAO cuentaDAO = new CuentaAhorroDAO();
    private TransaccionDAO transaccionDAO = new TransaccionDAO();
    private int idCliente;
    private String nombreCliente;
    private JTable tablaCuentas;
    private JTable tablaTransacciones;
    private JLabel lblSaldoTotal;
    private FondoAnimado fondo;

    public ClienteFrame(int idCliente, String nombreCliente) {
        this.idCliente = idCliente;
        this.nombreCliente = nombreCliente;
        initComponents();
        cargarDatos();
    }

    private void initComponents() {
        setTitle("Cooperativa BEB — Portal del Cliente");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setMinimumSize(new Dimension(900, 600));

        try {
            URL imgUrl = getClass().getClassLoader().getResource("logo.png");
            if (imgUrl != null) setIconImage(new ImageIcon(imgUrl).getImage());
        } catch (Exception ignored) {}

        fondo = new FondoAnimado();
        fondo.setLayout(new BorderLayout());
        setContentPane(fondo);

        JPanel panelTop = new JPanel(new BorderLayout());
        panelTop.setBackground(new Color(8, 8, 8, 230));
        panelTop.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 2, 0, Tema.DORADO_OSCURO),
            new EmptyBorder(12, 24, 12, 24)));

        JPanel panelIzq = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
        panelIzq.setOpaque(false);
        try {
            URL imgUrl = getClass().getClassLoader().getResource("logo.png");
            if (imgUrl != null) {
                Image img = new ImageIcon(imgUrl).getImage()
                    .getScaledInstance(40, 40, Image.SCALE_SMOOTH);
                panelIzq.add(new JLabel(new ImageIcon(img)));
            }
        } catch (Exception ignored) {}

        JPanel panelNombreBanco = new JPanel(new GridLayout(2, 1, 0, 2));
        panelNombreBanco.setOpaque(false);
        JLabel lblBanco = new JLabel("COOPERATIVA BEB");
        lblBanco.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblBanco.setForeground(Tema.DORADO_PRINCIPAL);
        JLabel lblPortal = new JLabel("Portal del Cliente");
        lblPortal.setFont(Tema.FUENTE_PEQUEÑA);
        lblPortal.setForeground(Tema.TEXTO_SECUNDARIO);
        panelNombreBanco.add(lblBanco);
        panelNombreBanco.add(lblPortal);
        panelIzq.add(panelNombreBanco);

        JPanel panelCentro = new JPanel(new GridLayout(2, 1, 0, 4));
        panelCentro.setOpaque(false);
        JLabel lblBienvenida = new JLabel("Bienvenido, " + nombreCliente, SwingConstants.CENTER);
        lblBienvenida.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblBienvenida.setForeground(Tema.DORADO_PRINCIPAL);
        lblSaldoTotal = new JLabel("Calculando saldo...", SwingConstants.CENTER);
        lblSaldoTotal.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblSaldoTotal.setForeground(Tema.VERDE_EXITO);
        panelCentro.add(lblBienvenida);
        panelCentro.add(lblSaldoTotal);

        JButton btnCerrar = new JButton("  Cerrar sesion  ");
        btnCerrar.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnCerrar.setBackground(Tema.NEGRO_CARD);
        btnCerrar.setForeground(Tema.DORADO_PRINCIPAL);
        btnCerrar.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Tema.DORADO_OSCURO, 1),
            new EmptyBorder(8, 18, 8, 18)));
        btnCerrar.setFocusPainted(false);
        btnCerrar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnCerrar.addActionListener(e -> {
            fondo.detener();
            dispose();
            new LoginFrame().setVisible(true);
        });

        panelTop.add(panelIzq, BorderLayout.WEST);
        panelTop.add(panelCentro, BorderLayout.CENTER);
        panelTop.add(btnCerrar, BorderLayout.EAST);

        JPanel panelContenido = new JPanel(new BorderLayout());
        panelContenido.setBackground(new Color(10, 10, 10, 180));

        JTabbedPane tabs = new JTabbedPane();
        tabs.setFont(new Font("Segoe UI", Font.BOLD, 13));
        tabs.setBackground(new Color(15, 15, 15));
        tabs.setForeground(Tema.DORADO_PRINCIPAL);
        tabs.addTab("  Mis cuentas  ", crearPanelCuentas());
        tabs.addTab("  Mis transacciones  ", crearPanelTransacciones());
        tabs.addTab("  Mis inversiones  ", crearPanelInversiones());
        panelContenido.add(tabs, BorderLayout.CENTER);

        JPanel panelEstado = new JPanel(new BorderLayout());
        panelEstado.setBackground(new Color(5, 5, 5, 220));
        panelEstado.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(1, 0, 0, 0, Tema.NEGRO_BORDE),
            new EmptyBorder(5, 20, 5, 20)));
        JLabel lblEstado = new JLabel("Portal Cliente — Solo lectura");
        lblEstado.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblEstado.setForeground(Tema.TEXTO_SUTIL);
        JLabel lblVer = new JLabel("Cooperativa BEB v1.0 — SENATI 2025");
        lblVer.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblVer.setForeground(Tema.TEXTO_SUTIL);
        panelEstado.add(lblEstado, BorderLayout.WEST);
        panelEstado.add(lblVer, BorderLayout.EAST);

        fondo.add(panelTop, BorderLayout.NORTH);
        fondo.add(panelContenido, BorderLayout.CENTER);
        fondo.add(panelEstado, BorderLayout.SOUTH);
    }

    private JPanel crearPanelCuentas() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setOpaque(false);
        panel.setBorder(new EmptyBorder(16, 20, 12, 20));

        JPanel panelNorth = new JPanel(new BorderLayout());
        panelNorth.setOpaque(false);
        panelNorth.setBorder(new EmptyBorder(0, 0, 10, 0));
        JLabel lblTitulo = crearTitulo("Mis cuentas de ahorro");
        JPanel panelBtns = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        panelBtns.setOpaque(false);
        JButton btnPDF   = crearBtnReporte("Descargar PDF",   new Color(180, 40, 40));
        JButton btnExcel = crearBtnReporte("Descargar Excel", new Color(34, 139, 34));
        panelBtns.add(btnPDF);
        panelBtns.add(btnExcel);
        panelNorth.add(lblTitulo, BorderLayout.WEST);
        panelNorth.add(panelBtns, BorderLayout.EAST);

        String[] cols = {"Nro. Cuenta", "Saldo (S/.)", "Tipo", "Estado", "Fecha apertura"};
        tablaCuentas = new JTable(new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        });
        Tema.aplicarTabla(tablaCuentas);
        tablaCuentas.setBackground(new Color(20, 20, 20));
        JScrollPane scroll = new JScrollPane(tablaCuentas);
        scroll.setOpaque(false);
        scroll.getViewport().setBackground(new Color(20, 20, 20));
        scroll.setBorder(BorderFactory.createLineBorder(Tema.NEGRO_BORDE, 1));

        JPanel panelCards = new JPanel(new GridLayout(1, 3, 16, 0));
        panelCards.setOpaque(false);
        panelCards.setBorder(new EmptyBorder(12, 0, 0, 0));
        panelCards.add(crearCard("Total cuentas", "—", Tema.DORADO_PRINCIPAL));
        panelCards.add(crearCard("Saldo total", "—", Tema.VERDE_EXITO));
        panelCards.add(crearCard("Cuentas activas", "—", Tema.TEXTO_SECUNDARIO));

        panel.add(panelNorth, BorderLayout.NORTH);
        panel.add(scroll, BorderLayout.CENTER);
        panel.add(panelCards, BorderLayout.SOUTH);

        btnPDF.addActionListener(e -> generarReporte("PDF", "CUENTAS"));
        btnExcel.addActionListener(e -> generarReporte("EXCEL", "CUENTAS"));
        return panel;
    }

    private JPanel crearPanelTransacciones() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setOpaque(false);
        panel.setBorder(new EmptyBorder(16, 20, 16, 20));

        JPanel panelNorth = new JPanel(new BorderLayout());
        panelNorth.setOpaque(false);
        panelNorth.setBorder(new EmptyBorder(0, 0, 10, 0));
        JLabel lblTitulo = crearTitulo("Mis transacciones recientes");
        JPanel panelBtns = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        panelBtns.setOpaque(false);
        JButton btnPDF   = crearBtnReporte("Descargar PDF",   new Color(180, 40, 40));
        JButton btnExcel = crearBtnReporte("Descargar Excel", new Color(34, 139, 34));
        panelBtns.add(btnPDF);
        panelBtns.add(btnExcel);
        panelNorth.add(lblTitulo, BorderLayout.WEST);
        panelNorth.add(panelBtns, BorderLayout.EAST);

        String[] cols = {"Tipo", "Monto (S/.)", "Saldo anterior", "Saldo posterior", "Fecha", "Estado"};
        tablaTransacciones = new JTable(new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        });
        Tema.aplicarTabla(tablaTransacciones);
        tablaTransacciones.setBackground(new Color(20, 20, 20));
        JScrollPane scroll = new JScrollPane(tablaTransacciones);
        scroll.setOpaque(false);
        scroll.getViewport().setBackground(new Color(20, 20, 20));
        scroll.setBorder(BorderFactory.createLineBorder(Tema.NEGRO_BORDE, 1));

        panel.add(panelNorth, BorderLayout.NORTH);
        panel.add(scroll, BorderLayout.CENTER);

        btnPDF.addActionListener(e -> generarReporte("PDF", "TRANSACCIONES"));
        btnExcel.addActionListener(e -> generarReporte("EXCEL", "TRANSACCIONES"));
        return panel;
    }

    private JPanel crearPanelInversiones() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setOpaque(false);
        panel.setBorder(new EmptyBorder(16, 20, 16, 20));

        JPanel panelNorth = new JPanel(new BorderLayout());
        panelNorth.setOpaque(false);
        panelNorth.setBorder(new EmptyBorder(0, 0, 10, 0));
        JLabel lblTitulo = crearTitulo("Mis planes de inversion");
        JPanel panelBtns = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        panelBtns.setOpaque(false);
        JButton btnPDF   = crearBtnReporte("Descargar PDF",   new Color(180, 40, 40));
        JButton btnExcel = crearBtnReporte("Descargar Excel", new Color(34, 139, 34));
        panelBtns.add(btnPDF);
        panelBtns.add(btnExcel);
        panelNorth.add(lblTitulo, BorderLayout.WEST);
        panelNorth.add(panelBtns, BorderLayout.EAST);

        String[] cols = {"ID", "Monto invertido", "Tasa %", "Plazo", "Vencimiento", "Ganancia est.", "Estado"};
        JTable tablaPlanes = new JTable(new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        });
        Tema.aplicarTabla(tablaPlanes);
        tablaPlanes.setBackground(new Color(20, 20, 20));
        JScrollPane scroll = new JScrollPane(tablaPlanes);
        scroll.setOpaque(false);
        scroll.getViewport().setBackground(new Color(20, 20, 20));
        scroll.setBorder(BorderFactory.createLineBorder(Tema.NEGRO_BORDE, 1));

        try {
            com.cooperativabeb.dao.PlanInversionDAO planDAO =
                new com.cooperativabeb.dao.PlanInversionDAO();
            DefaultTableModel model = (DefaultTableModel) tablaPlanes.getModel();
            for (com.cooperativabeb.model.PlanInversion p :
                    planDAO.listarPorCliente(idCliente)) {
                model.addRow(new Object[]{
                    p.getIdPlan(),
                    String.format("S/. %.2f", p.getMontoInvertido()),
                    p.getTasaPactada() + "%",
                    p.getPlazoMeses() + " meses",
                    p.getFechaVencimiento(),
                    String.format("S/. %.2f", p.calcularGanancia()),
                    p.getEstado()
                });
            }
        } catch (Exception e) {
            System.err.println("Error cargando planes: " + e.getMessage());
        }

        panel.add(panelNorth, BorderLayout.NORTH);
        panel.add(scroll, BorderLayout.CENTER);

        btnPDF.addActionListener(e -> generarReporte("PDF", "PLANES"));
        btnExcel.addActionListener(e -> generarReporte("EXCEL", "PLANES"));
        return panel;
    }

    private void generarReporte(String formato, String tipo) {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        chooser.setDialogTitle("Selecciona carpeta destino");
        if (chooser.showSaveDialog(this) != JFileChooser.APPROVE_OPTION) return;
        String ruta = chooser.getSelectedFile().getAbsolutePath();
        SwingWorker<String, Void> worker = new SwingWorker<>() {
            @Override
            protected String doInBackground() throws Exception {
                if (formato.equals("PDF")) {
                    return switch (tipo) {
                        case "CUENTAS"       -> ReportePDF.generarReporteCuentas(ruta);
                        case "TRANSACCIONES" -> ReportePDF.generarReporteTransacciones(ruta);
                        case "PLANES"        -> ReportePDF.generarReportePlanes(ruta);
                        default -> throw new Exception("Tipo desconocido");
                    };
                } else {
                    return switch (tipo) {
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
                    int abrir = JOptionPane.showConfirmDialog(ClienteFrame.this,
                        "Reporte generado: " + new File(archivo).getName() +
                        "\n\n¿Abrir la carpeta?", "Descarga completa",
                        JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE);
                    if (abrir == JOptionPane.YES_OPTION)
                        Desktop.getDesktop().open(new File(ruta));
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(ClienteFrame.this,
                        "Error: " + e.getMessage(), "Error",
                        JOptionPane.ERROR_MESSAGE);
                }
            }
        };
        worker.execute();
    }

    private JLabel crearTitulo(String texto) {
        JLabel lbl = new JLabel(texto);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lbl.setForeground(Tema.DORADO_PRINCIPAL);
        lbl.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 3, 0, 0, Tema.DORADO_PRINCIPAL),
            new EmptyBorder(0, 10, 0, 0)));
        return lbl;
    }

    private JButton crearBtnReporte(String texto, Color color) {
        JButton btn = new JButton(texto);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(new EmptyBorder(7, 14, 7, 14));
        return btn;
    }

    private JPanel crearCard(String titulo, String valor, Color color) {
        JPanel card = new JPanel(new BorderLayout(4, 6));
        card.setBackground(new Color(15, 15, 15, 200));
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 3, 0, 0, color),
            new EmptyBorder(12, 16, 12, 16)));
        JLabel lblT = new JLabel(titulo);
        lblT.setFont(Tema.FUENTE_PEQUEÑA);
        lblT.setForeground(Tema.TEXTO_SECUNDARIO);
        JLabel lblV = new JLabel(valor);
        lblV.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblV.setForeground(color);
        card.add(lblT, BorderLayout.NORTH);
        card.add(lblV, BorderLayout.CENTER);
        return card;
    }

    private void cargarDatos() {
        List<CuentaAhorro> cuentas = cuentaDAO.listarPorCliente(idCliente);
        DefaultTableModel modelCuentas = (DefaultTableModel) tablaCuentas.getModel();
        modelCuentas.setRowCount(0);
        double saldoTotal = 0;
        int activas = 0;
        for (CuentaAhorro c : cuentas) {
            modelCuentas.addRow(new Object[]{
                c.getNroCuenta(),
                String.format("S/. %.2f", c.getSaldo()),
                c.getTipoCuenta(),
                c.getEstado(),
                c.getFechaApertura()
            });
            saldoTotal += c.getSaldo();
            if (c.getEstado().equals("ACTIVA")) activas++;
        }
        lblSaldoTotal.setText(String.format("Saldo total disponible: S/. %.2f", saldoTotal));

        DefaultTableModel modelTrans = (DefaultTableModel) tablaTransacciones.getModel();
        modelTrans.setRowCount(0);
        for (CuentaAhorro c : cuentas) {
            for (Transaccion t : transaccionDAO.listarPorCuenta(c.getIdCuenta())) {
                modelTrans.addRow(new Object[]{
                    t.getTipo(),
                    String.format("S/. %.2f", t.getMonto()),
                    String.format("S/. %.2f", t.getSaldoAnterior()),
                    String.format("S/. %.2f", t.getSaldoPosterior()),
                    t.getFechaTransaccion(),
                    t.getEstado()
                });
            }
        }
    }
}
