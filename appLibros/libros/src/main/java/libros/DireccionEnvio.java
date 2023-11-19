package libros;

/**
 * Representa la Dirección de Envío.
 */
public class DireccionEnvio {
    private String direccion;

    /**
     * Constructor.
     * @param direccion
     */
    public DireccionEnvio(String direccion) {
        this.direccion = direccion;
  
    }


    // getters y setters
    public String getDirección() {
        return direccion;
    }


    public void setDirección(String direccion) {
        this.direccion = direccion;
    }

}