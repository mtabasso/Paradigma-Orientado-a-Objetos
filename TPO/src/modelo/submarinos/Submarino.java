package modelo.submarinos;

import modelo.Config;
import modelo.Entidad;
import modelo.barco.Barco;
import modelo.proyectiles.Proyectil;

public abstract class Submarino extends Entidad {
    private static final float ANCHO = 70f;
    private static final float ALTO = 28f;

    private final TipoSubmarino tipo;
    private int vida;
    private final int danio;
    private final float recarga;
    private final int recompensaDinero;
    private final int puntos;
    private final int rondaAparicion;
    private final float velocidad;
    private final boolean desdeIzquierda;
    private float tiempoHastaDisparo;

    protected Submarino(TipoSubmarino tipo, int vida, int danio, float recarga,
                        int recompensaDinero, int puntos, int rondaAparicion,
                        float velocidad, float posY, boolean desdeIzquierda) {
        // Nace justo afuera de la pantalla, del lado desde el que entra.
        super(desdeIzquierda ? -ANCHO : Config.ANCHO, posY, ANCHO, ALTO);
        this.tipo = tipo;
        this.vida = vida;
        this.danio = danio;
        this.recarga = recarga;
        this.recompensaDinero = recompensaDinero;
        this.puntos = puntos;
        this.rondaAparicion = rondaAparicion;
        this.velocidad = velocidad;
        this.desdeIzquierda = desdeIzquierda;
        this.tiempoHastaDisparo = recarga;
    }

    /** Cada subtipo decide QUE proyectil crea. */
    protected abstract Proyectil crearProyectil(Barco objetivo);

    /** Template method: controla la recarga y delega la creacion en el subtipo. */
    public final Proyectil disparar(Barco objetivo) {
        tiempoHastaDisparo = recarga;
        return crearProyectil(objetivo);
    }

    public void mover(float dt) {
        float dir = desdeIzquierda ? 1f : -1f;
        setPosX(getPosX() + dir * velocidad * dt);
    }

    @Override
    public void actualizar(float dt) {
        mover(dt);
        tiempoHastaDisparo = Math.max(0f, tiempoHastaDisparo - dt);
    }

    public boolean puedeDisparar() {
        boolean visible = getPosX() >= 0 && getPosX() + getAncho() <= Config.ANCHO;
        return visible && tiempoHastaDisparo <= 0f;
    }

    /** Como nace afuera de la pantalla, fueraDePantalla() no sirve: miramos si ya cruzo todo. */
    public boolean haTerminadoRecorrido() {
        return desdeIzquierda ? getPosX() > Config.ANCHO : getPosX() + getAncho() < 0;
    }

    public void recibirDanio(int d) { vida -= d; }
    public boolean estaDestruido() { return vida <= 0; }

    public TipoSubmarino getTipo() { return tipo; }
    public int getDanio() { return danio; }
    public int getPuntos() { return puntos; }
    public int getRecompensaDinero() { return recompensaDinero; }
    public int getRondaAparicion() { return rondaAparicion; }
}
