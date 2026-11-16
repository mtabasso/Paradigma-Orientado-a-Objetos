package modelo.juego;


public class Jugador {
    private int dinero;
    private int puntaje;

    public void sumarPuntaje(int p) {
        if (p > 0) {
            puntaje += p;
        }
    }

    public void sumarDinero(int m) {
        if (m > 0) {
            dinero += m;
        }
    }

    public boolean gastarDinero (int m) {
        if (m < 0 || dinero < m) {
            return false;
            }
        dinero -= m;
        return true;    
    }

    public void cobrarRecompensa(int puntos, int dinero) {
        sumarPuntaje(puntos);
        sumarDinero(dinero);
    }

    public int getDinero() {return dinero;}
    public int getPuntaje() {return puntaje;}
}
