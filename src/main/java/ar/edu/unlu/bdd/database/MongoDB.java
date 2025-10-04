package ar.edu.unlu.bdd.database;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoIterable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Clase Singleton para gestionar la conexión a la base de datos MongoDB.
 * Maneja la creación, eliminación y verificaciones de la base de datos.
 */
public class MongoDB {

    private static final Logger logger = LoggerFactory.getLogger(MongoDB.class);

    // --- Variables de Conexión ---
    private static final String CONNECTION_STRING = "mongodb://localhost:27017";
    private static final String DATABASE_NAME = "miMongoDB";

    // --- Singleton Instance ---
    private static MongoDB instance;

    // --- Objetos de Conexión Reutilizables ---
    private MongoClient mongoClient;
    private MongoDatabase database;

    /**
     * Constructor privado para asegurar el patrón Singleton.
     * Inicializa la conexión con el servidor y gestiona la base de datos.
     */
    private MongoDB() {
        try {
            System.out.println("\n=== INICIALIZACIÓN DE MONGODB ===");

            // 1. Conectar al servidor MongoDB
            mongoClient = MongoClients.create(CONNECTION_STRING);

            // 2. Gestión de la base de datos
            gestionarBaseDatos();

            System.out.println("=== CONEXIÓN ESTABLECIDA EXITOSAMENTE ===\n");

        } catch (Exception e) {
            System.err.println("Error al conectar a MongoDB. Asegúrate de que el servidor esté corriendo en " + CONNECTION_STRING);
            logger.error("Error en inicialización de MongoDB: {}", e.getMessage(), e);
        }
    }

    /**
     * Gestiona la creación o eliminación de la base de datos.
     */
    private void gestionarBaseDatos() {
        try {
            boolean dbExiste = verificarExistenciaBaseDatos();

            if (dbExiste) {
                System.out.println("Base de datos '" + DATABASE_NAME + "' ya existe.");
                System.out.println("Se encontraron las siguientes colecciones:");
                mostrarColecciones();

                // Preguntar al usuario qué hacer
                if (solicitarDecisionUsuario()) {
                    eliminarBaseDatos();
                    System.out.println("Base de datos eliminada. Creando nueva base de datos...");
                } else {
                    System.out.println("Usando base de datos existente.");
                }
            } else {
                System.out.println("Base de datos '" + DATABASE_NAME + "' no existe. Creando nueva base de datos...");
            }

            // Obtener/crear referencia a la base de datos
            database = mongoClient.getDatabase(DATABASE_NAME);

        } catch (Exception e) {
            logger.error("Error al gestionar la base de datos: {}", e.getMessage(), e);
            throw e;
        }
    }

    /**
     * Verifica si la base de datos existe listando todas las bases de datos.
     *
     * @return true si la base de datos existe, false en caso contrario
     */
    private boolean verificarExistenciaBaseDatos() {
        try {
            MongoIterable<String> databaseNames = mongoClient.listDatabaseNames();

            for (String dbName : databaseNames) {
                if (DATABASE_NAME.equals(dbName)) {
                    return true;
                }
            }
            return false;

        } catch (Exception e) {
            logger.error("Error al verificar existencia de base de datos: {}", e.getMessage(), e);
            return false;
        }
    }

    /**
     * Lista las colecciones de la base de datos.
     */
    private void mostrarColecciones() {
        try {
            if (verificarExistenciaBaseDatos()) {
                // Crear referencia temporal para listar colecciones
                MongoDatabase tempDb = mongoClient.getDatabase(DATABASE_NAME);
                MongoIterable<String> collections = tempDb.listCollectionNames();

                int count = 0;
                for (String collectionName : collections) {
                    System.out.println("    " + collectionName);
                    count++;
                }

                if (count == 0) {
                    System.out.println("No hay colecciones en la base de datos.");
                }
            }
        } catch (Exception e) {
            logger.error("Error al mostrar colecciones: {}", e.getMessage(), e);
        }
    }

    /**
     * Solicita al usuario que decida qué hacer con la base de datos existente.
     *
     * @return true si el usuario quiere eliminar y recrear, false si quiere usar la existente
     */
    private boolean solicitarDecisionUsuario() {
        System.out.println("\n¿Qué deseas hacer con la base de datos existente?");
        System.out.println("1) Usar la base de datos existente");
        System.out.println("2) Eliminar y empezar desde cero");

        while (true) {
            try {
                int opcion = ar.edu.unlu.bdd.utils.CFZValidatorUtils.solicitarNumeroPorTeclado("Ingrese su opción (1-2): ");

                if (opcion == 1) {
                    return false;
                } else if (opcion == 2) {
                    // Confirmar eliminación
                    System.out.println("\nATENCIÓN: Esta acción eliminará TODOS los datos de la base de datos '" + DATABASE_NAME + "'");
                    String confirmacion = ar.edu.unlu.bdd.utils.CFZValidatorUtils.solicitarEntradaPorTeclado("¿Está seguro que desea continuar? (si/no): ");

                    if ("si".equals(confirmacion.toLowerCase()) || "s".equals(confirmacion.toLowerCase()) || "yes".equals(confirmacion.toLowerCase()) || "y".equals(confirmacion.toLowerCase())) {
                        return true;
                    } else {
                        System.out.println("Eliminación cancelada. Usando base de datos existente.");
                        return false;
                    }
                } else {
                    System.out.println("Opción inválida. Por favor ingrese 1 o 2.");
                }
            } catch (Exception e) {
                System.out.println("Error en entrada. Intente nuevamente.");
            }
        }
    }

    /**
     * Elimina completamente la base de datos.
     */
    private void eliminarBaseDatos() {
        try {
            mongoClient.getDatabase(DATABASE_NAME).drop();
            logger.info("Base de datos '{}' eliminada completamente.", DATABASE_NAME);
            System.out.println("Base de datos '" + DATABASE_NAME + "' eliminada exitosamente.");

        } catch (Exception e) {
            logger.error("Error al eliminar la base de datos: {}", e.getMessage(), e);
            System.err.println("Error al eliminar la base de datos: " + e.getMessage());
            throw e;
        }
    }

    // -------------------------------------------------------------------------
    // --- Métodos Públicos para Acceso ---
    // -------------------------------------------------------------------------

    /**
     * Devuelve la única instancia de la clase MongoDB (Singleton).
     *
     * @return La instancia de la conexión a MongoDB.
     */
    public static MongoDB getInstance() {
        if (instance == null) {
            instance = new MongoDB();
        }
        return instance;
    }

    /**
     * Devuelve el objeto MongoDatabase para realizar operaciones.
     * Este es el método que usarás para acceder a las colecciones.
     *
     * @return El objeto MongoDatabase.
     */
    public MongoDatabase getDatabase() {
        return database;
    }

    /**
     * Método opcional para cerrar la conexión con el servidor.
     * Ideal para llamarlo al cerrar la aplicación.
     */
    public void closeConnection() {
        if (mongoClient != null) {
            mongoClient.close();
            System.out.println("Conexión a MongoDB cerrada.");
        }
    }

    /**
     * Método público para eliminar la base de datos manualmente.
     * Útil para casos específicos donde se necesite resetear datos.
     */
    public void eliminarBaseDatosManualmente() {
        eliminarBaseDatos();
        // Recrear referencia después de eliminar
        database = mongoClient.getDatabase(DATABASE_NAME);
    }

    /**
     * Verifica si existen datos en alguna colección de la base de datos.
     *
     * @return true si hay al menos un elemento en alguna colección
     */
    public boolean tieneDatos() {
        try {
            if (verificarExistenciaBaseDatos()) {
                MongoIterable<String> collectionNames = database.listCollectionNames();

                for (String collectionName : collectionNames) {
                    long count = database.getCollection(collectionName).countDocuments();
                    if (count > 0) {
                        return true;
                    }
                }
            }
            return false;
        } catch (Exception e) {
            logger.error("Error al verificar si la base de datos tiene datos: {}", e.getMessage(), e);
            return false;
        }
    }

    /**
     * Muestra estadísticas de la base de datos actual.
     */
    public void mostrarEstadisticas() {
        try {
            System.out.println("\nESTADÍSTICAS DE LA BASE DE DATOS '" + DATABASE_NAME + "':");
            System.out.println("=============================================================");

            MongoIterable<String> collectionNames = database.listCollectionNames();
            int totalColecciones = 0;
            long totalDocumentos = 0;

            for (String collectionName : collectionNames) {
                long count = database.getCollection(collectionName).countDocuments();
                System.out.println("Colección '" + collectionName + "': " + count + " documentos");
                totalDocumentos += count;
                totalColecciones++;
            }

            System.out.println("=============================================================");
            System.out.println("Total colecciones: " + totalColecciones);
            System.out.println("Total documentos: " + totalDocumentos);
            System.out.println("=============================================================\n");

        } catch (Exception e) {
            logger.error("Error al mostrar estadísticas: {}", e.getMessage(), e);
            System.err.println("Error al obtener estadísticas de la base de datos");
        }
    }
}