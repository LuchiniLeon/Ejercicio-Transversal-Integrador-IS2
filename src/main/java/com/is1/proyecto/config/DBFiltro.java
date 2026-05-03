package com.is1.proyecto.config;

import static spark.Spark.after;
import static spark.Spark.before;
import static spark.Spark.halt;

public class DBFiltro {

     public static void configure() {

        DBConfigSingleton dbConfig = DBConfigSingleton.getInstance();

        before((req, res) -> {
            try {
                dbConfig.openConnection(); 
                System.out.println(req.url());

            } catch (Exception e) {
                System.err.println("Error al abrir conexión: " + e.getMessage());
                halt(500, "Error interno del servidor");
            }
        });

        after((req, res) -> {
            try {
                dbConfig.closeConnection(); 
            } catch (Exception e) {
                System.err.println("Error al cerrar conexión: " + e.getMessage());
            }
        });
    }
    
}
