package com.cooperativabeb.view;

import com.cooperativabeb.dao.ClienteDAO;
import com.cooperativabeb.dao.TransaccionDAO;
import com.cooperativabeb.model.Cliente;
import com.cooperativabeb.model.Transaccion;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class MainFrame extends JFrame {

    private String usuarioActual;
    private ClienteDAO clienteDAO = new ClienteDAO();
    private TransaccionDAO transaccionDAO = new TransaccionDAO();
    private JTable tablaClientes;
    private JTable tablaTransacciones;

    public MainFrame(String usuario) {
        this.usuarioActual = usuario;
        initComponents();
        cargarDatos();
    }

    private void initComponents() {
        setTitle("Cooperativa BEB — Panel Principal");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 650);
        setLocationRelativeTo(null);

        // Barra superior
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
        btnCerrar.addActionListener(e -> {
            dispose();
            new LoginFrame().setVisible(true);
        });

        panelTop.add(lblSistema, BorderLayout.WEST);
        panelTop.add(lblBienvenida, BorderLayout.CENTER);
        panelTop.add(btnCerrar, BorderLayout.EAST);

        JTabbedPane tabs = new JTabbedPane();
        tabs.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tabs.addTab("Clientes", crearPanelClientes());
        tabs.addTab("Transacciones recientes", crearPanelTransacciones());

        add(panelTop, BorderLayout.NORTH);
        add(tabs, BorderLayout.CENTER);
    }

    private JPanel crearPanelClientes() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(new EmptyBorder(15, 15, 15, 15));

        // Busqueda
        JPanel panelBusqueda = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        JLabel lblBuscar = new JLabel("Buscar:");
        JTextField txtBuscar = new JTextField(22);
        JButton btnBuscar = new JButton("Buscar");
        JButton btnVerTodos = new JButton("Ver todos");
        panelBusqueda.add(lblBuscar);
        panelBusqueda.add(txtBuscar);
        panelBusqueda.add(btnBuscar);
        panelBusqueda.add(btnVerTodos);

        // Tabla
        String[] cols = {"ID", "DNI", "Nombre completo", "Telefono", "Email", "Estado"};
        tablaClientes = new JTable(new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        });
        tablaClientes.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tablaClientes.setRowHeight(28);
        tablaClientes.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        tablaClientes.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scroll = new JScrollPane(tablaClientes);

        // Botones CRUD
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        JButton btnNuevo    = new JButton("+ Nuevo cliente");
        JButton btnEditar   = new JButton("Editar");
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
        panel.add(scroll, BorderLayout.CENTER);
        panel.add(panelBotones, BorderLayout.SOUTH);

        // Acciones
        btnBuscar.addActionListener(e -> {
            String texto = txtBuscar.getText().trim();
            if (!texto.isEmpty())
                cargarTablaClientes(clienteDAO.buscarPorNombre(texto));
        });
        btnVerTodos.addActionListener(e -> {
            txtBuscar.setText("");
            cargarTablaClientes(clienteDAO.listarTodos());
        });

        btnNuevo.addActionListener(e -> {
            ClienteFormDialog form = new ClienteFormDialog(this, null);
            form.setVisible(true);
            if (form.isGuardado())
                cargarTablaClientes(clienteDAO.listarTodos());
        });

        btnEditar.addActionListener(e -> {
            int fila = tablaClientes.getSelectedRow();
            if (fila < 0) {
                JOptionPane.showMessageDialog(this, "Seleccione un cliente para editar");
                return;
            }
            int id = (int) tablaClientes.getValueAt(fila, 0);
            Cliente c = clienteDAO.buscarPorId(id);
            if (c != null) {
                ClienteFormDialog form = new ClienteFormDialog(this, c);
                form.setVisible(true);
                if (form.isGuardado())
                    cargarTablaClientes(clienteDAO.listarTodos());
            }
        });

        btnDesactivar.addActionListener(e -> {
            int fila = tablaClientes.getSelectedRow();
            if (fila < 0) {
                JOptionPane.showMessageDialog(this, "Seleccione un cliente");
                return;
            }
            int id = (int) tablaClientes.getValueAt(fila, 0);
            String nombre = tablaClientes.getValueAt(fila, 2).toString();
            int confirm = JOptionPane.showConfirmDialog(this,
                "Desactivar a " + nombre + "?", "Confirmar",
                JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            if (confirm == JOptionPane.YES_OPTION) {
                if (clienteDAO.eliminar(id)) {
                    JOptionPane.showMessageDialog(this, "Cliente desactivado correctamente");
                    cargarTablaClientes(clienteDAO.listarTodos());
                }
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
        cargarTablaTransacciones();
    }

    private void cargarTablaClientes(List<Cliente> lista) {
        DefaultTableModel model = (DefaultTableModel) tablaClientes.getModel();
        model.setRowCount(0);
        for (Cliente c : lista) {
            model.addRow(new Object[]{
                c.getIdCliente(), c.getDni(),
                c.getNombreCompleto(), c.getTelefono(),
                c.getEmail(), c.getEstado()
            });
        }
    }

    private void cargarTablaTransacciones() {
        DefaultTableModel model = (DefaultTableModel) tablaTransacciones.getModel();
        model.setRowCount(0);
        for (Transaccion t : transaccionDAO.listarUltimas(20)) {
            model.addRow(new Object[]{
                t.getIdTransaccion(), t.getTipo(),
                String.format("S/. %.2f", t.getMonto()),
                String.format("S/. %.2f", t.getSaldoAnterior()),
                String.format("S/. %.2f", t.getSaldoPosterior()),
                t.getFechaTransaccion(), t.getEstado()
            });
        }
    }
}
