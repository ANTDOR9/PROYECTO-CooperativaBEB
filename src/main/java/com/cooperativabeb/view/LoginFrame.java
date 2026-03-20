package com.cooperativabeb.view;

import com.cooperativabeb.connection.ConexionOracle;
import com.cooperativabeb.dao.UsuarioDAO;
import com.cooperativabeb.model.UsuarioSistema;
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
    private UsuarioDAO usuarioDAO = new UsuarioDAO();

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

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(10, 10, 10, 120));
        panel.setBorder(new EmptyBorder(28, 36, 28, 36));
        panel.setPreferredSize(new Dimension(390, 530));

        GridBagConstraints g = new GridBagConstraints();
        g.gridx = 0;
        g.fill = GridBagConstraints.HORIZONTAL;
        g.weightx = 1.0;
        g.anchor = GridBagConstraints.CENTER;

        JLabel lblLogo = new JLabel();
        lblLogo.setHorizontalAlignment(SwingConstants.CENTER);
        try {
            URL imgUrl = getClass().getClassLoader().getResource("logo.png");
            if (imgUrl != null) {
                Image img = new ImageIcon(imgUrl).getImage()
                    .getScaledInstance(95, 95, Image.SCALE_SMOOTH);
                lblLogo.setIcon(new ImageIcon(img));
            }
        } catch (Exception ignored) {}

        JLabel lblNombre = new JLabel("COOPERATIVA BEB", SwingConstants.CENTER);
        lblNombre.setFont(new Font("Segoe UI", Font.BOLD, 17));
        lblNombre.setForeground(Tema.DORADO_PRINCIPAL);

        JLabel lblSistema = new JLabel("Sistema Financiero Premium", SwingConstants.CENTER);
        lblSistema.setFont(Tema.FUENTE_PEQUEÑA);
        lblSistema.setForeground(Tema.TEXTO_SECUNDARIO);

        JSeparator sep = new JSeparator();
        sep.setForeground(Tema.DORADO_OSCURO);

        // Fila icono VIP + campos
        JPanel panelFila = new JPanel(new GridBagLayout());
        panelFila.setOpaque(false);

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

        JPanel panelCampos = new JPanel(new GridBagLayout());
        panelCampos.setOpaque(false);
        GridBagConstraints gc = new GridBagConstraints();
        gc.gridx = 0; gc.fill = GridBagConstraints.HORIZONTAL; gc.weightx = 1.0;

        JLabel lblU = new JLabel("Usuario");
        lblU.setFont(Tema.FUENTE_PEQUEÑA);
        lblU.setForeground(Tema.TEXTO_SECUNDARIO);

        txtUsuario = new JTextField();
        txtUsuario.setPreferredSize(new Dimension(240, 42));
        Tema.aplicarCampo(txtUsuario);
        txtUsuario.putClientProperty("JTextField.placeholderText", "Ingrese su usuario");

        JLabel lblP = new JLabel("Contrasena");
        lblP.setFont(Tema.FUENTE_PEQUEÑA);
        lblP.setForeground(Tema.TEXTO_SECUNDARIO);

        txtPassword = new JPasswordField();
        txtPassword.setPreferredSize(new Dimension(240, 42));
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

        GridBagConstraints gf = new GridBagConstraints();
        gf.gridy=0; gf.insets=new Insets(0,0,0,14);
        gf.anchor=GridBagConstraints.CENTER;
        panelFila.add(lblVip, gf);
        gf.gridx=1; gf.insets=new Insets(0,0,0,0);
        gf.fill=GridBagConstraints.HORIZONTAL; gf.weightx=1.0;
        panelFila.add(panelCampos, gf);

        lblMensaje = new JLabel(" ", SwingConstants.CENTER);
        lblMensaje.setFont(Tema.FUENTE_PEQUEÑA);
        lblMensaje.setForeground(Tema.ROJO_ERROR);

        btnIngresar = new JButton("INGRESAR AL SISTEMA");
        btnIngresar.setPreferredSize(new Dimension(420, 46));
        Tema.aplicarBotonPrimario(btnIngresar);
        btnIngresar.setFont(new Font("Segoe UI", Font.BOLD, 13));

        JLabel lblVersion = new JLabel("v1.0 — SENATI 2025", SwingConstants.CENTER);
        lblVersion.setFont(Tema.FUENTE_PEQUEÑA);
        lblVersion.setForeground(Tema.TEXTO_SUTIL);

        g.gridy=0; g.insets=new Insets(0,0,6,0);   panel.add(lblLogo, g);
        g.gridy=1; g.insets=new Insets(0,0,3,0);   panel.add(lblNombre, g);
        g.gridy=2; g.insets=new Insets(0,0,12,0);  panel.add(lblSistema, g);
        g.gridy=3; g.insets=new Insets(0,0,16,0);  panel.add(sep, g);
        g.gridy=4; g.insets=new Insets(0,0,8,0);   panel.add(panelFila, g);
        g.gridy=5; g.insets=new Insets(0,0,4,0);   panel.add(lblMensaje, g);
        g.gridy=6; g.insets=new Insets(0,0,14,0);  panel.add(btnIngresar, g);
        g.gridy=7; g.insets=new Insets(0,0,0,0);   panel.add(lblVersion, g);

        fondo.add(panel, new GridBagConstraints());

        btnIngresar.addActionListener(e -> validarLogin());
        txtPassword.addActionListener(e -> validarLogin());
        txtUsuario.addActionListener(e -> txtPassword.requestFocus());
    }

    private void validarLogin() {
        String username = txtUsuario.getText().trim();
        String password = new String(txtPassword.getPassword()).trim();

        if (username.isEmpty() || password.isEmpty()) {
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

        lblMensaje.setText("Verificando...");
        lblMensaje.setForeground(Tema.TEXTO_SECUNDARIO);
        btnIngresar.setEnabled(false);

        SwingWorker<UsuarioSistema, Void> worker = new SwingWorker<>() {
            @Override
            protected UsuarioSistema doInBackground() {
                return usuarioDAO.autenticar(username, password);
            }

            @Override
            protected void done() {
                try {
                    UsuarioSistema usuario = get();
                    if (usuario != null) {
                        lblMensaje.setText("Acceso correcto. Cargando...");
                        lblMensaje.setForeground(Tema.VERDE_EXITO);
                        fondo.detener();
                        Timer timer = new Timer(800, e -> {
                            dispose();
                            abrirPantalla(usuario);
                        });
                        timer.setRepeats(false);
                        timer.start();
                    } else {
                        lblMensaje.setText("Usuario o contrasena incorrectos");
                        lblMensaje.setForeground(Tema.ROJO_ERROR);
                        txtPassword.setText("");
                        txtPassword.requestFocus();
                        btnIngresar.setEnabled(true);
                    }
                } catch (Exception e) {
                    lblMensaje.setText("Error de conexion");
                    lblMensaje.setForeground(Tema.ROJO_ERROR);
                    btnIngresar.setEnabled(true);
                }
            }
        };
        worker.execute();
    }

    private void abrirPantalla(UsuarioSistema usuario) {
        switch (usuario.getRol()) {
            case "ADMIN", "ASESOR" ->
                new MainFrame(usuario.getUsername()).setVisible(true);
            case "CLIENTE" ->
                new ClienteFrame(
                    usuario.getIdCliente(),
                    usuario.getNombreCompleto()
                ).setVisible(true);
        }
    }
}
