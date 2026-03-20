package com.cooperativabeb.view;

import com.cooperativabeb.dao.ClienteDAO;
import com.cooperativabeb.dao.UsuarioDAO;
import com.cooperativabeb.model.Cliente;
import com.cooperativabeb.model.UsuarioSistema;
import com.cooperativabeb.util.Tema;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class UsuariosPanel extends JPanel {

    private UsuarioDAO usuarioDAO = new UsuarioDAO();
    private ClienteDAO clienteDAO = new ClienteDAO();
    private JTable tablaUsuarios;

    public UsuariosPanel() {
        initComponents();
        cargarUsuarios();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(15, 15, 15, 15));
        setBackground(Tema.NEGRO_FONDO);

        // Título
        JLabel lblTitulo = new JLabel("Gestion de Usuarios del Sistema");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblTitulo.setForeground(Tema.DORADO_PRINCIPAL);
        lblTitulo.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 3, 0, 0, Tema.DORADO_PRINCIPAL),
            new EmptyBorder(0, 10, 10, 0)));

        // Tabla
        String[] cols = {"ID", "Username", "Rol", "Nombre completo", "Email", "Estado", "Ultimo acceso"};
        tablaUsuarios = new JTable(new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        });
        Tema.aplicarTabla(tablaUsuarios);
        tablaUsuarios.setBackground(Tema.NEGRO_CARD);
        tablaUsuarios.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scroll = new JScrollPane(tablaUsuarios);
        scroll.getViewport().setBackground(Tema.NEGRO_CARD);
        scroll.setBorder(BorderFactory.createLineBorder(Tema.NEGRO_BORDE, 1));

        // Botones
        JPanel panelBtns = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        panelBtns.setBackground(Tema.NEGRO_FONDO);

        JButton btnNuevo    = new JButton("+ Nuevo usuario");
        JButton btnEditar   = new JButton("Editar");
        JButton btnDesact   = new JButton("Desactivar");
        JButton btnRefrescar = new JButton("Refrescar");

        Tema.aplicarBotonPrimario(btnNuevo);
        Tema.aplicarBotonSecundario(btnEditar);
        Tema.aplicarBotonPeligro(btnDesact);
        Tema.aplicarBotonSecundario(btnRefrescar);

        panelBtns.add(btnNuevo);
        panelBtns.add(btnEditar);
        panelBtns.add(btnDesact);
        panelBtns.add(btnRefrescar);

        add(lblTitulo, BorderLayout.NORTH);
        add(scroll, BorderLayout.CENTER);
        add(panelBtns, BorderLayout.SOUTH);

        // Acciones
        btnNuevo.addActionListener(e -> abrirFormulario(null));
        btnEditar.addActionListener(e -> {
            int fila = tablaUsuarios.getSelectedRow();
            if (fila < 0) { JOptionPane.showMessageDialog(this, "Seleccione un usuario"); return; }
            int id = (int) tablaUsuarios.getValueAt(fila, 0);
            usuarioDAO.listarTodos().stream()
                .filter(u -> u.getIdUsuario() == id)
                .findFirst()
                .ifPresent(this::abrirFormulario);
        });
        btnDesact.addActionListener(e -> {
            int fila = tablaUsuarios.getSelectedRow();
            if (fila < 0) { JOptionPane.showMessageDialog(this, "Seleccione un usuario"); return; }
            int id = (int) tablaUsuarios.getValueAt(fila, 0);
            String username = tablaUsuarios.getValueAt(fila, 1).toString();
            if (username.equals("admin")) {
                JOptionPane.showMessageDialog(this, "No se puede desactivar el admin principal");
                return;
            }
            int ok = JOptionPane.showConfirmDialog(this,
                "Desactivar usuario: " + username + "?",
                "Confirmar", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            if (ok == JOptionPane.YES_OPTION) {
                if (usuarioDAO.desactivar(id)) {
                    JOptionPane.showMessageDialog(this, "Usuario desactivado");
                    cargarUsuarios();
                }
            }
        });
        btnRefrescar.addActionListener(e -> cargarUsuarios());
    }

    private void abrirFormulario(UsuarioSistema usuario) {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this),
            usuario == null ? "Nuevo usuario" : "Editar usuario", true);
        dialog.setSize(420, 420);
        dialog.setLocationRelativeTo(this);
        dialog.getContentPane().setBackground(Tema.NEGRO_FONDO);

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Tema.NEGRO_FONDO);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Tema.DORADO_OSCURO, 1),
                usuario == null ? "Registrar nuevo usuario" : "Editar usuario",
                TitledBorder.LEFT, TitledBorder.TOP,
                new Font("Segoe UI", Font.BOLD, 12), Tema.DORADO_PRINCIPAL),
            new EmptyBorder(10, 15, 10, 15)));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 4, 6, 4);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        JTextField txtUsername  = crearCampo("Username: *");
        JPasswordField txtPass  = new JPasswordField();
        JTextField txtNombre    = crearCampo("Nombre completo:");
        JTextField txtEmail     = crearCampo("Email:");
        JComboBox<String> cmbRol = new JComboBox<>(new String[]{"ADMIN","ASESOR","CLIENTE"});
        JComboBox<String> cmbEstado = new JComboBox<>(new String[]{"ACTIVO","INACTIVO"});
        JComboBox<Cliente> cmbCliente = new JComboBox<>();

        // Estilizar
        Tema.aplicarCampo(txtUsername);
        Tema.aplicarCampo(txtNombre);
        Tema.aplicarCampo(txtEmail);
        txtPass.setBackground(Tema.NEGRO_CARD);
        txtPass.setForeground(Tema.TEXTO_PRINCIPAL);
        txtPass.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Tema.NEGRO_BORDE, 1),
            new EmptyBorder(6, 10, 6, 10)));
        txtPass.setFont(Tema.FUENTE_NORMAL);
        txtPass.setPreferredSize(new Dimension(0, 36));

        // Cargar clientes para asignar
        clienteDAO.listarTodos().stream()
            .filter(c -> c.getEstado().equals("ACTIVO"))
            .forEach(cmbCliente::addItem);

        // Si editar, cargar datos
        if (usuario != null) {
            txtUsername.setText(usuario.getUsername());
            txtUsername.setEditable(false);
            txtNombre.setText(usuario.getNombreCompleto());
            txtEmail.setText(usuario.getEmail());
            cmbRol.setSelectedItem(usuario.getRol());
            cmbEstado.setSelectedItem(usuario.getEstado());
        }

        Object[][] filas = {
            {"Username:", txtUsername},
            {"Contrasena:", txtPass},
            {"Nombre:", txtNombre},
            {"Email:", txtEmail},
            {"Rol:", cmbRol},
            {"Estado:", cmbEstado},
            {"Cliente:", cmbCliente}
        };

        for (int i = 0; i < filas.length; i++) {
            gbc.gridx=0; gbc.gridy=i; gbc.weightx=0.3;
            JLabel lbl = new JLabel(filas[i][0].toString());
            lbl.setFont(Tema.FUENTE_PEQUEÑA);
            lbl.setForeground(Tema.TEXTO_SECUNDARIO);
            panel.add(lbl, gbc);
            gbc.gridx=1; gbc.weightx=0.7;
            panel.add((Component) filas[i][1], gbc);
        }

        JPanel panelBtns = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 8));
        panelBtns.setBackground(Tema.NEGRO_FONDO);
        JButton btnCancelar = new JButton("Cancelar");
        JButton btnGuardar  = new JButton(usuario == null ? "Guardar" : "Actualizar");
        Tema.aplicarBotonSecundario(btnCancelar);
        Tema.aplicarBotonPrimario(btnGuardar);
        btnGuardar.setPreferredSize(new Dimension(110, 36));
        btnCancelar.setPreferredSize(new Dimension(100, 36));
        panelBtns.add(btnCancelar);
        panelBtns.add(btnGuardar);

        dialog.setLayout(new BorderLayout());
        dialog.add(panel, BorderLayout.CENTER);
        dialog.add(panelBtns, BorderLayout.SOUTH);

        btnCancelar.addActionListener(e -> dialog.dispose());
        btnGuardar.addActionListener(e -> {
            if (txtUsername.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "El username es obligatorio");
                return;
            }
            String pass = new String(txtPass.getPassword()).trim();
            if (usuario == null && pass.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "La contrasena es obligatoria");
                return;
            }
            if (usuario == null && usuarioDAO.existeUsername(txtUsername.getText().trim())) {
                JOptionPane.showMessageDialog(dialog, "Ese username ya existe");
                return;
            }

            UsuarioSistema u = usuario != null ? usuario : new UsuarioSistema();
            u.setUsername(txtUsername.getText().trim());
            if (!pass.isEmpty()) u.setPassword(pass);
            u.setNombreCompleto(txtNombre.getText().trim());
            u.setEmail(txtEmail.getText().trim());
            u.setRol(cmbRol.getSelectedItem().toString());
            u.setEstado(cmbEstado.getSelectedItem().toString());

            Cliente clienteSelec = (Cliente) cmbCliente.getSelectedItem();
            if (clienteSelec != null && u.getRol().equals("CLIENTE"))
                u.setIdCliente(clienteSelec.getIdCliente());

            boolean ok = usuario == null ? usuarioDAO.insertar(u) : usuarioDAO.actualizar(u);
            if (ok) {
                JOptionPane.showMessageDialog(dialog,
                    usuario == null ? "Usuario creado exitosamente" : "Usuario actualizado");
                dialog.dispose();
                cargarUsuarios();
            } else {
                JOptionPane.showMessageDialog(dialog, "Error al guardar el usuario");
            }
        });

        dialog.setVisible(true);
    }

    private JTextField crearCampo(String placeholder) {
        JTextField tf = new JTextField();
        tf.setPreferredSize(new Dimension(0, 36));
        Tema.aplicarCampo(tf);
        tf.putClientProperty("JTextField.placeholderText", placeholder);
        return tf;
    }

    private void cargarUsuarios() {
        DefaultTableModel model = (DefaultTableModel) tablaUsuarios.getModel();
        model.setRowCount(0);
        for (UsuarioSistema u : usuarioDAO.listarTodos()) {
            model.addRow(new Object[]{
                u.getIdUsuario(),
                u.getUsername(),
                u.getRol(),
                u.getNombreCompleto(),
                u.getEmail(),
                u.getEstado(),
                u.getUltimoAcceso() != null ? u.getUltimoAcceso().toString() : "Nunca"
            });
        }
    }
}
