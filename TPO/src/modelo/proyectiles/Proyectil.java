package modelo.proyectiles;

import modelo.Entidad;
import modelo.barco.Barco;

public abstract class Proyectil extends Entidad {
    private final float velocidad;
    private final int danio;
    private final float dirX;   // direccion normalizada
    private final float dirY;

    protected Proyectil(float x, float y, float ancho, float alto,
                        float velocidad, int danio, float dirX, float dirY) {
        super(x - ancho / 2f, y, ancho, alto);
        this.velocidad = velocidad;
        this.danio = danio;
        double largo = Math.hypot(dirX, dirY);
        if (largo == 0) { // sin direccion: sube
            this.dirX = 0f;
            this.dirY = -1f;
        } else {
            this.dirX = (float) (dirX / largo);
            this.dirY = (float) (dirY / largo);
        }
    }

    /** Cada proyectil define como se mueve. */
    public abstract void mover(float dt);

    @Override
    public void actualizar(float dt) { mover(dt); }

    public void impactar(Barco b) {
        b.recibirDanio(danio);
        desactivar();
    }

    protected float getVelocidad() { return velocidad; }
    protected float getDirX() { return dirX; }
    protected float getDirY() { return dirY; }
    public int getDanio() { return danio; }
}
