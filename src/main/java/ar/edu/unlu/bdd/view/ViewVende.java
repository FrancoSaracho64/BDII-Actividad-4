package ar.edu.unlu.bdd.view;

import ar.edu.unlu.bdd.controller.ControllerVende;
import ar.edu.unlu.bdd.utils.CFZValidatorUtils;
import ar.edu.unlu.bdd.utils.Listado;

/**
 * Vista para la gestión de relaciones Sucursal-Producto.
 * <p>
 * Esta interfaz permite manejar qué productos vende cada sucursal,
 * implementando las operaciones ABMyC con validaciones específicas.
 * <p>
 * Funcionalidades:
 * - Alta: Asociar un producto a una sucursal
 * - Baja: Eliminar la asociación
 * - Modificación: Cambiar qué producto vende una sucursal
 * - Consulta: Buscar relaciones por sucursal, producto o mostrar todas
 */
public class ViewVende {

    public ViewVende(ControllerVende controllerVende) {
        int opc;
        do {
            Listado.listarMenu("Vende");
            opc = CFZValidatorUtils.solicitarNumeroPorTeclado("Ingrese su opción: ");

            switch (opc) {
                case 1: {
                    inputAlta(controllerVende);
                    break;
                }
                case 2: {
                    inputBaja(controllerVende);
                    break;
                }
                case 3: {
                    inputModificacion(controllerVende);
                    break;
                }
                case 4: {
                    inputConsulta(controllerVende);
                    break;
                }
                case 5: {
                    System.out.println("Saliendo del módulo Vende...");
                    break;
                }
                default: {
                    System.out.println("Opción incorrecta. Por favor seleccione una opción válida (1-5).");
                    break;
                }
            }
        } while (opc != 5);
    }

    /**
     * Maneja la entrada de datos para dar de alta una relación sucursal/producto/precio/stock.
     */
    private void inputAlta(ControllerVende controllerVende) {
        System.out.println("\nALTA de Relación Sucursal-Producto");
        System.out.println("═══════════════════════════════════════════");

        String sucursal;
        int codigoProducto;
        double precio;
        int stock;

        // Entrada sucursal
        while (true) {
            sucursal = CFZValidatorUtils.solicitarEntradaPorTeclado("Ingrese el nombre de la sucursal: ");
            if (!sucursal.isEmpty() && sucursal.length() <= 100) break;
            else {
                System.out.println("El nombre de la sucursal no puede estar vacío y debe tener menos de 100 caracteres.");
            }
        }

        // Entrada código producto
        while (true) {
            codigoProducto = CFZValidatorUtils.solicitarNumeroPorTeclado("Ingrese el código del producto: ");
            if (codigoProducto > 0) break;
            else {
                System.out.println("El código debe ser un número válido mayor a 0.");
            }
        }

        // Entrada precio
        while (true) {
            precio = CFZValidatorUtils.solicitarDoublePorTeclado("Ingrese el precio de venta: $");
            if (precio > 0) break;
            else System.out.println("El precio debe ser un número positivo mayor a 0.");
        }

        // Entrada stock
        while (true) {
            stock = CFZValidatorUtils.solicitarNumeroPorTeclado("Ingrese la cantidad en stock: ");
            if (stock >= 0) break;
            else System.out.println("El stock debe ser un número mayor o igual a 0.");
        }

        controllerVende.alta(sucursal, codigoProducto, precio, stock);
        System.out.println();
    }

    /**
     * Maneja la entrada de datos para dar de baja una relación sucursal-producto.
     */
    private void inputBaja(ControllerVende controllerVende) {
        System.out.println("\nBAJA de Relación Sucursal-Producto");
        System.out.println("═══════════════════════════════════════════");

        String sucursal;
        int codigoProducto;

        // Entrada sucursal
        while (true) {
            sucursal = CFZValidatorUtils.solicitarEntradaPorTeclado("Ingrese el nombre de la sucursal: ");
            if (!sucursal.isEmpty() && sucursal.length() <= 100) break;
            else
                System.out.println("El nombre de la sucursal no puede estar vacío y debe tener menos de 100 caracteres.");
        }

        // Entrada código producto
        while (true) {
            codigoProducto = CFZValidatorUtils.solicitarNumeroPorTeclado("Ingrese el código del producto: ");
            if (codigoProducto > 0) break;
            else System.out.println("El código debe ser un número válido mayor a 0.");
        }

        controllerVende.baja(sucursal, codigoProducto);
        System.out.println();
    }

    /**
     * Maneja la entrada de datos para modificar precio y stock de una relación existente.
     */
    private void inputModificacion(ControllerVende controllerVende) {
        System.out.println("\nMODIFICACIÓN de Precio y Stock");
        System.out.println("═══════════════════════════════════════════");
        System.out.println("Esta operación actualiza el precio y stock de una relación existente.");

        String sucursal;
        int codigoProducto;
        double nuevoPrecio;
        int nuevoStock;

        // Entrada sucursal
        while (true) {
            sucursal = CFZValidatorUtils.solicitarEntradaPorTeclado("Ingrese el nombre de la sucursal: ");
            if (!sucursal.isEmpty() && sucursal.length() <= 100) break;
            else
                System.out.println("El nombre de la sucursal no puede estar vacío y debe tener menos de 100 caracteres.");
        }

        // Entrada producto
        while (true) {
            codigoProducto = CFZValidatorUtils.solicitarNumeroPorTeclado("Ingrese el código del producto: ");
            if (codigoProducto > 0) break;
            else System.out.println("El código debe ser un número válido mayor a 0.");
        }

        // Entrada nuevo precio
        while (true) {
            nuevoPrecio = CFZValidatorUtils.solicitarDoublePorTeclado("Ingrese el nuevo precio: $");
            if (nuevoPrecio > 0) break;
            else System.out.println("El precio debe ser un número positivo mayor a 0.");
        }

        // Entrada nuevo stock
        while (true) {
            nuevoStock = CFZValidatorUtils.solicitarNumeroPorTeclado("Ingrese el nuevo stock: ");
            if (nuevoStock >= 0) break;
            else System.out.println("El stock debe ser un número mayor o igual a 0.");
        }

        controllerVende.modificacion(sucursal, codigoProducto, nuevoPrecio, nuevoStock);
        System.out.println();
    }

    /**
     * Maneja las opciones de consulta de relaciones sucursal-producto.
     */
    private void inputConsulta(ControllerVende controllerVende) {
        System.out.println("\nCONSULTA de Relaciones Sucursal-Producto");
        System.out.println("═══════════════════════════════════════════");
        System.out.println("Opciones de consulta:");
        System.out.println("• 'todas' - Mostrar todas las relaciones");
        System.out.println("• Nombre de sucursal - Ver productos de esa sucursal");
        System.out.println("• Código de producto - Ver sucursales que lo venden");

        String consulta;
        while (true) {
            consulta = CFZValidatorUtils.solicitarEntradaPorTeclado("Ingrese su consulta: ");
            if (!consulta.isEmpty()) break;
            else System.out.println("La consulta no puede estar vacía.");
        }

        controllerVende.consulta(consulta);
        System.out.println();
    }
}
