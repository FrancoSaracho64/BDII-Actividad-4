package ar.edu.unlu.bdd.controller;

import ar.edu.unlu.bdd.view.ViewSucursal;
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
import static com.mongodb.client.model.Updates.set;

public class ControllerSucursal {
    private static final Logger logger = LoggerFactory.getLogger(ControllerSucursal.class);
    private final MongoCollection<Document> sucursalesCollection;

    public ControllerSucursal(MongoDatabase database) {
        this.sucursalesCollection = database.getCollection("sucursales");
    }

    public void iniciarVista() {
        new ViewSucursal(this);
    }

    public void alta(String nombre, String direccion) {
        try {
            Document nuevaSucursal = new Document("nombre", nombre)
                    .append("direccion", direccion);

            sucursalesCollection.insertOne(nuevaSucursal);
            System.out.println("Sucursal '" + nombre + "' agregada con éxito.");
        } catch (Exception e) {
            logger.error("Error al dar de alta la sucursal: {}", e.getMessage(), e);
        }
    }

    public void baja(String nombre) {
        try {
            DeleteResult result = sucursalesCollection.deleteOne(eq("nombre", nombre));

            if (result.getDeletedCount() > 0) {
                System.out.println("Sucursal '" + nombre + "' eliminada con éxito. Documentos eliminados: " + result.getDeletedCount());
            } else {
                logger.warn("No se encontró una sucursal con el nombre: {}. No se eliminaron documentos.", nombre);
            }
        } catch (Exception e) {
            logger.error("Error al dar de baja la sucursal: {}", e.getMessage(), e);
        }
    }

    public void modificacion(String nombre, String direccion) {
        try {
            UpdateResult result = sucursalesCollection.updateOne(
                    eq("nombre", nombre),
                    set("direccion", direccion)
            );

            if (result.getModifiedCount() > 0) {
                System.out.println("Sucursal '" + nombre + "' modificada con éxito. Campos actualizados: " + result.getModifiedCount());
            } else {
                logger.warn("No se encontró una sucursal con el nombre: {}. No se realizó ninguna modificación.", nombre);
            }
        } catch (Exception e) {
            logger.error("Error al modificar la sucursal: {}", e.getMessage(), e);
        }
    }

    /**
     * Consulta una sucursal por nombre o muestra todas si el parámetro es "todas".
     * @param nombre El nombre de la sucursal a buscar, o "todas" para listar todas.
     */
    public void consulta(String nombre) {
        Bson filtro = (nombre != null && nombre.equalsIgnoreCase("todas"))
                ? new Document()
                : eq("nombre", nombre);

        try (MongoCursor<Document> cursor = sucursalesCollection.find(filtro).iterator()) {

            if (!cursor.hasNext() && !"todas".equalsIgnoreCase(nombre)) {
                logger.warn("No se encontró ninguna sucursal con el nombre: {}", nombre);
                return;
            } else if (!cursor.hasNext()) {
                System.out.println("La colección 'sucursales' está vacía.");
                return;
            }

            System.out.println("Resultados de la consulta:");
            while (cursor.hasNext()) {
                Document sucursal = cursor.next();
                System.out.println(" - Nombre: " + sucursal.getString("nombre")
                        + " | Dirección: " + sucursal.getString("direccion")
                        + " | ID: " + sucursal.getObjectId("_id"));
            }

            System.out.println("Consulta finalizada.");

        } catch (Exception e) {
            logger.error("Error al consultar las sucursales: {}", e.getMessage(), e);
        }
    }
}
