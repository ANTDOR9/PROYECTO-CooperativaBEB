package com.cooperativabeb.view;

import com.cooperativabeb.dao.ClienteDAO;
import com.cooperativabeb.dao.CuentaAhorroDAO;
import com.cooperativabeb.view.DashboardPanel;
import com.cooperativabeb.view.UsuariosPanel;
import com.cooperativabeb.view.ReportesPanel;
import com.cooperativabeb.view.PlanInversionPanel;
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

        JButton btnCerrar = new JButton("Cerrar Sesion");
        btnCerrar.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        btnCerrar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnCerrar.addActionListener(e -> { dispose(); new LoginFrame().setVisible(true); });

        panelTop.add(lblSistema, BorderLayout.WEST);
        panelTop.add(lblBienvenida, BorderLayout.CENTER);

        JButton btnRefrescar = new JButton("↻ Refrescar");
        btnRefrescar.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        btnRefrescar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnRefrescar.addActionListener(e -> cargarDatos());

        JPanel panelDer = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        panelDer.setOpaque(false);
        panelDer.add(btnRefrescar);
        panelDer.add(btnCerrar);
        panelTop.add(panelDer, BorderLayout.EAST);

        JTabbedPane tabs = new JTabbedPane();
        tabs.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tabs.addTab("Dashboard", new DashboardPanel());
        tabs.addTab("Usuarios", new UsuariosPanel());
        tabs.addTab("Reportes PDF", new ReportesPanel());
        tabs.addTab("Clientes", crearPanelClientes());
        tabs.addTab("Cuentas de ahorro", crearPanelCuentas());
        tabs.addTab("Planes de inversion", new PlanInversionPanel());
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
        JButton btnNuevo = new JButton("+ Nuevo cliente");
        JButton btnEditar = new JButton("Editar");
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
        JButton btnOperar = new JButton("Deposito / Retiro");
        JButton btnRefrescar = new JButton("Refrescar");
        btnOperar.setBackground(new Color(100, 149, 237));
        btnOperar.setForeground(Color.WHITE);
        btnOperar.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnOperar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnRefrescar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        panelBotones.add(btnOperar);
        panelBotones.add(btnRefrescar);

        panel.add(new JScrollPane(tablaCuentas), BorderLayout.CENTER);
        panel.add(panelBotones, BorderLayout.SOUTH);

        btnRefrescar.addActionListener(e -> cargarTablaCuentas());
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
