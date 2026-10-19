package modelo.juego;

import modelo.Config;
import modelo.barco.Barco;
import modelo.barco.Bomba;
import modelo.proyectiles.Proyectil;
import modelo.submarinos.Submarino;
import modelo.submarinos.SubmarinoSimple;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/** Coordina el bucle de juego. No dibuja ni lee el teclado. */
public class Juego {
    private final Barco barco = new Barco();
    private final List<Submarino> submarinos = new ArrayList<>();
    private final List<Proyectil> proyectiles = new ArrayList<>();
    private final List<Bomba> bombas = new ArrayList<>();
    private final Random azar = new Random();

    private boolean partidaActiva = true;
    private float tiempo = 0f;

    // --- TEMPORAL Hito 1: en el Hito 2 esto pasa a Ronda y Jugador ---
    private static final float INTERVALO_SPAWN = 3f;
    private float tiempoHastaSpawn = 1f;
    private int puntaje = 0;

    public void actualizar(float dt) {
        if (!partidaActiva) return;
        tiempo += dt;

        barco.actualizar(dt);
        generarSubmarinos(dt);

        for (Submarino s : submarinos) {
            s.actualizar(dt);
            if (s.puedeDisparar()) {
                proyectiles.add(s.disparar(barco));
            }
        }
        for (Bomba b : bombas) b.actualizar(dt);
        for (Proyectil p : proyectiles) p.actualizar(dt);

        resolverColisiones();
        limpiarEntidades();

        if (barco.estaDestruido()) partidaActiva = false;
    }

    /** Lo llama el controlador cuando el jugador aprieta la tecla de bomba. */
    public void soltarBomba() {
        if (!partidaActiva) return;
        Bomba b = barco.soltarBomba();
        if (b != null) bombas.add(b);
    }

    private void generarSubmarinos(float dt) {
        tiempoHastaSpawn -= dt;
        if (tiempoHastaSpawn > 0f) return;
        tiempoHastaSpawn = INTERVALO_SPAWN;
        float y = Config.PROFUNDIDAD_MIN
                + azar.nextFloat() * (Config.PROFUNDIDAD_MAX - Config.PROFUNDIDAD_MIN);
        submarinos.add(new SubmarinoSimple(y, azar.nextBoolean()));
    }

    private void resolverColisiones() {
        // bomba vs submarino
        for (Bomba b : bombas) {
            if (!b.estaActiva()) continue;
            for (Submarino s : submarinos) {
                if (s.estaActiva() && b.colisionaCon(s)) {
                    b.impactar(s);
                    if (s.estaDestruido()) {
                        s.desactivar();
                        puntaje += s.getPuntos();
                    }
                    break; // una bomba, un impacto
                }
            }
        }
        // proyectil vs barco
        for (Proyectil p : proyectiles) {
            if (p.estaActiva() && p.colisionaCon(barco)) {
                p.impactar(barco);
            }
        }
    }

    /** removeIf evita la ConcurrentModificationException de borrar dentro de un for. */
    private void limpiarEntidades() {
        submarinos.removeIf(s -> !s.estaActiva() || s.haTerminadoRecorrido());
        proyectiles.removeIf(p -> !p.estaActiva() || p.fueraDePantalla());
        bombas.removeIf(b -> !b.estaActiva() || b.fueraDePantalla());
    }

    public Barco getBarco() { return barco; }
    public List<Submarino> getSubmarinos() { return Collections.unmodifiableList(submarinos); }
    public List<Proyectil> getProyectiles() { return Collections.unmodifiableList(proyectiles); }
    public List<Bomba> getBombas() { return Collections.unmodifiableList(bombas); }
    public boolean isPartidaActiva() { return partidaActiva; }
    public int getPuntaje() { return puntaje; }
    public float getTiempo() { return tiempo; }
}
