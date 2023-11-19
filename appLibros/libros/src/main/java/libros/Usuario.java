package libros;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvValidationException;

/**
 * Representa el Usuario del programa.
 */
public class Usuario {
    private String usuario;
    private String contraseña;
    private String plan;

    private List<Libro> librosPrestados;
    private List<Revista> revistasPrestadas;
    

    // modo Préstamo
    private int plazoMaximoPrestamo;
    private String horarioEntrega;
    private Sucursal sucursalRecogida;
    private boolean entrega24;
    private DireccionEnvio direcciónEnvío;


    /**
     * Constructor.
     * @param usuario
     * @param contraseña
     * @param plan
     */
    public Usuario(String usuario, String contraseña, String plan) {
        this.usuario = usuario;
        this.contraseña = contraseña;
        this.plan = plan;
        this.librosPrestados = new ArrayList<>();
        this.revistasPrestadas = new ArrayList<>();

        this.plazoMaximoPrestamo = ( plan.equalsIgnoreCase("Premium")) ?  50 : 30 ;
        this.horarioEntrega = "";
        this.sucursalRecogida = null;
        this.direcciónEnvío = null;
        this.entrega24 = false;
    }

    // getters y setters

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getContraseña() {
        return contraseña;
    }

    public void setContraseña(String contraseña) {
        this.contraseña = contraseña;
    }

    public String getPlan() {
        return plan;
    }

    public void setPlan(String plan) {
        this.plan = plan;
    }

    public List<Libro> getLibrosPrestados() {
        return librosPrestados;
    }

    public void prestarLibro(Libro libro) {
        librosPrestados.add(libro);
    }

    // para libros ----
    public int getCantidadLibrosPrestados(){
        return librosPrestados.size();

    }

    public void agregarLibroPrestado(Libro libro){
        librosPrestados.add(libro);
    }
    // ---- 

    // para revistas ----
    public List<Revista> getRevistasPrestadas() {
        return revistasPrestadas;
    }
    public int getCantidadRevistasPrestadas(){
        return revistasPrestadas.size();
    }

    public void agregarRevistaPrestada(Revista revista){
        revistasPrestadas.add(revista);
    }
    // ----


    // vaciar lista ----
    public void vaciarLista(){
        librosPrestados.clear();
        revistasPrestadas.clear();
    }
    //----

    // modo préstamo
    public void setHorarioEntrega(String horarioEntrega) {
        this.horarioEntrega = horarioEntrega;
    }

    public void setSucursalRecogida(Sucursal sucursalRecogida) {
        this.sucursalRecogida = sucursalRecogida;
    }

    public void setDirecciónEnvío(DireccionEnvio direcciónEnvío) {
        this.direcciónEnvío = direcciónEnvío;
    }

    public void setEntrega24(boolean entrega24) {
        this.entrega24 = entrega24;
    }

    public DireccionEnvio getDirecciónEnvío() {
        return direcciónEnvío;
    }
    
    // para plazo máx
    public int getPlazoMaximoPrestamo() {
        return this.plazoMaximoPrestamo;
    }

    // para la lista de prestamos 
    public List<Prestamo> getPrestamos(){
        List<Prestamo> prestamos = new ArrayList<>();
        prestamos.addAll(librosPrestados );
        prestamos.addAll(revistasPrestadas );

        return prestamos;
    }
    

    // modo perfil -----------------------------
    public void cambiarTipoCliente(String nuevoPlan) {
        this.plan = nuevoPlan;
        // actualizar el plazo máximo de préstamo según el nuevo plan
        plazoMaximoPrestamo = nuevoPlan.equalsIgnoreCase("Premium")  ? 50  : 30 ;
    
    }

    public void aplicarCuponPremium() {
        if(this.plan.equals("Premium" )){
            plazoMaximoPrestamo += 15;
            System.out.println( "\nCupón aplicado.");
        }
        else{
            System.out.println("\nEl cupón solo aplica para clientes Premium");
        }
    }

    // -------- cambiar la contraseña -----
    public void cambiarContraseña(String nuevaContraseña) {
        String archivoCSV = "usuarios.csv";
        List<String[]> registros = new ArrayList<>();
        String[] linea;

        // leer el archivo y guardar los registros
        try (CSVReader reader = new CSVReader(new FileReader(archivoCSV))){
            while((linea = reader.readNext()) != null){
                
                if(linea[0].equals(getUsuario())){
                    linea[1] = nuevaContraseña; 
                }
                registros.add(linea);
            }
        } 
        catch (CsvValidationException  | IOException e){
            e.printStackTrace(); 
            return; 
        }

        // escribir el archivo CSV  los datos actualizados
        try(CSVWriter writer = new CSVWriter(new FileWriter(archivoCSV))){
            for(String[] registro : registros) {
                writer.writeNext(registro);
            }
        } 
        catch (IOException e) {
            e.printStackTrace(); 
        }

        this.contraseña = nuevaContraseña;
    }

    // para verificar la contraseña 
    public boolean verificarContraseña(String contraseña) {
        return this.contraseña.equals(contraseña);
    }
    
}

