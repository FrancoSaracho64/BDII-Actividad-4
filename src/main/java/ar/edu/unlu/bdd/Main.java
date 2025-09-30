package ar.edu.unlu.bdd;

import ar.edu.unlu.bdd.database.MongoDB;
import ar.edu.unlu.bdd.menu.Menu;

public class Main {
    public static void main(String[] args) {
        // Creamos la base de datos
        MongoDB mongoDB = MongoDB.getInstance();
        // Inicializamos el menú
        new Menu(mongoDB.getDatabase());
    }
}