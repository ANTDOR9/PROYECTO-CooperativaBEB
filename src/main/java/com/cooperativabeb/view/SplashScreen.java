package com.cooperativabeb.view;

import com.cooperativabeb.util.Tema;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.net.URL;

public class SplashScreen extends JWindow {

    private JProgressBar progressBar;
    private JLabel lblEstado;

    public SplashScreen() {
        initComponents();
    }

    private void initComponents() {
        setSize(480, 320);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Tema.NEGRO_FONDO);
        panel.setBorder(BorderFactory.createLineBorder(Tema.DORADO_OSCURO, 1));

        // Panel logo
        JPanel panelLogo = new JPanel(new BorderLayout(0, 10));
        panelLogo.setBackground(Tema.NEGRO_FONDO);
        panelLogo.setBorder(new EmptyBorder(30, 40, 20, 40));

        // Logo imagen
        JLabel lblLogo = new JLabel();
        lblLogo.setHorizontalAlignment(SwingConstants.CENTER);
        try {
            URL imgUrl = getClass().getClassLoader().getResource("logo.png");
            if (imgUrl != null) {
                ImageIcon icon = new ImageIcon(imgUrl);
                Image img = icon.getImage().getScaledInstance(120, 120, Image.SCALE_SMOOTH);
                lblLogo.setIcon(new ImageIcon(img));
            }
        } catch (Exception e) {
            lblLogo.setText("BEB");
            lblLogo.setFont(new Font("Segoe UI", Font.BOLD, 48));
            lblLogo.setForeground(Tema.DORADO_PRINCIPAL);
        }

        JLabel lblNombre = new JLabel("COOPERATIVA BEB", SwingConstants.CENTER);
        lblNombre.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblNombre.setForeground(Tema.DORADO_PRINCIPAL);

        JLabel lblSistema = new JLabel("Sistema Financiero Premium", SwingConstants.CENTER);
        lblSistema.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblSistema.setForeground(Tema.TEXTO_SECUNDARIO);

        JPanel panelTexto = new JPanel(new GridLayout(2, 1, 0, 4));
        panelTexto.setBackground(Tema.NEGRO_FONDO);
        panelTexto.add(lblNombre);
        panelTexto.add(lblSistema);

        panelLogo.add(lblLogo, BorderLayout.CENTER);
        panelLogo.add(panelTexto, BorderLayout.SOUTH);

        // Panel progreso
        JPanel panelProgress = new JPanel(new BorderLayout(0, 8));
        panelProgress.setBackground(Tema.NEGRO_FONDO);
        panelProgress.setBorder(new EmptyBorder(0, 40, 25, 40));

        lblEstado = new JLabel("Iniciando sistema...", SwingConstants.CENTER);
        lblEstado.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblEstado.setForeground(Tema.TEXTO_SECUNDARIO);

        progressBar = new JProgressBar(0, 100);
        progressBar.setPreferredSize(new Dimension(0, 4));
        progressBar.setBackground(Tema.NEGRO_BORDE);
        progressBar.setForeground(Tema.DORADO_PRINCIPAL);
        progressBar.setBorderPainted(false);
        progressBar.setStringPainted(false);

        JLabel lblVersion = new JLabel("v1.0 — SENATI 2025", SwingConstants.CENTER);
        lblVersion.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        lblVersion.setForeground(Tema.TEXTO_SUTIL);

        panelProgress.add(lblEstado, BorderLayout.NORTH);
        panelProgress.add(progressBar, BorderLayout.CENTER);
        panelProgress.add(lblVersion, BorderLayout.SOUTH);

        panel.add(panelLogo, BorderLayout.CENTER);
        panel.add(panelProgress, BorderLayout.SOUTH);
        add(panel);
    }

    public void mostrar(Runnable alTerminar) {
        setVisible(true);
        SwingWorker<Void, Integer> worker = new SwingWorker<>() {
            @Override
            protected Void doInBackground() throws Exception {
                String[] mensajes = {
                    "Iniciando sistema...",
                    "Conectando a Oracle...",
                    "Cargando modulos...",
                    "Verificando credenciales...",
                    "Preparando interfaz...",
                    "Listo."
                };
                for (int i = 0; i <= 100; i++) {
                    int idx = Math.min(i / 20, mensajes.length - 1);
                    final int prog = i;
                    final String msg = mensajes[idx];
                    SwingUtilities.invokeLater(() -> {
                        progressBar.setValue(prog);
                        lblEstado.setText(msg);
                    });
                    Thread.sleep(20);
                }
                return null;
            }
            @Override
            protected void done() {
                dispose();
                alTerminar.run();
            }
        };
        worker.execute();
    }
}
