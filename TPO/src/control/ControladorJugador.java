package control;

import modelo.juego.Juego;

import java.util.EnumMap;
import java.util.Map;

public class ControladorJugador {
    private final Juego juego;
    private final Map<Tecla, Accion> teclas = new EnumMap<>(Tecla.class);
    private boolean izquierdaPresionada;
    private boolean derechaPresionada;

    public ControladorJugador(Juego juego) {
        this.juego = juego;
        teclas.put(Tecla.FLECHA_IZQUIERDA, Accion.IZQUIERDA);
        teclas.put(Tecla.A, Accion.IZQUIERDA);
        teclas.put(Tecla.FLECHA_DERECHA, Accion.DERECHA);
        teclas.put(Tecla.D, Accion.DERECHA);
        teclas.put(Tecla.ESPACIO, Accion.SOLTAR_BOMBA);
    }

    public void teclaPresionada(Tecla t) {
        Accion accion = teclas.get(t);
        if (accion == null) return;
        switch (accion) {
            case IZQUIERDA -> izquierdaPresionada = true;
            case DERECHA -> derechaPresionada = true;
            case SOLTAR_BOMBA -> juego.soltarBomba(); // la recarga la controla el Barco
        }
    }

    public void teclaSoltada(Tecla t) {
        Accion accion = teclas.get(t);
        if (accion == null) return;
        switch (accion) {
            case IZQUIERDA -> izquierdaPresionada = false;
            case DERECHA -> derechaPresionada = false;
            default -> { }
        }
    }

    /** Se llama una vez por frame, antes de Juego.actualizar(). */
    public void actualizar() {
        int dir = (derechaPresionada ? 1 : 0) - (izquierdaPresionada ? 1 : 0);
        juego.getBarco().mover(dir);
    }
}
