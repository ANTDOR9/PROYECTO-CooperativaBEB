package com.cooperativabeb.view;

import com.cooperativabeb.dao.CuentaAhorroDAO;
import com.cooperativabeb.dao.TransaccionDAO;
import com.cooperativabeb.model.CuentaAhorro;
import com.cooperativabeb.model.Transaccion;
import com.cooperativabeb.util.Tema;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
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

        // Fondo animado como base
        fondo = new FondoAnimado();
        fondo.setLayout(new BorderLayout());
        setContentPane(fondo);

        // Barra superior
        JPanel panelTop = new JPanel(new BorderLayout());
        panelTop.setBackground(new Color(8, 8, 8, 230));
        panelTop.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 2, 0, Tema.DORADO_OSCURO),
            new EmptyBorder(12, 24, 12, 24)));

        // Logo + nombre banco
        JPanel panelIzq = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
        panelIzq.setOpaque(false);
        try {
            URL imgUrl = getClass().getClassLoader().getResource("logo.png");
            if (imgUrl != null) {
                Image img = new ImageIcon(imgUrl).getImage()
                    .getScaledInstance(40, 40, Image.SCALE_SMOOTH);
                JLabel lblIcono = new JLabel(new ImageIcon(img));
                panelIzq.add(lblIcono);
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

        // Centro — bienvenida y saldo
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

        // Botón cerrar sesión — más grande y elegante
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

        // Panel contenido con fondo semi transparente
        JPanel panelContenido = new JPanel(new BorderLayout());
        panelContenido.setBackground(new Color(10, 10, 10, 180));

        // Pestañas
        JTabbedPane tabs = new JTabbedPane();
        tabs.setFont(new Font("Segoe UI", Font.BOLD, 13));
        tabs.setBackground(new Color(15, 15, 15));
        tabs.setForeground(Tema.DORADO_PRINCIPAL);
        tabs.setBorder(new EmptyBorder(0, 0, 0, 0));
        tabs.addTab("  Mis cuentas  ", crearPanelCuentas());
        tabs.addTab("  Mis transacciones  ", crearPanelTransacciones());
        tabs.addTab("  Mis inversiones  ", crearPanelInversiones());
        panelContenido.add(tabs, BorderLayout.CENTER);

        // Barra estado inferior
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
        panel.setBorder(new EmptyBorder(20, 20, 15, 20));

        JLabel lblTitulo = new JLabel("Mis cuentas de ahorro");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblTitulo.setForeground(Tema.DORADO_PRINCIPAL);
        lblTitulo.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 3, 0, 0, Tema.DORADO_PRINCIPAL),
            new EmptyBorder(0, 10, 0, 0)));

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

        // Tarjetas resumen
        JPanel panelCards = new JPanel(new GridLayout(1, 3, 16, 0));
        panelCards.setOpaque(false);
        panelCards.setBorder(new EmptyBorder(14, 0, 0, 0));
        panelCards.add(crearCard("Total cuentas", "—", Tema.DORADO_PRINCIPAL, "tc"));
        panelCards.add(crearCard("Saldo total", "—", Tema.VERDE_EXITO, "st"));
        panelCards.add(crearCard("Cuentas activas", "—", Tema.TEXTO_SECUNDARIO, "ca"));

        panel.add(lblTitulo, BorderLayout.NORTH);
        panel.add(scroll, BorderLayout.CENTER);
        panel.add(panelCards, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel crearPanelTransacciones() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setOpaque(false);
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel lblTitulo = new JLabel("Mis transacciones recientes");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblTitulo.setForeground(Tema.DORADO_PRINCIPAL);
        lblTitulo.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 3, 0, 0, Tema.DORADO_PRINCIPAL),
            new EmptyBorder(0, 10, 0, 0)));

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

        panel.add(lblTitulo, BorderLayout.NORTH);
        panel.add(scroll, BorderLayout.CENTER);
        return panel;
    }

    private JPanel crearPanelInversiones() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setOpaque(false);
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel lblTitulo = new JLabel("Mis planes de inversion");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblTitulo.setForeground(Tema.DORADO_PRINCIPAL);
        lblTitulo.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 3, 0, 0, Tema.DORADO_PRINCIPAL),
            new EmptyBorder(0, 10, 0, 0)));

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

        panel.add(lblTitulo, BorderLayout.NORTH);
        panel.add(scroll, BorderLayout.CENTER);
        return panel;
    }

    private JPanel crearCard(String titulo, String valor, Color color, String id) {
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
        lblV.setName(id);

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
