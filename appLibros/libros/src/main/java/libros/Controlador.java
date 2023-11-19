package libros;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.io.FileReader;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvValidationException;

import java.util.ArrayList; 

/**
 * Representa el controlador del programa.
 */
public class Controlador {
    private List<Usuario> listaUsuarios;
    private List<Sucursal> listaSucursales;
    private Usuario usuarioActual; 
    

    public Controlador(){
        this.listaSucursales = new ArrayList<>();
        this.listaUsuarios = new ArrayList<>();
    }

    /**
     * Registra el usuario.
     * @param usuario
     * @param contraseña
     * @param plan
     * @return
     */
    public boolean registrarUsuario(String usuario, String contraseña, String plan){
        // crea un nuevo usuario
        Usuario nuevoUsuario = new Usuario(usuario, contraseña, plan);
        // agrega el nuevo usuario a la lista
        listaUsuarios.add(nuevoUsuario);
        // se establece
        this.usuarioActual = nuevoUsuario;
        return true;
    }

    public List<Usuario> getListaUsuarios(){
        return listaUsuarios;
    }
    

    /**
     * @param usuario
     * @return
     */
    public boolean usuarioExistente(String usuario){
        // cargar usuarios del csv 
        if(listaUsuarios.isEmpty()) {
            try{
                listaUsuarios = cargarUsuariosCSV("usuarios.csv");
            } 
            catch ( IOException | CsvValidationException e){
                System.out.println("No se han podido cargar los datos. " + e.getMessage());
            }
        }
    
        // ver si el usuario ya existe en la lista
        for(Usuario user : listaUsuarios){
            if(user.getUsuario().equals(usuario)){
                System.out.println("");
                return true;
            }
        }
        return false;
    }

    /**
     * Establecer usuario actual.
     * @param nombreUsuario
     */
    public void putUsuarioActual(String nombreUsuario) {
        for (Usuario u : listaUsuarios) {
            if (u.getUsuario().equals(nombreUsuario)) {
                usuarioActual = u;
                break;
            }
        }
    }

    public Usuario getUsuarioActual() {
        return usuarioActual;
    }

    // ____________ MODO SELECCIÓN _______________

    /**
     * Agregar Libro
     * @param usuarioActual
     * @param nombreLibro
     * @param autorLibro
     */
    public void agregarLibro(Usuario usuarioActual, String nombreLibro, String autorLibro) {
    
        // ver el límite de libros dado el user
        int limiteLibros = usuarioActual.getPlan().equalsIgnoreCase("Premium") ? 5 :  3 ;
    
        // si ya alcanzó el máximo de selecciones
        if(usuarioActual.getCantidadLibrosPrestados() >= limiteLibros ){
            System.out.println( "Ha alcanzado el límite de libros prestados, no puede prestar más libros.");
            return;
        }
    
        // un nuevo libro
        Libro nuevoLibro = new Libro(nombreLibro, autorLibro);
        // add el libro a la lista de libros  del user
        usuarioActual.agregarLibroPrestado(nuevoLibro);
        registrarPrestamoCsv(usuarioActual);
        System.out.println("\nLibro '" + nuevoLibro.getNombre() + "' agregado a " + usuarioActual.getUsuario() + "."); 
    }
  
    /**
     * Agregar Revista.
     * @param usuarioActual
     * @param nombreRevista
     * @param autorRevista
     */
    public void agregarRevista(Usuario usuarioActual, String nombreRevista, String autorRevista){
        // nueva revista
        Revista nuevaRevista = new Revista(nombreRevista, autorRevista);
        // se agrega la revista al user
        usuarioActual.agregarRevistaPrestada(nuevaRevista);

        System.out.println("\nRevista '" +  nuevaRevista.getNombreRevista() + "' agregada a " + usuarioActual.getUsuario() + ".");
        registrarPrestamoCsv(usuarioActual);
    }

    // ----- vaciar la lista 
    public void vaciarLista() {
        usuarioActual.vaciarLista();
    
        System.out.println("Se ha vaciado la lista de libros y revistas de " + usuarioActual.getUsuario() + ".");
        registrarPrestamoCsv(usuarioActual);
    }


    // ____________ MODO PRÉSTAMO _______________
    
    /**
     * Seleccionar sucursal.
     * @return
     */
    public Sucursal seleccionarSucursal() {
        Scanner scan = new Scanner(System.in);
    
        System.out.println("\nSeleccione una sucursal para recoger el préstamo:");
        List<Sucursal> sucursales = getListaSucursales();

        // verificar que existen sucursales en la lista 
        if(sucursales.isEmpty()){
            System.out.println("No hay sucursales disponibles.");
            return null; 
        }

        for(int i = 0 ; i <  sucursales.size() ;  i++ ){
            System.out.println((i + 1) + ". " +   sucursales.get(i).getNombre() );
        }
    
        int op1;
        do{
            System.out.print("\nIngrese el número de la sucursal que desee: ");
            while(!scan.hasNextInt() ){
                System.out.println("Ingrese un número válido.");
                scan.next();
            }
            op1 = scan.nextInt();
    
            if(op1 < 1 || op1 > sucursales.size() ){
                System.out.println( "La opción ingresada no es válida.");
            }
        } 
        while(op1 < 1 || op1 > sucursales.size()) ;
    
        return sucursales.get(op1 -  1);
    }

    public void agregarSucursal() {
        Scanner scan = new Scanner(System.in);
    
        System.out.print("Ingrese el nombre de la nueva sucursal: ");
        String nombreSucursal = scan.nextLine();
    
        Sucursal nuevaSucursal = new Sucursal(nombreSucursal);
        listaSucursales.add(nuevaSucursal);
    
        System.out.println("Sucursal '" + nombreSucursal + "' agregada.");
    }
    
    
    public List<Sucursal> getListaSucursales() {
        return listaSucursales;
    }
    
    // seleccionar dirección de envío
    public DireccionEnvio selectDireccionEnvio() {
        Scanner scan = new Scanner(System.in);

        // lista de direcciones de envío 
        List<DireccionEnvio> direccionesEnvio = obtainDireccionesEnvioUser();

        if(direccionesEnvio.isEmpty()){
            System.out.println("No ha ingresado ninguna dirección de envío.");
            return null;
        }

        // mostrar las direcciones de envío 
        System.out.println("\nDirecciones de envío ingresadas por el usuario:");
        for(int i = 0 ;  i < direccionesEnvio.size() ;  i++){
            System.out.println(( i + 1 ) + ". " + direccionesEnvio.get(i).getDirección() );
        }

        int op2;
        do{
            System.out.print("Ingrese el número de la dirección de envío que desee: ");
            while( !scan.hasNextInt()){
                System.out.println("Ingrese un número válido.");
                scan.next();
            }
            op2  = scan.nextInt();

            if(op2 < 1 || op2 > direccionesEnvio.size()){
                System.out.println("La opción ingresada no es válida" );
            }
        } 
        while( op2 < 1  || op2 > direccionesEnvio.size());

        // obtiene la dirección de envío seleccionada
        DireccionEnvio direcciónEnvíoSeleccionada  = direccionesEnvio.get(op2 - 1) ;
        return direcciónEnvíoSeleccionada;
    }

    // obtener direcciones de envío ingresadas por el usuario
    private List<DireccionEnvio> obtainDireccionesEnvioUser(){
        Scanner scan = new Scanner(System.in);
        List<DireccionEnvio> direccionesEnvio =  new ArrayList<>();

        System.out.println("\nIngrese las direcciones de envío (ingrese ' fin ' si desea terminar):");

        String direccion;
        while(true){
            System.out.print("Dirección: ");
            direccion = scan.nextLine();

            if(direccion.equalsIgnoreCase("fin")){
                break;
            }

            DireccionEnvio direcciónEnvío  = new DireccionEnvio(direccion);
            direccionesEnvio.add(direcciónEnvío);
        }

        return direccionesEnvio;
    }
    
    // para plazo máximo
    public int getPlazoMaximoPrestamo(){
        if(usuarioActual == null){
            return 0; 
        }
        return usuarioActual.getPlazoMaximoPrestamo();
    }

    // para imprimir el listado de préstamos
    public void imprimirListadoPrestamo() {
    
        System.out.println("\nListado de Préstamos para el usuario: " + usuarioActual.getUsuario());
    
        //  la lista de préstamos del usuario actual
        List<Prestamo> prestamos = usuarioActual.getPrestamos();
    
        // mostrar detalles de cada préstamo
        for(Prestamo prestamo :  prestamos){
            System.out.println("Nombre: " + prestamo.getNombre());
            System.out.println("Autor: " + prestamo.getAutor());
            System.out.println("Fecha de Préstamo: " + prestamo.getFechaPrestamo());
            System.out.println( "--------------------" );
        }
    }
    
    // -------------------------------- CSV ---------------------------------------
    // NOTA:
    // Para realizar el siguiente fragmento de código se hizo uso de un ejemplo brindado por la IA de ChatGPT para utilizarlo como referencia.
    // Con el apoyo de la referencia del siguiente código se realizó la estructura lógica para cargar los usuarios del CSV.
    // OpenAI.: Crea una función en Java llamada cargarUsuariosCSV, para cargar información de usuarios desde un archivo csv y retornar una lista de objetos Usuario. 
    //La función debe aceptar un parámetro que representa la ruta del archivo csv. Usa las excepciones IOException. Para cada línea de 3 elementos, 
    //extrae el nombre de usuario, la contraseña y el plan, crea un objeto Usuario con esto, la función devuelve la lista de usuarios. [consultado el 18/11/2023].

  
    //* @param archivoCSV Ruta del archivo CSV que contiene la información de los usuarios.
    //* @return Lista de objetos Usuario cargados desde el archivo CSV.
    //* @throws IOException Excepción lanzada en caso de error de lectura del archivo.
    //*/
    //private List<Usuario> cargarUsuariosCSV(String archivoCSV) throws IOException {
    //    List<Usuario> usuarios = new ArrayList<>();

    //    try (CSVReader reader = new CSVReader(new FileReader(archivoCSV))) {
    //        String[] linea;

    //        while ((linea = reader.readNext()) != null && linea.length == 3) {
    //            String nombreUsuario = linea[0];
    //            String contraseña = linea[1];
    //            String plan = linea[2];

    //            Usuario usuario = new Usuario(nombreUsuario, contraseña, plan);
    //            usuarios.add(usuario);
    //        }
    //    }

    //    return usuarios;
    //}

    /**
     * Cargar usuarios desde CSV.
     * @param archivoCSV
     * @return
     * @throws IOException
     * @throws CsvValidationException
     */
    private List<Usuario> cargarUsuariosCSV( String archivoCSV) throws IOException, CsvValidationException{
        List<Usuario> usuarios = new ArrayList<>();
    
        try(CSVReader reader = new CSVReader( new FileReader(archivoCSV))){
            String[] linea;
            while((linea = reader.readNext()) != null) {
                
                if(linea.length >= 3){
                    String nombreUsuario = linea[0];
                    String contraseña = linea[1];
                    String plan = linea[2];
    
                    Usuario usuario = new Usuario(nombreUsuario, contraseña, plan);
                    usuarios.add(usuario);
                } 
                else {
                    System.out.println("");
                }
            }
        } 
        catch (CsvValidationException e) {
            System.out.println("Error de validación." + e.getMessage());
        }
    
        return usuarios;
    }

    /**
     * Modificar tipo cliente
     * @param nuevoPlan
     */
    public void modificarTipoDeCliente(String nuevoPlan) {
        String archivoCSV = "usuarios.csv";
        List<String[]> registros = new ArrayList<>();
        String[] linea;

        // leer el archivo csv y guardar los registros
        try (CSVReader reader = new CSVReader(new FileReader(archivoCSV))) {
            while ((linea = reader.readNext()) != null) {
                if (linea[0].equals(usuarioActual.getUsuario())) {
                    linea[2] = nuevoPlan; 
                }
                registros.add(linea);
            }
        } catch (CsvValidationException | IOException e) {
            e.printStackTrace(); 
            return; 
        }

        // escribir en archivo los datos actualizados
        try (CSVWriter writer = new CSVWriter(new FileWriter(archivoCSV))) {
            for (String[] registro : registros) {
                writer.writeNext(registro);
            }
        } catch (IOException e) {
            e.printStackTrace(); 
        }

        // Actualizar el plan en el usuario actual en memoria
        usuarioActual.setPlan(nuevoPlan);
    }

    /**
     * Registrar préstamo en csv
     * @param usuario
     */
    public void registrarPrestamoCsv(Usuario usuario){
        String archivoPrestamosCSV = "prestamos.csv";
        File archivo = new File(archivoPrestamosCSV);
    
        // ver si el archivo ya existe con encabezados
        boolean escribirEncabezados = !archivo.exists()  || archivo.length() == 0 ;
    
        List<String[]> registros = new ArrayList<>();
        try (CSVReader reader = new CSVReader(new FileReader(archivoPrestamosCSV))) {
            String[] linea;
            while ((linea = reader.readNext()) != null) {
                if (!linea[0].equals(usuario.getUsuario())) {
                    registros.add(linea);
                }
            }
        } catch (IOException | CsvValidationException e) {
            System.out.println("Error al leer el archivo de préstamos: " + e.getMessage());
            e.printStackTrace();
        }
    
        // agregar los préstamos del usuario 
        List<Prestamo>  prestamos = usuario.getPrestamos();
        for(Prestamo prestamo : prestamos){
            String[] registro = {
                usuario.getUsuario(),
                prestamo instanceof  Libro ? "Libro" : "Revista",
                prestamo.getNombre(),
                prestamo.getAutor(),
                prestamo.getFechaPrestamo().toString()
            };
            registros.add(registro);
        }

        try (CSVWriter writer = new CSVWriter(new FileWriter(archivoPrestamosCSV))) {
            // escribir encabezados  si no hay encabezados
            if (escribirEncabezados){
                String[] encabezados = { "Nombre Usuario", "Tipo", "Nombre", "Autor", "Fecha de Préstamo" };
                writer.writeNext(encabezados);
            }
            
            for(String[] registro : registros){
                writer.writeNext(registro);
            }
        } 
        catch (IOException e) {
            System.out.println("Error al escribir en el archivo de préstamos: " + e.getMessage() );
            e.printStackTrace();
        }
    }
    
    // NOTA:
    // Para realizar el siguiente fragmento de código se hizo uso de un ejemplo brindado por la IA de ChatGPT.
    // Con el apoy del ejemplo del siguiente código se realizó la estructura lógica para cargar los préstamos del usuarios del CSV.
    // OpenAI.: Brinda un código en java para que cargue los préstamos de un usuario desde un archivo csv cuando el usuario inicie sesión, 
    // buscar en el archivo las entradas correspondientes al usuario y cargar las listas de libros y revistas prestadas en el objeto Usuario. [consultado el 18/11/2023].


    // public void cargarPrestamosDeUsuario(Usuario usuario) {
    //     String archivoPrestamosCSV = "prestamos.csv";
    
    //     try (CSVReader reader = new CSVReader(new FileReader(archivoPrestamosCSV))) {
    //         String[] linea;
    
    //         // Limpiar las listas actuales de préstamos del usuario
    //         usuario.getLibrosPrestados().clear();
    //         usuario.getRevistasPrestadas().clear();
    //         reader.readNext();
    
    //         // Leer cada línea y cargar los préstamos correspondientes al usuario
    //         while ((linea = reader.readNext()) != null) {
    //             if (linea[0].equals(usuario.getUsuario())) {
    //                 String tipo = linea[1];
    //                 String nombre = linea[2];
    //                 String autor = linea[3];
    //                 String fechaPrestamo = linea[4];
    
    //                 if (tipo.equals("Libro")) {
    //                     Libro libro = new Libro(nombre, autor); // Asumiendo que Libro tiene un constructor con nombre y autor
    //                     libro.setFechaPrestamo(LocalDate.parse(fechaPrestamo)); // Asumiendo que hay un método setFechaPrestamo
    //                     usuario.agregarLibroPrestado(libro);
    //                 } else if (tipo.equals("Revista")) {
    //                     Revista revista = new Revista(nombre, autor); // Asumiendo que Revista tiene un constructor con nombre y autor
    //                     revista.setFechaPrestamo(LocalDate.parse(fechaPrestamo)); // Asumiendo que hay un método setFechaPrestamo
    //                     usuario.agregarRevistaPrestada(revista);
    //                 }
    //             }
    //         }
    //     } catch (IOException | CsvValidationException e) {
    //         System.out.println("Error al cargar los préstamos desde el archivo CSV: " + e.getMessage());
    //         e.printStackTrace();
    //     }
    // }
    
    
    /**
     * Cargar registro de csv
     * @param usuario
     */
    public void loadPrestamosUser(Usuario usuario) {
        String archivoPrestamosCSV = "prestamos.csv";

        try(CSVReader reader = new CSVReader(new FileReader(archivoPrestamosCSV))){
            String[] linea;

            // limpiar las listas de préstamos 
            usuario.getLibrosPrestados().clear();
            usuario.getRevistasPrestadas().clear();

            // omitir encabezados
            reader.readNext();

            // leer cada línea y cargar los préstamos 
            while((linea = reader.readNext()) != null) {
                if(linea[0].equals(usuario.getUsuario())) {
                    String tipo = linea[1];
                    String nombre = linea[2];
                    String autor = linea[3];
                    String fechaPrestamo = linea[4];

                    if (tipo.equals("Libro")) {
                        Libro libro = new Libro(nombre, autor); 
                        libro.setFechaPrestamo(LocalDate.parse(fechaPrestamo)); 
                        usuario.agregarLibroPrestado(libro);
                    } else if (tipo.equals("Revista")) {
                        Revista revista = new Revista(nombre, autor); 
                        revista.setFechaPrestamo(LocalDate.parse(fechaPrestamo)); 
                        usuario.agregarRevistaPrestada(revista);
                    }
                }
            }
        } catch (IOException | CsvValidationException e) {
            System.out.println("Error al cargar los préstamos del archivo " + e.getMessage());
            e.printStackTrace();
        }
    }

}
