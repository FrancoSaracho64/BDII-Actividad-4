package ar.edu.unlu.bdd.view;

import ar.edu.unlu.bdd.controller.ControllerSucursal;
import ar.edu.unlu.bdd.utils.CFZValidatorUtils;
import ar.edu.unlu.bdd.utils.Listado;

public class ViewSucursal {
    public ViewSucursal(ControllerSucursal controllerSucursal) {
        int opc;
        do {
            Listado.listarMenu("Sucursal");
            opc = CFZValidatorUtils.solicitarNumeroPorTeclado("Ingrese su opción: ");
            switch (opc) {
                case 1: {
                    inputAlta(controllerSucursal);
                    break;
                }
                case 2: {
                    inputBaja(controllerSucursal);
                    break;
                }
                case 3: {
                    inputModificacion(controllerSucursal);
                    break;
                }
                case 4: {
                    inputConsulta(controllerSucursal);
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


    private void inputAlta(ControllerSucursal controladorSucursal) {
        String nombre;
        String direccion;

        // Entrada nombre
        while (true) {
            nombre = CFZValidatorUtils.solicitarEntradaPorTeclado("Ingrese el nombre de la sucursal: ");
            if (!nombre.isEmpty() && nombre.length() <= 100) break;
            else System.out.println("El nombre no puede estar vacío y debe tener menos de 100 caracteres.");
        }

        // Entrada direccion
        while (true) {
            direccion = CFZValidatorUtils.solicitarEntradaPorTeclado("Ingrese la dirección de la sucursal: ");
            if (!direccion.isEmpty() && direccion.length() <= 100) break;
            else System.out.println("La dirección no puede estar vacía y debe tener menos de 100 caracteres.");
        }
        controladorSucursal.alta(nombre, direccion);
    }

    private void inputBaja(ControllerSucursal controllerSucursal) {
        String nombre;
        // Entrada nombre
        while (true) {
            nombre = CFZValidatorUtils.solicitarEntradaPorTeclado("Ingrese el nombre de la sucursal: ");
            if (!nombre.isEmpty() && nombre.length() <= 100) break;
            else System.out.println("El nombre no puede estar vacío y debe tener menos de 100 caracteres.");
        }
        controllerSucursal.baja(nombre);
    }

    private void inputModificacion(ControllerSucursal controllerSucursal) {
        String nombre;
        String direccion;

        // Entrada nombre
        while (true) {
            nombre = CFZValidatorUtils.solicitarEntradaPorTeclado("Ingrese el nombre de la sucursal: ");
            if (!nombre.isEmpty() && nombre.length() <= 100) break;
            else System.out.println("El nombre no puede estar vacío y debe tener menos de 100 caracteres.");
        }

        // Entrada NACIONALIDAD
        while (true) {
            direccion = CFZValidatorUtils.solicitarEntradaPorTeclado("Ingrese la dirección: ");
            if (!direccion.isEmpty() && direccion.length() <= 100) break;
            else System.out.println("La dirección no puede estar vacía y debe tener menos de 100 caracteres.");
        }

        controllerSucursal.modificacion(nombre, direccion);
    }

    private void inputConsulta(ControllerSucursal controllerSucursal) {
        String nombre;
        // Entrada nombre
        while (true) {
            nombre = CFZValidatorUtils.solicitarEntradaPorTeclado("Ingrese el nombre de la sucursal: ");
            if (!nombre.isEmpty() && nombre.length() <= 100) break;
            else System.out.println("El nombre no puede estar vacío y debe tener menos de 100 caracteres.");
        }
        controllerSucursal.consulta(nombre);

    }
}