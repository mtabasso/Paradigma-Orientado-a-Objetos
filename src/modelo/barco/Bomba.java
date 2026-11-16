package modelo.barco;

import modelo.Entidad;
import modelo.Destructible;

public class Bomba extends Entidad {
    private static final float ANCHO = 10f;
    private static final float ALTO = 14f;

    private final float velocidad; // px/s
    private final int danio = 1;

    Bomba(float centroX, float y, float velocidad) {
        super(centroX - ANCHO / 2f, y, ANCHO, ALTO);
        this.velocidad = velocidad;
    }

    public void caer(float dt) {
        setPosY(getPosY() + velocidad * dt);
    }

    @Override
    public void actualizar(float dt) { caer(dt); }

    public void impactar(Destructible objetivo) {
        objetivo.recibirDanio(danio);
        desactivar();
    }
}
