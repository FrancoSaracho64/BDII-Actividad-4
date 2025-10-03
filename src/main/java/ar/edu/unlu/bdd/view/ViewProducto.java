package ar.edu.unlu.bdd.view;

import ar.edu.unlu.bdd.controller.ControllerProducto;
import ar.edu.unlu.bdd.utils.CFZValidatorUtils;
import ar.edu.unlu.bdd.utils.Listado;

public class ViewProducto {
    public ViewProducto(ControllerProducto controllerProducto) {
        int opc;
        do {
            Listado.listarMenu("Producto");
            opc = CFZValidatorUtils.solicitarNumeroPorTeclado("Ingrese su opción: ");
            switch (opc) {
                case 1: {
                    inputAlta(controllerProducto);
                    break;
                }
                case 2: {
                    inputBaja(controllerProducto);
                    break;
                }
                case 3: {
                    inputModificacion(controllerProducto);
                    break;
                }
                case 4: {
                    inputConsulta(controllerProducto);
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


    private void inputAlta(ControllerProducto controllerProducto) {
        int codigo;
        String descripcion;
        int stock;

        // Entrada código
        while (true) {
            codigo = CFZValidatorUtils.solicitarNumeroPorTeclado("Ingrese el código del producto: ");
            if (codigo > 0) break;
            else System.out.println("El código debe ser un número válido mayor a 0.");
        }

        // Entrada descripción
        while (true) {
            descripcion = CFZValidatorUtils.solicitarEntradaPorTeclado("Ingrese la descripción del producto: ");
            if (!descripcion.isEmpty() && descripcion.length() <= 100) break;
            else System.out.println("La descripción no puede estar vacía y debe tener menos de 100 caracteres.");
        }

        // Entrada stock
        while (true) {
            stock = CFZValidatorUtils.solicitarNumeroPorTeclado("Ingrese el stock del producto: ");
            if (stock >= 0) break;
            else System.out.println("El stock debe ser un número válido mayor o igual a 0.");
        }
        controllerProducto.alta(codigo, descripcion, stock);
    }

    private void inputBaja(ControllerProducto controllerProducto) {
        int codigo;
        // Conteo de productos antes de eliminar

        // Entrada código
        while (true) {
            codigo = CFZValidatorUtils.solicitarNumeroPorTeclado("Ingrese el código del producto: ");
            if (codigo > 0) break;
            else System.out.println("El código debe ser un número válido mayor a 0.");
        }
        controllerProducto.baja(codigo);
    }

    private void inputModificacion(ControllerProducto controllerProducto) {
        int codigo;
        String descripcion;
        int stock;

        // Entrada código
        while (true) {
            codigo = CFZValidatorUtils.solicitarNumeroPorTeclado("Ingrese el código del producto: ");
            if (codigo > 0) break;
            else System.out.println("El código debe ser un número válido mayor a 0.");
        }

        // Entrada descripción
        while (true) {
            descripcion = CFZValidatorUtils.solicitarEntradaPorTeclado("Ingrese la nueva descripción del producto: ");
            if (!descripcion.isEmpty() && descripcion.length() <= 100) break;
            else System.out.println("La descripción no puede estar vacía y debe tener menos de 100 caracteres.");
        }

        // Entrada stock
        while (true) {
            stock = CFZValidatorUtils.solicitarNumeroPorTeclado("Ingrese el nuevo stock del producto: ");
            if (stock >= 0) break;
            else System.out.println("El stock debe ser un número válido mayor o igual a 0.");
        }

        controllerProducto.modificacion(codigo, descripcion, stock);
    }

    private void inputConsulta(ControllerProducto controllerProducto) {
        String entrada;
        // Entrada código o "todas"
        while (true) {
            System.out.print("Ingrese el código del producto a consultar o 'todas' para ver todos: ");
            entrada = CFZValidatorUtils.solicitarEntradaPorTeclado().trim();

            if (entrada.equalsIgnoreCase("todas")) {
                break;
            }

            try {
                int codigo = Integer.parseInt(entrada);
                if (codigo > 0) break;
                else System.out.println("El código debe ser un número válido mayor a 0.");
            } catch (NumberFormatException e) {
                System.out.println("Debe ingresar un código válido (número) o 'todas' para consultar todos los productos.");
            }
        }
        controllerProducto.consulta(entrada);
    }
}
