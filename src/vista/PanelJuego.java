package vista;

import control.ControladorJugador;
import control.Tecla;
import modelo.Config;
import modelo.Entidad;
import modelo.barco.Barco;
import modelo.juego.EstadisticasPartida;
import modelo.juego.EstadoJuego;
import modelo.juego.Juego;
import modelo.submarinos.TipoSubmarino;
import modelo.tienda.ItemTienda;
import modelo.tienda.Tienda;

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
import java.util.Map;

public class PanelJuego extends JPanel {
    private static final Font FUENTE_HUD = new Font("SansSerif", Font.BOLD, 14);
    private static final Font FUENTE_TITULO = new Font("SansSerif", Font.BOLD, 40);
    private static final Font FUENTE_TEXTO = new Font("SansSerif", Font.PLAIN, 18);
    private static final Color FONDO_CARTEL = new Color(0, 0, 0, 190);   // negro semitransparente

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
            case KeyEvent.VK_T -> Tecla.T;
            case KeyEvent.VK_1, KeyEvent.VK_NUMPAD1 -> Tecla.NUM_1;
            case KeyEvent.VK_2, KeyEvent.VK_NUMPAD2 -> Tecla.NUM_2;
            case KeyEvent.VK_3, KeyEvent.VK_NUMPAD3 -> Tecla.NUM_3;
            case KeyEvent.VK_4, KeyEvent.VK_NUMPAD4 -> Tecla.NUM_4;
            case KeyEvent.VK_5, KeyEvent.VK_NUMPAD5 -> Tecla.NUM_5;
            case KeyEvent.VK_6, KeyEvent.VK_NUMPAD6 -> Tecla.NUM_6;
            case KeyEvent.VK_7, KeyEvent.VK_NUMPAD7 -> Tecla.NUM_7;
            case KeyEvent.VK_8, KeyEvent.VK_NUMPAD8 -> Tecla.NUM_8;
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

        switch (juego.getEstado()) {
            case TIENDA -> dibujarTienda(g);
            case FIN -> dibujarFin(g);
            case JUGANDO -> { }
        }
    }

    // HUD

    private void dibujarHud(Graphics2D g) {
        Barco barco = juego.getBarco();

        g.setFont(FUENTE_HUD);
        g.setColor(Color.BLACK);
        g.drawString("Puntaje: " + juego.getJugador().getPuntaje(), 10, 20);
        g.drawString("Dinero: $" + juego.getJugador().getDinero(), 10, 40);
        g.drawString("Tiempo: " + formatearTiempo(juego.getTiempo()), 10, 60);

        barra(g, "Salud", 240, 8, barco.getSaludActual(), barco.getSaludMaxima(), new Color(60, 190, 80));
        barra(g, "Combustible", 240, 30, barco.getCombustible(), barco.getCombustibleMaximo(), new Color(230, 190, 40));
        barra(g, "Bombas", 240, 52, barco.getCargas(), barco.getCapacidadMaximaBombas(), Color.ORANGE);

        g.setColor(Color.BLACK);
        g.drawString("T: tienda", Config.ANCHO - 80, 20);
    }

    /** Barra con etiqueta y "actual/maximo"; se pone roja por debajo del 30%. */
    private static void barra(Graphics2D g, String etiqueta, int x, int y,
                              float actual, float maximo, Color color) {
        int ancho = 200;
        int alto = 16;
        float proporcion = maximo > 0 ? actual / maximo : 0f;

        g.setColor(Color.BLACK);
        g.drawString(etiqueta, x - 100, y + 13);
        g.setColor(Color.DARK_GRAY);
        g.fillRect(x, y, ancho, alto);
        g.setColor(proporcion > 0.3f ? color : Color.RED);
        g.fillRect(x, y, (int) (ancho * proporcion), alto);
        g.setColor(Color.WHITE);
        g.drawString((int) actual + "/" + (int) maximo, x + 6, y + 13);
    }

    // Tienda

    private void dibujarTienda(Graphics2D g) {
        Tienda tienda = juego.getTienda();
        int dinero = juego.getJugador().getDinero();
        int x = 150;
        int y = 140;

        g.setColor(FONDO_CARTEL);
        g.fillRect(x - 30, y - 10, Config.ANCHO - 2 * (x - 30), 440);

        g.setColor(Color.WHITE);
        g.setFont(FUENTE_TITULO);
        g.drawString("TIENDA", x, y + 35);
        g.setFont(FUENTE_TEXTO);
        g.drawString("(juego en pausa)   Dinero: $" + dinero, x + 170, y + 35);

        // Lista de Items
        ItemTienda[] items = ItemTienda.values();
        for (int i = 0; i < items.length; i++) {
            ItemTienda item = items[i];
            int fila = y + 80 + i * 32;

            String detalle = item.getNombre();
            if (item.esMejora()) {
                detalle += "  (nivel " + tienda.getVecesComprado(item) + "/" + item.getNivelMaximo() + ")";
            }

            String precio;
            if (tienda.estaAgotado(item)) {
                g.setColor(Color.GRAY);
                precio = "AGOTADO";
            } else {
                int p = tienda.getPrecio(item);
                g.setColor(p <= dinero ? Color.WHITE : new Color(255, 120, 120));
                precio = "$" + p;
            }
            g.drawString((i + 1) + ".  " + detalle, x, fila);
            g.drawString(precio, x + 480, fila);
        }

        g.setColor(Color.YELLOW);
        g.drawString(controlador.getMensajeTienda(), x, y + 350);
        g.setColor(Color.LIGHT_GRAY);
        g.drawString("1-8: comprar     T: volver al juego", x, y + 400);
    }

    // Fin de partida

    private void dibujarFin(Graphics2D g) {
        EstadisticasPartida est = juego.getEstadisticas();
        int x = 300;
        int y = 150;

        g.setColor(FONDO_CARTEL);
        g.fillRect(x - 40, y - 10, Config.ANCHO - 2 * (x - 40), 300);

        g.setColor(Color.WHITE);
        g.setFont(FUENTE_TITULO);
        g.drawString("GAME OVER", x, y + 40);

        g.setFont(FUENTE_TEXTO);
        g.drawString("Puntaje final: " + est.getPuntajeFinal(), x, y + 90);
        g.drawString("Tiempo sobrevivido: " + formatearTiempo(est.getTiempoSobrevivido()), x, y + 120);

        g.drawString("Submarinos destruidos", x, y + 170);
        int fila = y + 200;
        Map<TipoSubmarino, Integer> destruidos = est.getSubmarinosDestruidos();
        if (destruidos.isEmpty()) {
            g.drawString("   ninguno", x, fila);
            fila += 28;
        }
        for (Map.Entry<TipoSubmarino, Integer> entrada : destruidos.entrySet()) {
            g.drawString("   " + entrada.getKey() + ": " + entrada.getValue(), x, fila);
            fila += 28;
        }
        g.drawString("   Total: " + est.getTotalDestruidos(), x, fila);
    }

    // Utilidades

    /** 75.3 segundos -> "1:15" */
    private static String formatearTiempo(float segundos) {
        int total = (int) segundos;
        return String.format("%d:%02d", total / 60, total % 60);
    }

    private static void rect(Graphics2D g, Entidad e) {
        g.fillRect(Math.round(e.getPosX()), Math.round(e.getPosY()),
                Math.round(e.getAncho()), Math.round(e.getAlto()));
    }
}