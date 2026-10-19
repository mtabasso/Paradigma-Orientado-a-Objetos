package modelo;

/** Constantes del mundo. Unidades: pixeles y segundos. */
public final class Config {
    private Config() {}

    public static final int ANCHO = 900;
    public static final int ALTO = 600;
    public static final float NIVEL_AGUA = 120f;     // y de la superficie
    public static final float PROFUNDIDAD_MIN = 200f; // rango de y donde aparecen submarinos
    public static final float PROFUNDIDAD_MAX = 520f;
}
