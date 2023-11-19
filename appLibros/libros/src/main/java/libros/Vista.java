package libros;

import java.util.Scanner;

import com.opencsv.CSVWriter;

import kotlin.jvm.Strictfp;

import java.util.List;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Representa la vista dle programa.
 */
public class Vista {
    private Controlador controlador;
    private Usuario usuarioActual;
    private List<Usuario> listaUsuarios;    

    public Vista(){
        this.controlador = new Controlador();
        this.usuarioActual = null;
        this.listaUsuarios = new ArrayList<>();
    }

    // -------------- MENÚ INICIO -----------------
    public void show_and_ask(){
        Scanner scan = new Scanner(System.in); // neuva instancia del scanner
        String op1;

        do{
                System.out.println("\n||    LIBROS    ||");
                System.out.println("1. Modo Registro");
                System.out.println("2. Ingresar");
                System.out.println("3. Salir");

            System.out.print("Seleccione una opción: ");
            op1 = scan.nextLine();

            switch (op1) {
                case "1":
                    modoRegistro() ;
                    break;

                case "2" :
                    ingresar() ;
                    break;

                case "3":
                    System.out.println("---");
                    return; 
                default:
                    System.out.println( "La opción ingresada no es válida.");
            }
        }
        while (!op1.equals("3"));
    }    

    // ---- Para ingresar -----
    private void ingresar(){
        Scanner scan = new Scanner(System.in);

        System.out.print("Ingrese su nombre de usuario: ");
        String nameUser = scan.nextLine();
        System.out.print("Ingrese su contraseña: ");
        String contraseña = scan.nextLine();

        // verificación de los datos para el ingreso
        if(verifyData(nameUser, contraseña)){
            controlador.putUsuarioActual(nameUser);
            usuarioActual  = controlador.getUsuarioActual();
            controlador.loadPrestamosUser(usuarioActual) ;
            System.out.println("\n¡Bienvenidx " +  nameUser  + "!\n" );
            show_menu_userRegistrado();
        } 
        else{
            System.out.println("\nError al iniciar sesión, verifique sus datos de ingreso.");
        }
    }
    
    //-------------------REGISTRAR USUARIO--------------------
    private void modoRegistro() {
        Scanner scan = new Scanner(System.in);
    
        System.out.print("Ingrese un nombre de usuario: ");
        String usuario =  scan.nextLine();

        if(controlador.usuarioExistente(usuario)){ // si el nombre de usuario se encuentra ya en existencia
            System.out.println("El nombre de usuario ya existe, por favor seleccione uno diferente.");
            return;
        }
    
        System.out.print("Ingrese una contraseña: ");
        String  contraseña = scan.nextLine();
    
        // pedir plan (base o premium) y verificar que esté correcto
        System.out.print("Seleccione un plan ( Base / Premium ): ");
        String plan  = scan.nextLine();

        if(!plan.equalsIgnoreCase("Base") && !plan.equalsIgnoreCase("Premium")){
            System.out.println("Ingrese una opción válida (Base o Premium).");
            return;
        }
    
        // guardar el usuario
        boolean okRegistro = controlador.registrarUsuario(usuario, contraseña, plan);
    
        // guardar datos en csv si se registro correctamente
        if(okRegistro){
            saveIncsv(usuario, contraseña , plan);
            System.out.println("Se ha registrado el usuario.");

            // guardar usuarioActual como el ingresado
            this.usuarioActual = new Usuario(usuario, contraseña, plan);
    
        } 
        else{
            System.out.println( "Error al realizar el registro.");
        }
    }
    
    /**
     * Guardar el usuario en el csv.
     * @param usuario
     * @param contraseña
     * @param plan
     */
    private void saveIncsv(String usuario, String contraseña, String plan) {
        String csvName = "usuarios.csv";
    
        try(CSVWriter writer  = new CSVWriter(new FileWriter(csvName, true)) ){
            // Agregar nueva línea al CSV
            writer.writeNext(new String[]{usuario , contraseña, plan});
        } 
        catch (IOException e) {
            System.out.println("Error al guardar datos.");
            e.printStackTrace();
        }
    }

    //--------- verificación de usuario ---------
    /**
     * @param nameUser
     * @param contraseña
     * @return
     */
    private boolean verifyData(String nameUser, String contraseña) {
        // buscar si el usuario existe
        if(controlador.usuarioExistente(nameUser) ) {
            // buscar si hay coincidencia
            for(Usuario user : controlador.getListaUsuarios()) {
                if(user.getUsuario().equals(nameUser) && user.getContraseña().equals(contraseña ) ){
                    usuarioActual = user;
                    return true;
                }
            }
        }
        // si no hay coincidencia
        System.out.println("Error: usuario no encontrado o datos de ingreso incorrectos.");
        return false;
    }

    // --------------------MENÚ DE USUARIO REGISTRADO -----------------------------
    private void show_menu_userRegistrado(){
        Scanner scan = new Scanner(System.in);
        String op2;

        do{
             System.out.println("\n||    LIBROS    ||");
            System.out.println("1. Modo Selección");
            System.out.println("2. Modo Préstamo");
            System.out.println("3. Modo Perfil") ;
            System.out.println("4. Salir");

            System.out.print("Seleccione una opción: ") ;
            op2 = scan.nextLine();

            switch(op2){
                case "1":
                    modoSeleccion();
                    break;
                case "2" :
                    modoPrestamo();
                    break;
                case "3":
                    modoPerfil();
                    break;
                case "4" :
                    System.out.println("---");
                    return;
                default:
                    System.out.println( "La opción ingresada no es válida.");
            }
        } 
        while(!op2.equals("4") );
    }
    
    //--------------------- OPCIONES PARA EL MODO SELECCIÓN --------------------------
    private void modoSeleccion() {
        Scanner scan =  new Scanner(System.in);

        System.out.println("\no---- Modo Selección: " +  usuarioActual.getUsuario() + " ----o");
        String op3;

        do{
            System.out.println("1. Agregar Libro");
            System.out.println("2. Agregar Revista");
            System.out.println("3. Vaciar Lista");
            System.out.println("4. Volver al Menú Principal");

            System.out.print("Seleccione una opción: ");
            op3 = scan.nextLine();

            switch (op3) {
                case "1":
                    agregarLibro();
                    break;
                case "2":
                    agregarRevista();
                    break;
                case "3":
                    vaciarLista();
                    break;
                case "4":
                    System.out.println(" ←-");
                    break;
                default:
                    System.out.println( "La opción ingresada no es válida.");
            }
        } 
        while (!op3.equals("4"));
    }

    //     //   AGREGAR LIBROS  //     // 
    private void agregarLibro() {
        Scanner scan = new Scanner(System.in);
        System.out.print("Ingrese el nombre del libro: " );
        String nombreLibro = scan.nextLine() ;
        System.out.print("Ingrese el autor del libro: " );
        String autorLibro = scan.nextLine();
    
        // agregar el libro al controlador
        controlador.agregarLibro(usuarioActual, nombreLibro , autorLibro);
    }
    
    //     //   AGREGAR REVISTAS  //     // 
    private void agregarRevista() {
        Scanner scan = new Scanner(System.in);
        System.out.print("Ingrese el nombre de la revista: " );
        String nombreRevista = scan.nextLine() ;
        System.out.print("Ingrese el autor de la revista: " );
        String autorRevista = scan.nextLine();

        // agregar revistas al controlador
        controlador.agregarRevista(usuarioActual, nombreRevista , autorRevista);
    }

    //     //   VACIAR LISTA  //     // 
    private void vaciarLista(){
        controlador.vaciarLista();
        System.out.println("La lista de selección se ha vaciado.");
    }

    //--------------------- OPCIONES PARA EL MODO PRÉSTAMO --------------------------
    private void modoPrestamo(){
        Scanner scan = new Scanner(System.in);

        // si la selección está vacía no se puede continuar
        if(usuarioActual.getLibrosPrestados().isEmpty() && usuarioActual.getRevistasPrestadas().isEmpty()){
            System.out.println("No tiene libros o revistas que puedan ser prestados");
            return;
        }
    
        System.out.println("\no---- Modo Préstamo" + usuarioActual.getUsuario() + " ----o");
        // el plazo máx según el tipo de usuario
        int plazoMax =controlador.getPlazoMaximoPrestamo();
        int plazoEntrega =0;

        boolean si = false;
        while(!si){
            try{
                System.out.print("\nIngrese el plazo de entrega (días, máximo " + usuarioActual.getPlazoMaximoPrestamo() + "): ");
                plazoEntrega = scan.nextInt();
                if (plazoEntrega > plazoMax){
                    System.out.println("El plazo ingresado es mayor que el permitido") ;
                    return;
                }
                else{
                    si = true ;
                }
                
            } 
            catch(java.util.InputMismatchException e) {
                System.out.println( "El dato ingresado no es válido");
                scan.nextLine(); // limpiar para no tener problema
            }
        }     

        //-------- Para sucursal
        System.out.println("\n  ---Sucursal---");
        System.out.println("1. Agregar nueva sucursal");
        System.out.println("2. Seleccionar una sucursal existente");
        scan.nextLine(); // limpiar para no tener problema
        System.out.print("Seleccione una opción: ");
        String opSucursal = scan.nextLine();
        

        switch(opSucursal){
            case "1" :
                 controlador.agregarSucursal();
                break;
            case "2":
                Sucursal sucursalSeleccionada = controlador.seleccionarSucursal();
                if(sucursalSeleccionada == null){
                    System.out.println("No hay sucursales, no se puede continuar.");
                    return; // vuelve al menú 
                }
                usuarioActual.setSucursalRecogida(sucursalSeleccionada);
                break;
            default:
                System.out.println("Opción no válida.");
                return; // vuelve al menú 
        }

        //-------- Para dirección       
        // horario de entrega (solo para los premium)
        if(usuarioActual.getPlan().equalsIgnoreCase("Premium")){
            System.out.print("\nIngrese el horario de entrega (am/pm): ");
            String horarioEntrega = scan.next();

            switch(horarioEntrega){
                case "am":
                     usuarioActual.setHorarioEntrega(horarioEntrega);
                     System.out.println("Su horario de entrega será por la mañana. ");
                    break;
                case "pm":
                    
                     usuarioActual.setHorarioEntrega(horarioEntrega);
                     System.out.println("Su horario de entrega será por la tarde.");
                    break;
                default:
                    System.out.println("Opción no válida.");
                    return; 
            }
        }
    
        // sucursal para recoger el préstamo (solo para no premium)
        if (!usuarioActual.getPlan().equalsIgnoreCase("Premium")) {
            // llama al controlador para seleccionar la sucursal
            Sucursal sucursal = controlador.seleccionarSucursal();
            usuarioActual.setSucursalRecogida(sucursal);
        }
    
        // mostrar listado de préstamo
        System.out.println("\n  |--- Listado Préstamo:");
        controlador.imprimirListadoPrestamo();
    
        // Seleccionar dirección de envío (solo para usuarios premium)
        if (usuarioActual.getPlan().equalsIgnoreCase("Premium")) {
            // Llamar al controlador para seleccionar la dirección de envío
            DireccionEnvio direcciónEnvío = controlador.selectDireccionEnvio();
            usuarioActual.setDirecciónEnvío(direcciónEnvío);
        }
    
        // Definir si va a pasar por el préstamo a las 12 o 24 horas de haber realizado la solicitud (solo para usuarios no premium)
        if (!usuarioActual.getPlan().equalsIgnoreCase("Premium")) {
            System.out.print("\n\nSeleccione el horario de recogida ( 12/24 ) horas: ");
            String horarioRecogida = scan.nextLine();
            // switch para que no haya error por dato no válido
            switch (horarioRecogida) {
                case "12":
                    usuarioActual.setEntrega24(false);
                     System.out.println("Puede recoger su pedido en 12 horas. ");
                    break;
                case "24":
                    
                    usuarioActual.setEntrega24(true);
                     System.out.println("Puede recoger su pedido en 24 horas. ");
                    break;
                default:
                    System.out.println( "La opción ingresada no es válida.");
                    return; // back al menú
            }
        }
    }
    
    //--------------------- OPCIONES PARA EL MODO PERFIL --------------------------
    public void modoPerfil(){
        Scanner scan = new Scanner(System.in);
        System.out.println("\no---- Modo Perfil" + usuarioActual.getUsuario() + " ----o");
        System.out.println("a. Modificar tipo de usuario");
        System.out.println("b. Aplicar cupón de 15 días adicionales (Premium)");
        System.out.println("c. Cambiar contraseña");
        System.out.print("Seleccione una opción (a, b, c): ");
        String op4 = scan.nextLine();

        switch(op4.toLowerCase()){
            case "a":
                System.out.print("Ingrese el nuevo tipo de usuario (Base - Premium): ");
                String nuevoPlan = scan.nextLine();

                switch (nuevoPlan){
                case "Base":
                     usuarioActual.cambiarTipoCliente(nuevoPlan);
                     controlador.modificarTipoDeCliente(nuevoPlan);

                     System.out.println("Se ha modificado el tipo de usuario a: Cliente Base");
                    break;
                case "Premium":
                    
                     usuarioActual.cambiarTipoCliente(nuevoPlan);
                     controlador.modificarTipoDeCliente(nuevoPlan);
                      System.out.println("Se ha modificado el tipo de cliente a: Cliente Premium");
                    break;
                default:
                    System.out.println("Opción no válida.");
                    return; // Vuelve al menú principal
                }
            
                break;
            case "b":
                usuarioActual.aplicarCuponPremium();
                break;
            case "c":
                System.out.print("Ingrese la nueva contraseña: ");
                String nuevaContraseña = scan.nextLine();
                usuarioActual.cambiarContraseña(nuevaContraseña);
                System.out.println("La contraseña se ha actualizado.");
                break;
            default:
                System.out.println( "La opción ingresada no es válida.");
        }
    }
}
