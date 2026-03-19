package com.cooperativabeb.view;

import com.cooperativabeb.connection.ConexionOracle;
import com.formdev.flatlaf.FlatDarkLaf;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class LoginFrame extends JFrame {

    private JTextField txtUsuario;
    private JPasswordField txtPassword;
    private JButton btnIngresar;
    private JLabel lblMensaje;

    public LoginFrame() {
        initComponents();
    }

    private void initComponents() {
        setTitle("Cooperativa BEB — Iniciar Sesion");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(420, 500);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel panelPrincipal = new JPanel(new BorderLayout());
        panelPrincipal.setBorder(new EmptyBorder(30, 40, 30, 40));

        JPanel panelTop = new JPanel(new GridLayout(3, 1, 0, 8));
        panelTop.setBorder(new EmptyBorder(0, 0, 30, 0));

        JLabel lblBanco = new JLabel("COOPERATIVA BEB", SwingConstants.CENTER);
        lblBanco.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblBanco.setForeground(new Color(100, 149, 237));

        JLabel lblSistema = new JLabel("Sistema Financiero", SwingConstants.CENTER);
        lblSistema.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        JLabel lblSeparador = new JLabel("─────────────────────", SwingConstants.CENTER);
        lblSeparador.setForeground(new Color(100, 149, 237));

        panelTop.add(lblBanco);
        panelTop.add(lblSistema);
        panelTop.add(lblSeparador);

        JPanel panelForm = new JPanel(new GridLayout(6, 1, 0, 10));

        JLabel lblUsuario = new JLabel("Usuario:");
        lblUsuario.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        txtUsuario = new JTextField();
        txtUsuario.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtUsuario.setPreferredSize(new Dimension(0, 40));
        txtUsuario.putClientProperty("JTextField.placeholderText", "Ingrese su usuario");

        JLabel lblPassword = new JLabel("Contrasena:");
        lblPassword.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        txtPassword = new JPasswordField();
        txtPassword.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtPassword.setPreferredSize(new Dimension(0, 40));
        txtPassword.putClientProperty("JTextField.placeholderText", "Ingrese su contrasena");

        lblMensaje = new JLabel("", SwingConstants.CENTER);
        lblMensaje.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblMensaje.setForeground(Color.RED);

        btnIngresar = new JButton("INGRESAR AL SISTEMA");
        btnIngresar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnIngresar.setPreferredSize(new Dimension(0, 45));
        btnIngresar.setBackground(new Color(100, 149, 237));
        btnIngresar.setForeground(Color.WHITE);
        btnIngresar.setFocusPainted(false);
        btnIngresar.setCursor(new Cursor(Cursor.HAND_CURSOR));

        panelForm.add(lblUsuario);
        panelForm.add(txtUsuario);
        panelForm.add(lblPassword);
        panelForm.add(txtPassword);
        panelForm.add(lblMensaje);
        panelForm.add(btnIngresar);

        JPanel panelBottom = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panelBottom.setBorder(new EmptyBorder(20, 0, 0, 0));
        JLabel lblVersion = new JLabel("v1.0 — SENATI 2025");
        lblVersion.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        panelBottom.add(lblVersion);

        panelPrincipal.add(panelTop, BorderLayout.NORTH);
        panelPrincipal.add(panelForm, BorderLayout.CENTER);
        panelPrincipal.add(panelBottom, BorderLayout.SOUTH);

        add(panelPrincipal);

        btnIngresar.addActionListener(e -> validarLogin());
        txtPassword.addActionListener(e -> validarLogin());
        txtUsuario.addActionListener(e -> txtPassword.requestFocus());
    }

    private void validarLogin() {
        String usuario = txtUsuario.getText().trim();
        String password = new String(txtPassword.getPassword()).trim();

        if (usuario.isEmpty() || password.isEmpty()) {
            lblMensaje.setText("Complete todos los campos");
            lblMensaje.setForeground(Color.ORANGE);
            return;
        }

        ConexionOracle conexion = ConexionOracle.getInstancia();
        if (!conexion.probarConexion()) {
            lblMensaje.setText("Error: Sin conexion a la base de datos");
            lblMensaje.setForeground(Color.RED);
            return;
        }

        if ((usuario.equals("admin") && password.equals("admin")) ||
            (usuario.equals("asesor") && password.equals("asesor"))) {

            lblMensaje.setText("Acceso correcto. Cargando...");
            lblMensaje.setForeground(Color.GREEN);
            btnIngresar.setEnabled(false);

            Timer timer = new Timer(800, e -> {
                dispose();
                new MainFrame(usuario).setVisible(true);
            });
            timer.setRepeats(false);
            timer.start();
        } else {
            lblMensaje.setText("Usuario o contrasena incorrectos");
            lblMensaje.setForeground(Color.RED);
            txtPassword.setText("");
            txtPassword.requestFocus();
        }
    }
}
