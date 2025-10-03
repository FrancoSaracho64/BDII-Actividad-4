package ar.edu.unlu.bdd.controller;

import ar.edu.unlu.bdd.view.ViewEmpleado;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Updates.combine;
import static com.mongodb.client.model.Updates.set;

public class ControllerEmpleado {
    private static final Logger logger = LoggerFactory.getLogger(ControllerEmpleado.class);
    private final MongoCollection<Document> empleadosCollection;
    private final MongoDatabase database;

    public ControllerEmpleado(MongoDatabase database) {
        this.database = database;
        this.empleadosCollection = database.getCollection("empleados");
    }

    public void iniciarVista() {
        new ViewEmpleado(this);
    }

    public void alta(int dni, String nombre, String apellido, String sucursal) {
        try {
            // Verificar que el DNI no existe ya
            if (verificarExistenciaEmpleado(dni)) {
                System.out.println("Error: Ya existe un empleado con el DNI '" + dni + "'. No se puede crear duplicado.");
                return;
            }

            // Verificar que la sucursal existe
            if (!verificarExistenciaSucursal(sucursal)) {
                System.out.println("Error: La sucursal '" + sucursal + "' no existe. No se puede crear el empleado.");
                return;
            }

            Document nuevoEmpleado = new Document("dni", dni)
                    .append("nombre", nombre)
                    .append("apellido", apellido)
                    .append("sucursal", sucursal);

            empleadosCollection.insertOne(nuevoEmpleado);
            System.out.println("Empleado '" + nombre + " " + apellido + "' agregado con éxito.");
        } catch (Exception e) {
            logger.error("Error al dar de alta el empleado: {}", e.getMessage(), e);
        }
    }

    public void baja(int dni) {
        try {
            DeleteResult result = empleadosCollection.deleteOne(eq("dni", dni));

            if (result.getDeletedCount() > 0) {
                System.out.println("Empleado con DNI '" + dni + "' eliminado con éxito. Documentos eliminados: " + result.getDeletedCount());
            } else {
                logger.warn("No se encontró un empleado con el DNI: {}. No se eliminaron documentos.", dni);
            }
        } catch (Exception e) {
            logger.error("Error al dar de baja el empleado: {}", e.getMessage(), e);
        }
    }

    public void modificacion(int dni, String nombre, String apellido, String sucursal) {
        try {
            // Verificar que la sucursal existe
            if (!verificarExistenciaSucursal(sucursal)) {
                System.out.println("Error: La sucursal '" + sucursal + "' no existe. No se puede modificar el empleado.");
                return;
            }

            UpdateResult result = empleadosCollection.updateOne(
                    eq("dni", dni),
                    combine(
                            set("nombre", nombre),
                            set("apellido", apellido),
                            set("sucursal", sucursal)
                    )
            );

            if (result.getModifiedCount() > 0) {
                System.out.println("Empleado con DNI '" + dni + "' modificado con éxito. Campos actualizados: " + result.getModifiedCount());
            } else {
                logger.warn("No se encontró un empleado con el DNI: {}. No se realizó ninguna modificación.", dni);
            }
        } catch (Exception e) {
            logger.error("Error al modificar el empleado: {}", e.getMessage(), e);
        }
    }

    /**
     * Consulta un empleado por DNI o muestra todos si el parámetro es "todos".
     *
     * @param dni El DNI del empleado a buscar, o "todos" para listar todos.
     */
    public void consulta(String dni) {
        Bson filtro;

        try {
            if ("todas".equalsIgnoreCase(dni)) {
                filtro = new Document();
            } else {
                int dniInt = Integer.parseInt(dni);
                filtro = eq("dni", dniInt);
            }

            try (MongoCursor<Document> cursor = empleadosCollection.find(filtro).iterator()) {

                if (!cursor.hasNext() && !"todas".equalsIgnoreCase(dni)) {
                    logger.warn("No se encontró ningún empleado con el DNI: {}", dni);
                    return;
                } else if (!cursor.hasNext()) {
                    System.out.println("La colección 'empleados' está vacía.");
                    return;
                }

                System.out.println("Resultados de la consulta:");
                while (cursor.hasNext()) {
                    Document empleado = cursor.next();
                    System.out.println(" - DNI: " + empleado.getInteger("dni")
                            + " | Nombre: " + empleado.getString("nombre")
                            + " | Apellido: " + empleado.getString("apellido")
                            + " | Sucursal: " + empleado.getString("sucursal")
                            + " | ID: " + empleado.getObjectId("_id"));
                }

                System.out.println("Consulta finalizada.");

            } catch (Exception e) {
                logger.error("Error al consultar los empleados: {}", e.getMessage(), e);
            }
        } catch (NumberFormatException e) {
            logger.warn("El DNI debe ser un número válido o 'todas' para consultar todos los empleados.");
        }
    }

    /**
     * Verifica si una sucursal existe en la base de datos.
     *
     * @param nombreSucursal Nombre de la sucursal a verificar
     * @return true si la sucursal existe, false en caso contrario
     */
    private boolean verificarExistenciaSucursal(String nombreSucursal) {
        try {
            // Conectar a la colección de sucursales
            MongoCollection<Document> sucursalesCollection = database.getCollection("sucursales");

            // Buscar la sucursal por nombre
            Document sucursal = sucursalesCollection.find(eq("nombre", nombreSucursal)).first();

            return sucursal != null;
        } catch (Exception e) {
            logger.error("Error al verificar la existencia de la sucursal '{}': {}", nombreSucursal, e.getMessage(), e);
            return false;
        }
    }

    /**
     * Verifica si un empleado con el DNI dado ya existe.
     *
     * @param dni DNI del empleado a verificar
     * @return true si ya existe, false en caso contrario
     */
    private boolean verificarExistenciaEmpleado(int dni) {
        try {
            Document empleado = empleadosCollection.find(eq("dni", dni)).first();
            return empleado != null;
        } catch (Exception e) {
            logger.error("Error al verificar la existencia del empleado '{}': {}", dni, e.getMessage(), e);
            return false;
        }
    }
}
