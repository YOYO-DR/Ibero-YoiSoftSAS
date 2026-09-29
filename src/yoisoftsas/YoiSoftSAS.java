// package yoisoftsas;

import java.sql.SQLOutput;
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
        // Funcion para seleccionar un empleado, busca por codigo y retorna vacio si no existe
        System.out.println("Seleccion de empleado: ");
        if (empleados.isEmpty()){
            System.out.println("No hay empleados registrados, registre un empleado primero.");
            return null;
        }

        while (true){
                // Solicitar el codigo
                String codigo_empleado_buscar = leerTexto("Ingrese el codigo del empleado a buscar: ");
                if (codigo_empleado_buscar.equals("0")){
                    return null;
                }
                ArrayList<String[]> resultado = buscarEnLista(empleados, camposEmpleados, "codigo", codigo_empleado_buscar);

                if (resultado==null){
                    System.out.println("No se encontro un empleado con el codigo \""+codigo_empleado_buscar+"\". Intente con otro codigo.");
                    continue;
                }

                return resultado.get(0);
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

        // Con la funcion de seleccionar empleado
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

    static String[] seleccionarEquipo(){
        // Funcion para seleccionar un equipo, busca por codigo y retorna vacio si no existe
        System.out.println("Seleccion de equipo: ");
        if (equipos.isEmpty()){
            System.out.println("No hay equipos registrados, registre un equipo primero.");
            return null;
        }

        while (true){
            // Solicitar el codigo
            String codigo_equipo_buscar = leerTexto("Ingrese el codigo del equipo a buscar: ");
            if (codigo_equipo_buscar.equals("0")){
                return null;
            }
            ArrayList<String[]> resultado = buscarEnLista(equipos, camposEquipos, "codigo", codigo_equipo_buscar);

            if (resultado==null){
                System.out.println("No se encontro un equipo con el codigo \""+codigo_equipo_buscar+"\". Intente con otro codigo.");
                continue;
            }

            return resultado.get(0);
        }

    }


    static String[] crearSolicitudPendiente (String codigo, String codigoEmpleado, String codigoEquipo, String descripcion, String prioridad, String estado){
        String[] nuevaSolicitud = {codigo, codigoEmpleado, codigoEquipo, descripcion, prioridad, estado};
        solicitudesPendientes.add(nuevaSolicitud);
        return nuevaSolicitud;
    }

    static void registrarSolicitud(){
        // Funcion para registrar una solicitud
        System.out.println("--- REGISTRAR SOLICITUD ---");
        System.out.println("[Escriba 0 en cualquier campo para cancelar la creacion]");

        // Validar que hayan empleados y equipos ya que es dependencia
        if (empleados.isEmpty()){
            System.out.println("No hay empleados registrados, registre un empleado primero.");
            return;
        }

        if (equipos.isEmpty()){
            System.out.println("No hay equipos registrados, registre un equipo primero.");
            return;
        }

        String codigo;

        while (true){
            // Pido el codigo de la solicitud y valido que no exista en los registros
            codigo=leerTexto("Codigo: ").toUpperCase();
            if (codigo.equals("0")){
                System.out.println("Registro cancelado");
                return;
            }
            // Valido que una solicitud no exista en las soicitudes pendientes ni solucionadas
            Boolean repetida_encontrado=false;
            for (String[] solicitud : solicitudesPendientes){
                if (solicitud[0].equals(codigo)){
                    repetida_encontrado=true;
                }
            }

            for (String[] solicitud : solicitudesSolucionadas){
                if (solicitud[0].equals(codigo)){
                    repetida_encontrado=true;
                }
            }

            // Si hay una reptida, decir que ese codigo ya esta ocupado
            if (repetida_encontrado){
                System.out.println("El codigo \""+codigo+"\" ya esta ocupado. Intenta con otro codigo.");
                continue;
            }

            break;
        }

        // Con la funcion de seleccionar empleado
        String[] empleadoResponsable = seleccionarEmpleado();
        if (empleadoResponsable==null){
            System.out.println("Registro cancelado");
            return;
        }
        String codigoEmpleado = empleadoResponsable[0];

        // Seleccionar un equipo con la funcion de seleccionar equipo
        String[] equipoSeleccionado = seleccionarEquipo();
        if (equipoSeleccionado==null){
            System.out.println("Registro cancelado");
            return;
        }
        String codigoEquipo = equipoSeleccionado[0];

        // Ingresar descripcion del problema
        String descripcion = leerTexto("Describe el problema: ");

        if (descripcion.equals("0")){
            System.out.println("Registro cancelado.");
            return;
        }

        // Seleccion de nivel de prioridad
        String prioridad = seleccionarOpcion("Prioridades disponibles: ", prioridades);
        if (prioridad.equals("0")){
            System.out.println("Registro cancelado");
            return;
        }
        // Se crea la solicitud con estado penmdiente, primer posicion de estadosSolicitud
        crearSolicitudPendiente(codigo, codigoEmpleado, codigoEquipo, descripcion, prioridad, estadosSolicitud[0]);

        System.out.println("Solicitud registrada correctamente.");
        System.out.println("Codigo: "+codigo+" | Empleado: "+empleadoResponsable[1]+" | Codigo equipo: "+codigoEquipo+" | Prioridad: "+prioridad+" | Estado: "+estadosSolicitud[0]);

    }

    static void mostrarSubMenuConsulta(){
        // Funcion para mostrar el sub-menu de opciones para las consultas
        System.out.println();
        System.out.println("==================================");
        System.out.println("        SUBMENU CONSULTAS         ");
        System.out.println("==================================");
        System.out.println("1. Listar todos los empleados");
        System.out.println("2. Listar todos los equipos");
        System.out.println("3. Buscar empleado por codigo");
        System.out.println("4. Buscar equipo por codigo");
        System.out.println("5. Buscar solicitud por codigo (Pendiente o solucionada)");
        System.out.println("6. Salir");
    }

    static void listarEmpleados(){
        // Funcion para listar todos los empleados
        System.out.println("\n--- LISTADO DE EMPLEADOS ---");

        // Validar si hay empleados
        if (empleados.size()<1){
            System.out.println("No existen empleados registrados.");
            return;
        }

        for (String[] empleado : empleados){
            // {"codigo", "nombre", "area", "correo"}
            System.out.println(empleado[0]+" - "+empleado[1]+" - "+empleado[2]+" - "+empleado[3]);
        }
    }

    static void listarEquipos(){
        // Funcion para listar todos los equipos
        System.out.println("\n--- LISTADO DE EQUIPOS ---");

        // Validar si hay empleados
        if (equipos.size()<1){
            System.out.println("No existen equipos registrados.");
            return;
        }

        for (String[] equipo : equipos){
            // {"codigo", "tipo", "marca", "codigoEmpleado", "estado"};
            String[] empleado = buscarEnLista(equipos, camposEquipos, "codigo",equipo[3]).get(0);

            // Validar si se encontro el empleado (posiblemente nunca ocurra este error pero se valida)
            if (empleado==null){
                System.out.println("Ocurrio un error listando los equipos, intenta de nuevo");
            }
            System.out.println(equipo[0]+" - "+equipo[1]+" - "+equipo[2]+" - "+empleado[1]+" - "+equipo[4]);
        }
    }

    static void buscarEmpleadoPorCodigo(){
        // Buscar un empleado por codigo
        System.out.println("\n--- BUSQUEDA DE EMPLEADO POR CODIGO ---");
        System.out.println("[Escribe 0 para salir de la busqueda de empleado]]");

        // Validar que haya empleados registrados
        if (empleados.size()<1){
            System.out.println("No hay empleados registrados.");
            return;
        }

        while (true){

            String codigoEmpleadoBuscar = leerTexto("Ingrese el codigo de empleado a buscar: ");

            if (codigoEmpleadoBuscar.equals("0")){
                break;
            }

            ArrayList<String[]> empleadosEncontrados = buscarEnLista(empleados, camposEmpleados, "codigo", codigoEmpleadoBuscar);

            if (empleadosEncontrados==null){
                System.out.println("No se encontro un empleado con ese codigo. Intenta con otro codigo.");
                continue;
            }

            String[] empleadoEncontrado=empleadosEncontrados.get(0);

            System.out.println("Empleado encontrado: ");
            System.out.println(empleadoEncontrado[0] + " - "+ empleadoEncontrado[1] + " - "+empleadoEncontrado[2] + " - "+empleadoEncontrado[3]);
            System.out.println();
        }

    }

    static void buscarEquipoPorCodigo(){
        // Buscar un equipo por codigo
        System.out.println("\n--- BUSQUEDA DE EQUIPO POR CODIGO ---");
        System.out.println("[Escribe 0 para salir de la busqueda de equipo]]");

        // Validar que haya equipos registrados
        if (equipos.size()<1){
            System.out.println("No hay equipos registrados.");
            return;
        }

        while (true){

            String codigoEquipoBuscar = leerTexto("Ingrese el codigo del equipo a buscar: ");

            if (codigoEquipoBuscar.equals("0")){
                break;
            }

            ArrayList<String[]> equiposEncontrados = buscarEnLista(equipos, camposEquipos, "codigo", codigoEquipoBuscar);

            if (equiposEncontrados==null){
                System.out.println("No se encontro un equipo con ese codigo. Intenta con otro codigo.");
                continue;
            }

            String[] equipoEncontrado=equiposEncontrados.get(0);

            System.out.println("Equipo encontrado: ");
            System.out.println(equipoEncontrado[0] + " - "+ equipoEncontrado[1] + " - "+equipoEncontrado[2] + " - "+equipoEncontrado[3]+" - "+equipoEncontrado[4]);
            System.out.println();
        }
    }

    static void buscarSolicitudPorCodigo(){
        // funcion para buscar solicitudes pendientes o solucionadas por codigo
        System.out.println("\n--- BUSQUEDA DE SOLICITUDES POR CODIGO ---");
        System.out.println("[Escribe 0 para salir de la busqueda de solicitudes]");

        // Validar que haya solicitudes
        if (solicitudesPendientes.size()<1 && solicitudesSolucionadas.size()<1){
            System.out.println("No hay equipos registrados.");
            return;
        }

        while (true){
            String codigoSolicitudBuscar = leerTexto("Ingrese el codigo de la solicitud a buscar: ").toUpperCase();

            if (codigoSolicitudBuscar.equals("0")){
                break;
            }

            String[] solicitudEncontrada=null;

            // Buscar la solicitud en las pendientes
            for (String[] solicitud : solicitudesPendientes){
                if (solicitud[0].equals(codigoSolicitudBuscar)){
                        solicitudEncontrada=solicitud;}
            }


            // Buscar en las solicitudes solucionadas si no se ha encontrado

            for (String[] solicitud : solicitudesSolucionadas){
                if (solicitud[0].equals(codigoSolicitudBuscar)){
                    solicitudEncontrada=solicitud;
                }
            }

            if (solicitudEncontrada==null){
                System.out.println("La solicitud por codigo \""+codigoSolicitudBuscar+"\" no existe.");
                continue;
            }

            //Solicitud encontrada, obtener el empleado y equipo
            String[] empleado = buscarEnLista(empleados, camposEmpleados, "codigo", solicitudEncontrada[1]).get(0);
            String[] equipo = buscarEnLista(equipos, camposEquipos, "codigo",solicitudEncontrada[2]).get(0);

            // Imprimir solicitud encontrada
            // {"codigo", "codigoEmpleado", "codigoEquipo", "descripcion", "prioridad", "estado"};
            System.out.println("Solicitud \""+codigoSolicitudBuscar+"\" encontrada:");
            System.out.println(" Codigo: "+ solicitudEncontrada[0]);
            System.out.println(" Empleado: "+ empleado[0]+" - "+empleado[1]);
            System.out.println(" Equipo: "+ equipo[0]+" - "+equipo[1]);
            System.out.println(" Descripcion: "+ solicitudEncontrada[3]);
            System.out.println(" Prioridad: "+ solicitudEncontrada[4]);
            System.out.println(" Estado: "+ solicitudEncontrada[5]);
            System.out.println();
        }
    }

    static void consultarRegistros(){
        // Funcion para consultar diferentes registros
        // Listar todos los empleados, equipos, solicitudes pendientes y solucionadas
        // Buscar empleado, equipo, o solicitud por codigo y ver su detalle completo
        System.out.println("--- CONSULTAR REGISTROS ---");
        System.out.println("[Consulta de empleados, equipos y solicitudes]");

        int opcion=0;

        while (opcion!=6){
            mostrarSubMenuConsulta();

            opcion = leerEntero("Seleccione la conuslta a realizar: ");

            switch (opcion) {
                case 1: listarEmpleados(); break;
                case 2: listarEquipos(); break;
                case 3: buscarEmpleadoPorCodigo(); break;
                case 4: buscarEquipoPorCodigo(); break;
                case 5: buscarSolicitudPorCodigo(); break;
                case 6: System.out.println("Saliendo de la consulta de registros");break;
                default: System.out.println("Opcion invalida. Elija un numero del 1 al 6.");
            }
        }

    }

    public static void main(String[] args) {
        int opcion = 0; // Gaurdar la opcion del usuario para el menu
        while (opcion != 8) { // Mantengo el sistema hasta que el usuario seleccione salir con 8
            mostrarMenu();
            opcion = leerEntero("Seleccione una opcion: ");
            switch (opcion) {
                case 1: registrarEmpleado(); break;
                case 2: registrarEquipo(); break;
                case 3: registrarSolicitud(); break;
                case 4: consultarRegistros(); break;
                case 5: System.out.println(">> pendiente: atender solicitud"); break;
                case 6: System.out.println(">> pendiente: mostrar pendientes"); break;
                case 7: System.out.println(">> pendiente: mostrar solucionadas"); break;
                case 8: System.out.println("Gracias por usar YoiSoft SAS. Hasta pronto."); break;
                default: System.out.println("Opcion invalida. Elija un numero del 1 al 8.");
            }
        }
    }
}
