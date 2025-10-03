package ar.edu.unlu.bdd.view;

import ar.edu.unlu.bdd.controller.ControllerEmpleado;
import ar.edu.unlu.bdd.utils.CFZValidatorUtils;
import ar.edu.unlu.bdd.utils.Listado;

public class ViewEmpleado {
    public ViewEmpleado(ControllerEmpleado controllerEmpleado) {
        int opc;
        do {
            Listado.listarMenu("Empleado");
            opc = CFZValidatorUtils.solicitarNumeroPorTeclado("Ingrese su opción: ");
            switch (opc) {
                case 1: {
                    inputAlta(controllerEmpleado);
                    break;
                }
                case 2: {
                    inputBaja(controllerEmpleado);
                    break;
                }
                case 3: {
                    inputModificacion(controllerEmpleado);
                    break;
                }
                case 4: {
                    inputConsulta(controllerEmpleado);
                    break;
                }
                case 5: {
                    System.out.println("Saliendo...");
                    break;
                }
                default: {
                    System.out.println("Opción incorrecta.");
                    break;
                }
            }
        } while (opc != 5);
    }


    private void inputAlta(ControllerEmpleado controllerEmpleado) {
        int dni;
        String nombre;
        String apellido;
        String sucursal;

        // Entrada DNI
        while (true) {
            dni = CFZValidatorUtils.solicitarNumeroPorTeclado("Ingrese el DNI del empleado: ");
            if (dni > 0 && dni < 100000000) break;
            else System.out.println("El DNI debe ser un número válido mayor a 0 y menor a 100000000.");
        }

        // Entrada nombre
        while (true) {
            nombre = CFZValidatorUtils.solicitarEntradaPorTeclado("Ingrese el nombre del empleado: ");
            if (!nombre.isEmpty() && nombre.length() <= 100) break;
            else System.out.println("El nombre no puede estar vacío y debe tener menos de 100 caracteres.");
        }

        // Entrada apellido
        while (true) {
            apellido = CFZValidatorUtils.solicitarEntradaPorTeclado("Ingrese el apellido del empleado: ");
            if (!apellido.isEmpty() && apellido.length() <= 100) break;
            else System.out.println("El apellido no puede estar vacío y debe tener menos de 100 caracteres.");
        }

        // Entrada sucursal
        while (true) {
            sucursal = CFZValidatorUtils.solicitarEntradaPorTeclado("Ingrese el nombre de la sucursal: ");
            if (!sucursal.isEmpty() && sucursal.length() <= 100) break;
            else
                System.out.println("El nombre de la sucursal no puede estar vacío y debe tener menos de 100 caracteres.");
        }
        controllerEmpleado.alta(dni, nombre, apellido, sucursal);
    }

    private void inputBaja(ControllerEmpleado controllerEmpleado) {
        int dni;
        // Entrada DNI
        while (true) {
            dni = CFZValidatorUtils.solicitarNumeroPorTeclado("Ingrese el DNI del empleado: ");
            if (dni > 0 && dni < 100000000) break;
            else System.out.println("El DNI debe ser un número válido mayor a 0 y menor a 100000000.");
        }
        controllerEmpleado.baja(dni);
    }

    private void inputModificacion(ControllerEmpleado controllerEmpleado) {
        int dni;
        String nombre;
        String apellido;
        String sucursal;

        // Entrada DNI
        while (true) {
            dni = CFZValidatorUtils.solicitarNumeroPorTeclado("Ingrese el DNI del empleado: ");
            if (dni > 0 && dni < 100000000) break;
            else System.out.println("El DNI debe ser un número válido mayor a 0 y menor a 100000000.");
        }

        // Entrada nombre
        while (true) {
            nombre = CFZValidatorUtils.solicitarEntradaPorTeclado("Ingrese el nombre del empleado: ");
            if (!nombre.isEmpty() && nombre.length() <= 100) break;
            else System.out.println("El nombre no puede estar vacío y debe tener menos de 100 caracteres.");
        }

        // Entrada apellido
        while (true) {
            apellido = CFZValidatorUtils.solicitarEntradaPorTeclado("Ingrese el apellido del empleado: ");
            if (!apellido.isEmpty() && apellido.length() <= 100) break;
            else System.out.println("El apellido no puede estar vacío y debe tener menos de 100 caracteres.");
        }

        // Entrada sucursal
        while (true) {
            sucursal = CFZValidatorUtils.solicitarEntradaPorTeclado("Ingrese el nombre de la sucursal: ");
            if (!sucursal.isEmpty() && sucursal.length() <= 100) break;
            else
                System.out.println("El nombre de la sucursal no puede estar vacío y debe tener menos de 100 caracteres.");
        }

        controllerEmpleado.modificacion(dni, nombre, apellido, sucursal);
    }

    private void inputConsulta(ControllerEmpleado controllerEmpleado) {
        String entrada;
        // Entrada DNI o "todas"
        while (true) {
            System.out.print("Ingrese el DNI del empleado a consultar o 'todas' para ver todos: ");
            entrada = CFZValidatorUtils.solicitarEntradaPorTeclado().trim();

            if (entrada.equalsIgnoreCase("todas")) {
                break;
            }

            try {
                int dni = Integer.parseInt(entrada);
                if (dni > 0 && dni < 100000000) break;
                else System.out.println("El DNI debe ser un número válido mayor a 0 y menor a 100000000.");
            } catch (NumberFormatException e) {
                System.out.println("Debe ingresar un DNI válido (número) o 'todas' para consultar todos los empleados.");
            }
        }
        controllerEmpleado.consulta(entrada);
    }
}
