package ar.edu.unlu.bdd.menu;

import ar.edu.unlu.bdd.controller.ControllerEmpleado;
import ar.edu.unlu.bdd.controller.ControllerProducto;
import ar.edu.unlu.bdd.controller.ControllerSucursal;
import ar.edu.unlu.bdd.controller.ControllerVende;
import ar.edu.unlu.bdd.utils.CFZValidatorUtils;
import com.mongodb.client.MongoDatabase;


public class Menu {
    public Menu(MongoDatabase database) {
        int opc;
        do {
            System.out.println("\n----------------------------------------------");
            System.out.println("Actividad 4 - BD2");
            System.out.println("Seleccione la tabla con la que desea operar:");
            System.out.println("1 - Empleado");
            System.out.println("2 - Producto");
            System.out.println("3 - Sucursal");
            System.out.println("4 - Productos a vender");
            System.out.println("5 - Salir");

            opc = CFZValidatorUtils.solicitarNumeroPorTeclado("Ingrese su opción: ");

            switch (opc) {
                case 1:
                    operarEmpleados(database);
                    break;
                case 2:
                    operarProductos(database);
                    break;
                case 3:
                    operarSucursales(database);
                    break;
                case 4:
                    operarProductosAVender(database);
                    break;
                case 5:
                    System.out.println("Saliendo...");
                    break;
                default:
                    System.out.println("Opción incorrecta.");
            }
        } while (opc != 5);
    }

    private void operarProductosAVender(MongoDatabase database) {
        ControllerVende controller = new ControllerVende(database);
        controller.iniciarVista();
    }

    private void operarSucursales(MongoDatabase database) {
        ControllerSucursal controller = new ControllerSucursal(database);
        controller.iniciarVista();
    }

    private void operarProductos(MongoDatabase database) {
        ControllerProducto controller = new ControllerProducto(database);
        controller.iniciarVista();
    }

    private void operarEmpleados(MongoDatabase database) {
        ControllerEmpleado controller = new ControllerEmpleado(database);
        controller.iniciarVista();
    }
}