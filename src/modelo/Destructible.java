package modelo;

public interface Destructible {

    /**
     * Resta d puntos de vida. La vida nunca queda negativa.
     *
     * @param d cantidad de dano, mayor o igual a 0
     * @throws IllegalArgumentException si d es negativo
     */
    void recibirDanio(int d);

    boolean estaDestruido();
}