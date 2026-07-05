package com.is1.proyecto.controller;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.is1.proyecto.models.Admin;
import com.is1.proyecto.models.Persona;
import com.is1.proyecto.models.User;
import com.is1.proyecto.service.AdminService;

import jakarta.persistence.criteria.CriteriaBuilder.In;
import spark.ModelAndView;
import spark.Request;
import spark.Response;

public class AdminController {

    private static final Logger logger = LoggerFactory.getLogger(AdminController.class);
   
    public static Object alta(Request req, Response res){

        String dni = req.queryParams("dni");
        String cargo = req.queryParams("cargo");
        String sector = req.queryParams("sector");

        Integer dni_Persona = null;
        try {
            if (dni != null && !dni.isEmpty()) {
                dni_Persona = Integer.parseInt(dni);
            }
        } catch (NumberFormatException e) {
            logger.warn("El DNI debe ser numérico");
            res.redirect("/admin/alta?error=" + URLEncoder.encode("El DNI debe ser numérico", StandardCharsets.UTF_8));
            return "";
        }

        try{
            AdminService.crearAdmin(dni_Persona, cargo, sector);

            Persona persona = Persona.findFirst("dni = ?", dni_Persona);
            
            String nombre = "";
            String apellido = "";

            if(persona!=null){
                nombre = persona.getNombre();
                apellido = persona.getApellido();
            }

            String msg = "Administrador: " + nombre + " " + apellido + " habilitado con éxito.";
            logger.info(msg);
            res.redirect("/admin/alta?message=" + URLEncoder.encode(msg, StandardCharsets.UTF_8));
            return "";
        } catch (IllegalArgumentException e) {
            logger.warn("Argumentos invalidos en la creacion de administrador: {}", e.getMessage());
            res.status(400);
            res.redirect("/admin/alta?error=" + URLEncoder.encode(e.getMessage(), StandardCharsets.UTF_8));
            return "";

        } catch (IllegalStateException e) {
            logger.warn("Error de estado al crear administrador: {}", e.getMessage());
            res.status(409);
            res.redirect("/admin/alta?error=" + URLEncoder.encode(e.getMessage(), StandardCharsets.UTF_8));
            return "";

        } catch (Exception e) {
            logger.error("Error interno del servidor en la creación de administrador para el DNI: " + dni, e);
            res.status(500);
            res.redirect("/admin/alta?error=Error interno del servidor");
            return "";
        }
    }

    public static ModelAndView formAlta(Request req, Response res){
         Map<String, Object> model = new HashMap<>();
        
        String successMessage = req.queryParams("message");
        if (successMessage != null && !successMessage.isEmpty()) {
            model.put("successMessage", successMessage);
        }
    
        String errorMessage = req.queryParams("error");
        if (errorMessage != null && !errorMessage.isEmpty()) {
            model.put("errorMessage", errorMessage);
        }
    
        return new ModelAndView(model, "admin_form.mustache");
    }

    //Metodo que recibe la peticion para mostrar la plantilla de opciones de asignacion a profesores
    public static ModelAndView opcionesAsignacion(Request req, Response res) {

        return new ModelAndView(new HashMap<>(), "opciones_asignacion.mustache");
    }

    //Taller
    public static ModelAndView formAltaTaller(Request req, Response res) {
        Map<String, Object> model = new HashMap<>();
        
        String currentUsername = req.session().attribute("currentUserUsername");
        User user = User.findFirst("nombreUsuario = ?", currentUsername);
        Admin admin = Admin.findFirst("dni_Persona = ?", user.getDNI());
    
        if (admin == null) {
            res.redirect("/dashboard");
            return null;
        }

        model.put("docentes", AdminService.obtenerDocentes());

        return new ModelAndView(model, "/alta_admin.mustache");
    }

    public static Object altaTaller(Request req, Response res) {
        try {
            String currentUsername = req.session().attribute("currentUserUsername");

            if (currentUsername == null) {
                logger.warn("Intento de acceso a alta de taller sin iniciar sesión");
                res.redirect("/login");
                return "";
            }

            User user = User.findFirst("nombreUsuario = ?", currentUsername);
            Admin admin = Admin.findFirst("dni_Persona = ?", user.getDNI());
        
            if (admin == null) {
                logger.warn("Intento de crear taller desde un usuario no administrador");
                res.redirect("/dashboard");
                return "";
            }

            //Leer todo como String primero para evitar NullPointerExceptions en el .equals
            String titulo = req.queryParams("titulo");
            String horaStr = req.queryParams("hora");
            String vigenteStr = req.queryParams("vigente");
            String dniDocenteStr = req.queryParams("dniDocente");

            //Conversion a integer y boolean de forma segura
            Integer hora = Integer.parseInt(horaStr);
            Boolean vigente = "1".equals(vigenteStr); // Invertir el equals evita el NullPointerException
            Integer dniDocente = Integer.parseInt(dniDocenteStr);

            AdminService.crearTallerComoAdmin(titulo, hora, vigente, dniDocente);
        
            String msg = "Taller '" + titulo + "' creado con éxito.";
            logger.info(msg);
            res.redirect("/admin/taller/lista?message=" + URLEncoder.encode(msg, StandardCharsets.UTF_8));
        
            return "";
        } catch (NumberFormatException e) {
            logger.warn("Error de formato numérico al crear taller. Input inválido.");
            res.redirect("/admin/taller/alta?error=" + URLEncoder.encode("La hora y el DNI deben ser campos numéricos obligatorios.", StandardCharsets.UTF_8));
            return "";

        } catch (IllegalArgumentException e) {
            logger.warn("Argumento invalido para la creación de taller: {}", e.getMessage());
            res.redirect("/admin/taller/alta?error=" + URLEncoder.encode(e.getMessage(), StandardCharsets.UTF_8));
            return "";

        } catch (Exception e) {
            logger.error("Error interno del servidor al intentar crear un taller", e);
            String errorMsg = e.getMessage() != null ? e.getMessage() : "Error interno del servidor";
            res.redirect("/admin/taller/alta?error=" + URLEncoder.encode(errorMsg, StandardCharsets.UTF_8));
            
            return "";
        }
    }

    public static ModelAndView listaTalleres(Request req, Response res) {
        String currentUsername = req.session().attribute("currentUserUsername");

         if (currentUsername == null) {
                logger.warn("Intento de acceso a listar talleres sin iniciar sesión");
                res.redirect("/login");
                return null;
            }
        try{
            User user = User.findFirst("nombreUsuario = ?", currentUsername);
            if (user == null) {
                logger.warn("Sesión fantasma: el usuario {} ya no existe en la BD.", currentUsername);
                req.session().invalidate();
                res.redirect("/login");
                return null;
            }

            Admin admin = Admin.findFirst("dni_Persona = ?", user.getDNI());
            if (admin == null) {
                logger.warn("Intento de listar talleres desde un usuario no administrador.");
                res.redirect("/dashboard");
                return null;
            }

            Map<String, Object> model = new HashMap<>();
            model.put("talleres", AdminService.obtenerTalleres());

            String successMessage = req.queryParams("message");
            if (successMessage != null && !successMessage.isEmpty()) {
                model.put("successMessage", successMessage);
            }

            return new ModelAndView(model, "admin_lista-talleres.mustache");
    
        }catch (Exception e){
            logger.error("Error crítico de base de datos al listar los talleres.", e);
            res.redirect("/dashboard?error=" + URLEncoder.encode("Error al cargar talleres", StandardCharsets.UTF_8));
            return null;
        }
    }

    public static ModelAndView formAsignarDocenteTaller(Request req, Response res) {
        String currentUsername = req.session().attribute("currentUserUsername");
        if (currentUsername == null) {
                logger.warn("Intento de acceso a asignar docente sin iniciar sesión");
                res.redirect("/login");
                return null;
            }
        try{
            User user = User.findFirst("nombreUsuario = ?", currentUsername);
            if (user == null) {
                logger.warn("Sesión fantasma: el usuario {} ya no existe en la BD.", currentUsername);
                req.session().invalidate();
                res.redirect("/login");
                return null;
            }

            Admin admin = Admin.findFirst("dni_Persona = ?", user.getDNI());
            if (admin == null) {
                logger.warn("Intento de asignar docente a un taller con un usuario no administrador");
                res.redirect("/dashboard");
                return null;
            }

            Map<String, Object> model = new HashMap<>();
            model.put("docentes", AdminService.obtenerDocentes());
            model.put("talleres", AdminService.obtenerTalleres());

            String errorMessage = req.queryParams("error");
            if (errorMessage != null && !errorMessage.isEmpty()) model.put("errorMessage", errorMessage);

            return new ModelAndView(model, "asignar_docente-taller.mustache");
        }catch(Exception e){
            logger.error("Error crítico de base de datos al cargar el formulario de asignación.", e);
            res.redirect("/dashboard?error=" + URLEncoder.encode("Error al cargar los datos", StandardCharsets.UTF_8));
            return null;
        }
       
    }

    public static Object asignarDocenteTaller(Request req, Response res) {
        try {
            String currentUsername = req.session().attribute("currentUserUsername");
            if (currentUsername == null) {
                logger.warn("Intento de acceso a asignar docente sin iniciar sesión");
                res.redirect("/login");
                return null;
            }

            User user = User.findFirst("nombreUsuario = ?", currentUsername);
            if (user == null) {
                logger.warn("Sesión fantasma: el usuario {} ya no existe en la BD.", currentUsername);
                req.session().invalidate();
                res.redirect("/login");
                return null;
            }

            Admin admin = Admin.findFirst("dni_Persona = ?", user.getDNI());

            if (admin == null) {
                logger.warn("Intento de asignar docente a un taller con un usuario no administrador");
                res.redirect("/dashboard");
                return "";
            }

            String idTallerStr = req.queryParams("idTaller");
            String dniDocenteStr = req.queryParams("dniDocente");

            Integer idTaller = Integer.parseInt(idTallerStr);
            Integer dniDocente =Integer.parseInt(dniDocenteStr);

            AdminService.asignarDocenteATaller(idTaller, dniDocente);
            String msg = "Docente asignado al taller con éxito.";
            logger.info(msg);
            res.redirect("/admin/taller/lista?message=" + URLEncoder.encode(msg, StandardCharsets.UTF_8));
            return "";
            
         } catch (NumberFormatException e) {
            logger.warn("Error de formato numérico al asignar docente a taller. Input inválido.");
            res.redirect("/admin/taller/alta?error=" + URLEncoder.encode("La hora y el DNI deben ser campos numéricos obligatorios.", StandardCharsets.UTF_8));
            return "";

        } catch (IllegalArgumentException e) {
            logger.warn("Argumentos invalidos para la asignacion de docente a taller: {}", e.getMessage());
            res.redirect("/admin/taller/asignar?error=" + URLEncoder.encode(e.getMessage(), StandardCharsets.UTF_8));
            return "";
        } catch (Exception e) {
            logger.error("Error al asignar docente a taller: ", e);
            res.redirect("/admin/taller/asignar?error=" + URLEncoder.encode("Error interno del servidor.", StandardCharsets.UTF_8));
            return "";
        }
    }

    //Materias
    //get formulario ALta Materia
    public static ModelAndView formAltaMateria(Request req, Response res) {

        String currentUsername = req.session().attribute("currentUserUsername");
        if (currentUsername == null) {
                logger.warn("Intento de acceso al formulario de alta de materia sin iniciar sesión");
                res.redirect("/login");
                return null;
            }
        try{
            User user = User.findFirst("nombreUsuario = ?", currentUsername);
            if (user == null) {
                logger.warn("Sesión fantasma: el usuario {} ya no existe en la BD.", currentUsername);
                req.session().invalidate();
                res.redirect("/login");
                return null;
            }

            Admin admin = Admin.findFirst("dni_Persona = ?", user.getDNI());
            if (admin == null) {
                logger.warn("Intento de acceder al formulario de alta de materia con un usuario no administrador");
                res.redirect("/dashboard");
                return null;
            }

            Map<String, Object> model = new HashMap<>();

            model.put("docentes", AdminService.obtenerDocentes());
            model.put("carreras", AdminService.obtenerCarreras());

            String errorMessage = req.queryParams("error");
            if (errorMessage != null && !errorMessage.isEmpty()) {
                model.put("errorMessage", errorMessage);
            }

            return new ModelAndView(model, "admin_materia_alta.mustache");
        }catch (Exception e){
            logger.error("Error crítico de base de datos al cargar el formulario de creacion de materia.", e);
            res.redirect("/dashboard?error=" + URLEncoder.encode("Error al cargar los datos", StandardCharsets.UTF_8));
            return null;
        }
        
    }

    public static ModelAndView formAltaCarrera(Request req, Response res) {
        String currentUsername = req.session().attribute("currentUserUsername");
        if (currentUsername == null) {
                logger.warn("Intento de acceso al formulario de alta de carrera sin iniciar sesión");
                res.redirect("/login");
                return null;
            }

        try{
            User user = User.findFirst("nombreUsuario = ?", currentUsername);
            if (user == null) {
                logger.warn("Sesión fantasma: el usuario {} ya no existe en la BD.", currentUsername);
                req.session().invalidate();
                res.redirect("/login");
                return null;
            }
            Admin admin = Admin.findFirst("dni_Persona = ?", user.getDNI());
            if (admin == null) {
                logger.warn("Intento de acceder al formulario de alta de carrera con un usuario no administrador");
                res.redirect("/dashboard");
                return null;
            }

            Map<String, Object> model = new HashMap<>();

            String successMessage = req.queryParams("message");
            if (successMessage != null && !successMessage.isEmpty()) {
                model.put("successMessage", successMessage);
            }

            String errorMessage = req.queryParams("error");
            if (errorMessage != null && !errorMessage.isEmpty()) {
                model.put("errorMessage", errorMessage);
            }

            return new ModelAndView(model, "admin_carrera_alta.mustache");
        }catch (Exception e){
            logger.error("Error crítico de base de datos al cargar el formulario de creacion de carrera.", e);
            res.redirect("/dashboard?error=" + URLEncoder.encode("Error al cargar los datos", StandardCharsets.UTF_8));
            return null;
        }
        
    }

    public static Object altaCarrera(Request req, Response res) {
        try {
            String currentUsername = req.session().attribute("currentUserUsername");
            if (currentUsername == null) {
                logger.warn("Intento de crear una carrera sin iniciar sesión");
                res.redirect("/login");
                return null;
            }
            User user = User.findFirst("nombreUsuario = ?", currentUsername);
            if (user == null) {
                logger.warn("Sesión fantasma: el usuario {} ya no existe en la BD.", currentUsername);
                req.session().invalidate();
                res.redirect("/login");
                return null;
            }
            Admin admin = Admin.findFirst("dni_Persona = ?", user.getDNI());

            if (admin == null) {
                logger.warn("Intento de crear una carrera con un usuario no administrador");
                res.redirect("/dashboard");
                return "";
            }

            String idCarreraStr = req.queryParams("idCarrera");
            String nombre = req.queryParams("nombre");
            String duracionStr = req.queryParams("duracion");
            String modalidad = req.queryParams("modalidad");

            Integer idCarrera = Integer.parseInt(idCarreraStr);
            Integer duracion = Integer.parseInt(duracionStr);

            AdminService.crearCarreraComoAdmin(idCarrera, nombre, duracion, modalidad);

            String msg = "Carrera creada con éxito";
            logger.info(msg);
            res.redirect("/admin/carrera/lista?message=" + URLEncoder.encode(msg, StandardCharsets.UTF_8));
            return "";
        } catch (NumberFormatException e) {
            logger.warn("Error de formato numérico al crear carrera. Input inválido.");
            res.redirect("/admin/carrera/alta?error=" + URLEncoder.encode("La duracion y el id de carrera deben ser campos numéricos obligatorios.", StandardCharsets.UTF_8));
            return "";
        } catch (IllegalArgumentException | IllegalStateException e) {
            logger.warn("Validación fallida al crear carrera: {}", e.getMessage());
            res.redirect("/admin/carrera/alta?error=" + URLEncoder.encode(e.getMessage(), StandardCharsets.UTF_8));
            return "";
        } catch (Exception e) {
             logger.error("Error crítico de base de datos al crear carrera.", e);
            res.redirect("/dashboard?error=" + URLEncoder.encode("Error interno al cargar nueva carrera", StandardCharsets.UTF_8));
            return null;
        }
    }

    public static ModelAndView listaCarreras(Request req, Response res) {
        String currentUsername = req.session().attribute("currentUserUsername");
        if (currentUsername == null) {
                logger.warn("Intento de listar carreras sin iniciar sesión");
                res.redirect("/login");
                return null;
            }
        try{
            User user = User.findFirst("nombreUsuario = ?", currentUsername);
            if (user == null) {
                    logger.warn("Sesión fantasma: el usuario {} ya no existe en la BD.", currentUsername);
                    req.session().invalidate();
                    res.redirect("/login");
                    return null;
                }
            Admin admin = Admin.findFirst("dni_Persona = ?", user.getDNI());
            if (admin == null) {
                logger.warn("Intento de listar carreras con un usuario no administrador");
                res.redirect("/dashboard");
                return null;
            }

            Map<String, Object> model = new HashMap<>();
            model.put("carreras", AdminService.obtenerCarreras());

            String successMessage = req.queryParams("message");
            if (successMessage != null && !successMessage.isEmpty()) {
                model.put("successMessage", successMessage);
            }

            return new ModelAndView(model, "admin_lista-carreras.mustache");
    
        }catch (Exception e){
            logger.error("Error crítico de base de datos al listar carreras.", e);
            res.redirect("/dashboard?error=" + URLEncoder.encode("Error al listar carreras", StandardCharsets.UTF_8));
            return null;
        }
    }

    //post alta materia
    public static Object altaMateria(Request req, Response res) {

        try {

            String currentUsername = req.session().attribute("currentUserUsername");
             if (currentUsername == null) {
                logger.warn("Intento de crear materia sin iniciar sesión");
                res.redirect("/login");
                return null;
            }
            User user = User.findFirst("nombreUsuario = ?", currentUsername);
            if (user == null) {
                    logger.warn("Sesión fantasma: el usuario {} ya no existe en la BD.", currentUsername);
                    req.session().invalidate();
                    res.redirect("/login");
                    return null;
                }
            Admin admin = Admin.findFirst("dni_Persona = ?", user.getDNI());
            if (admin == null) {
                logger.warn("Intento de crear materia con un usuario no administrador");
                res.redirect("/dashboard");
                return "";
            }

            String codigoStr = req.queryParams("codigo");
            String nombre = req.queryParams("nombre");
            String horasTotalesStr = req.queryParams("horasTotales");
            String dniDocenteStr = req.queryParams("dniDocente");
            String idCarreraStr = req.queryParams("idCarrera");

            Integer codigo = Integer.parseInt(codigoStr);
            Integer horasTotales = Integer.parseInt(horasTotalesStr);
            Integer dniDocente = Integer.parseInt(dniDocenteStr);
            Integer idCarrera = Integer.parseInt(idCarreraStr);

            AdminService.crearMateriaComoAdmin(codigo, nombre, horasTotales, admin.getDni(), dniDocente, idCarrera);

            String msg = "Materia creada con éxito";
            logger.info(msg);
            res.redirect("/admin/materia/lista?message="
                    + URLEncoder.encode(msg, StandardCharsets.UTF_8));

            return "";

        } catch (NumberFormatException e) {
            logger.warn("Error de formato numérico al crear materia. Input inválido.");
            res.redirect("/admin/materia/alta?error=" + URLEncoder.encode("El código, las horas totales, el dni del docente y el id de carrera deben ser campos numéricos obligatorios.", StandardCharsets.UTF_8));
            return "";
        } catch (IllegalArgumentException | IllegalStateException e) {
            logger.warn("Validación fallida al crear materia: {}", e.getMessage());
            res.redirect("/admin/materia/alta?error=" + URLEncoder.encode(e.getMessage(), StandardCharsets.UTF_8));
            return "";
        } catch (Exception e) {
            logger.error("Error crítico de base de datos al crear la materia.", e);
            res.redirect("/admin/materia/alta?error="
                    + URLEncoder.encode("Error interno al crear la materia", StandardCharsets.UTF_8));

            return "";
        }
    }
    
    //lista materias
    public static ModelAndView listaMaterias(Request req, Response res) {
        try{
            String currentUsername = req.session().attribute("currentUserUsername");
            if (currentUsername == null) {
                logger.warn("Intento de listar materias sin iniciar sesión");
                res.redirect("/login");
                return null;
            }
            User user = User.findFirst("nombreUsuario = ?", currentUsername);
            if (user == null) {
                    logger.warn("Sesión fantasma: el usuario {} ya no existe en la BD.", currentUsername);
                    req.session().invalidate();
                    res.redirect("/login");
                    return null;
                }
            Admin admin = Admin.findFirst("dni_Persona = ?", user.getDNI());
            if (admin == null) {
                logger.warn("Intento de listar materias con un usuario no administrador");
                res.redirect("/dashboard");
                return null;
            }

            Map<String, Object> model = new HashMap<>();

            model.put("materias", AdminService.obtenerMaterias());

            String successMessage = req.queryParams("message");

            if (successMessage != null && !successMessage.isEmpty()) {
                model.put("successMessage", successMessage);
            }

            return new ModelAndView(model, "admin_lista-materias.mustache");
        
        }catch(Exception e){
            logger.error("Error crítico de base de datos al listar materias.", e);
            res.redirect("/dashboard?error=" + URLEncoder.encode("Error al listar materias", StandardCharsets.UTF_8));
            return null;
        }
    }

    //get formulario asignar docente a materia
    public static ModelAndView formAsignarDocenteMateria(Request req, Response res) {

        try{
            String currentUsername = req.session().attribute("currentUserUsername");
            if (currentUsername == null) {
                logger.warn("Intento de acceso a asignar docente a materias sin iniciar sesión");
                res.redirect("/login");
                return null;
            }
            User user = User.findFirst("nombreUsuario = ?", currentUsername);
            if (user == null) {
                    logger.warn("Sesión fantasma: el usuario {} ya no existe en la BD.", currentUsername);
                    req.session().invalidate();
                    res.redirect("/login");
                    return null;
                }
            Admin admin = Admin.findFirst("dni_Persona = ?", user.getDNI());
            if (admin == null) {
                logger.warn("Intento de acceso a asignar docente a materias con un usuario no administrador");
                res.redirect("/dashboard");
                return null;
            }

            Map<String, Object> model = new HashMap<>();

            model.put("docentes", AdminService.obtenerDocentes());
            model.put("materias", AdminService.obtenerMaterias());

            String errorMessage = req.queryParams("error");

            if (errorMessage != null && !errorMessage.isEmpty()) {
                model.put("errorMessage", errorMessage);
            }

            return new ModelAndView(model, "asignar_docente-materia.mustache");
        
        }catch (Exception e){
            logger.error("Error crítico de base de datos al acceder al formulario de asignacion de docentes a materias.", e);
            res.redirect("/dashboard?error=" + URLEncoder.encode("Error al acceder al formulario", StandardCharsets.UTF_8));
            return null;
        }
    }

    //post asignar docente a materia
    public static Object asignarDocenteMateria(Request req, Response res) {

        try {
            String currentUsername = req.session().attribute("currentUserUsername");
            if (currentUsername == null) {
                logger.warn("Intento de asignar docente a materias sin iniciar sesión");
                res.redirect("/login");
                return null;
            }
            User user = User.findFirst("nombreUsuario = ?", currentUsername);
            if (user == null) {
                    logger.warn("Sesión fantasma: el usuario {} ya no existe en la BD.", currentUsername);
                    req.session().invalidate();
                    res.redirect("/login");
                    return null;
                }
            Admin admin = Admin.findFirst("dni_Persona = ?", user.getDNI());
            if (admin == null) {
                logger.warn("Intento de asignar docente a materias con un usuario no administrador");
                res.redirect("/dashboard");
                return "";
            }

            String idMateriaStr = req.queryParams("idMateria");
            String dniDocenteStr = req.queryParams("dniDocente");

            Integer idMateria = Integer.parseInt(idMateriaStr);
            Integer dniDocente = Integer.parseInt(dniDocenteStr);

            AdminService.asignarDocenteAMateria(idMateria,dniDocente);

            String msg = "Docente asignado a la materia con éxito.";
            logger.info(msg);
            res.redirect("/admin/materia/lista?message="
                    + URLEncoder.encode(msg, StandardCharsets.UTF_8));
            return "";

        } catch (NumberFormatException e) {
        logger.warn("Error de formato numérico al asignar docente a materia. Input inválido.");
        res.redirect("/admin/materia/asignar?error=" + URLEncoder.encode("El id de materia y dni docente deben ser campos numéricos obligatorios.", StandardCharsets.UTF_8));
        return "";
        } catch (IllegalArgumentException | IllegalStateException e) {
            logger.warn("Validación fallida al asignar docente a materia: {}", e.getMessage());
            res.redirect("/admin/materia/asignar?error=" + URLEncoder.encode(e.getMessage(), StandardCharsets.UTF_8));
            return "";

        } catch (Exception e) {
            logger.error("Error crítico de base de datos al asignar docente a materia.", e);
            res.redirect("/admin/materia/asignar?error="
                    + URLEncoder.encode("Error interno del servidor",
                            StandardCharsets.UTF_8));
            return "";
        }
    }
    
}
