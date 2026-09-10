/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package yoisoftsas;

import java.util.ArrayList;
import java.util.Scanner;
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

    static ArrayList<String[]> buscarEmpleados(String campo, String valor){
        // funcion para buscar un empleado por un campo en especifico
        // no manejo el error de que si el campo no existe porque la funcion que llame esta funcion debe validar como quiere buscar el empleado
        int campoABuscar=-1;
        ArrayList<String[]> empleadosEncontrados= new ArrayList<>();
        for (int i = 0; i < camposEmpleados.length; i++){
            if (camposEmpleados[i].equals(campo)){
                campoABuscar=i;
            }
        }
        // Recorro los empleados actuales
        for (int i=0;i < empleados.size();i++){
            if (empleados.get(i)[campoABuscar].equals(valor)){
                empleadosEncontrados.add(empleados.get(i));
            }
        }

        if (empleadosEncontrados.size()<=0){
            return null;
        }

        return empleadosEncontrados;
    }

    static String[] crearEmpleado(String codigo, String nombre, String area, String correo){
        // Funcion base para registrar el empleado, la funcion que lo llame debe validar los campos
        String[] empleado = {codigo,nombre,area,correo};
        empleados.add(empleado);
        return empleado;
    }

    static String seleccionarArea(){
        // funcion para gestiona la selecicon de un area
        while (true){
            // Le muestro las areas que estan diponibles
            System.out.println("Areas disponibles:");
            for (int i=0;i<areas.length;i++){
                System.out.println("  "+(i+1)+". "+areas[i]);
            }
            int opcion=leerEntero("Seleccione el area:");
            if (opcion==0){
                // Si cancela retorno 0 para salir de la creacion del usuario tambien
                return "0";
            }
            // Retorno el area
            if (opcion >=1 && opcion <= areas.length){
                return areas[opcion-1];
            }
            // repito
            System.out.println("Error: elina un numero entre 1 y "+areas.length+".");
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
            codigo=leerTexto("Codigo: ");
            if (codigo.equals("0")){
                System.out.println("Registro cancelado");
                return;
            }
            // Valido que el empleado con ese codigo no exista
            if (buscarEmpleados("codigo",codigo)!=null){
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

        String area = seleccionarArea();
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

    public static void main(String[] args) {
        int opcion = 0; // Gaurdar la opcion del usuario para el menu
        while (opcion != 8) { // Mantengo el sistema hasta que el usuario seleccione salir con 8
            mostrarMenu();
            opcion = leerEntero("Seleccione una opcion: ");
            switch (opcion) {
                case 1: registrarEmpleado(); break;
                case 2: System.out.println(">> pendiente: registrar equipo"); break;
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
