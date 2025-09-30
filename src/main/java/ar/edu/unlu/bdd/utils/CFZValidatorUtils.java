package ar.edu.unlu.bdd.utils;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class CFZValidatorUtils {
    private static final Scanner teclado = new Scanner(System.in);

    private CFZValidatorUtils() {
    }

    /**
     * Solicita un número por teclado mostrando un mensaje personalizado.
     * Reintenta hasta que el usuario ingrese un número válido.
     *
     * @param mensaje mensaje que se mostrará al usuario
     * @return número ingresado por el usuario
     */
    public static Integer solicitarNumeroPorTeclado(String mensaje) {
        Integer numero = null;
        boolean valido = false;

        do {
            System.out.print(mensaje);
            String linea = teclado.nextLine().trim(); // Leemos la línea completa y eliminamos espacios

            if (linea.isEmpty()) { // Si solo presionó Enter
                System.out.println("\n***********************************************");
                System.out.println("La entrada no puede estar vacía. Intente otra vez.");
                System.out.println("***********************************************\n");
                continue;
            }

            try {
                numero = Integer.parseInt(linea);
                valido = true;
            } catch (NumberFormatException ex) {
                System.out.println("\n***********************************************");
                System.out.println("Debe introducir un número válido. Intente otra vez.");
                System.out.println("***********************************************\n");
            }
        } while (!valido);

        return numero;
    }

    public static Integer solicitarNumeroPorTeclado() {
        return solicitarNumeroPorTeclado("Ingrese un número: ");
    }

    /**
     * Solicita una cadena por teclado mostrando un mensaje personalizado.
     * Reintenta hasta que el usuario ingrese algo no vacío.
     *
     * @param mensaje mensaje que se mostrará al usuario
     * @return texto ingresado por el usuario
     */
    public static String solicitarEntradaPorTeclado(String mensaje) {
        String entrada;
        boolean valido = false;

        do {
            System.out.print(mensaje);
            entrada = teclado.nextLine().trim(); // Eliminamos espacios al inicio y fin

            if (entrada.isEmpty()) {
                System.out.println("\n***********************************************");
                System.out.println("La entrada no puede estar vacía. Intente otra vez.");
                System.out.println("***********************************************\n");
            } else {
                valido = true;
            }
        } while (!valido);

        return entrada;
    }

    public static String solicitarEntradaPorTeclado() {
        return solicitarEntradaPorTeclado("Ingrese un valor: ");
    }

    public static Timestamp solicitarTimeStampPorTeclado() {
        Timestamp fecha = null;
        String inFecha;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        boolean fechaValida = false;

        do {
            System.out.print("Ingrese una fecha (dd/MM/yyyy): ");
            inFecha = teclado.nextLine();

            try {
                // Parseamos la fecha
                LocalDate fecha2 = LocalDate.parse(inFecha, formatter);

                // Generamos Timestamp con hora 00:00:00
                LocalDateTime fechaHora = fecha2.atStartOfDay();
                fecha = Timestamp.valueOf(fechaHora);

                fechaValida = true; // si llegamos acá, la fecha es válida

            } catch (Exception e) {
                System.out.println("\n***********************************************");
                System.out.println("Debe ingresar una fecha valida (dd/MM/yyyy)");
                System.out.println("***********************************************\n");
            }
        } while (!fechaValida);

        return fecha;

    }

    /**
     * Solicita un número decimal (double) por teclado mostrando un mensaje personalizado.
     * Reintenta hasta que el usuario ingrese un número válido.
     *
     * @param mensaje mensaje que se mostrará al usuario
     * @return número ingresado por el usuario
     */
    public static Double solicitarDoublePorTeclado(String mensaje) {
        Double numero = null;
        boolean valido = false;

        do {
            System.out.print(mensaje);
            String linea = teclado.nextLine().trim(); // Leemos la línea completa y eliminamos espacios

            if (linea.isEmpty()) { // Si solo presionó Enter
                System.out.println("\n***********************************************");
                System.out.println("La entrada no puede estar vacía. Intente otra vez.");
                System.out.println("***********************************************\n");
                continue;
            }

            try {
                numero = Double.parseDouble(linea.replace(",", "."));
                // permitimos que si el usuario pone coma decimal, se convierta a punto
                valido = true;
            } catch (NumberFormatException ex) {
                System.out.println("\n***********************************************");
                System.out.println("Debe introducir un número decimal válido. Intente otra vez.");
                System.out.println("***********************************************\n");
            }
        } while (!valido);

        return numero;
    }

    public static Double solicitarDoublePorTeclado() {
        return solicitarDoublePorTeclado("Ingrese un número decimal: ");
    }

    /**
     * Solicita una fecha por teclado en formato dd/mm/yyyy y valida el formato.
     * Reintenta hasta que el usuario ingrese una fecha válida.
     *
     * @return fecha ingresada por el usuario en formato dd/mm/yyyy
     */
    public static String solicitarFechaPorTeclado() {
        return solicitarFechaPorTeclado("Ingrese una fecha (dd/mm/yyyy): ");
    }

    /**
     * Solicita una fecha por teclado en formato dd/mm/yyyy y valida el formato.
     * Reintenta hasta que el usuario ingrese una fecha válida.
     *
     * @param mensaje mensaje que se mostrará al usuario
     * @return fecha ingresada por el usuario en formato dd/mm/yyyy
     */
    public static String solicitarFechaPorTeclado(String mensaje) {
        String fecha = null;
        boolean fechaValida = false;

        do {
            System.out.print(mensaje);
            String entrada = teclado.nextLine().trim();

            if (entrada.isEmpty()) {
                System.out.println("\n***********************************************");
                System.out.println("La fecha no puede estar vacía. Intente otra vez.");
                System.out.println("***********************************************\n");
                continue;
            }

            if (validarFormatoFecha(entrada)) {
                fecha = entrada;
                fechaValida = true;
            } else {
                System.out.println("\n***********************************************");
                System.out.println("Formato de fecha inválido. Use dd/mm/yyyy (ej: 25/12/2023)");
                System.out.println("***********************************************\n");
            }
        } while (!fechaValida);

        return fecha;
    }

    /**
     * Valida que una fecha esté en el formato dd/mm/yyyy
     *
     * @param fecha fecha a validar
     * @return true si la fecha tiene el formato correcto, false en caso contrario
     */
    private static boolean validarFormatoFecha(String fecha) {
        // Verificar que tenga exactamente 10 caracteres
        if (fecha.length() != 10) {
            return false;
        }

        // Verificar que tenga el formato dd/mm/yyyy
        if (fecha.charAt(2) != '/' || fecha.charAt(5) != '/') {
            return false;
        }

        try {
            // Extraer día, mes y año
            String dayStr = fecha.substring(0, 2);
            String monthStr = fecha.substring(3, 5);
            String yearStr = fecha.substring(6, 10);

            // Convertir a números
            int day = Integer.parseInt(dayStr);
            int month = Integer.parseInt(monthStr);
            int year = Integer.parseInt(yearStr);

            // Validar rangos
            if (day < 1 || day > 31) {
                return false;
            }
            if (month < 1 || month > 12) {
                return false;
            }
            if (year < 1900 || year > 2100) {
                return false;
            }

            // Validaciones adicionales por mes
            if (month == 2) { // Febrero
                if (day > 29) {
                    return false;
                }
                // Verificar año bisiesto
                return day != 29 || esYearBisiesto(year);
            } else if (month == 4 || month == 6 || month == 9 || month == 11) { // Meses con 30 días
                return day <= 30;
            }

            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Verifica si un año es bisiesto
     *
     * @param year año a verificar
     * @return true si es bisiesto, false en caso contrario
     */
    private static boolean esYearBisiesto(int year) {
        return (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0);
    }
}