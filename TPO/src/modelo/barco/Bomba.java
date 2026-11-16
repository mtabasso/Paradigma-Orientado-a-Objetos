package modelo.barco;

import modelo.Entidad;
import modelo.submarinos.Submarino;

public class Bomba extends Entidad {
    private static final float ANCHO = 10f;
    private static final float ALTO = 14f;

    private final float velocidad = 180f; // px/s
    private final int danio = 1;

    public Bomba(float centroX, float y) {
        super(centroX - ANCHO / 2f, y, ANCHO, ALTO);
    }

    public void caer(float dt) {
        setPosY(getPosY() + velocidad * dt);
    }

    @Override
    public void actualizar(float dt) { caer(dt); }

    public void impactar(Submarino s) {
        s.recibirDanio(danio);
        desactivar();
    }
}
