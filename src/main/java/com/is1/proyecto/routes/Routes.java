package com.is1.proyecto.routes;

import spark.ModelAndView;
import spark.template.mustache.MustacheTemplateEngine;
import static spark.Spark.get;
import static spark.Spark.post;

import java.util.HashMap;

import com.is1.proyecto.controller.AdminController;
import com.is1.proyecto.controller.AuthController;
import com.is1.proyecto.controller.DashboardController;
import com.is1.proyecto.controller.PasswordRecoveryController;
import com.is1.proyecto.controller.DocenteController;
import com.is1.proyecto.controller.ProfileController;
import com.is1.proyecto.controller.SuperAdminController;
import com.is1.proyecto.controller.TallerController;
import com.is1.proyecto.controller.UserController;
import com.is1.proyecto.controller.EditorController;

public class Routes {
    
    public static void configure() {

        MustacheTemplateEngine engine = new MustacheTemplateEngine();
        // GET
        // GET: Muestra el formulario de creación de cuenta.
        get("/user/create", (req, res) -> UserController.formCreate(req, res), engine);

        get("/docente/alta", (req, res) -> DocenteController.formAlta(req, res), engine);

        get("/admin/alta", (req, res) -> AdminController.formAlta(req, res), engine);

        get("/superadmin/alta", (req, res) -> SuperAdminController.formAlta(req, res), engine);

        // GET: Ruta para mostrar el dashboard (panel de control) del usuario.
        // Requiere que el usuario esté autenticado.
        get("/dashboard", (req, res) -> DashboardController.dashboard(req, res), engine);

        get("/login", (req, res) -> AuthController.vistaLogin(req, res), engine);
        // GET: Ruta para cerrar la sesión del usuario.
        get("/logout", (req, res) -> AuthController.logout(req, res));

        // GET: Muestra el formulario de inicio de sesión (login).
        get("/", (req, res) -> AuthController.vistaLogin(req, res), engine);

        // GET: Ruta de alias para el formulario de creación de cuenta.
        get("/user/new", (req, res) -> UserController.formNew(req, res), engine); // Especifica el motor de plantillas para esta ruta.

        // GET: Ver perfil del usuario
        get("/profile", (req, res) -> ProfileController.profile(req, res), engine);

        get("/editor", (req, res) -> EditorController.editar(req, res), engine);

       // get("/save", (req, res) -> EditorController.save(req, res), engine);
        
        //GET: Recuperacion de contraseña
        get("/forgot-password", (req, res) -> PasswordRecoveryController.formNew(req, res), engine);

        get("/mensaje-advertencia", (req, res) -> EditorController.redireccion(req, res), engine);

        get("/pedido-cambio", (req, res) -> EditorController.pedido(req, res), engine);

        //GET: Reestablecimiento con token
        get("/reset-password", (req, res) -> PasswordRecoveryController.resetPasswordGet(req, res), engine);

        // GET: Muestra las opciones para asignar un profesor
        get("/asignar/profesor", (req, res) -> AdminController.opcionesAsignacion(req, res), engine);

        // RUTAS PARA TALLER
        get("/taller/alta", (req,res) -> TallerController.formAlta(req, res), engine);

        get("/taller/lista", (req,res) -> TallerController.listaPorDocente(req, res), engine);
        
        // --- Rutas POST para manejar envíos de formularios y APIs ---

        // POST
        // POST: Maneja el envío del formulario de creación de nueva cuenta.
        post("/user/new", (req, res) ->  UserController.create(req, res));

        post("/profile", (req, res) -> ProfileController.profile(req, res));

        post("/editor" , (req, res) -> EditorController.editar(req, res));

        post("/save-usuario", (req, res) -> EditorController.saveUser(req, res));

        post("/save-admin", (req, res) -> EditorController.saveAdmin(req, res));
     
        // POST: Maneja el envío del formulario de inicio de sesión.
        post("/login", (req, res) -> AuthController.login(req, res));

        // POST: Maneja el envío del formulario de Alta de Profesor (HU001)
        post("/docente/alta", (req, res) -> DocenteController.alta(req, res));

        post("/superadmin/alta", (req, res) -> SuperAdminController.alta(req, res));

        post("/admin/alta", (req, res) -> AdminController.alta(req, res));

        post("/superadmin/alta", (req, res) -> SuperAdminController.alta(req, res));

        // POST: Endpoint para añadir usuarios (API que devuelve JSON, no HTML).
        post("/add_users", (req, res) -> UserController.addUser(req, res));

        //POST: Recuperacion de contraseña
        post("/forgot-password", (req, res) -> PasswordRecoveryController.forgotPasswordPost(req, res));

        post("/mensaje-advertencia", (req, res) -> EditorController.redireccion(req, res));

        post("/pedido-cambio", (req, res) -> EditorController.pedido(req, res));

        //POST: Reestablecimiento con token
        post("/reset-password", (req, res) -> PasswordRecoveryController.resetPasswordPost(req, res));

        // POST PARA TALLER
        post("/taller/alta", (req, res)-> TallerController.alta(req, res));

    }
}
