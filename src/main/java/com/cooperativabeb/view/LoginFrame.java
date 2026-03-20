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
    private FondoAnimado fondo;

    public LoginFrame() {
        initComponents();
    }

    private void initComponents() {
        setTitle("Cooperativa BEB — Acceso al Sistema");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(420, 580);
        setLocationRelativeTo(null);
        setResizable(false);

        try {
            URL imgUrl = getClass().getClassLoader().getResource("logo.png");
            if (imgUrl != null) setIconImage(new ImageIcon(imgUrl).getImage());
        } catch (Exception ignored) {}

        fondo = new FondoAnimado();
        fondo.setLayout(new GridBagLayout());
        setContentPane(fondo);

        // Panel central con GridBagLayout para control total
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(10, 10, 10, 210));
        panel.setBorder(new EmptyBorder(28, 36, 28, 36));
        panel.setPreferredSize(new Dimension(390, 530));

        GridBagConstraints g = new GridBagConstraints();
        g.gridx = 0;
        g.fill = GridBagConstraints.HORIZONTAL;
        g.weightx = 1.0;
        g.anchor = GridBagConstraints.CENTER;

        // Logo
        JLabel lblLogo = new JLabel();
        lblLogo.setHorizontalAlignment(SwingConstants.CENTER);
        try {
            URL imgUrl = getClass().getClassLoader().getResource("logo.png");
            if (imgUrl != null) {
                Image img = new ImageIcon(imgUrl).getImage()
                    .getScaledInstance(80, 80, Image.SCALE_SMOOTH);
                lblLogo.setIcon(new ImageIcon(img));
            }
        } catch (Exception ignored) {}

        // Nombre
        JLabel lblNombre = new JLabel("COOPERATIVA BEB", SwingConstants.CENTER);
        lblNombre.setFont(new Font("Segoe UI", Font.BOLD, 17));
        lblNombre.setForeground(Tema.DORADO_PRINCIPAL);

        // Subtítulo
        JLabel lblSistema = new JLabel("Sistema Financiero Premium", SwingConstants.CENTER);
        lblSistema.setFont(Tema.FUENTE_PEQUEÑA);
        lblSistema.setForeground(Tema.TEXTO_SECUNDARIO);

        // Separador
        JSeparator sep = new JSeparator();
        sep.setForeground(Tema.DORADO_OSCURO);

        // Fila icono VIP + campos
        JPanel panelFila = new JPanel(new GridBagLayout());
        panelFila.setOpaque(false);

        // Icono VIP
        JLabel lblVip = new JLabel();
        lblVip.setHorizontalAlignment(SwingConstants.CENTER);
        try {
            URL vipUrl = getClass().getClassLoader().getResource("Icono_VIP_dorado.png");
            if (vipUrl != null) {
                Image vipImg = new ImageIcon(vipUrl).getImage()
                    .getScaledInstance(60, 60, Image.SCALE_SMOOTH);
                lblVip.setIcon(new ImageIcon(vipImg));
            }
        } catch (Exception ignored) {}

        // Panel campos derecha
        JPanel panelCampos = new JPanel(new GridBagLayout());
        panelCampos.setOpaque(false);
        GridBagConstraints gc = new GridBagConstraints();
        gc.gridx = 0; gc.fill = GridBagConstraints.HORIZONTAL; gc.weightx = 1.0;

        JLabel lblU = new JLabel("Usuario");
        lblU.setFont(Tema.FUENTE_PEQUEÑA);
        lblU.setForeground(Tema.TEXTO_SECUNDARIO);

        txtUsuario = new JTextField();
        txtUsuario.setPreferredSize(new Dimension(270, 44));
        Tema.aplicarCampo(txtUsuario);
        txtUsuario.putClientProperty("JTextField.placeholderText", "Ingrese su usuario");

        JLabel lblP = new JLabel("Contrasena");
        lblP.setFont(Tema.FUENTE_PEQUEÑA);
        lblP.setForeground(Tema.TEXTO_SECUNDARIO);

        txtPassword = new JPasswordField();
        txtPassword.setPreferredSize(new Dimension(270, 44));
        txtPassword.setBackground(Tema.NEGRO_CARD);
        txtPassword.setForeground(Tema.TEXTO_PRINCIPAL);
        txtPassword.setCaretColor(Tema.DORADO_PRINCIPAL);
        txtPassword.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Tema.NEGRO_BORDE, 1),
            new EmptyBorder(6, 10, 6, 10)));
        txtPassword.setFont(Tema.FUENTE_NORMAL);
        txtPassword.putClientProperty("JTextField.placeholderText", "Ingrese su contrasena");

        gc.gridy=0; gc.insets=new Insets(0,0,3,0); panelCampos.add(lblU, gc);
        gc.gridy=1; gc.insets=new Insets(0,0,6,0); panelCampos.add(txtUsuario, gc);
        gc.gridy=2; gc.insets=new Insets(0,0,3,0); panelCampos.add(lblP, gc);
        gc.gridy=3; gc.insets=new Insets(0,0,0,0); panelCampos.add(txtPassword, gc);

        // Armar fila VIP + campos
        GridBagConstraints gf = new GridBagConstraints();
        gf.gridy = 0; gf.insets = new Insets(0, 0, 0, 14);
        gf.anchor = GridBagConstraints.CENTER;
        panelFila.add(lblVip, gf);
        gf.gridx = 1; gf.insets = new Insets(0,0,0,0);
        gf.fill = GridBagConstraints.HORIZONTAL; gf.weightx = 1.0;
        panelFila.add(panelCampos, gf);

        // Mensaje
        lblMensaje = new JLabel(" ", SwingConstants.CENTER);
        lblMensaje.setFont(Tema.FUENTE_PEQUEÑA);
        lblMensaje.setForeground(Tema.ROJO_ERROR);

        // Botón
        btnIngresar = new JButton("INGRESAR AL SISTEMA");
        btnIngresar.setPreferredSize(new Dimension(420, 48));
        Tema.aplicarBotonPrimario(btnIngresar);
        btnIngresar.setFont(new Font("Segoe UI", Font.BOLD, 13));

        // Versión
        JLabel lblVersion = new JLabel("v1.0 — SENATI 2025", SwingConstants.CENTER);
        lblVersion.setFont(Tema.FUENTE_PEQUEÑA);
        lblVersion.setForeground(Tema.TEXTO_SUTIL);

        // Agregar todo al panel con espaciado
        g.gridy=0;  g.insets=new Insets(0,0,6,0);   panel.add(lblLogo, g);
        g.gridy=1;  g.insets=new Insets(0,0,3,0);   panel.add(lblNombre, g);
        g.gridy=2;  g.insets=new Insets(0,0,12,0);  panel.add(lblSistema, g);
        g.gridy=3;  g.insets=new Insets(0,0,16,0);  panel.add(sep, g);
        g.gridy=4;  g.insets=new Insets(0,0,8,0);   panel.add(panelFila, g);
        g.gridy=5;  g.insets=new Insets(0,0,4,0);   panel.add(lblMensaje, g);
        g.gridy=6;  g.insets=new Insets(0,0,14,0);  panel.add(btnIngresar, g);
        g.gridy=7;  g.insets=new Insets(0,0,0,0);   panel.add(lblVersion, g);

        fondo.add(panel, new GridBagConstraints());

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

        if (usuario.equals("cliente") && password.equals("cliente")) {
            lblMensaje.setText("Acceso correcto. Cargando...");
            lblMensaje.setForeground(Tema.VERDE_EXITO);
            btnIngresar.setEnabled(false);
            fondo.detener();
            Timer timer2 = new Timer(800, e -> {
                dispose();
                new ClienteFrame(1, "Cliente Demo").setVisible(true);
            });
            timer2.setRepeats(false);
            timer2.start();
        } else if ((usuario.equals("admin") && password.equals("admin")) ||
            (usuario.equals("asesor") && password.equals("asesor"))) {
            lblMensaje.setText("Acceso correcto. Cargando...");
            lblMensaje.setForeground(Tema.VERDE_EXITO);
            btnIngresar.setEnabled(false);
            fondo.detener();
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
