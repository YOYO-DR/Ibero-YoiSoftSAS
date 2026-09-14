// package yoisoftsas;

import java.util.*;
import java.util.regex.Pattern;

/**
 *
 * @author yoiner
 */
public class YoiSoftSAS {

    // Arreglos con cantidades fijas
    static String[] areas = {"Administracion", "Contabilidad", "Sistemas", "Ventas", "Logistica"};
    static String[] prioridades = {"Alta", "Media", "Baja"};

    static String[] camposEmpleados = {"codigo", "nombre", "area", "correo"}; //Mapeo campos de los empleados

    // Arraylist porque no se cuantos empleados y equipos se van a utilizar
    static ArrayList<String[]> empleados = new ArrayList<>(); // Se va a guardar como {codigo, nombre, area, correo} de 0 a 3
    static ArrayList<String[]> equipos = new ArrayList<>(); // Se va a guardar como {codigo, tipo, marca, codigoEmpleado, estado} de 0 a 4

    // Equipos
    static String[] tiposEquipo = {"Computador", "Portatil", "Impresora", "Monitor", "Telefono"};
    static String[] estadosEquipo = {"Operativo", "En reparacion", "Fuera de servicio"};
    static String[] camposEquipos = {"codigo", "tipo", "marca", "codigoEmpleado", "estado"};

    // Estados de una solicitud
    static String[] estadosSolicitud = {"Pendiente", "En atencion", "Solucionada"};

    // Campos de cada solicitud
    static String[] camposSolicitud = {"codigo", "codigoEmpleado", "codigoEquipo", "descripcion", "prioridad", "estado"};

    // Cola de solicitudes pendientes
    static Queue<String[]> solicitudesPendientes = new LinkedList<>();

    // Pila de solicitudes solucionadas
    static Stack<String[]> solicitudesSolucionadas = new Stack<>();

    // Input Scanner para todo el sistema
    static Scanner sc = new Scanner(System.in);

    static void mostrarMenu() {
        // Funcion para mostrar el menu de opciones y nombre de la emprsa
        System.out.println();
        System.out.println("========================================================");
        System.out.println("                      YOISOFT SAS                       ");
        System.out.println("        SISTEMA DE SOPORTE TECNICO EMPRESARIAL          ");
        System.out.println("========================================================");
        System.out.println("1. Registrar empleado");
        System.out.println("2. Registrar equipo");
        System.out.println("3. Crear solicitud");
        System.out.println("4. Consultar registros");
        System.out.println("5. Atender siguiente solicitud");
        System.out.println("6. Mostrar solicitudes pendientes");
        System.out.println("7. Mostrar solicitudes solucionadas");
        System.out.println("8. Salir");
    }

    static int leerEntero(String mensaje){
        // Funcion para manerar el input de numeros enteros
        while (true) {
            // Con el while mantengo validando con el try, hasta que sea un numero entero lo ingresado
            System.out.print(mensaje);
            try {
                return Integer.parseInt(sc.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Error: Debe ingresar un numero entero.");
            }
        }
    }

    static String leerTexto(String mensaje){
        // Funcion para manejar el input de texto
        while (true){
            System.out.print(mensaje);
            String dato = sc.nextLine().trim(); // trim para quitar los espacios de los bordes
            if (dato.isEmpty()){
                System.out.println("Error: este campo no puede quedar vacio.");
                continue;
            }
            return dato;
        }
    }

    static String[] crearEmpleado(String codigo, String nombre, String area, String correo){
        // Funcion base para registrar el empleado, la funcion que lo llame debe validar los campos
        String[] empleado = {codigo,nombre,area,correo};
        empleados.add(empleado);
        return empleado;
    }

    // Muestra las opciones de un arreglo y devuelve la seleccionada, o "0" si cancela
    static String seleccionarOpcion(String titulo, String[] opciones) {
        while (true) {
            System.out.println(titulo);
            for (int i = 0; i < opciones.length; i++) {
                System.out.println("  " + (i + 1) + ". " + opciones[i]);
            }
            int opcion = leerEntero("Seleccione una opcion [0 para cancelar]: ");
            if (opcion == 0) {
                return "0";
            }
            if (opcion >= 1 && opcion <= opciones.length) {
                return opciones[opcion - 1];
            }
            System.out.println("Error: elija un numero entre 1 y " + opciones.length + ".");
        }
    }

    static String leerCorreo(String mensaje){
        // Funcion para leer un correo
        // Utilizo una expresion regular
        Pattern regex = Pattern.compile("^[\\w.+-]+@[\\w-]+\\.[\\w.-]+$");

        while (true) {
            String correo = leerTexto(mensaje);

            if (correo.equals("0")){
                return "0";
            }
            // Utilizo el regex apra validar el patron del correo
            if (regex.matcher(correo).matches()){
                return correo;
            }
            System.out.println("Error: correo invalido, intente de nuevo.");
        }
    }

    static void registrarEmpleado(){
        System.out.println("--- REGISTRAR EMPLEADO ---");
        System.out.println("[Escriba 0 en cualquier campo para cancelar la creacion]");

        String codigo;

        while (true){
            // Pido el codigo del empleado y valido que no exista en los registros
            codigo=leerTexto("Codigo: ").toUpperCase();
            if (codigo.equals("0")){
                System.out.println("Registro cancelado");
                return;
            }
            // Valido que el empleado con ese codigo no exista
            if (buscarEnLista(empleados, camposEmpleados,"codigo",codigo)!=null){
                System.out.println("Este codigo de empeado ya existe, por favor intente con otro codigo.");
                continue;
            }
            break;
        }

        String nombre = leerTexto("Nombre completo: ");
        if(nombre.equals("0")){
            System.out.println("Registro cancelado");
            return;
        }

        String area = seleccionarOpcion("Areas disponibles:", areas);
        if (area.equals("0")){
            System.out.println("Registro cancelado");
            return;
        }

        String correo = leerCorreo("Correo electronico: ");
        if (correo.equals("0")) {
            System.out.println("Registro cancelado.");
            return;
        }

        String[] nuevoEmpleado = crearEmpleado(codigo, nombre, area, correo);
        System.out.println("Empleado registrado correctamente.");
        System.out.println("Codigo: "+codigo+" | Nombre: "+nombre+" | Area: "+area+" | Correo: "+correo);

    }

    static String[] seleccionarEmpleado(){
        // Funcion para seleccionar un empleado, retorna su ID
        if (empleados.isEmpty()){
            System.out.println("No hay empleados registrados, registre un empleado primero.");
            return null;
        }
        while (true){
            System.out.println("Empleados registrados:");
            for (int i=0;i<empleados.size();i++){
                String[] empleado = empleados.get(i);
                System.out.println("  "+(i+1) +". "+empleado[0]+" - "+empleado[1]+" ("+empleado[2]+")");
            }
            int opcion = leerEntero("Seleccione el empleado responsable (0 para cancelar): ");
            if (opcion==0){
                return null;
            }
            if (opcion >=1 && opcion <= empleados.size()){
                return empleados.get(opcion-1); // retorno solo el codigo
            }
            if (empleados.size()==1){
                System.out.println("Error: debe seleccionar un empleado");
                continue;
            }
            System.out.println("Error: elija un numero entre 1 y "+empleados.size());
        }
    }

    static String[] crearEquipo(String codigo, String tipo, String marca, String codigoEmpleado, String estado){
        // Funcion base para registrar el equipo, la funcion que lo llame debe validar los campos
        String[] equipo = {codigo,tipo,marca,codigoEmpleado, estado};
        equipos.add(equipo);
        return equipo;
    }

    static ArrayList<String[]> buscarEnLista(ArrayList<String[]> lista, String[] campos, String campoBuscar, String valor){
        // Funcion para buscar un valor dentro de un arraylist por campo
        ArrayList<String[]> encontrados = new ArrayList<>();
        int idCampoBuscar = -1;
        for (int i = 0; i<campos.length; i++){
            if (campos[i].equals(campoBuscar)){
                idCampoBuscar=i;
                break; // salgo porque ya lo encontre
            }
        }
        if (idCampoBuscar==-1){
            return null; // Si no existe el campo, retorno null
        }
        for (int i =0; i < lista.size();i++){
            if (lista.get(i)[idCampoBuscar].equalsIgnoreCase(valor)){ // bussco el valor ignorando las mayus y minus
                encontrados.add(lista.get(i));
            }
        }
        if (encontrados.size()<1){
            return null;
        }
        return encontrados;

    }

    static void registrarEquipo(){
        System.out.println("--- REGISTRAR EQUIPO ---");
        System.out.println("[Escriba 0 en cualquier campo para cancelar la creacion]");

        // Validar si existen empleados antes de crear el equipo
        if (empleados.isEmpty()){
            System.out.println("No hay empleados registrados, registre un empleado primero.");
            return;
        }

        String codigo;

        while (true){
            // Pido el codigo del equipo y valido que no exista en los registros
            codigo=leerTexto("Codigo: ").toUpperCase();
            if (codigo.equals("0")){
                System.out.println("Registro cancelado");
                return;
            }
            // Valido que el equipo con ese codigo no exista
            if (buscarEnLista(equipos, camposEquipos, "codigo", codigo)!=null){
                System.out.println("Este codigo de equipo ya existe, por favor intente con otro codigo.");
                continue;
            }
            break;
        }

        String tipo = seleccionarOpcion("Tipos de Equipo disponibles: ", tiposEquipo);
        if(tipo.equals("0")){
            System.out.println("Registro cancelado");
            return;
        }

        String marca = leerTexto("Marca: ");
        if (marca.equals("0")){
            System.out.println("Registro cancelado");
            return;
        }

        // Con la funcion de seleccionar empleado, se mitia el tener que solicitar codigo y luego valdiar si existe, sino que se lista las opcines de los empleados creads
        String[] empleadoResponsable = seleccionarEmpleado();
        if (empleadoResponsable==null){
            System.out.println("Registro cancelado");
            return;
        }
        String codigoEmpleado = empleadoResponsable[0];

        String estado = seleccionarOpcion("Estados de equipo disponibles:", estadosEquipo);
        if (estado.equals("0")){
            System.out.println("Registro cancelado");
            return;
        }

        String[] nuevoEquipo = crearEquipo(codigo,tipo, marca, codigoEmpleado,estado);
        System.out.println("Equipo registrado correctamente.");
        System.out.println("Codigo: "+codigo+" | Tipo: "+tipo+" | Marca: "+marca+" | Empleado: "+empleadoResponsable[1]+" | Estado: "+estado);
    }

    public static void main(String[] args) {
        int opcion = 0; // Gaurdar la opcion del usuario para el menu
        while (opcion != 8) { // Mantengo el sistema hasta que el usuario seleccione salir con 8
            mostrarMenu();
            opcion = leerEntero("Seleccione una opcion: ");
            switch (opcion) {
                case 1: registrarEmpleado(); break;
                case 2: registrarEquipo(); break;
                case 3: System.out.println(">> pendiente: crear solicitud"); break;
                case 4: System.out.println(">> pendiente: consultar registros"); break;
                case 5: System.out.println(">> pendiente: atender solicitud"); break;
                case 6: System.out.println(">> pendiente: mostrar pendientes"); break;
                case 7: System.out.println(">> pendiente: mostrar solucionadas"); break;
                case 8: System.out.println("Gracias por usar YoiSoft SAS. Hasta pronto."); break;
                default: System.out.println("Opcion invalida. Elija un numero del 1 al 8.");
            }
        }
    }
}
