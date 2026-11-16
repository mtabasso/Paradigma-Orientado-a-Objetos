package modelo.tienda;

import modelo.barco.Barco;
import modelo.juego.Jugador;

import java.util.EnumMap;
import java.util.Map;

public class Tienda {
    private static final int SALUD = 30;
    private static final float COMBUSTIBLE = 50f;
    private static final int MUNICION = 10;
    private static final int EXTRA_SALUD_MAX = 25;
    private static final int EXTRA_CAP_MUNICION = 10;
    private static final float EXTRA_CAP_COMBUSTIBLE = 50f;
    private static final float FACTOR_VEL_BOMBAS = 1.25f;

    private final Jugador jugador;
    private final Barco barco;
    private final Map<ItemTienda, Integer> vecesComprado = new EnumMap<>(ItemTienda.class);

    public Tienda(Jugador jugador, Barco barco) {
        this.jugador = jugador;
        this.barco = barco;
    }

    /***
     * @throws DineroInsuficienteException si el jugador no tiene suficiente dinero
     * @throws IllegalStateException si el item ya esta agotado
     */
    public void comprar(ItemTienda item) throws DineroInsuficienteException {
        if (estaAgotado(item)) {
            throw new IllegalStateException("Ya compraste todos los niveles de " + item.getNombre());
        }
        int precio = getPrecio(item);
        if (!jugador.gastarDinero(precio)) {
            throw new DineroInsuficienteException(precio, jugador.getDinero());
        }
        aplicar(item);
        vecesComprado.merge(item, 1, Integer::sum);
    }

    private void aplicar(ItemTienda item) {
        switch (item) {
            case SALUD -> barco.reparar(SALUD);
            case REPARACION_TOTAL -> barco.reparar();
            case COMBUSTIBLE -> barco.reabastecerCombustible(COMBUSTIBLE);
            case MUNICION -> barco.recargarBombas(MUNICION);
            case MEJORA_SALUD_MAX -> barco.mejorarSaludMaxima(EXTRA_SALUD_MAX);
            case MEJORA_CAP_MUNICION -> barco.mejorarCapacidadMunicion(EXTRA_CAP_MUNICION);
            case MEJORA_CAP_COMBUSTIBLE -> barco.mejorarCapacidadCombustible(EXTRA_CAP_COMBUSTIBLE);
            case MEJORA_VEL_BOMBAS -> barco.mejorarVelocidadBombas(FACTOR_VEL_BOMBAS);
        }
    }

    public int getPrecio(ItemTienda item) {
        return item.getPrecio(getVecesComprado(item));
    }

    public boolean estaAgotado(ItemTienda item) {
        return item.estaAgotado(getVecesComprado(item));
    }

    public int getVecesComprado(ItemTienda item) {
        return vecesComprado.getOrDefault(item, 0);
    }
}