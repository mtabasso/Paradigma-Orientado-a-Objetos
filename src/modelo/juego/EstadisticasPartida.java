package modelo.juego;

import modelo.submarinos.TipoSubmarino;

import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;

public class EstadisticasPartida {
    private int puntajeFinal;
    private float tiempoSobrevivido;
    private final Map<TipoSubmarino, Integer> submarinosDestruidos = new EnumMap<>(TipoSubmarino.class);

    public void registrarDestruccion(TipoSubmarino tipo){
        submarinosDestruidos.merge(tipo,1, Integer::sum);
    }

    public void cerrar(int puntaje, float tiempo) {
        this.puntajeFinal = puntaje;
        this.tiempoSobrevivido = tiempo;
    }

    public int getCantidadDestruidos(TipoSubmarino tipo){
        return submarinosDestruidos.getOrDefault(tipo, 0);
    }

    public int getTotalDestruidos(){
        int total = 0;
        for (int cantidad: submarinosDestruidos.values()) total += cantidad;
        return total;
    }

    public Map<TipoSubmarino, Integer> getSubmarinosDestruidos() {
        return Collections.unmodifiableMap(submarinosDestruidos);
    }

    public int getPuntajeFinal() {
        return puntajeFinal;
    }

    public float getTiempoSobrevivido() {
        return tiempoSobrevivido;
    }
}
