package modelo.barco;

import modelo.Config;
import modelo.Entidad;
import modelo.Destructible;

public class Barco extends Entidad implements Destructible {
    private static final float ANCHO = 80f;
    private static final float ALTO = 30f;
    private static final float CONSUMO = 4f;

    private final float velocidad = 250f;   // px/s
    private float recarga = 0.5f;     // s entre bombas
    private float tiempoHastaProximaBomba = 0f;
    private int direccion = 0;              // -1 izquierda, 0 quieto, 1 derecha

    // Valores maximos. Mejorables
    private int saludMaxima = 100;
    private float combustibleMaximo = 100f;
    private int capacidadMaximaBombas = 30;
    private float velocidadBombas = 180f;

    // Valores actuales
    private int saludActual = saludMaxima;
    private float combustible = combustibleMaximo;
    private int cargas = capacidadMaximaBombas;

    public Barco() {
        super((Config.ANCHO - ANCHO) / 2f, Config.NIVEL_AGUA - ALTO, ANCHO, ALTO);
    }

    /** Indica direccion del barco */
    public void mover(int direccion) {
        this.direccion = Integer.signum(direccion);
    }

    @Override
    public void actualizar(float dt) {
        if (direccion != 0 && combustible > 0f) {
            float x = getPosX() + direccion * velocidad * dt;
            setPosX(Math.max(0f, Math.min(Config.ANCHO - getAncho(), x)));
            combustible = Math.max(0f, combustible - CONSUMO * dt);
        }
       tiempoHastaProximaBomba = Math.max(0f, tiempoHastaProximaBomba - dt);
    }

    /**
     * Crea una bomba si termino la recarga; si no, devuelve null.
     */
    public Bomba soltarBomba() {
        if (tiempoHastaProximaBomba > 0f || cargas == 0) return null;
        tiempoHastaProximaBomba = recarga;
        cargas--;
        return new Bomba(getCentroX(), getPosY() + getAlto(), velocidadBombas);
    }

    @Override
    public void recibirDanio(int d) {
    if (d < 0 ) throw new IllegalArgumentException("El daño no puede ser negativo: " +d) ;
    saludActual = Math.max(0, saludActual - d);
    }

    @Override
    public boolean estaDestruido() { return saludActual <= 0; }

    public void reparar(int h) {
        if (h < 0) throw new IllegalArgumentException("La reparación no puede ser negativa: " + h);
        saludActual = Math.min(saludMaxima, saludActual + h);
    }

    public void reparar() {
        reparar(saludMaxima);
    }

    public void reabastecerCombustible(float c) {
        if (c < 0) throw new IllegalArgumentException("El combustible no puede ser negativo: " + c);
        combustible = Math.min(combustibleMaximo, combustible + c);
    }

    public void recargarBombas(int n) {
        if (n < 0 ) throw new IllegalArgumentException("La cargas no pueden ser negativas: " + n);
        cargas = Math.min(capacidadMaximaBombas, cargas + n);
    }

// Mejoras

    public void mejorarSaludMaxima(int extra) {
        saludMaxima += extra;
        saludActual += extra;
    }

    public void mejorarCapacidadMunicion(int extra) {
        capacidadMaximaBombas += extra;
    }

    public void mejorarCapacidadCombustible(float extra) {
        combustibleMaximo += extra;
    }

    public void mejorarVelocidadBombas(float factor) {
        velocidadBombas *= factor;
    }

// Getters

    public int getSaludActual() {return saludActual;}
    public int getSaludMaxima() {return saludMaxima;}
    public float getCombustible() {return combustible;}
    public float getCombustibleMaximo() {return combustibleMaximo;}
    public int getCargas() {return cargas;}
    public int getCapacidadMaximaBombas() {return capacidadMaximaBombas;}
    public float getVelocidadBombas() {return velocidadBombas;}
}
