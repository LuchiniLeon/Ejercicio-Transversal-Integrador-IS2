package com.is1.proyecto.routes;

import static spark.Spark.get;
import static spark.Spark.post;

import com.is1.proyecto.controller.AuthController;
import com.is1.proyecto.controller.DashboardController;
import com.is1.proyecto.controller.ProfesorController;
import com.is1.proyecto.controller.ProfileController;
import com.is1.proyecto.controller.UserController;

public class Routes {
    
    public static void configure() {

        // GET
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

    
        // POST
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
