package ar.edu.unlu.bdd.controller;

import ar.edu.unlu.bdd.view.ViewProducto;
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

public class ControllerProducto {
    private static final Logger logger = LoggerFactory.getLogger(ControllerProducto.class);
    private final MongoCollection<Document> productosCollection;

    public ControllerProducto(MongoDatabase database) {
        this.productosCollection = database.getCollection("productos");
    }

    public void iniciarVista() {
        new ViewProducto(this);
    }

    public void alta(int codigo, String descripcion, int stock) {
        try {
            // Verificar que el producto no existe ya
            if (verificarExistenciaProducto(codigo)) {
                System.out.println("Error: Ya existe un producto con el código '" + codigo + "'. No se puede crear duplicado.");
                return;
            }

            Document nuevoProducto = new Document("codigo", codigo)
                    .append("descripcion", descripcion)
                    .append("stock", stock);

            productosCollection.insertOne(nuevoProducto);
            System.out.println("Producto '" + descripcion + "' agregado con éxito.");
        } catch (Exception e) {
            logger.error("Error al dar de alta el producto: {}", e.getMessage(), e);
        }
    }

    public void baja(int codigo) {
        try {
            DeleteResult result = productosCollection.deleteOne(eq("codigo", codigo));

            if (result.getDeletedCount() > 0) {
                System.out.println("Producto con código '" + codigo + "' eliminado con éxito. Documentos eliminados: " + result.getDeletedCount());
            } else {
                logger.warn("No se encontró un producto con el código: {}. No se eliminaron documentos.", codigo);
            }
        } catch (Exception e) {
            logger.error("Error al dar de baja el producto: {}", e.getMessage(), e);
        }
    }

    public void modificacion(int codigo, String descripcion, int stock) {
        try {
            UpdateResult result = productosCollection.updateOne(
                    eq("codigo", codigo),
                    combine(
                            set("descripcion", descripcion),
                            set("stock", stock)
                    )
            );

            if (result.getModifiedCount() > 0) {
                System.out.println("Producto con código '" + codigo + "' modificado con éxito. Campos actualizados: " + result.getModifiedCount());
            } else {
                logger.warn("No se encontró un producto con el código: {}. No se realizó ninguna modificación.", codigo);
            }
        } catch (Exception e) {
            logger.error("Error al modificar el producto: {}", e.getMessage(), e);
        }
    }

    /**
     * Consulta un producto por código o muestra todos si el parámetro es "todos".
     *
     * @param codigo El código del producto a buscar, o "todos" para listar todos.
     */
    public void consulta(String codigo) {
        Bson filtro;

        try {
            if ("todas".equalsIgnoreCase(codigo)) {
                filtro = new Document();
            } else {
                int codigoInt = Integer.parseInt(codigo);
                filtro = eq("codigo", codigoInt);
            }

            try (MongoCursor<Document> cursor = productosCollection.find(filtro).iterator()) {

                if (!cursor.hasNext() && !"todas".equalsIgnoreCase(codigo)) {
                    logger.warn("No se encontró ningún producto con el código: {}", codigo);
                    return;
                } else if (!cursor.hasNext()) {
                    System.out.println("La colección 'productos' está vacía.");
                    return;
                }

                System.out.println("Resultados de la consulta:");
                while (cursor.hasNext()) {
                    Document producto = cursor.next();
                    System.out.println(" - Código: " + producto.getInteger("codigo")
                            + " | Descripción: " + producto.getString("descripcion")
                            + " | Stock: " + producto.getInteger("stock")
                            + " | ID: " + producto.getObjectId("_id"));
                }

                System.out.println("Consulta finalizada.");

            } catch (Exception e) {
                logger.error("Error al consultar los productos: {}", e.getMessage(), e);
            }
        } catch (NumberFormatException e) {
            logger.warn("El código debe ser un número válido o 'todas' para consultar todos los productos.");
        }
    }

    /**
     * Verifica si un producto con el código dado ya existe.
     *
     * @param codigo Código del producto a verificar
     * @return true si ya existe, false en caso contrario
     */
    private boolean verificarExistenciaProducto(int codigo) {
        try {
            Document producto = productosCollection.find(eq("codigo", codigo)).first();
            return producto != null;
        } catch (Exception e) {
            logger.error("Error al verificar la existencia del producto '{}': {}", codigo, e.getMessage(), e);
            return false;
        }
    }
}
