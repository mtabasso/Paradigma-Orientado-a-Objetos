package modelo.tienda;

public class DineroInsuficienteException extends Exception {
    private final int precio;
    private final int disponible;

    public DineroInsuficienteException(int precio, int disponible) {
        super("Dinero insuficiente: cuesta " + precio + " y tenes " + disponible);
        this.precio = precio;
        this.disponible = disponible;
    }

    public int getFaltante() { return precio - disponible; }
}