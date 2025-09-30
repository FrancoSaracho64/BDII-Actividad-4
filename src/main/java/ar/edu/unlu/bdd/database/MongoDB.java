package ar.edu.unlu.bdd.database;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

/**
 * Clase Singleton para gestionar la conexión a la base de datos MongoDB.
 */
public class MongoDB {

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
     * Inicializa la conexión con el servidor.
     */
    private MongoDB() {
        try {
            // 1. Conectar al servidor MongoDB
            mongoClient = MongoClients.create(CONNECTION_STRING);

            // 2. Obtener la referencia a la base de datos
            database = mongoClient.getDatabase(DATABASE_NAME);

            System.out.println("Conexión a MongoDB establecida con éxito. Base de datos: " + DATABASE_NAME);

        } catch (Exception e) {
            System.err.println("Error al conectar a MongoDB. Asegúrate de que el servidor esté corriendo en " + CONNECTION_STRING);
            e.printStackTrace();
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
}