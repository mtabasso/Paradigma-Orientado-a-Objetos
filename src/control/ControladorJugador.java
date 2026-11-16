package control;

import modelo.juego.Juego;
import modelo.tienda.DineroInsuficienteException;
import modelo.tienda.ItemTienda;


import java.util.EnumMap;
import java.util.Map;

public class ControladorJugador {
    private static final Tecla[] TECLAS_COMPRA = {
        Tecla.NUM_1,  Tecla.NUM_2, Tecla.NUM_3, Tecla.NUM_4,
        Tecla.NUM_5, Tecla.NUM_6, Tecla.NUM_7, Tecla.NUM_8
    };

    private final Juego juego;
    private final Map<Tecla, Accion> teclas = new EnumMap<>(Tecla.class);
    private final Map<Tecla, ItemTienda> compras = new EnumMap<>(Tecla.class);
    private boolean izquierdaPresionada;
    private boolean derechaPresionada;
    private String mensajeTienda = "";

    public ControladorJugador(Juego juego) {
        this.juego = juego;
        teclas.put(Tecla.FLECHA_IZQUIERDA, Accion.IZQUIERDA);
        teclas.put(Tecla.A, Accion.IZQUIERDA);
        teclas.put(Tecla.FLECHA_DERECHA, Accion.DERECHA);
        teclas.put(Tecla.D, Accion.DERECHA);
        teclas.put(Tecla.ESPACIO, Accion.SOLTAR_BOMBA);
        teclas.put(Tecla.T, Accion.ALTERNAR_TIENDA);

        ItemTienda[] items = ItemTienda.values();
        for (int i = 0; i < items.length && i < TECLAS_COMPRA.length; i++) {
            compras.put(TECLAS_COMPRA[i], items[i]);
        }
    }

    public void teclaPresionada(Tecla t) {
        ItemTienda item = compras.get(t);
        if (item != null) {
            comprar(item);
            return;
        }
        Accion accion = teclas.get(t);
        if (accion == null) return;
        switch (accion) {
            case IZQUIERDA -> izquierdaPresionada = true;
            case DERECHA -> derechaPresionada = true;
            case SOLTAR_BOMBA -> juego.soltarBomba();
            case ALTERNAR_TIENDA -> {
                juego.alternarTienda();
                mensajeTienda = "";
            }
        }
    }

    public void teclaSoltada(Tecla t) {
        Accion accion = teclas.get(t);
        if (accion == null) return;
        switch (accion) {
            case IZQUIERDA -> izquierdaPresionada = false;
            case DERECHA -> derechaPresionada = false;
            default -> { }
        }
    }

    private void comprar(ItemTienda item) {
        if (juego.getTienda().estaAgotado(item)) {
            mensajeTienda = item.getNombre() + ": ya tenes el nivel maximo";
            return;
        }
        try {
            juego.comprar(item);
            mensajeTienda = "Compraste: " + item.getNombre();
        } catch (DineroInsuficienteException e) {
            mensajeTienda = "Dinero insuficiente";
        }
    }

    /** Se llama una vez por frame, antes de Juego.actualizar(). */
    public void actualizar() {
        int dir = (derechaPresionada ? 1 : 0) - (izquierdaPresionada ? 1 : 0);
        juego.getBarco().mover(dir);
    }
    public String getMensajeTienda() {return mensajeTienda;}
}
