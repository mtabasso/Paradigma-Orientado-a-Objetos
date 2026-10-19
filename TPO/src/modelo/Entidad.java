package modelo;

/**
 * Clase base de todo lo que tiene posicion y tamano en el mundo.
 * (posX, posY) es la esquina superior izquierda. No sabe dibujarse.
 */
public abstract class Entidad {
    private float posX;
    private float posY;
    private final float ancho;
    private final float alto;
    private boolean activa = true;

    protected Entidad(float posX, float posY, float ancho, float alto) {
        this.posX = posX;
        this.posY = posY;
        this.ancho = ancho;
        this.alto = alto;
    }

    public abstract void actualizar(float dt);

    /** Colision por rectangulos (AABB). */
    public boolean colisionaCon(Entidad otra) {
        return posX < otra.posX + otra.ancho && posX + ancho > otra.posX
            && posY < otra.posY + otra.alto && posY + alto > otra.posY;
    }

    public boolean fueraDePantalla() {
        return posX + ancho < 0 || posX > Config.ANCHO
            || posY + alto < 0 || posY > Config.ALTO;
    }

    /** Marca la entidad para que Juego la saque de su lista. */
    public void desactivar() { activa = false; }
    public boolean estaActiva() { return activa; }

    public float getPosX() { return posX; }
    public float getPosY() { return posY; }
    public float getAncho() { return ancho; }
    public float getAlto() { return alto; }
    public float getCentroX() { return posX + ancho / 2f; }
    public float getCentroY() { return posY + alto / 2f; }

    protected void setPosX(float x) { posX = x; }
    protected void setPosY(float y) { posY = y; }
}
