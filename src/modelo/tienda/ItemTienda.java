package modelo.tienda;

/** Lo que se puede comprar. Cada item guarda sus precios en un arreglo, uno por nivel. */
public enum ItemTienda {
    // Consumibles
    SALUD("Reparacion (+30 salud)", true, 30),
    REPARACION_TOTAL("Reparacion total", true, 80),
    COMBUSTIBLE("Combustible (+50)", true, 25),
    MUNICION("Municion (+10 bombas)", true, 20),
    // Mejoras//
    MEJORA_SALUD_MAX("Salud maxima +25", false, 60, 120, 200),
    MEJORA_CAP_MUNICION("Capacidad de bombas +10", false, 40, 80, 150),
    MEJORA_CAP_COMBUSTIBLE("Tanque de combustible +50", false, 40, 80, 150),
    MEJORA_VEL_BOMBAS("Bombas mas rapidas (+25%)", false, 50, 100, 200);



    private final String nombre;
    private final boolean repetible;
    private final int[] preciosPorNivel;

    ItemTienda(String nombre, boolean repetible, int... preciosPorNivel) {
        this.nombre = nombre;
        this.repetible = repetible;
        this.preciosPorNivel = preciosPorNivel;
    }

    public int getPrecio(int vecesComprado) {
        if (repetible) return preciosPorNivel[0];
        return preciosPorNivel[vecesComprado];
    }

    public boolean estaAgotado(int vecesComprado) {
        return !repetible && vecesComprado >= preciosPorNivel.length;
    }

    public int getNivelMaximo() {return preciosPorNivel.length;}

    public boolean esMejora() {return !repetible;}
    public String getNombre() { return nombre; }
}