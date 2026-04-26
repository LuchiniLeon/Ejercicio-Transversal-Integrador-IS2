package com.is1.proyecto; // Define el paquete de la aplicación, debe coincidir con la estructura de carpetas.

// Importaciones necesarias para la aplicación Spark
import com.fasterxml.jackson.databind.ObjectMapper; // Utilidad para serializar/deserializar objetos Java a/desde JSON.
import static spark.Spark.*; // Importa los métodos estáticos principales de Spark (get, post, before, after, etc.).

// Importaciones específicas para ActiveJDBC (ORM para la base de datos)
import org.javalite.activejdbc.Base; // Clase central de ActiveJDBC para gestionar la conexión a la base de datos.
// Importaciones de Spark para renderizado de plantillas
import spark.ModelAndView; // Representa un modelo de datos y el nombre de la vista a renderizar.
import spark.template.mustache.MustacheTemplateEngine; // Motor de plantillas Mustache para Spark.

// Importaciones estándar de Java
import java.util.HashMap; // Para crear mapas de datos (modelos para las plantillas).
import java.util.Map; // Interfaz Map, utilizada para Map.of() o HashMap.

// Importaciones de clases del proyecto
import com.is1.proyecto.config.DBConfigSingleton; // Clase Singleton para la configuración de la base de datos.
import com.is1.proyecto.controller.AuthController;
import com.is1.proyecto.controller.DashboardController;
import com.is1.proyecto.controller.ProfesorController;
import com.is1.proyecto.controller.ProfileController;
import com.is1.proyecto.controller.UserController;
import com.is1.proyecto.models.User; // Modelo de ActiveJDBC que representa la tabla 'users'.
/**
 * Clase principal de la aplicación Spark.
 * Configura las rutas, filtros y el inicio del servidor web.
 */
public class App {

    // Instancia estática y final de ObjectMapper para la
    // serialización/deserialización JSON.
    // Se inicializa una sola vez para ser reutilizada en toda la aplicación.
    private static final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Método principal que se ejecuta al iniciar la aplicación.
     * Aquí se configuran todas las rutas y filtros de Spark.
     */
    public static void main(String[] args) {
        port(8080); // Configura el puerto en el que la aplicación Spark escuchará las peticiones
                    // (por defecto es 4567).

        // Obtener la instancia única del singleton de configuración de la base de
        // datos.
        DBConfigSingleton dbConfig = DBConfigSingleton.getInstance();

        // --- Filtro 'before' para gestionar la conexión a la base de datos ---
        // Este filtro se ejecuta antes de cada solicitud HTTP.
        before((req, res) -> {
            try {
                // Abre una conexión a la base de datos utilizando las credenciales del
                // singleton.
                Base.open(dbConfig.getDriver(), dbConfig.getDbUrl(), dbConfig.getUser(), dbConfig.getPass());
                System.out.println(req.url());

            } catch (Exception e) {
                // Si ocurre un error al abrir la conexión, se registra y se detiene la
                // solicitud
                // con un código de estado 500 (Internal Server Error) y un mensaje JSON.
                System.err.println("Error al abrir conexión con ActiveJDBC: " + e.getMessage());
                halt(500, "{\"error\": \"Error interno del servidor: Fallo al conectar a la base de datos.\"}"
                        + e.getMessage());
            }
        });

        // --- Filtro 'after' para cerrar la conexión a la base de datos ---
        // Este filtro se ejecuta después de que cada solicitud HTTP ha sido procesada.
        after((req, res) -> {
            try {
                // Cierra la conexión a la base de datos para liberar recursos.
                Base.close();
            } catch (Exception e) {
                // Si ocurre un error al cerrar la conexión, se registra.
                System.err.println("Error al cerrar conexión con ActiveJDBC: " + e.getMessage());
            }
        });

        // --- Rutas GET para renderizar formularios y páginas HTML ---


        // GET: Muestra el formulario de creación de cuenta.
        get("/user/create", (req, res) -> UserController.formCreate(req, res));

        get("/profesor/alta", (req, res) -> ProfesorController.formAlta(req, res));

        // GET: Ruta para mostrar el dashboard (panel de control) del usuario.
        // Requiere que el usuario esté autenticado.
        get("/dashboard", (req, res) -> DashboardController.dashboard(req, res));

        // GET: Ruta para cerrar la sesión del usuario.
        get("/logout", (req, res) -> AuthController.logout(req, res));

        // GET: Muestra el formulario de inicio de sesión (login).
        get("/", (req, res) -> AuthController.showLogin(req, res));

        // GET: Ruta de alias para el formulario de creación de cuenta.
        get("/user/new", (req, res) -> UserController.formNew(req, res)); // Especifica el motor de plantillas para esta ruta.

        // GET: Ver perfil del usuario
        get("/profile", (req, res) -> ProfileController.profile(req, res));
        // --- Rutas POST para manejar envíos de formularios y APIs ---

        // POST: Maneja el envío del formulario de creación de nueva cuenta.
        post("/user/new", (req, res) ->  UserController.create(req, res));
     

        // POST: Maneja el envío del formulario de inicio de sesión.
        post("/login", (req, res) -> AuthController.login(req, res));

        // POST: Maneja el envío del formulario de Alta de Profesor (HU001)
        post("/profesor/alta", (req, res) -> ProfesorController.alta(req, res));

        // POST: Endpoint para añadir usuarios (API que devuelve JSON, no HTML).
        post("/add_users", (req, res) -> UserController.addUser(req, res));

    } 
} 