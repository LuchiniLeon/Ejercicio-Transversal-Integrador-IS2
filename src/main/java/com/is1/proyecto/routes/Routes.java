package com.is1.proyecto.routes;

import com.is1.proyecto.controller.AdminController;
import com.is1.proyecto.controller.AuthController;
import com.is1.proyecto.controller.CarreraInscripcionController;
import com.is1.proyecto.controller.DashboardController;
import com.is1.proyecto.controller.DocenteController;
import com.is1.proyecto.controller.EditorController;
import com.is1.proyecto.controller.EstudiaController;
import com.is1.proyecto.controller.EstudianteController;
import com.is1.proyecto.controller.MateriaController;
import com.is1.proyecto.controller.MateriaInscripcionController;
import com.is1.proyecto.controller.PasswordRecoveryController;
import com.is1.proyecto.controller.ProfileController;
import com.is1.proyecto.controller.SuperAdminController;
import com.is1.proyecto.controller.TallerController;
import com.is1.proyecto.controller.UserController;
import com.is1.proyecto.controller.EditorController;
import com.is1.proyecto.controller.NotaController;
import com.is1.proyecto.controller.EstudianteController;
import com.is1.proyecto.controller.EstudiaController;

import static spark.Spark.get;
import static spark.Spark.post;
import spark.template.mustache.MustacheTemplateEngine;

public class Routes {
    
    public static void configure() {

        MustacheTemplateEngine engine = new MustacheTemplateEngine();
        // GET
        // GET: Muestra el formulario de creación de cuenta.
        get("/user/create", (req, res) -> UserController.formCreate(req, res), engine);

        get("/estudiante/alta", (req,res) -> EstudianteController.formAlta(req, res), engine);

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

        // RUTAS PARA NOTAS
        get("/user/nota", (req, res) -> NotaController.formAlta(req, res), engine);
        get("/nota/alta", (req, res) -> NotaController.formAlta(req, res), engine);   
        get("/nota/lista", (req, res) -> NotaController.lista(req, res), engine);
        get("/nota/editar/:id", (req, res) -> NotaController.formEditar(req, res), engine);

        post("/nota/alta", (req, res) -> NotaController.alta(req, res));
        post("/nota/editar/:id", (req, res) -> NotaController.editar(req, res));
        post("/nota/eliminar/:id", (req, res) -> NotaController.eliminar(req, res));
        //Rutas post para menejar la carga de notas

        // RUTAS PARA TALLER
        get("/taller/alta", (req,res) -> TallerController.formAlta(req, res), engine);

        get("/taller/lista", (req,res) -> TallerController.listaPorDocente(req, res), engine);
        
        get("/taller/editar/:id", (req, res) -> TallerController.formEditar(req, res), engine);

        //RUTAS PARA MATERIAS
        get("/materia/lista", (req, res) -> MateriaController.lista(req, res), engine);

        get("/materia/editar/:id", (req, res) -> MateriaController.formEditar(req, res), engine);

        //RUTAS ADMIN MATERIAS
        get("/admin/materia/alta",(req, res) -> AdminController.formAltaMateria(req, res),engine);

        get("/admin/materia/lista",(req, res) -> AdminController.listaMaterias(req, res),engine);

        get("/admin/carrera/alta", (req, res) -> AdminController.formAltaCarrera(req, res), engine);
        get("/admin/carrera/lista", (req, res) -> AdminController.listaCarreras(req, res), engine);

        get("/admin/materia/asignar",(req, res) -> AdminController.formAsignarDocenteMateria(req, res),engine);
        //----------- ver
        
        get("/admin/taller/alta",(req, res) -> AdminController.formAltaTaller(req, res), engine);;

        // RUTAS INSCRIPCION ALUMNO A MATERIA
        get("/inscripcion/materia", (req, res) -> MateriaInscripcionController.listaMaterias(req, res), engine);
        get("/estudiante/mis-materias", (req, res) -> MateriaInscripcionController.misMaterias(req, res), engine);
        //get("/estado/materia", (req, res) -> EstadoAcademicoController.estadoMateria(req, res), engine);

        // RUTAS INSCRIPCION ALUMNO A CARRERA
        get("/inscripcion/carrera", (req, res) -> CarreraInscripcionController.listaCarreras(req, res), engine);
        get("/estado/carrera", (req, res) -> CarreraInscripcionController.estadoCarrera(req, res), engine);
        //get("/estado/taller", (req, res) -> EstadoAcademicoController.estadoTaller(req, res), engine);

        // RUTAS NOTAS (docente — esqueleto para integración futura)
        //get("/nota/alta", (req, res) -> NotaController.formAlta(req, res), engine);

        // RUTAS INSCRIPCION ALUMNO A TALLER
        get("/estudiante/talleres", (req, res) -> EstudiaController.listaTaller(req, res), engine);

        get("/estudiante/mis-talleres", (req, res) -> EstudiaController.misTalleres(req, res), engine);
        
        get("/admin/taller/lista",(req, res) -> AdminController.listaTalleres(req, res), engine);

        get("/admin/taller/asignar", (req, res) -> AdminController.formAsignarDocenteTaller(req, res), engine);

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

        post("/estudiante/alta", (req, res) -> EstudianteController.alta(req, res));

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

        post("/taller/editar/:id", (req, res) -> TallerController.editar(req, res));

        post("/taller/eliminar/:id", (req, res) -> TallerController.eliminar(req, res));

        //POST PARA MATERIAS
        post("/materia/editar/:id", (req, res) -> MateriaController.editar(req, res));

        post("/materia/eliminar/:id", (req, res) -> MateriaController.eliminar(req, res));
        //------- ver
        //admin MATERIAS
        post("/admin/materia/alta",(req, res) -> AdminController.altaMateria(req, res));

        post("/admin/carrera/alta", (req, res) -> AdminController.altaCarrera(req, res));

        post("/admin/materia/asignar",(req, res) -> AdminController.asignarDocenteMateria(req, res));
        
        //TALLER
        post("/admin/taller/alta", (req, res) -> AdminController.altaTaller(req, res));

        post("/admin/taller/asignar", (req, res) -> AdminController.asignarDocenteTaller(req, res));

        // POST PARA INSCRIPCIONES DE ALUMNO A MATERIA
        post("/inscripcion/materia/:id/inscribir", (req, res) -> MateriaInscripcionController.inscribir(req, res));
        post("/inscripcion/materia/:id/desinscribir", (req, res) -> MateriaInscripcionController.desincribir(req, res));

        // POST PARA INSCRIPCIONES DE ALUMNO A CARRERA
        post("/inscripcion/carrera/:id/inscribir", (req, res) -> CarreraInscripcionController.inscribir(req, res));

        // POST NOTAS (docente — esqueleto para integración futura)
        //post("/nota/alta", (req, res) -> NotaController.alta(req, res));

        // POST PARA INSCRIPCIONES DE ALUMNO A TALLER
        post("/estudiante/talleres/:id/inscribir", (req, res) -> EstudiaController.inscribir(req, res));
        
        post("/estudiante/talleres/:id/desinscribir", (req, res) -> EstudiaController. desincribir(req, res));
    }
}
