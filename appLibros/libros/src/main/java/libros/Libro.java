package libros;

/**
 * Representa el Libro como herencia de Préstamo.
 */
public class Libro extends Prestamo {
    private String nombre;
    private String autor;

    /**
     * Constructor.s
     * @param nombre
     * @param autor
     */
    public Libro(String nombre, String autor) {
        super(nombre, autor);
        this.nombre = nombre;
        this.autor = autor;
    }

    // getters y setters
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }
}
