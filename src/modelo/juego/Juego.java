package modelo.juego;

import modelo.Config;
import modelo.barco.Barco;
import modelo.barco.Bomba;
import modelo.proyectiles.Proyectil;
import modelo.submarinos.FabricaSubmarinos;
import modelo.submarinos.Submarino;
import modelo.submarinos.TipoSubmarino;
import modelo.tienda.Tienda;
import modelo.tienda.ItemTienda;
import modelo.tienda.DineroInsuficienteException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Iterator;

public class Juego {
    private static final float INTERVALO_SPAWN = 3f;

    private final Barco barco = new Barco();
    private final Jugador jugador = new Jugador();
    private final Tienda tienda = new Tienda(jugador, barco);
    private final EstadisticasPartida estadisticas = new EstadisticasPartida();
    private final FabricaSubmarinos fabrica = new FabricaSubmarinos();
    private final Random azar;

    private final List<Submarino> submarinos = new ArrayList<>();
    private final List<Proyectil> proyectiles = new ArrayList<>();
    private final List<Bomba> bombas = new ArrayList<>();

    private EstadoJuego estado = EstadoJuego.JUGANDO;
    private float tiempo = 0f;
    private float tiempoHastaSpawn = 1f;

    public Juego() {
        this(new Random());
    }

    public Juego(Random azar) {
        this.azar = azar;
    }

    public void actualizar(float dt) {
        if (estado != EstadoJuego.JUGANDO) return;
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

        if (barco.estaDestruido()) terminarPartida();
    }

    /** Llama al controlador */
    public void soltarBomba() {
        if (estado != EstadoJuego.JUGANDO) return;
        Bomba b = barco.soltarBomba();
        if (b != null) bombas.add(b);
    }

    public void alternarTienda() {
        if (estado == EstadoJuego.JUGANDO) estado = EstadoJuego.TIENDA;
        else if (estado == EstadoJuego.TIENDA) estado = EstadoJuego.JUGANDO;
    }

    public void comprar(ItemTienda item) throws DineroInsuficienteException {
        if (estado != EstadoJuego.TIENDA) return;
        tienda.comprar(item);
    }

    private void generarSubmarinos(float dt) {
        tiempoHastaSpawn -= dt;
        if (tiempoHastaSpawn > 0f) return;
        tiempoHastaSpawn = INTERVALO_SPAWN;
        float y = Config.PROFUNDIDAD_MIN
                + azar.nextFloat() * (Config.PROFUNDIDAD_MAX - Config.PROFUNDIDAD_MIN);
        submarinos.add(fabrica.crear(TipoSubmarino.SIMPLE, y, azar.nextBoolean()));
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
                        jugador.cobrarRecompensa(s.getPuntos(), s.getRecompensaDinero());
                        estadisticas.registrarDestruccion(s.getTipo());
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

    private void terminarPartida() {
        estado = EstadoJuego.FIN;
        estadisticas.cerrar(jugador.getPuntaje(), tiempo);
    }

    private void limpiarEntidades() {
        Iterator<Submarino> it  = submarinos.iterator();
        while (it.hasNext()) {
            Submarino s = it.next();
            if (!s.estaActiva() || s.haTerminadoRecorrido()) {
                it.remove();
            }
        }
        proyectiles.removeIf(p -> !p.estaActiva() || p.fueraDePantalla());
        bombas.removeIf(b -> !b.estaActiva() || b.fueraDePantalla());
    }

    public Barco getBarco() { return barco; }
    public List<Submarino> getSubmarinos() { return Collections.unmodifiableList(submarinos); }
    public List<Proyectil> getProyectiles() { return Collections.unmodifiableList(proyectiles); }
    public List<Bomba> getBombas() { return Collections.unmodifiableList(bombas); }
    public Tienda getTienda() { return tienda; }
    public EstadoJuego getEstado() { return estado; }
    public Jugador getJugador() { return jugador; }
    public EstadisticasPartida getEstadisticas() { return estadisticas; }
    public float getTiempo() { return tiempo; }
}