package com.is1.proyecto; // Define el paquete de la aplicación, debe coincidir con la estructura de carpetas.

// Importaciones necesarias para la aplicación Spark
import static spark.Spark.*; // Importa los métodos estáticos principales de Spark (get, post, before, after, etc.).

import com.is1.proyecto.config.DBFiltro;
import com.is1.proyecto.routes.Routes;

/**
 * Clase principal de la aplicación Spark.
 * Configura las rutas, filtros y el inicio del servidor web.
 */
public class App {
    /**
     * Método principal que se ejecuta al iniciar la aplicación.
     * Aquí se configuran todas las rutas y filtros de Spark.
     */
    public static void main(String[] args) {
        port(8080); // Configura el puerto en el que la aplicación Spark escuchará las peticiones
                    // (por defecto es 4567).

        DBFiltro.configure();
 
        Routes.configure();
    } 
} 