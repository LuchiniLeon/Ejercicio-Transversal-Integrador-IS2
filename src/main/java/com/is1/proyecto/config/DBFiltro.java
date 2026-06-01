package com.is1.proyecto.config;

import static spark.Spark.after;
import static spark.Spark.before;
import static spark.Spark.halt;

import com.is1.proyecto.service.SuperAdminService;

public class DBFiltro {

    public static void configure() {

        DBConfigSingleton dbConfig = DBConfigSingleton.getInstance();
        final boolean[] iniciado = {false};

        // Inicializar BD ANTES de que llegue cualquier request
        dbConfig.openConnection();
        dbConfig.initDatabase();
        try {
            SuperAdminService.cargaSuperAdmin();
        } catch (Exception e) {
            System.err.println("Error SuperAdmin: " + e.getMessage());
        }
        dbConfig.closeConnection();

        before((req, res) -> dbConfig.openConnection());
        after((req, res) -> dbConfig.closeConnection());
    }
        
}

