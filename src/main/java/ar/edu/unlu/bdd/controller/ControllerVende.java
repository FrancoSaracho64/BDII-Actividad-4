package ar.edu.unlu.bdd.controller;

import ar.edu.unlu.bdd.view.ViewVende;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Updates.combine;
import static com.mongodb.client.model.Updates.set;

/**
 * Controller para manejar las operaciones CRUD de la entidad Vende.
 * <p>
 * La entidad Vende representa la relación entre Sucursal y Producto.
 * Una sucursal puede vender múltiples productos.
 * Un producto puede ser vendido por múltiples sucursales.
 * <p>
 * Operaciones implementadas:
 * - Alta: Asociar un producto a una sucursal
 * - Baja: Eliminar la asociación producto-sucursal
 * - Modificación: Cambiar la sucursal de un producto ya asociado
 * - Consulta: Buscar relaciones específicas o listar todas
 * <p>
 * Validaciones aplicadas:
 * - La sucursal debe existir
 * - El producto debe existir
 * - La combinación sucursal-producto debe ser única
 */
public class ControllerVende {
    private static final Logger logger = LoggerFactory.getLogger(ControllerVende.class);
    private final MongoCollection<Document> vendeCollection;
    private final MongoDatabase database;

    public ControllerVende(MongoDatabase database) {
        this.database = database;
        this.vendeCollection = database.getCollection("vende");
    }

    public void iniciarVista() {
        new ViewVende(this);
    }

    /**
     * Da de alta una relación sucursal-producto con precio y stock específicos.
     * Valida que tanto la sucursal como el producto existan.
     *
     * @param sucursal       Nombre de la sucursal
     * @param codigoProducto Código del producto
     * @param precio         Precio de venta para esta sucursal
     * @param stock          Cantidad en stock para esta sucursal
     */
    public void alta(String sucursal, int codigoProducto, double precio, int stock) {
        try {
            // Validar que la sucursal existe
            if (!verificarExistenciaSucursal(sucursal)) {
                System.out.println("Error: La sucursal '" + sucursal + "' no existe. No se puede crear la relación.");
                return;
            }

            // Validar que el producto existe
            if (!verificarExistenciaProducto(codigoProducto)) {
                System.out.println("Error: El producto con código '" + codigoProducto + "' no existe. No se puede crear la relación.");
                return;
            }

            // Validar que la relación no existe ya
            if (verificarExistenciaRelacion(sucursal, codigoProducto)) {
                System.out.println("Error: La relación sucursal '" + sucursal + "' - producto '" + codigoProducto + "' ya existe.");
                return;
            }

            Document nuevaRelacion = new Document("sucursal", sucursal)
                    .append("codigoProducto", codigoProducto)
                    .append("precio", precio)
                    .append("stock", stock);

            vendeCollection.insertOne(nuevaRelacion);
            System.out.println("Relación creada: Sucursal '" + sucursal + "' ahora vende el producto '" + codigoProducto +
                    "' por $" + precio + " (Stock: " + stock + " unidades).");

        } catch (Exception e) {
            logger.error("Error al dar de alta la relación sucursal-producto: {}", e.getMessage(), e);
        }
    }

    /**
     * Da de baja una relación sucursal-producto específica.
     *
     * @param sucursal       Nombre de la sucursal
     * @param codigoProducto Código del producto
     */
    public void baja(String sucursal, int codigoProducto) {
        try {
            DeleteResult result = vendeCollection.deleteOne(
                    and(eq("sucursal", sucursal), eq("codigoProducto", codigoProducto))
            );

            if (result.getDeletedCount() > 0) {
                System.out.println("Relación eliminada: Sucursal '" + sucursal + "' ya no vende el producto '" + codigoProducto + "'.");
                System.out.println("Documentos eliminados: " + result.getDeletedCount());
            } else {
                logger.warn("No se encontró la relación sucursal '{}' - producto '{}'. No se eliminaron documentos.", sucursal, codigoProducto);
            }
        } catch (Exception e) {
            logger.error("Error al dar de baja la relación sucursal-producto: {}", e.getMessage(), e);
        }
    }

    /**
     * Modifica una relación existente actualizando precio y stock.
     *
     * @param sucursal       Nombre de la sucursal
     * @param codigoProducto Código del producto
     * @param nuevoPrecio    Nuevo precio de venta
     * @param nuevoStock     Nueva cantidad en stock
     */
    public void modificacion(String sucursal, int codigoProducto, double nuevoPrecio, int nuevoStock) {
        try {
            // Validar que la relación existe
            if (!verificarExistenciaRelacion(sucursal, codigoProducto)) {
                System.out.println("Error: No existe la relación sucursal '" + sucursal + "' - producto '" + codigoProducto + "'.");
                return;
            }

            UpdateResult result = vendeCollection.updateOne(
                    and(eq("sucursal", sucursal), eq("codigoProducto", codigoProducto)),
                    combine(set("precio", nuevoPrecio), set("stock", nuevoStock))
            );

            if (result.getModifiedCount() > 0) {
                System.out.println("Relación modificada: Sucursal '" + sucursal + "' - Producto '" + codigoProducto +
                        "' actualizado. Nuevo precio: $" + nuevoPrecio + ", Nuevo stock: " + nuevoStock + " unidades.");
                System.out.println("Documentos modificados: " + result.getModifiedCount());
            } else {
                logger.warn("No se encontró la relación sucursal '{}' - producto '{}'. No se realizó ninguna modificación.", sucursal, codigoProducto);
            }
        } catch (Exception e) {
            logger.error("Error al modificar la relación sucursal-producto: {}", e.getMessage(), e);
        }
    }

    /**
     * Consulta las relaciones sucursal-producto.
     *
     * @param consulta Tipo de consulta: "todas", nombre de sucursal, o código de producto como string
     */
    public void consulta(String consulta) {
        try {
            MongoCursor<Document> cursor;

            if ("todas".equalsIgnoreCase(consulta)) {
                // Mostrar todas las relaciones
                cursor = vendeCollection.find().iterator();

                if (!cursor.hasNext()) {
                    System.out.println("No hay relaciones sucursal-producto registradas.");
                    return;
                }

                System.out.println("Todas las relaciones sucursal-producto:");

            } else {
                // Intentar interpretar como código de producto (número)
                try {
                    int codigoProducto = Integer.parseInt(consulta);
                    cursor = vendeCollection.find(eq("codigoProducto", codigoProducto)).iterator();

                    if (!cursor.hasNext()) {
                        System.out.println("El producto '" + codigoProducto + "' no está siendo vendido por ninguna sucursal.");
                        return;
                    }

                    System.out.println("Sucursales que venden el producto '" + codigoProducto + "':");

                } catch (NumberFormatException e) {
                    // Interpretar como nombre de sucursal
                    cursor = vendeCollection.find(eq("sucursal", consulta)).iterator();

                    if (!cursor.hasNext()) {
                        System.out.println("La sucursal '" + consulta + "' no vende ningún producto o no existe.");
                        return;
                    }

                    System.out.println("Productos vendidos por la sucursal '" + consulta + "':");
                }
            }

            System.out.println("═══════════════════════════════════════════════════════════════════════");
            while (cursor.hasNext()) {
                Document relacion = cursor.next();
                System.out.println(" - Sucursal: " + relacion.getString("sucursal")
                        + " | Producto: " + relacion.getInteger("codigoProducto")
                        + " | Precio: $" + relacion.getDouble("precio")
                        + " | Stock: " + relacion.getInteger("stock") + " unidades"
                        + " | ID: " + relacion.getObjectId("_id"));
            }
            System.out.println("═══════════════════════════════════════════════════════════════════════");
            System.out.println("Consulta finalizada.");

        } catch (Exception e) {
            logger.error("Error al consultar las relaciones sucursal-producto: {}", e.getMessage(), e);
        }
    }

    /**
     * Verifica si una sucursal existe en la base de datos.
     */
    private boolean verificarExistenciaSucursal(String nombreSucursal) {
        try {
            MongoCollection<Document> sucursalesCollection = database.getCollection("sucursales");
            Document sucursal = sucursalesCollection.find(eq("nombre", nombreSucursal)).first();
            return sucursal != null;
        } catch (Exception e) {
            logger.error("Error al verificar la existencia de la sucursal '{}': {}", nombreSucursal, e.getMessage(), e);
            return false;
        }
    }

    /**
     * Verifica si un producto existe en la base de datos.
     */
    private boolean verificarExistenciaProducto(int codigoProducto) {
        try {
            MongoCollection<Document> productosCollection = database.getCollection("productos");
            Document producto = productosCollection.find(eq("codigo", codigoProducto)).first();
            return producto != null;
        } catch (Exception e) {
            logger.error("Error al verificar la existencia del producto '{}': {}", codigoProducto, e.getMessage(), e);
            return false;
        }
    }

    /**
     * Verifica si ya existe una relación entre una sucursal y un producto.
     */
    private boolean verificarExistenciaRelacion(String sucursal, int codigoProducto) {
        try {
            Document relacion = vendeCollection.find(
                    and(eq("sucursal", sucursal), eq("codigoProducto", codigoProducto))
            ).first();
            return relacion != null;
        } catch (Exception e) {
            logger.error("Error al verificar la existencia de la relación sucursal '{}' - producto '{}': {}",
                    sucursal, codigoProducto, e.getMessage(), e);
            return false;
        }
    }
}
