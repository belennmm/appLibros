package libros;

/**
 * Representa la Revista como herencia de Préstamo.
 */
public class Revista extends Prestamo{
    private String nombreRevista;
    private String autorRevista;

    /**
     * Constructor.
     * @param nombreRevista
     * @param autorRevista
     */
    public Revista(String nombreRevista, String autorRevista) {
        super(nombreRevista, autorRevista);
        this.nombreRevista = nombreRevista;
        this.autorRevista = autorRevista;
    }

    // getters y setters
    public String getNombreRevista() {
        return nombreRevista;
    }

    public void setNombreRevista(String nombreRevista) {
        this.nombreRevista = nombreRevista;
    }

    public String getAutorRevista() {
        return autorRevista;
    }

    public void setAutorRevista(String autorRevista) {
        this.autorRevista = autorRevista;
    }

    
}
