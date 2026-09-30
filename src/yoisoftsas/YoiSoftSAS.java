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

    // Campos de cada solicitud, no se utiliza pero sirve para saber su estructura
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

    static int leerEntero(String mensaje) {
        // Funcion para manerar el input de numeros enteros
        while (true) {
            // Con el while mantengo validando con el try, hasta que sea un numero entero lo ingresado
            System.out.print(mensaje);
            try {
                return Integer.parseInt(sc.nextLine().trim()); // Uso el trim para quitar espacios
            } catch (NumberFormatException e) {
                System.out.println("Error: Debe ingresar un numero entero.");
            }
        }
    }


    static String leerTexto(String mensaje, boolean permitirVacio) { // Para leer texto, aplico sobrecarga de parametros para poner el parametro de permitirVacio apara confirmaciones con Enter
        // Funcion para manejar el input de texto
        while (true) {
            System.out.print(mensaje);
            String dato = sc.nextLine().trim(); // trim para quitar los espacios de los bordes
            if (dato.isEmpty() && !permitirVacio) { // Valido si el dato ingresado esta vacio, y tambien si se permite que sea vcio, para los casos de solo confirmar con Enter
                System.out.println("Error: este campo no puede quedar vacio.");
                continue;
            }
            return dato;
        }
    }

    static String leerTexto(String mensaje) {
        // Por defecto, no se permite vacio
        return leerTexto(mensaje, false);
    }

    static String[] crearEmpleado(String codigo, String nombre, String area, String correo) {
        // Funcion base para registrar el empleado, la funcion que lo llame debe validar los campos
        String[] empleado = {codigo, nombre, area, correo};
        empleados.add(empleado);
        return empleado;
    }


    static String seleccionarOpcion(String titulo, String[] opciones) { // Muestra las opciones de un arreglo y devuelve la seleccionada, o "0" si cancela
        while (true) {
            System.out.println(titulo);

            for (int i = 0; i < opciones.length; i++) { // Recorro las opciones y las formateo para imprimir y mostrar
                System.out.println("  " + (i + 1) + ". " + opciones[i]);
            }
            int opcion = leerEntero("Seleccione una opcion: ");
            if (opcion == 0) { // Si es 0, salgo de la seleccion
                return "0";
            }
            if (opcion >= 1 && opcion <= opciones.length) { // Validar que la opcion este en el rango de opciones
                return opciones[opcion - 1];
            }
            System.out.println("Error: elija un numero entre 1 y " + opciones.length + ".");
        }
    }

    static String leerCorreo(String mensaje) {
        // Funcion para leer un correo

        Pattern regex = Pattern.compile("^[\\w.+-]+@[\\w-]+\\.[\\w.-]+$"); // Utilizo una expresion regular para validar el correo

        while (true) {
            String correo = leerTexto(mensaje);

            if (correo.equals("0")) {
                return "0";
            }
            // Utilizo el regex para validar el patron del correo
            if (regex.matcher(correo).matches()) {
                return correo;
            }
            System.out.println("Error: correo invalido, intente de nuevo.");
        }
    }

    static void registrarEmpleado() {
        System.out.println("--- REGISTRAR EMPLEADO ---");
        System.out.println("[Escriba 0 en cualquier campo para cancelar la creacion]");

        String codigo;

        while (true) {
            // Pido el codigo del empleado y valido que no exista en los registros
            codigo = leerTexto("Codigo: ").toUpperCase();
            if (codigo.equals("0")) {
                System.out.println("Registro cancelado");
                return;
            }
            // Valido que el empleado con ese codigo no exista
            if (buscarEnLista(empleados, camposEmpleados, "codigo", codigo) != null) { // Busco en los empleados si existe ese codigo de empleado
                System.out.println("Este codigo de empeado ya existe, por favor intente con otro codigo.");
                continue;
            }
            break; // salgo de la seleccion de codigo
        }

        String nombre = leerTexto("Nombre completo: ");
        if (nombre.equals("0")) {
            System.out.println("Registro cancelado");
            return;
        }

        String area = seleccionarOpcion("Areas disponibles:", areas); // Utilizo la funcion para seleccionar una funcion de la lista deifnida de areas
        if (area.equals("0")) {
            System.out.println("Registro cancelado");
            return;
        }

        String correo = leerCorreo("Correo electronico: ");
        if (correo.equals("0")) {
            System.out.println("Registro cancelado.");
            return;
        }

        crearEmpleado(codigo, nombre, area, correo); // Creo al empleado con sus datos, ya validados
        System.out.println("Empleado registrado correctamente.");
        System.out.println("Codigo: " + codigo + " | Nombre: " + nombre + " | Area: " + area + " | Correo: " + correo);

    }

    static String[] seleccionarEmpleado() {
        // Funcion para seleccionar un empleado, busca por codigo y retorna vacio si no existe o no hay empleados
        System.out.println("Seleccion de empleado: ");
        if (empleados.isEmpty()) {
            System.out.println("No hay empleados registrados, registre un empleado primero.");
            return null;
        }

        while (true) {
            // Solicitar el codigo
            String codigo_empleado_buscar = leerTexto("Ingrese el codigo del empleado a buscar: ");
            if (codigo_empleado_buscar.equals("0")) {
                return null;
            }
            ArrayList<String[]> resultado = buscarEnLista(empleados, camposEmpleados, "codigo", codigo_empleado_buscar); // Busco el empleado por su codigo

            if (resultado == null) {
                System.out.println("No se encontro un empleado con el codigo \"" + codigo_empleado_buscar + "\". Intente con otro codigo.");
                continue;
            }

            return resultado.get(0); // Retorno el empleado, como son por codigo y unicos, siempre sera el primero si llega a este punto
        }

    }

    static String[] crearEquipo(String codigo, String tipo, String marca, String codigoEmpleado, String estado) {
        // Funcion base para registrar el equipo, la funcion que lo llame debe validar los campos
        String[] equipo = {codigo, tipo, marca, codigoEmpleado, estado};
        equipos.add(equipo);
        return equipo;
    }

    static ArrayList<String[]> buscarEnLista(ArrayList<String[]> lista, String[] campos, String campoBuscar, String valor) {
        // Funcion para buscar un valor dentro de un arraylist por campo
        ArrayList<String[]> encontrados = new ArrayList<>(); // Lista para guardar los resultados buscados
        int idCampoBuscar = -1; // Variable para saber con que campo se va a buscar segun la lista y sus campos que tenga
        for (int i = 0; i < campos.length; i++) {
            if (campos[i].equals(campoBuscar)) { // Si el campo es igual al que se va a buscar, guardo su indice
                idCampoBuscar = i;
                break; // salgo porque ya lo encontre
            }
        }
        if (idCampoBuscar == -1) {
            return null; // Si no existe el campo, retorno null
        }
        for (int i = 0; i < lista.size(); i++) {
            // Por cada item de la lista, segun su indice del campo, pregunto su valor ignorando mayusculas y minusculas
            if (lista.get(i)[idCampoBuscar].equalsIgnoreCase(valor)) {
                encontrados.add(lista.get(i));
            }
        }
        if (encontrados.isEmpty()) { // Retorno null si no hay resultados
            return null;
        }
        return encontrados;

    }

    static void registrarEquipo() {
        System.out.println("--- REGISTRAR EQUIPO ---");
        System.out.println("[Escriba 0 en cualquier campo para cancelar la creacion]");

        // Validar si existen empleados antes de crear el equipo
        if (empleados.isEmpty()) {
            System.out.println("No hay empleados registrados, registre un empleado primero.");
            return;
        }

        String codigo;

        while (true) {
            // Pido el codigo del equipo y valido que no exista en los registros
            codigo = leerTexto("Codigo: ").toUpperCase(); // Convierto a mayusculas para guardar todos los codigos asi
            if (codigo.equals("0")) {
                System.out.println("Registro cancelado");
                return;
            }
            // Valido que el equipo con ese codigo no exista
            if (buscarEnLista(equipos, camposEquipos, "codigo", codigo) != null) {
                System.out.println("Este codigo de equipo ya existe, por favor intente con otro codigo.");
                continue;
            }
            break;
        }

        String tipo = seleccionarOpcion("Tipos de Equipo disponibles: ", tiposEquipo);
        if (tipo.equals("0")) {
            System.out.println("Registro cancelado");
            return;
        }

        String marca = leerTexto("Marca: ");
        if (marca.equals("0")) {
            System.out.println("Registro cancelado");
            return;
        }

        // Con la funcion de seleccionar empleado
        String[] empleadoResponsable = seleccionarEmpleado();
        if (empleadoResponsable == null) {
            System.out.println("Registro cancelado");
            return;
        }
        String codigoEmpleado = empleadoResponsable[0]; // Guardo el codigo del empleado para crear el equipo

        String estado = seleccionarOpcion("Estados de equipo disponibles:", estadosEquipo);
        if (estado.equals("0")) {
            System.out.println("Registro cancelado");
            return;
        }

        crearEquipo(codigo, tipo, marca, codigoEmpleado, estado); // creo el equipo ya con los datos validados
        System.out.println("Equipo registrado correctamente.");
        System.out.println("Codigo: " + codigo + " | Tipo: " + tipo + " | Marca: " + marca + " | Empleado: " + empleadoResponsable[1] + " | Estado: " + estado);
    }

    static String[] seleccionarEquipo() {
        // Funcion para seleccionar un equipo, busca por codigo y retorna vacio si no existe
        System.out.println("Seleccion de equipo: ");
        if (equipos.isEmpty()) {
            System.out.println("No hay equipos registrados, registre un equipo primero.");
            return null;
        }

        while (true) {
            // Solicitar el codigo
            String codigo_equipo_buscar = leerTexto("Ingrese el codigo del equipo a buscar: ");
            if (codigo_equipo_buscar.equals("0")) {
                return null;
            }
            ArrayList<String[]> resultado = buscarEnLista(equipos, camposEquipos, "codigo", codigo_equipo_buscar); // buscar equipo por su codigo

            if (resultado == null) { // Si no hay resultado, le indico que busque con otro codigo
                System.out.println("No se encontro un equipo con el codigo \"" + codigo_equipo_buscar + "\". Intente con otro codigo.");
                continue;
            }

            return resultado.get(0); // Retonro el equipo encontrado
        }

    }


    static String[] crearSolicitudPendiente(String codigo, String codigoEmpleado, String codigoEquipo, String descripcion, String prioridad, String estado) {
        // Funcion para crear una solicitud pendiente, en este caso se agrega a la COLA de solicitude pendientes con la funcion add
        String[] nuevaSolicitud = {codigo, codigoEmpleado, codigoEquipo, descripcion, prioridad, estado};
        solicitudesPendientes.add(nuevaSolicitud);
        return nuevaSolicitud;
    }

    static void registrarSolicitud() {
        // Funcion para registrar una solicitud
        System.out.println("--- REGISTRAR SOLICITUD ---");
        System.out.println("[Escriba 0 en cualquier campo para cancelar la creacion]");

        // Validar que hayan empleados y equipos ya que es dependencia
        if (empleados.isEmpty()) {
            System.out.println("No hay empleados registrados, registre un empleado primero.");
            return;
        }

        if (equipos.isEmpty()) {
            System.out.println("No hay equipos registrados, registre un equipo primero.");
            return;
        }

        String codigo;

        while (true) {
            // Pido el codigo de la solicitud y valido que no exista en los registros
            codigo = leerTexto("Codigo: ").toUpperCase();
            if (codigo.equals("0")) {
                System.out.println("Registro cancelado");
                return;
            }
            // Valido que una solicitud no exista en las soicitudes pendientes ni solucionadas
            Boolean repetida_encontrado = false; // Bandera para saber si hay una sol repetida
            for (String[] solicitud : solicitudesPendientes) {
                if (solicitud[0].equals(codigo)) {
                    repetida_encontrado = true;
                }
            }

            // Busco tambien en solucionadas
            for (String[] solicitud : solicitudesSolucionadas) {
                if (solicitud[0].equals(codigo)) {
                    repetida_encontrado = true;
                }
            }

            // Si hay una repetida, quiere decir que ese codigo ya esta ocupado
            if (repetida_encontrado) {
                System.out.println("El codigo \"" + codigo + "\" ya esta ocupado. Intenta con otro codigo.");
                continue;
            }

            break;
        }

        // Con la funcion de seleccionar empleado
        String[] empleadoResponsable = seleccionarEmpleado();
        if (empleadoResponsable == null) {
            System.out.println("Registro cancelado");
            return;
        }
        String codigoEmpleado = empleadoResponsable[0];

        // Seleccionar un equipo con la funcion de seleccionar equipo
        String[] equipoSeleccionado = seleccionarEquipo();
        if (equipoSeleccionado == null) {
            System.out.println("Registro cancelado");
            return;
        }
        String codigoEquipo = equipoSeleccionado[0];

        // Ingresar descripcion del problema
        String descripcion = leerTexto("Describe el problema: ");

        if (descripcion.equals("0")) {
            System.out.println("Registro cancelado.");
            return;
        }

        // Seleccion de nivel de prioridad
        String prioridad = seleccionarOpcion("Prioridades disponibles: ", prioridades); // Enlisto prioridades para seleccionar
        if (prioridad.equals("0")) {
            System.out.println("Registro cancelado");
            return;
        }
        // Se crea la solicitud con estado penmdiente, y la agrego a la COLA de tareas
        crearSolicitudPendiente(codigo, codigoEmpleado, codigoEquipo, descripcion, prioridad, estadosSolicitud[0]);

        System.out.println("Solicitud registrada correctamente.");
        System.out.println("Codigo: " + codigo + " | Empleado: " + empleadoResponsable[1] + " | Codigo equipo: " + codigoEquipo + " | Prioridad: " + prioridad + " | Estado: " + estadosSolicitud[0]);

    }

    static void mostrarSubMenuConsulta() {
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

    static void listarEmpleados() {
        // Funcion para listar todos los empleados
        System.out.println("\n--- LISTADO DE EMPLEADOS ---");

        // Validar si hay empleados
        if (empleados.size() < 1) {
            System.out.println("No existen empleados registrados.");
            return;
        }

        for (String[] empleado : empleados) {
            // {"codigo", "nombre", "area", "correo"}
            System.out.println(empleado[0] + " - " + empleado[1] + " - " + empleado[2] + " - " + empleado[3]);
        }
    }

    static void listarEquipos() {
        // Funcion para listar todos los equipos
        System.out.println("\n--- LISTADO DE EQUIPOS ---");

        // Validar si hay empleados
        if (equipos.size() < 1) {
            System.out.println("No existen equipos registrados.");
            return;
        }

        for (String[] equipo : equipos) {
            // {"codigo", "tipo", "marca", "codigoEmpleado", "estado"};
            String[] empleado = buscarEnLista(empleados, camposEmpleados, "codigo", equipo[3]).get(0);

            // Validar si se encontro el empleado (posiblemente nunca ocurra este error pero se valida)
            if (empleado == null) {
                System.out.println("Ocurrio un error listando los equipos, intenta de nuevo");
            }
            System.out.println(equipo[0] + " - " + equipo[1] + " - " + equipo[2] + " - " + empleado[1] + " - " + equipo[4]);
        }
    }

    static void buscarEmpleadoPorCodigo() {
        // Buscar un empleado por codigo
        System.out.println("\n--- BUSQUEDA DE EMPLEADO POR CODIGO ---");
        System.out.println("[Escribe 0 para salir de la busqueda de empleado]]");

        // Validar que haya empleados registrados
        if (empleados.isEmpty()) {
            System.out.println("No hay empleados registrados.");
            return;
        }

        while (true) {

            String codigoEmpleadoBuscar = leerTexto("Ingrese el codigo de empleado a buscar: ");

            if (codigoEmpleadoBuscar.equals("0")) {
                break;
            }

            ArrayList<String[]> empleadosEncontrados = buscarEnLista(empleados, camposEmpleados, "codigo", codigoEmpleadoBuscar); // Busco al empleado

            if (empleadosEncontrados == null) {
                System.out.println("No se encontro un empleado con ese codigo. Intenta con otro codigo.");
                continue;
            }

            String[] empleadoEncontrado = empleadosEncontrados.get(0);

            // IMprimo el empleado y sus valores
            System.out.println("Empleado encontrado: ");
            System.out.println(empleadoEncontrado[0] + " - " + empleadoEncontrado[1] + " - " + empleadoEncontrado[2] + " - " + empleadoEncontrado[3]);
            System.out.println();
        }

    }

    static void buscarEquipoPorCodigo() {
        // Buscar un equipo por codigo
        System.out.println("\n--- BUSQUEDA DE EQUIPO POR CODIGO ---");
        System.out.println("[Escribe 0 para salir de la busqueda de equipo]]");

        // Validar que haya equipos registrados
        if (equipos.isEmpty()) {
            System.out.println("No hay equipos registrados.");
            return;
        }

        while (true) {

            String codigoEquipoBuscar = leerTexto("Ingrese el codigo del equipo a buscar: ");

            if (codigoEquipoBuscar.equals("0")) {
                break;
            }

            ArrayList<String[]> equiposEncontrados = buscarEnLista(equipos, camposEquipos, "codigo", codigoEquipoBuscar); // busco el equipo

            if (equiposEncontrados == null) {
                System.out.println("No se encontro un equipo con ese codigo. Intenta con otro codigo.");
                continue;
            }

            String[] equipoEncontrado = equiposEncontrados.get(0);

            // Imprimo info del equipo encontrado
            System.out.println("Equipo encontrado: ");
            System.out.println(equipoEncontrado[0] + " - " + equipoEncontrado[1] + " - " + equipoEncontrado[2] + " - " + equipoEncontrado[3] + " - " + equipoEncontrado[4]);
            System.out.println();
        }
    }

    static void buscarSolicitudPorCodigo() {
        // funcion para buscar solicitudes pendientes o solucionadas por codigo
        System.out.println("\n--- BUSQUEDA DE SOLICITUDES POR CODIGO ---");
        System.out.println("[Escribe 0 para salir de la busqueda de solicitudes]");

        // Validar que haya solicitudes
        if (solicitudesPendientes.size() < 1 && solicitudesSolucionadas.size() < 1) {
            System.out.println("No hay solicitudes registradas.");
            return;
        }

        while (true) {
            String codigoSolicitudBuscar = leerTexto("Ingrese el codigo de la solicitud a buscar: ").toUpperCase(); // Convierto busque a mayuscula pq asi se guardan las solicitudes

            if (codigoSolicitudBuscar.equals("0")) {
                break;
            }

            String[] solicitudEncontrada = null; // Variable para guardar la solicitud encontrada

            // Buscar la solicitud en las pendientes
            for (String[] solicitud : solicitudesPendientes) {
                if (solicitud[0].equals(codigoSolicitudBuscar)) {
                    solicitudEncontrada = solicitud;
                }
            }


            // Buscar en las solicitudes solucionadas si no se ha encontrado

            for (String[] solicitud : solicitudesSolucionadas) {
                if (solicitud[0].equals(codigoSolicitudBuscar)) {
                    solicitudEncontrada = solicitud;
                }
            }

            if (solicitudEncontrada == null) {
                System.out.println("La solicitud por codigo \"" + codigoSolicitudBuscar + "\" no existe.");
                continue;
            }

            //Solicitud encontrada, obtener el empleado y equipo
            String[] empleado = buscarEnLista(empleados, camposEmpleados, "codigo", solicitudEncontrada[1]).get(0);
            String[] equipo = buscarEnLista(equipos, camposEquipos, "codigo", solicitudEncontrada[2]).get(0);

            // Imprimir solicitud encontrada
            // {"codigo", "codigoEmpleado", "codigoEquipo", "descripcion", "prioridad", "estado"};
            System.out.println("Solicitud \"" + codigoSolicitudBuscar + "\" encontrada:");
            System.out.println(" Codigo: " + solicitudEncontrada[0]);
            System.out.println(" Empleado: " + empleado[0] + " - " + empleado[1]);
            System.out.println(" Equipo: " + equipo[0] + " - " + equipo[1]);
            System.out.println(" Descripcion: " + solicitudEncontrada[3]);
            System.out.println(" Prioridad: " + solicitudEncontrada[4]);
            System.out.println(" Estado: " + solicitudEncontrada[5]);
            System.out.println();
        }
    }

    static void consultarRegistros() {
        // Funcion para consultar diferentes registros
        // Listar todos los empleados, equipos, solicitudes pendientes y solucionadas
        System.out.println("--- CONSULTAR REGISTROS ---");
        System.out.println("[Consulta de empleados, equipos y solicitudes]");

        int opcion = 0;

        while (opcion != 6) {
            mostrarSubMenuConsulta(); // Funcion para mostrar el menu de consulta de regiistos

            opcion = leerEntero("Seleccione la conuslta a realizar: ");

            switch (opcion) { // Utilizo el switch para mostar las opciones
                case 1:
                    listarEmpleados();
                    break;
                case 2:
                    listarEquipos();
                    break;
                case 3:
                    buscarEmpleadoPorCodigo();
                    break;
                case 4:
                    buscarEquipoPorCodigo();
                    break;
                case 5:
                    buscarSolicitudPorCodigo();
                    break;
                case 6:
                    System.out.println("Saliendo de la consulta de registros");
                    break;
                default:
                    System.out.println("Opcion invalida. Elija un numero del 1 al 6.");
            }
        }

    }

    static void listarSolicitudesPendientes() {
        // Listar solicitudes pendientes
        for (String[] solicitud : solicitudesPendientes) {
            // {"codigo", "codigoEmpleado", "codigoEquipo", "descripcion", "prioridad", "estado"};
            System.out.println("Solicitud \"" + solicitud[0] + "\":");
            System.out.println("  " + solicitud[0] + ". Descripcion: " + solicitud[3] + " - Prioridad: " + solicitud[4] + " - Estado: " + solicitud[5]);
        }
        System.out.println();
    }

    static void atenderSolicitud() {
        // funcion para atender una solicitud pendiente
        System.out.println("--- ATENDER SOLICITUDES PENDIENTES ---");
        System.out.println("[Escribe 0 pasa salir]");

        // Cosultar si hay solicitudes pendientes
        if (solicitudesPendientes.isEmpty()) {
            System.out.println("No hay solicitudes pendientes.");
            return;
        }


        while (true) {
            // Listar las solicitudes pendientes
            System.out.println("\nSolicitudes pendientes: ");
            listarSolicitudesPendientes();
            // Mirar siguiente solicitud a solucionar
            String[] solicitudASolucionar = solicitudesPendientes.peek(); // Utilizo peak para obtener la siguiente solicitud a solucionar sin sacarla de la COLA

            // Obtener empelado y equipo
            String[] empleado = buscarEnLista(empleados, camposEmpleados, "codigo", solicitudASolucionar[1]).get(0);
            String[] equipo = buscarEnLista(equipos, camposEquipos, "codigo", solicitudASolucionar[2]).get(0);

            // {"codigo", "codigoEmpleado", "codigoEquipo", "descripcion", "prioridad", "estado"};
            System.out.println("Siguiente solicitud a solucionar \"" + solicitudASolucionar[0] + "\": ");
            System.out.println("  Empleado: " + empleado[1]);
            System.out.println("  Equipo: " + equipo[1]);
            System.out.println("  Descripcion: " + solicitudASolucionar[3]);
            System.out.println("  Prioridad: " + solicitudASolucionar[4]);
            System.out.println("  Estado: " + solicitudASolucionar[5]);

            // Confirmar la solicitud
            String confirmacion = leerTexto("¿Confirmar solucion de la solicitud? (0 para salir, Enter para confirmar): ", true); // en este caso funciona los los prametros sobrecargados para permitir un vacio para solo confirmar con enter

            if (confirmacion.equals("0")) {
                System.out.println("Confirmacion cancelada.");
                // Listar cantidad de solicitudes faltantes
                System.out.println("Cantidad de solicitudes pendientes: " + solicitudesPendientes.size());
                return;
            }

            // Si dice que si, poner En atención la solicitud y luego en Solucionada
            System.out.println("Procesando solicitud \"" + solicitudASolucionar[0] + "\"...");
            solicitudASolucionar = solicitudesPendientes.poll(); // Utilizamos poll para obtener la primera de la cola, y asi mismo eliminarla de la cola
            // Se simula estado en atencion antes de solucionarla
            solicitudASolucionar[5] = "En atencion";
            System.out.println("Solicitud en atencion...");
            // Estado Soucionada
            solicitudASolucionar[5] = "Solucionada"; // Asignamos su estado final de solucionada
            solicitudesSolucionadas.push(solicitudASolucionar); // Se agrega la solciitud seleccionada a la PILA de solicitudes seleccionadas con push
            System.out.println("Solicitud solucionada correctamente, transferida a solicitudes solucionadas.");

            if (solicitudesPendientes.isEmpty()) {
                System.out.println("No hay màs solicitudes pendientes, saliendo.");
                return;
            }
            // Mostrar cantidad de solicitudes faltantes
            System.out.println("Cantidad de solicitudes pendientes: " + solicitudesPendientes.size());
        }


    }

    static void mostrarSolicitudesPendientes() {
        // Funcion para mostrar todas las soliciudes pendientes
        System.out.println("--- SOLICITUDES PENDIENTES ---");

        if (solicitudesPendientes.isEmpty()) {
            System.out.println("No hay solicitudes pendientes.\n");
            return;
        }

        // Con esta funcion para lsitar las sol pendietnes
        listarSolicitudesPendientes();
    }

    static void listarSolicitudesSolucionadas() {
        // Listar solicitudes solucionadas
        // Se imprimen de forma en que la ultima solciitud soluicionada aparezca de primero por ser una PILA

        for (int i = solicitudesSolucionadas.size(); i > 0; i--) {
            String[] solicitud = solicitudesSolucionadas.get(i - 1);
            // {"codigo", "codigoEmpleado", "codigoEquipo", "descripcion", "prioridad", "estado"};
            System.out.println("Solicitud \"" + solicitud[0] + "\":");
            System.out.println("  " + solicitud[0] + ". Descripcion: " + solicitud[3] + " - Prioridad: " + solicitud[4]);
        }

        System.out.println();

    }

    static void mostrarSolicitudesSolucionadas() {
        // Funcion para mostrar las solicitudes solucionadas
        System.out.println("--- SOLICITUDES SOLUCIONADAS ---");

        if (solicitudesSolucionadas.isEmpty()) {
            System.out.println("No hay solicitudes solucionadas.\n");
            return;
        }
        // Con esta funcion para listar las sol solucionadas
        listarSolicitudesSolucionadas();
    }

    public static void main(String[] args) {
        int opcion = 0; // Gaurdar la opcion del usuario para el menu
        while (opcion != 8) { // Mantengo el sistema hasta que el usuario seleccione salir con 8
            mostrarMenu();
            opcion = leerEntero("Seleccione una opcion: ");
            switch (opcion) {
                case 1:
                    registrarEmpleado();
                    break;
                case 2:
                    registrarEquipo();
                    break;
                case 3:
                    registrarSolicitud();
                    break;
                case 4:
                    consultarRegistros();
                    break;
                case 5:
                    atenderSolicitud();
                    break;
                case 6:
                    mostrarSolicitudesPendientes();
                    break;
                case 7:
                    mostrarSolicitudesSolucionadas();
                    break;
                case 8:
                    System.out.println("Gracias por usar YoiSoft SAS. Hasta pronto.");
                    break;
                default:
                    System.out.println("Opcion invalida. Elija un numero del 1 al 8.");
            }
        }
    }
}
