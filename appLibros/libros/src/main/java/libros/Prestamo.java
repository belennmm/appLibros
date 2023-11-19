package libros;
import java.time.LocalDate;

/**
 * Representa los Prestamos.
 */
public abstract class Prestamo{
    private String nombre;
    private String autor;
    private LocalDate fechaPrestamo;

    /**
     * Constructor.
     * @param nombre
     * @param autor
     */
    public Prestamo(String nombre, String autor) {
        this.nombre = nombre;
        this.autor = autor;
        this.fechaPrestamo = LocalDate.now();
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

    public LocalDate getFechaPrestamo() {
        return fechaPrestamo;
    }

    public void setFechaPrestamo(LocalDate fechaPrestamo) {
        this.fechaPrestamo = fechaPrestamo;
    }

    
}

