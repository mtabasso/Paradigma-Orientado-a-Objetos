package modelo.proyectiles;

/** Va en linea recta hacia el punto donde estaba el barco al ser disparado. */
public class Torpedo extends Proyectil {
    private final float objetivoX;
    private final float objetivoY;

    public Torpedo(float x, float y, float objetivoX, float objetivoY, int danio) {
        super(x, y, 8f, 20f, 160f, danio, objetivoX - x, objetivoY - y);
        this.objetivoX = objetivoX;
        this.objetivoY = objetivoY;
    }

    @Override
    public void mover(float dt) {
        setPosX(getPosX() + getDirX() * getVelocidad() * dt);
        setPosY(getPosY() + getDirY() * getVelocidad() * dt);
    }

    public float getObjetivoX() { return objetivoX; }
    public float getObjetivoY() { return objetivoY; }
}
