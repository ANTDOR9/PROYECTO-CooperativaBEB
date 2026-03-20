package com.cooperativabeb.view;

import com.cooperativabeb.connection.ConexionOracle;
import com.cooperativabeb.util.Tema;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.net.URL;

public class LoginFrame extends JFrame {

    private JTextField txtUsuario;
    private JPasswordField txtPassword;
    private JButton btnIngresar;
    private JLabel lblMensaje;

    public LoginFrame() {
        initComponents();
    }

    private void initComponents() {
        setTitle("Cooperativa BEB — Acceso al Sistema");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(440, 560);
        setLocationRelativeTo(null);
        setResizable(false);
        getContentPane().setBackground(Tema.NEGRO_FONDO);

        // Icono de la ventana
        try {
            URL imgUrl = getClass().getClassLoader().getResource("logo.png");
            if (imgUrl != null) setIconImage(new ImageIcon(imgUrl).getImage());
        } catch (Exception ignored) {}

        JPanel panelPrincipal = new JPanel(new BorderLayout(0, 0));
        panelPrincipal.setBackground(Tema.NEGRO_FONDO);
        panelPrincipal.setBorder(new EmptyBorder(30, 40, 30, 40));

        // Panel logo superior
        JPanel panelTop = new JPanel(new GridLayout(4, 1, 0, 8));
        panelTop.setBackground(Tema.NEGRO_FONDO);
        panelTop.setBorder(new EmptyBorder(0, 0, 25, 0));

        JLabel lblLogo = new JLabel();
        lblLogo.setHorizontalAlignment(SwingConstants.CENTER);
        try {
            URL imgUrl = getClass().getClassLoader().getResource("logo.png");
            if (imgUrl != null) {
                Image img = new ImageIcon(imgUrl).getImage()
                    .getScaledInstance(90, 90, Image.SCALE_SMOOTH);
                lblLogo.setIcon(new ImageIcon(img));
            }
        } catch (Exception e) {
            lblLogo.setText("BEB");
            lblLogo.setFont(new Font("Segoe UI", Font.BOLD, 36));
            lblLogo.setForeground(Tema.DORADO_PRINCIPAL);
        }

        JLabel lblNombre = new JLabel("COOPERATIVA BEB", SwingConstants.CENTER);
        lblNombre.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblNombre.setForeground(Tema.DORADO_PRINCIPAL);

        JLabel lblSistema = new JLabel("Sistema Financiero Premium", SwingConstants.CENTER);
        lblSistema.setFont(Tema.FUENTE_PEQUEÑA);
        lblSistema.setForeground(Tema.TEXTO_SECUNDARIO);

        JSeparator sep = new JSeparator();
        sep.setForeground(Tema.DORADO_OSCURO);
        sep.setBackground(Tema.DORADO_OSCURO);

        panelTop.add(lblLogo);
        panelTop.add(lblNombre);
        panelTop.add(lblSistema);
        panelTop.add(sep);

        // Panel formulario
        JPanel panelForm = new JPanel(new GridBagLayout());
        panelForm.setBackground(Tema.NEGRO_FONDO);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 0, 6, 0);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        JLabel lblUsuario = new JLabel("Usuario");
        lblUsuario.setFont(Tema.FUENTE_PEQUEÑA);
        lblUsuario.setForeground(Tema.TEXTO_SECUNDARIO);

        txtUsuario = new JTextField();
        txtUsuario.setPreferredSize(new Dimension(0, 42));
        Tema.aplicarCampo(txtUsuario);
        txtUsuario.putClientProperty("JTextField.placeholderText", "Ingrese su usuario");

        JLabel lblPassword = new JLabel("Contrasena");
        lblPassword.setFont(Tema.FUENTE_PEQUEÑA);
        lblPassword.setForeground(Tema.TEXTO_SECUNDARIO);

        txtPassword = new JPasswordField();
        txtPassword.setPreferredSize(new Dimension(0, 42));
        txtPassword.setBackground(Tema.NEGRO_CARD);
        txtPassword.setForeground(Tema.TEXTO_PRINCIPAL);
        txtPassword.setCaretColor(Tema.DORADO_PRINCIPAL);
        txtPassword.setBorder(javax.swing.BorderFactory.createCompoundBorder(
            javax.swing.BorderFactory.createLineBorder(Tema.NEGRO_BORDE, 1),
            javax.swing.BorderFactory.createEmptyBorder(6, 10, 6, 10)));
        txtPassword.setFont(Tema.FUENTE_NORMAL);
        txtPassword.putClientProperty("JTextField.placeholderText", "Ingrese su contrasena");

        lblMensaje = new JLabel("", SwingConstants.CENTER);
        lblMensaje.setFont(Tema.FUENTE_PEQUEÑA);

        btnIngresar = new JButton("INGRESAR AL SISTEMA");
        btnIngresar.setPreferredSize(new Dimension(0, 44));
        Tema.aplicarBotonPrimario(btnIngresar);
        btnIngresar.setFont(new Font("Segoe UI", Font.BOLD, 13));

        gbc.gridx=0; gbc.gridy=0;
        panelForm.add(lblUsuario, gbc);
        gbc.gridy=1;
        panelForm.add(txtUsuario, gbc);
        gbc.gridy=2;
        panelForm.add(lblPassword, gbc);
        gbc.gridy=3;
        panelForm.add(txtPassword, gbc);
        gbc.gridy=4;
        panelForm.add(lblMensaje, gbc);
        gbc.gridy=5;
        gbc.insets = new Insets(12, 0, 0, 0);
        panelForm.add(btnIngresar, gbc);

        // Footer
        JPanel panelBottom = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panelBottom.setBackground(Tema.NEGRO_FONDO);
        panelBottom.setBorder(new EmptyBorder(15, 0, 0, 0));
        JLabel lblVersion = new JLabel("v1.0 — SENATI 2025");
        lblVersion.setFont(Tema.FUENTE_PEQUEÑA);
        lblVersion.setForeground(Tema.TEXTO_SUTIL);
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
            lblMensaje.setForeground(Tema.AMARILLO_ALERTA);
            return;
        }

        ConexionOracle conexion = ConexionOracle.getInstancia();
        if (!conexion.probarConexion()) {
            lblMensaje.setText("Sin conexion a la base de datos");
            lblMensaje.setForeground(Tema.ROJO_ERROR);
            return;
        }

        if ((usuario.equals("admin") && password.equals("admin")) ||
            (usuario.equals("asesor") && password.equals("asesor"))) {
            lblMensaje.setText("Acceso correcto. Cargando...");
            lblMensaje.setForeground(Tema.VERDE_EXITO);
            btnIngresar.setEnabled(false);
            Timer timer = new Timer(800, e -> {
                dispose();
                new MainFrame(usuario).setVisible(true);
            });
            timer.setRepeats(false);
            timer.start();
        } else {
            lblMensaje.setText("Usuario o contrasena incorrectos");
            lblMensaje.setForeground(Tema.ROJO_ERROR);
            txtPassword.setText("");
            txtPassword.requestFocus();
        }
    }
}
