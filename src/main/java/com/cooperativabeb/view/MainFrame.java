package com.cooperativabeb.view;

import com.cooperativabeb.dao.ClienteDAO;
import com.cooperativabeb.dao.CuentaAhorroDAO;
import com.cooperativabeb.dao.TransaccionDAO;
import com.cooperativabeb.model.Cliente;
import com.cooperativabeb.model.CuentaAhorro;
import com.cooperativabeb.model.Transaccion;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class MainFrame extends JFrame {

    private String usuarioActual;
    private ClienteDAO clienteDAO = new ClienteDAO();
    private CuentaAhorroDAO cuentaDAO = new CuentaAhorroDAO();
    private TransaccionDAO transaccionDAO = new TransaccionDAO();
    private JTable tablaClientes;
    private JTable tablaCuentas;
    private JTable tablaTransacciones;

    public MainFrame(String usuario) {
        this.usuarioActual = usuario;
        initComponents();
        cargarDatos();
    }

    private void initComponents() {
        setTitle("Cooperativa BEB — Panel Principal");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1100, 680);
        setLocationRelativeTo(null);

        JPanel panelTop = new JPanel(new BorderLayout());
        panelTop.setBorder(new EmptyBorder(10, 20, 10, 20));
        panelTop.setBackground(new Color(30, 30, 46));

        JLabel lblSistema = new JLabel("COOPERATIVA BEB — Sistema Financiero");
        lblSistema.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        JLabel lblBienvenida = new JLabel("Bienvenido, " + usuarioActual.toUpperCase());
        lblBienvenida.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblBienvenida.setForeground(new Color(100, 149, 237));

        JButton btnRefrescar = new JButton("↻ Refrescar");
        btnRefrescar.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        btnRefrescar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnRefrescar.addActionListener(e -> cargarDatos());

        JButton btnCerrar = new JButton("Cerrar Sesion");
        btnCerrar.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        btnCerrar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnCerrar.addActionListener(e -> { dispose(); new LoginFrame().setVisible(true); });

        JPanel panelDer = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        panelDer.setOpaque(false);
        panelDer.add(btnRefrescar);
        panelDer.add(btnCerrar);

        panelTop.add(lblSistema, BorderLayout.WEST);
        panelTop.add(lblBienvenida, BorderLayout.CENTER);
        panelTop.add(panelDer, BorderLayout.EAST);

        JTabbedPane tabs = new JTabbedPane();
        tabs.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tabs.addTab("Dashboard", new DashboardPanel());
        tabs.addTab("Usuarios", new UsuariosPanel());
        tabs.addTab("Reportes PDF", new ReportesPanel());
        tabs.addTab("Clientes", crearPanelClientes());
        tabs.addTab("Cuentas de ahorro", crearPanelCuentas());

        PlanInversionPanel planPanel = new PlanInversionPanel();
        tabs.addTab("Planes de inversion", planPanel);
        tabs.addChangeListener(e -> {
            if (tabs.getSelectedComponent() instanceof PlanInversionPanel p) {
                p.refrescar();
            }
        });

        tabs.addTab("Transacciones recientes", crearPanelTransacciones());

        add(panelTop, BorderLayout.NORTH);
        add(tabs, BorderLayout.CENTER);
    }

    private JPanel crearPanelClientes() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(new EmptyBorder(15, 15, 15, 15));

        JPanel panelBusqueda = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        JTextField txtBuscar = new JTextField(22);
        JButton btnBuscar = new JButton("Buscar");
        JButton btnVerTodos = new JButton("Ver todos");
        panelBusqueda.add(new JLabel("Buscar:"));
        panelBusqueda.add(txtBuscar);
        panelBusqueda.add(btnBuscar);
        panelBusqueda.add(btnVerTodos);

        String[] cols = {"ID", "DNI", "Nombre completo", "Telefono", "Email", "Estado"};
        tablaClientes = new JTable(new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        });
        tablaClientes.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tablaClientes.setRowHeight(28);
        tablaClientes.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        tablaClientes.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        JButton btnNuevo     = new JButton("+ Nuevo cliente");
        JButton btnEditar    = new JButton("Editar");
        JButton btnDesactivar = new JButton("Desactivar");
        btnNuevo.setBackground(new Color(100, 149, 237));
        btnNuevo.setForeground(Color.WHITE);
        btnNuevo.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnNuevo.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnEditar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnDesactivar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        panelBotones.add(btnNuevo);
        panelBotones.add(btnEditar);
        panelBotones.add(btnDesactivar);

        panel.add(panelBusqueda, BorderLayout.NORTH);
        panel.add(new JScrollPane(tablaClientes), BorderLayout.CENTER);
        panel.add(panelBotones, BorderLayout.SOUTH);

        btnBuscar.addActionListener(e -> {
            String t = txtBuscar.getText().trim();
            if (!t.isEmpty()) cargarTablaClientes(clienteDAO.buscarPorNombre(t));
        });
        btnVerTodos.addActionListener(e -> {
            txtBuscar.setText("");
            cargarTablaClientes(clienteDAO.listarTodos());
        });
        btnNuevo.addActionListener(e -> {
            ClienteFormDialog form = new ClienteFormDialog(this, null);
            form.setVisible(true);
            if (form.isGuardado()) cargarTablaClientes(clienteDAO.listarTodos());
        });
        btnEditar.addActionListener(e -> {
            int fila = tablaClientes.getSelectedRow();
            if (fila < 0) { JOptionPane.showMessageDialog(this, "Seleccione un cliente"); return; }
            Cliente c = clienteDAO.buscarPorId((int) tablaClientes.getValueAt(fila, 0));
            if (c != null) {
                ClienteFormDialog form = new ClienteFormDialog(this, c);
                form.setVisible(true);
                if (form.isGuardado()) cargarTablaClientes(clienteDAO.listarTodos());
            }
        });
        btnDesactivar.addActionListener(e -> {
            int fila = tablaClientes.getSelectedRow();
            if (fila < 0) { JOptionPane.showMessageDialog(this, "Seleccione un cliente"); return; }
            int id = (int) tablaClientes.getValueAt(fila, 0);
            String nombre = tablaClientes.getValueAt(fila, 2).toString();
            int ok = JOptionPane.showConfirmDialog(this, "Desactivar a " + nombre + "?",
                "Confirmar", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            if (ok == JOptionPane.YES_OPTION && clienteDAO.eliminar(id)) {
                JOptionPane.showMessageDialog(this, "Cliente desactivado");
                cargarTablaClientes(clienteDAO.listarTodos());
            }
        });
        return panel;
    }

    private JPanel crearPanelCuentas() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(new EmptyBorder(15, 15, 15, 15));

        String[] cols = {"ID", "Nro. cuenta", "ID Cliente", "Saldo", "Tipo", "Estado", "Apertura"};
        tablaCuentas = new JTable(new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        });
        tablaCuentas.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tablaCuentas.setRowHeight(28);
        tablaCuentas.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        tablaCuentas.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        JButton btnNuevaCuenta = new JButton("+ Abrir cuenta");
        JButton btnOperar      = new JButton("Deposito / Retiro");
        JButton btnRefrescarC  = new JButton("Refrescar");

        btnNuevaCuenta.setBackground(new Color(201, 168, 76));
        btnNuevaCuenta.setForeground(Color.BLACK);
        btnNuevaCuenta.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnNuevaCuenta.setFocusPainted(false);
        btnNuevaCuenta.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btnOperar.setBackground(new Color(100, 149, 237));
        btnOperar.setForeground(Color.WHITE);
        btnOperar.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnOperar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnRefrescarC.setCursor(new Cursor(Cursor.HAND_CURSOR));

        panelBotones.add(btnNuevaCuenta);
        panelBotones.add(btnOperar);
        panelBotones.add(btnRefrescarC);

        panel.add(new JScrollPane(tablaCuentas), BorderLayout.CENTER);
        panel.add(panelBotones, BorderLayout.SOUTH);

        btnRefrescarC.addActionListener(e -> cargarTablaCuentas());

        btnOperar.addActionListener(e -> {
            int fila = tablaCuentas.getSelectedRow();
            if (fila < 0) { JOptionPane.showMessageDialog(this, "Seleccione una cuenta"); return; }
            String nro = tablaCuentas.getValueAt(fila, 1).toString();
            CuentaAhorro cuenta = cuentaDAO.buscarPorNro(nro);
            if (cuenta != null) {
                CuentaOperacionDialog op = new CuentaOperacionDialog(this, cuenta);
                op.setVisible(true);
                cargarTablaCuentas();
                cargarTablaTransacciones();
            }
        });

        btnNuevaCuenta.addActionListener(e -> {
            JDialog dialog = new JDialog(this, "Abrir nueva cuenta", true);
            dialog.setSize(380, 300);
            dialog.setLocationRelativeTo(this);
            dialog.getContentPane().setBackground(new Color(15, 15, 15));

            JPanel panel2 = new JPanel(new GridBagLayout());
            panel2.setBackground(new Color(15, 15, 15));
            panel2.setBorder(new EmptyBorder(20, 20, 20, 20));

            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(8, 8, 8, 8);
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.weightx = 1.0;

            JComboBox<Cliente> cmbCliente = new JComboBox<>();
            clienteDAO.listarTodos().stream()
                .filter(c -> c.getEstado().equals("ACTIVO"))
                .forEach(cmbCliente::addItem);

            JComboBox<String> cmbTipo = new JComboBox<>(
                new String[]{"BASICA", "PREMIUM", "INFANTIL"});

            JTextField txtSaldo = new JTextField();
            txtSaldo.setBackground(new Color(26, 26, 26));
            txtSaldo.setForeground(Color.WHITE);
            txtSaldo.setCaretColor(new Color(201, 168, 76));
            txtSaldo.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(42, 42, 42), 1),
                new EmptyBorder(6, 10, 6, 10)));
            txtSaldo.putClientProperty("JTextField.placeholderText", "Saldo inicial (minimo 100)");

            Font fLbl = new Font("Segoe UI", Font.PLAIN, 13);
            Color cLbl = new Color(140, 140, 140);

            JLabel l1 = new JLabel("Cliente:"); l1.setFont(fLbl); l1.setForeground(cLbl);
            JLabel l2 = new JLabel("Tipo:"); l2.setFont(fLbl); l2.setForeground(cLbl);
            JLabel l3 = new JLabel("Saldo inicial:"); l3.setFont(fLbl); l3.setForeground(cLbl);

            gbc.gridx=0; gbc.gridy=0; gbc.weightx=0.35; panel2.add(l1, gbc);
            gbc.gridx=1; gbc.weightx=0.65; panel2.add(cmbCliente, gbc);
            gbc.gridx=0; gbc.gridy=1; gbc.weightx=0.35; panel2.add(l2, gbc);
            gbc.gridx=1; gbc.weightx=0.65; panel2.add(cmbTipo, gbc);
            gbc.gridx=0; gbc.gridy=2; gbc.weightx=0.35; panel2.add(l3, gbc);
            gbc.gridx=1; gbc.weightx=0.65; panel2.add(txtSaldo, gbc);

            JButton btnGuardar  = new JButton("Abrir cuenta");
            JButton btnCancelar = new JButton("Cancelar");
            btnGuardar.setBackground(new Color(201, 168, 76));
            btnGuardar.setForeground(Color.BLACK);
            btnGuardar.setFont(new Font("Segoe UI", Font.BOLD, 13));
            btnGuardar.setFocusPainted(false);
            btnGuardar.setCursor(new Cursor(Cursor.HAND_CURSOR));
            btnCancelar.setCursor(new Cursor(Cursor.HAND_CURSOR));

            JPanel panelBtns2 = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
            panelBtns2.setBackground(new Color(15, 15, 15));
            panelBtns2.add(btnCancelar);
            panelBtns2.add(btnGuardar);

            gbc.gridx=0; gbc.gridy=3; gbc.gridwidth=2;
            gbc.insets = new Insets(16, 8, 0, 8);
            panel2.add(panelBtns2, gbc);

            dialog.add(panel2);
            btnCancelar.addActionListener(ev -> dialog.dispose());
            btnGuardar.addActionListener(ev -> {
                if (cmbCliente.getSelectedItem() == null) {
                    JOptionPane.showMessageDialog(dialog, "Seleccione un cliente");
                    return;
                }
                double saldo;
                try {
                    saldo = Double.parseDouble(txtSaldo.getText().trim());
                    if (saldo < 100) throw new NumberFormatException();
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(dialog, "Saldo minimo es S/. 100");
                    return;
                }
                Cliente clienteSel = (Cliente) cmbCliente.getSelectedItem();
                String tipo = cmbTipo.getSelectedItem().toString();
                try {
                    java.sql.Connection conn = com.cooperativabeb.connection.ConexionOracle
                        .getInstancia().getConexion();
                    java.sql.CallableStatement cs = conn.prepareCall(
                        "{call sp_abrir_cuenta(?,?,?,?)}");
                    cs.setInt(1, clienteSel.getIdCliente());
                    cs.setInt(2, 1);
                    cs.setString(3, tipo);
                    cs.setDouble(4, saldo);
                    cs.execute();
                    cs.close();
                    conn.close();
                    JOptionPane.showMessageDialog(dialog,
                        "Cuenta abierta para " + clienteSel.getNombreCompleto());
                    dialog.dispose();
                    cargarTablaCuentas();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(dialog, "Error: " + ex.getMessage());
                }
            });
            dialog.setVisible(true);
        });

        return panel;
    }

    private JPanel crearPanelTransacciones() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(new EmptyBorder(15, 15, 15, 15));

        String[] cols = {"ID", "Tipo", "Monto", "Saldo anterior", "Saldo posterior", "Fecha", "Estado"};
        tablaTransacciones = new JTable(new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        });
        tablaTransacciones.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tablaTransacciones.setRowHeight(28);
        tablaTransacciones.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));

        JLabel lblTitulo = new JLabel("Ultimas 20 transacciones del sistema");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblTitulo.setBorder(new EmptyBorder(0, 0, 10, 0));

        panel.add(lblTitulo, BorderLayout.NORTH);
        panel.add(new JScrollPane(tablaTransacciones), BorderLayout.CENTER);
        return panel;
    }

    private void cargarDatos() {
        cargarTablaClientes(clienteDAO.listarTodos());
        cargarTablaCuentas();
        cargarTablaTransacciones();
    }

    private void cargarTablaClientes(List<Cliente> lista) {
        DefaultTableModel m = (DefaultTableModel) tablaClientes.getModel();
        m.setRowCount(0);
        for (Cliente c : lista)
            m.addRow(new Object[]{c.getIdCliente(), c.getDni(),
                c.getNombreCompleto(), c.getTelefono(), c.getEmail(), c.getEstado()});
    }

    private void cargarTablaCuentas() {
        DefaultTableModel m = (DefaultTableModel) tablaCuentas.getModel();
        m.setRowCount(0);
        for (CuentaAhorro c : cuentaDAO.listarTodas())
            m.addRow(new Object[]{c.getIdCuenta(), c.getNroCuenta(),
                c.getIdCliente(), String.format("S/. %.2f", c.getSaldo()),
                c.getTipoCuenta(), c.getEstado(), c.getFechaApertura()});
    }

    private void cargarTablaTransacciones() {
        DefaultTableModel m = (DefaultTableModel) tablaTransacciones.getModel();
        m.setRowCount(0);
        for (Transaccion t : transaccionDAO.listarUltimas(20))
            m.addRow(new Object[]{t.getIdTransaccion(), t.getTipo(),
                String.format("S/. %.2f", t.getMonto()),
                String.format("S/. %.2f", t.getSaldoAnterior()),
                String.format("S/. %.2f", t.getSaldoPosterior()),
                t.getFechaTransaccion(), t.getEstado()});
    }
}
