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
        // Configuraciones para SQLite
        this.driver = "org.sqlite.JDBC"; // Driver JDBC para SQLite
        this.dbUrl = System.getProperty("db.url", "jdbc:sqlite:./db/dev.db");
        this.user = ""; // SQLite no usa usuario
        this.pass = ""; // SQLite no usa contraseña
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

