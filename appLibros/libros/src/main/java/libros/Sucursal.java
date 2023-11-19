package libros;

/**
 * Representa la Sucursal.
 */
public class Sucursal {
    private String nombre;
   
    /**
     * Constructor.
     * @param nombre
     */
    public Sucursal(String nombre) {
        this.nombre = nombre;
        
    }

    // getters y setters
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

}
