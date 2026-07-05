package com.is1.proyecto.controller;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.is1.proyecto.models.Estudiante;
import com.is1.proyecto.models.User;
import com.is1.proyecto.service.CarreraInscripcionService;

import spark.ModelAndView;
import spark.Request;
import spark.Response;

public class CarreraInscripcionController {
    private static final Logger logger = LoggerFactory.getLogger(CarreraInscripcionController.class);

    public static ModelAndView listaCarreras(Request req, Response res) {
        Map<String, Object> model = new HashMap<>();

        String message = req.queryParams("message");
        if (message != null && !message.isEmpty()) {
            model.put("successMessage", message);
        }

        String errorMessage = req.queryParams("error");
        if (errorMessage != null && !errorMessage.isEmpty()) {
            model.put("errorMessage", errorMessage);
        }

        try{
            String currentUsername = req.session().attribute("currentUserUsername");
            if (currentUsername == null || currentUsername.isEmpty()) {
                logger.warn("Intento de listar carreras sin iniciar sesión");
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

            Estudiante estudiante = Estudiante.findFirst("dni_Persona = ?", user.getDNI());
            if (estudiante == null) {
                logger.warn("Intento de listar carreras desde un usuario no estudiante");
                res.redirect("/dashboard");
                return null;
            }

            List<Map<String, Object>> carreras = CarreraInscripcionService.carrerasDisponibles(estudiante.getDni());
            model.put("carreras", carreras);
            model.put("hayCarreras", !carreras.isEmpty());

            return new ModelAndView(model, "lista-carreras-estudiantes.mustache");
        }catch(Exception e){
            logger.error("Error crítico de base de datos al listar carreras.", e);
            res.redirect("/dashboard?error=" + URLEncoder.encode("Error al listar carreras", StandardCharsets.UTF_8));
            return null;
        }
        
    }

    public static Object inscribir(Request req, Response res) {
        try {
            String currentUsername = req.session().attribute("currentUserUsername");
            if (currentUsername == null || currentUsername.isEmpty()) {
                logger.warn("Intento de inscribirse a carreras sin iniciar sesión");
                res.redirect("/login");
                return "";
            }

            User user = User.findFirst("nombreUsuario = ?", currentUsername);
            if (user == null) {
                logger.warn("Sesión fantasma: el usuario {} ya no existe en la BD.", currentUsername);
                req.session().invalidate();
                res.redirect("/login");
                return null;
            }

            Estudiante estudiante = Estudiante.findFirst("dni_Persona = ?", user.getDNI());
            if (estudiante == null) {
                logger.warn("Intento de inscribirse a una carrera desde un usuario no estudiante");
                res.redirect("/dashboard");
                return "";
            }

            String idCarreraStr = req.params(":id");
            Integer idCarrera = Integer.parseInt(idCarreraStr);

            CarreraInscripcionService.inscribirCarrera(estudiante.getDni(), idCarrera);
            String message = "Inscripción a carrera realizada con éxito";
            logger.info(message);
            res.redirect("/inscripcion/carrera?message=" + URLEncoder.encode(message, StandardCharsets.UTF_8));
            return "";

        } catch (NumberFormatException e) {
            logger.warn("Error de formato numerico al inscribirse a una carrera");
            res.redirect("/inscripcion/carrera?error=" + URLEncoder.encode("ID de carrera inválido", StandardCharsets.UTF_8));
            return "";
        } catch (IllegalArgumentException | IllegalStateException e) {
            logger.warn("Validacion fallida para la inscripcion a carreras: {}", e.getMessage());
            res.redirect("/inscripcion/carrera?error=" + URLEncoder.encode(e.getMessage(), StandardCharsets.UTF_8));
            return "";
        } catch (Exception e) {
            logger.error("Error durante la inscripcion a carrera", e);
            res.redirect("/inscripcion/carrera?error=" + URLEncoder.encode("Error interno del servidor", StandardCharsets.UTF_8));
            return "";
        }
    }

    public static ModelAndView estadoCarrera(Request req, Response res) {
        try{
            Map<String, Object> model = new HashMap<>();

            String message = req.queryParams("message");
            if (message != null && !message.isEmpty()) {
                model.put("successMessage", message);
            }

            String errorMessage = req.queryParams("error");
            if (errorMessage != null && !errorMessage.isEmpty()) {
                model.put("errorMessage", errorMessage);
            }

            String currentUsername = req.session().attribute("currentUserUsername");
            if (currentUsername == null || currentUsername.isEmpty()) {
                logger.warn("Intento de acceso a estado de carrera sin iniciar sesión");
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

            Estudiante estudiante = Estudiante.findFirst("dni_Persona = ?", user.getDNI());
            if (estudiante == null) {
                logger.warn("Intento de acceso a estado de carreras desde un usuario no estudiante");
                res.redirect("/dashboard");
                return null;
            }

            model.put("carreras", CarreraInscripcionService.listarCarrerasDeEstudiante(estudiante.getDni()));
            return new ModelAndView(model, "estado-carrera.mustache");
        }catch(Exception e){
            logger.error("Error crítico de base de datos al acceder al estado de carreras.", e);
            res.redirect("/dashboard?error=" + URLEncoder.encode("Error al acceder al estado de carreras", StandardCharsets.UTF_8));
            return null;
        }
        
    }
}
