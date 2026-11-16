package modelo.submarinos;

import modelo.barco.Barco;
import modelo.proyectiles.Proyectil;
import modelo.proyectiles.Torpedo;

public class SubmarinoSimple extends Submarino {
    // tipo, vida, danio, recarga, dinero, puntos, ronda, velocidad
    public SubmarinoSimple(float posY, boolean desdeIzquierda) {
        super(TipoSubmarino.SIMPLE, 1, 10, 4f, 20, 20, 1, 90f, posY, desdeIzquierda);
    }

    @Override
    protected Proyectil crearProyectil(Barco objetivo) {
        return new Torpedo(getCentroX(), getPosY(),
                           objetivo.getCentroX(), objetivo.getCentroY(), getDanio());
    }
}
