package modelo.submarinos;

import java.util.EnumSet;
import java.util.Set;

public class FabricaSubmarinos {

    private static final Set<TipoSubmarino> IMPLEMENTADOS = EnumSet.of(TipoSubmarino.SIMPLE);

    public boolean puedeCrear(TipoSubmarino tipo) {
        return IMPLEMENTADOS.contains(tipo);
    }

    public Submarino crear(TipoSubmarino tipo, float posY, boolean desdeIzquierda) {
        return switch (tipo) {
            case SIMPLE -> new SubmarinoSimple(posY, desdeIzquierda);
            default -> throw new IllegalArgumentException("Tipo todavía no implementado: " + tipo);
        };
    }
}
