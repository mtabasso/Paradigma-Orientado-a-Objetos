package app;

import control.ControladorJugador;
import modelo.juego.Juego;
import vista.PanelJuego;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Juego juego = new Juego();
            ControladorJugador controlador = new ControladorJugador(juego);
            PanelJuego panel = new PanelJuego(juego, controlador);

            JFrame ventana = new JFrame("Barco vs. Submarinos");
            ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            ventana.setResizable(false);
            ventana.add(panel);
            ventana.pack();
            ventana.setLocationRelativeTo(null);
            ventana.setVisible(true);
            panel.requestFocusInWindow();
        });
    }
}
