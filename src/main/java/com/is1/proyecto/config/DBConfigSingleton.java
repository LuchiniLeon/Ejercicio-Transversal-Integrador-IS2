// Archivo: com/is1/proyecto/config/DBConfigSingleton.java
package com.is1.proyecto.config;

import org.javalite.activejdbc.Base; // Necesitarás esta importación para usar Base.open y Base.close

public final class DBConfigSingleton {

    private static DBConfigSingleton instance;

    // Ya no es necesario que sean final si los vas a configurar dinámicamente o mantener una sola instancia
    private final String dbUrl;
    private final String user;
    private final String pass;
    private final String driver;

    
    // Constructor privado para evitar instanciación directa
    private DBConfigSingleton() {
        this.driver = "org.sqlite.JDBC";
        this.dbUrl = System.getProperty("db.url", "jdbc:sqlite:./db/dev.db");
        this.user = "";
        this.pass = "";
        new java.io.File("./db").mkdirs();
    }

    public void initDatabase() {
    try {
        String[] archivos = {
            "./db/relaciones.sql",
            "./db/schema-base.sql",
            "./db/entidades-especificas.sql"
        };
        for (String archivo : archivos) {
            java.io.File f = new java.io.File(archivo);
            if (f.exists()) {
                String sql = new String(
                    java.nio.file.Files.readAllBytes(java.nio.file.Paths.get(archivo)),
                    java.nio.charset.StandardCharsets.UTF_8
                );
                for (String statement : sql.split(";")) {
                    String trimmed = statement.trim();
                    if (!trimmed.isEmpty()) {
                        try {
                            Base.exec(trimmed);
                        } catch (Exception e) {
                            System.err.println("ERROR SQL: " + e.getMessage());
                        }
                    }
                }
                System.out.println("Ejecutado: " + archivo);
            }
        }
        java.util.List<java.util.Map> tablas = Base.findAll("SELECT name FROM sqlite_master WHERE type='table' ORDER BY name");
        System.out.println("=== TABLAS EN DB: " + tablas + " ===");
    } catch (Exception e) {
        System.err.println("Error initDatabase: " + e.getMessage());
    }
    }

    public static synchronized DBConfigSingleton getInstance() {
        if (instance == null) {
            instance = new DBConfigSingleton();
        }
        return instance;
    }

    // Métodos para abrir y cerrar la conexión
    public void openConnection() {
        // Utiliza los valores de las propiedades de la clase para abrir la conexión
        Base.open(this.driver, this.dbUrl, this.user, this.pass);
    }

    public void closeConnection() {
        Base.close();
    }

    // Getters existentes
    public String getDbUrl() {
        return dbUrl;
    }

    public String getUser() {
        return user;
    }

    public String getPass() {
        return pass;
    }

    public String getDriver() {
        return driver;
    }
}

