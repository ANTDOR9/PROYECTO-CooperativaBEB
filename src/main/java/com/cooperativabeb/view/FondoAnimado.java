package com.cooperativabeb.view;

import com.cooperativabeb.util.Tema;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class FondoAnimado extends JPanel {

    private List<Linea> lineas = new ArrayList<>();
    private List<Particula> particulas = new ArrayList<>();
    private Timer timer;
    private Random rand = new Random();

    public FondoAnimado() {
        setBackground(Tema.NEGRO_FONDO);
        setLayout(new BorderLayout());
        inicializarElementos();
        iniciarAnimacion();
    }

    private void inicializarElementos() {
        for (int i = 0; i < 15; i++) {
            lineas.add(new Linea(rand));
        }
        for (int i = 0; i < 40; i++) {
            particulas.add(new Particula(rand));
        }
    }

    private void iniciarAnimacion() {
        timer = new Timer(16, e -> {
            lineas.forEach(l -> l.actualizar(getWidth(), getHeight(), rand));
            particulas.forEach(p -> p.actualizar(getWidth(), getHeight(), rand));
            repaint();
        });
        timer.start();
    }

    public void detener() {
        if (timer != null) timer.stop();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Fondo negro
        g2.setColor(Tema.NEGRO_FONDO);
        g2.fillRect(0, 0, getWidth(), getHeight());

        // Líneas doradas diagonales
        for (Linea l : lineas) {
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (float)l.alpha));
            g2.setColor(Tema.DORADO_PRINCIPAL);
            g2.setStroke(new BasicStroke((float)l.grosor));
            double rad = Math.toRadians(-45);
            double dx = Math.cos(rad) * l.largo;
            double dy = Math.sin(rad) * l.largo;
            g2.draw(new Line2D.Double(l.x, l.y, l.x + dx, l.y + dy));
        }

        // Partículas flotantes
        for (Particula p : particulas) {
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (float)p.alpha));
            g2.setColor(Tema.DORADO_PRINCIPAL);
            g2.fillOval((int)(p.x - p.radio), (int)(p.y - p.radio),
                (int)(p.radio * 2), (int)(p.radio * 2));
        }

        // Reset composite
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
    }

    // ─── Clase interna Linea ───────────────────────────
    static class Linea {
        double x, y, largo, velocidad, alpha, grosor;

        Linea(Random rand) {
            reset(rand, true);
        }

        void reset(Random rand, boolean inicial) {
            largo     = 80 + rand.nextDouble() * 150;
            velocidad = 0.4 + rand.nextDouble() * 0.8;
            alpha     = 0.04f + rand.nextFloat() * 0.12f;
            grosor    = 0.5f + rand.nextFloat() * 1.0f;
            if (inicial) {
                x = rand.nextDouble() * 1200;
                y = rand.nextDouble() * 800;
            } else {
                x = -largo - rand.nextDouble() * 200;
                y = -50 - rand.nextDouble() * 200;
            }
        }

        void actualizar(int w, int h, Random rand) {
            x += velocidad;
            y += velocidad;
            if (x > w + largo + 50 || y > h + largo + 50) {
                reset(rand, false);
            }
        }
    }

    // ─── Clase interna Particula ───────────────────────
    static class Particula {
        double x, y, radio, velocidad, alpha;

        Particula(Random rand) {
            reset(rand, true);
        }

        void reset(Random rand, boolean inicial) {
            radio     = 0.5 + rand.nextDouble() * 2.0;
            velocidad = 0.15 + rand.nextDouble() * 0.4;
            alpha     = 0.08f + rand.nextFloat() * 0.25f;
            x         = rand.nextDouble() * 1200;
            y         = inicial ? rand.nextDouble() * 800 : 810;
        }

        void actualizar(int w, int h, Random rand) {
            y -= velocidad;
            if (y < -5) {
                x = rand.nextDouble() * w;
                y = h + 5;
                alpha = 0.08f + rand.nextFloat() * 0.25f;
            }
        }
    }
}
