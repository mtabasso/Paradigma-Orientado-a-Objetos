package modelo.barco;

import modelo.Config;
import modelo.Entidad;

public class Barco extends Entidad {
    public static final int SALUD_MAXIMA = 100;
    private static final float ANCHO = 80f;
    private static final float ALTO = 30f;

    private int saludActual = SALUD_MAXIMA;
    private final float velocidad = 250f;   // px/s
    private final float recarga = 0.5f;     // s entre bombas
    private float tiempoHastaProximaBomba = 0f;
    private int direccion = 0;              // -1 izquierda, 0 quieto, 1 derecha

    public Barco() {
        super((Config.ANCHO - ANCHO) / 2f, Config.NIVEL_AGUA - ALTO, ANCHO, ALTO);
    }

    /** Indica hacia donde se mueve el barco; el desplazamiento se aplica en actualizar(). */
    public void mover(int direccion) {
        this.direccion = Integer.signum(direccion);
    }

    @Override
    public void actualizar(float dt) {
        float x = getPosX() + direccion * velocidad * dt;
        setPosX(Math.max(0f, Math.min(Config.ANCHO - getAncho(), x)));
        tiempoHastaProximaBomba = Math.max(0f, tiempoHastaProximaBomba - dt);
    }

    /**
     * Crea una bomba si termino la recarga; si no, devuelve null.
     * (Las cargas limitadas llegan con CajaMunicion en el Hito 4.)
     */
    public Bomba soltarBomba() {
        if (tiempoHastaProximaBomba > 0f) return null;
        tiempoHastaProximaBomba = recarga;
        return new Bomba(getCentroX(), getPosY() + getAlto());
    }

    public void recibirDanio(int d) {
        saludActual = Math.max(0, saludActual - d);
    }

    public boolean estaDestruido() { return saludActual <= 0; }
    public int getSaludActual() { return saludActual; }
}
