package com.cooperativabeb.view;

import com.cooperativabeb.dao.ClienteDAO;
import com.cooperativabeb.model.Cliente;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class ClienteFormDialog extends JDialog {

    private JTextField txtDni, txtNombres, txtApellidos;
    private JTextField txtTelefono, txtEmail, txtDireccion;
    private JTextField txtFechaNac;
    private JComboBox<String> cmbEstado;
    private JButton btnGuardar, btnCancelar;

    private ClienteDAO clienteDAO = new ClienteDAO();
    private Cliente clienteEditar;
    private boolean guardado = false;

    public ClienteFormDialog(Frame parent, Cliente cliente) {
        super(parent, cliente == null ? "Nuevo Cliente" : "Editar Cliente", true);
        this.clienteEditar = cliente;
        initComponents();
        if (cliente != null) cargarDatos(cliente);
    }

    private void initComponents() {
        setSize(500, 520);
        setLocationRelativeTo(getParent());
        setResizable(false);

        JPanel panelPrincipal = new JPanel(new BorderLayout(10, 10));
        panelPrincipal.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Panel titulo
        JLabel lblTitulo = new JLabel(
            clienteEditar == null ? "Registrar nuevo cliente" : "Editar datos del cliente",
            SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblTitulo.setForeground(new Color(100, 149, 237));
        lblTitulo.setBorder(new EmptyBorder(0, 0, 10, 0));

        // Panel formulario
        JPanel panelForm = new JPanel(new GridBagLayout());
        panelForm.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(100, 149, 237), 1),
            "Datos personales",
            TitledBorder.LEFT, TitledBorder.TOP,
            new Font("Segoe UI", Font.BOLD, 12),
            new Color(100, 149, 237)));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 8, 6, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        txtDni       = new JTextField(20);
        txtNombres   = new JTextField(20);
        txtApellidos = new JTextField(20);
        txtTelefono  = new JTextField(20);
        txtEmail     = new JTextField(20);
        txtDireccion = new JTextField(20);
        txtFechaNac  = new JTextField(20);
        txtFechaNac.putClientProperty("JTextField.placeholderText", "yyyy-MM-dd");
        cmbEstado = new JComboBox<>(new String[]{"ACTIVO", "INACTIVO"});

        Font fuenteCampo = new Font("Segoe UI", Font.PLAIN, 13);
        for (JTextField tf : new JTextField[]{
                txtDni, txtNombres, txtApellidos,
                txtTelefono, txtEmail, txtDireccion, txtFechaNac}) {
            tf.setFont(fuenteCampo);
            tf.setPreferredSize(new Dimension(0, 32));
        }

        Object[][] campos = {
            {"DNI: *",          txtDni},
            {"Nombres: *",      txtNombres},
            {"Apellidos: *",    txtApellidos},
            {"Telefono:",       txtTelefono},
            {"Email:",          txtEmail},
            {"Direccion:",      txtDireccion},
            {"Fecha nac.:",     txtFechaNac},
            {"Estado:",         cmbEstado}
        };

        for (int i = 0; i < campos.length; i++) {
            gbc.gridx = 0; gbc.gridy = i; gbc.weightx = 0.3;
            JLabel lbl = new JLabel(campos[i][0].toString());
            lbl.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            panelForm.add(lbl, gbc);

            gbc.gridx = 1; gbc.weightx = 0.7;
            panelForm.add((Component) campos[i][1], gbc);
        }

        // Panel botones
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 5));

        btnCancelar = new JButton("Cancelar");
        btnCancelar.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        btnCancelar.setPreferredSize(new Dimension(110, 38));
        btnCancelar.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btnGuardar = new JButton(clienteEditar == null ? "Guardar" : "Actualizar");
        btnGuardar.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnGuardar.setPreferredSize(new Dimension(110, 38));
        btnGuardar.setBackground(new Color(100, 149, 237));
        btnGuardar.setForeground(Color.WHITE);
        btnGuardar.setFocusPainted(false);
        btnGuardar.setCursor(new Cursor(Cursor.HAND_CURSOR));

        panelBotones.add(btnCancelar);
        panelBotones.add(btnGuardar);

        panelPrincipal.add(lblTitulo, BorderLayout.NORTH);
        panelPrincipal.add(panelForm, BorderLayout.CENTER);
        panelPrincipal.add(panelBotones, BorderLayout.SOUTH);
        add(panelPrincipal);

        // Acciones
        btnCancelar.addActionListener(e -> dispose());
        btnGuardar.addActionListener(e -> guardar());
    }

    private void cargarDatos(Cliente c) {
        txtDni.setText(c.getDni());
        txtDni.setEditable(false);
        txtNombres.setText(c.getNombres());
        txtApellidos.setText(c.getApellidos());
        txtTelefono.setText(c.getTelefono());
        txtEmail.setText(c.getEmail());
        txtDireccion.setText(c.getDireccion());
        if (c.getFechaNacimiento() != null) {
            txtFechaNac.setText(
                new SimpleDateFormat("yyyy-MM-dd").format(c.getFechaNacimiento()));
        }
        cmbEstado.setSelectedItem(c.getEstado());
    }

    private void guardar() {
        // Validaciones
        if (txtDni.getText().trim().isEmpty()) {
            mostrarError("El DNI es obligatorio");
            txtDni.requestFocus();
            return;
        }
        if (txtDni.getText().trim().length() != 8) {
            mostrarError("El DNI debe tener 8 digitos");
            txtDni.requestFocus();
            return;
        }
        if (txtNombres.getText().trim().isEmpty()) {
            mostrarError("Los nombres son obligatorios");
            txtNombres.requestFocus();
            return;
        }
        if (txtApellidos.getText().trim().isEmpty()) {
            mostrarError("Los apellidos son obligatorios");
            txtApellidos.requestFocus();
            return;
        }

        Cliente c = clienteEditar != null ? clienteEditar : new Cliente();
        c.setDni(txtDni.getText().trim());
        c.setNombres(txtNombres.getText().trim());
        c.setApellidos(txtApellidos.getText().trim());
        c.setTelefono(txtTelefono.getText().trim());
        c.setEmail(txtEmail.getText().trim());
        c.setDireccion(txtDireccion.getText().trim());
        c.setEstado(cmbEstado.getSelectedItem().toString());

        if (!txtFechaNac.getText().trim().isEmpty()) {
            try {
                c.setFechaNacimiento(
                    new SimpleDateFormat("yyyy-MM-dd").parse(txtFechaNac.getText().trim()));
            } catch (ParseException ex) {
                mostrarError("Formato de fecha invalido. Use yyyy-MM-dd");
                txtFechaNac.requestFocus();
                return;
            }
        }

        boolean resultado;
        if (clienteEditar == null) {
            resultado = clienteDAO.insertar(c);
        } else {
            resultado = clienteDAO.actualizar(c);
        }

        if (resultado) {
            JOptionPane.showMessageDialog(this,
                clienteEditar == null ? "Cliente registrado exitosamente" : "Cliente actualizado exitosamente",
                "Exito", JOptionPane.INFORMATION_MESSAGE);
            guardado = true;
            dispose();
        } else {
            mostrarError("Error al guardar. Verifique que el DNI no este repetido.");
        }
    }

    private void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Error de validacion",
            JOptionPane.WARNING_MESSAGE);
    }

    public boolean isGuardado() { return guardado; }
}
