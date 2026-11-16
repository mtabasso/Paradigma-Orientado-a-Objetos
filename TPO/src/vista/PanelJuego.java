package vista;

import control.ControladorJugador;
import control.Tecla;
import modelo.Config;
import modelo.Entidad;
import modelo.barco.Barco;
import modelo.juego.Juego;

import javax.swing.JPanel;
import javax.swing.Timer;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/** Solo LEE el modelo y dibuja. Tambien traduce el teclado y mueve el reloj del juego. */
public class PanelJuego extends JPanel {
    private final Juego juego;
    private final ControladorJugador controlador;
    private long ultimoTick = System.nanoTime();

    public PanelJuego(Juego juego, ControladorJugador controlador) {
        this.juego = juego;
        this.controlador = controlador;
        setPreferredSize(new Dimension(Config.ANCHO, Config.ALTO));
        setFocusable(true);

        addKeyListener(new KeyAdapter() {
            @Override public void keyPressed(KeyEvent e) {
                Tecla t = traducir(e.getKeyCode());
                if (t != null) controlador.teclaPresionada(t);
            }
            @Override public void keyReleased(KeyEvent e) {
                Tecla t = traducir(e.getKeyCode());
                if (t != null) controlador.teclaSoltada(t);
            }
        });

        // Swing Timer corre en el hilo de eventos: input, logica y dibujo no se pisan.
        new Timer(16, e -> {
            long ahora = System.nanoTime();
            float dt = Math.min((ahora - ultimoTick) / 1_000_000_000f, 0.05f);
            ultimoTick = ahora;
            controlador.actualizar();
            juego.actualizar(dt);
            repaint();
        }).start();
    }

    private static Tecla traducir(int codigo) {
        return switch (codigo) {
            case KeyEvent.VK_LEFT -> Tecla.FLECHA_IZQUIERDA;
            case KeyEvent.VK_RIGHT -> Tecla.FLECHA_DERECHA;
            case KeyEvent.VK_A -> Tecla.A;
            case KeyEvent.VK_D -> Tecla.D;
            case KeyEvent.VK_SPACE -> Tecla.ESPACIO;
            default -> null;
        };
    }

    @Override
    protected void paintComponent(Graphics g0) {
        super.paintComponent(g0);
        Graphics2D g = (Graphics2D) g0;
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // cielo y agua
        g.setColor(new Color(170, 215, 245));
        g.fillRect(0, 0, Config.ANCHO, (int) Config.NIVEL_AGUA);
        g.setColor(new Color(20, 70, 130));
        g.fillRect(0, (int) Config.NIVEL_AGUA, Config.ANCHO, Config.ALTO);

        g.setColor(new Color(150, 90, 40));
        rect(g, juego.getBarco());

        g.setColor(new Color(60, 60, 70));
        juego.getSubmarinos().forEach(s -> rect(g, s));

        g.setColor(Color.ORANGE);
        juego.getBombas().forEach(b -> rect(g, b));

        g.setColor(Color.RED);
        juego.getProyectiles().forEach(p -> rect(g, p));

        dibujarHud(g);
    }

    private void dibujarHud(Graphics2D g) {
        g.setFont(new Font("SansSerif", Font.BOLD, 14));
        g.setColor(Color.BLACK);
        g.drawString("Puntaje: " + juego.getPuntaje(), 10, 20);

        int ancho = 200;
        float salud = juego.getBarco().getSaludActual() / (float) Barco.SALUD_MAXIMA;
        g.setColor(Color.DARK_GRAY);
        g.fillRect(10, 30, ancho, 14);
        g.setColor(salud > 0.3f ? new Color(60, 190, 80) : Color.RED);
        g.fillRect(10, 30, (int) (ancho * salud), 14);

        if (!juego.isPartidaActiva()) {
            g.setFont(new Font("SansSerif", Font.BOLD, 48));
            g.setColor(Color.WHITE);
            g.drawString("GAME OVER", Config.ANCHO / 2 - 140, Config.ALTO / 2);
        }
    }

    private static void rect(Graphics2D g, Entidad e) {
        g.fillRect(Math.round(e.getPosX()), Math.round(e.getPosY()),
                   Math.round(e.getAncho()), Math.round(e.getAlto()));
    }
}
