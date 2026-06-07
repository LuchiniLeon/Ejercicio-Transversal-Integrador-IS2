package com.is1.proyecto.controller;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.is1.proyecto.models.Estudiante;
import com.is1.proyecto.models.User;
import com.is1.proyecto.service.CarreraInscripcionService;

import spark.ModelAndView;
import spark.Request;
import spark.Response;

public class CarreraInscripcionController {

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

        String currentUsername = req.session().attribute("currentUserUsername");
        if (currentUsername == null || currentUsername.isEmpty()) {
            res.redirect("/login");
            return null;
        }

        User user = User.findFirst("nombreUsuario = ?", currentUsername);
        if (user == null) {
            res.redirect("/dashboard");
            return null;
        }

        Estudiante estudiante = Estudiante.findFirst("dni_Persona = ?", user.getDNI());
        if (estudiante == null) {
            res.redirect("/dashboard");
            return null;
        }

        List<Map<String, Object>> carreras = CarreraInscripcionService.carrerasDisponibles(estudiante.getDni());
        model.put("carreras", carreras);
        model.put("hayCarreras", !carreras.isEmpty());

        return new ModelAndView(model, "lista-carreras-estudiantes.mustache");
    }

    public static Object inscribir(Request req, Response res) {
        try {
            Integer idCarrera = Integer.parseInt(req.params(":id"));
            String currentUsername = req.session().attribute("currentUserUsername");
            if (currentUsername == null || currentUsername.isEmpty()) {
                res.redirect("/login");
                return "";
            }

            User user = User.findFirst("nombreUsuario = ?", currentUsername);
            if (user == null) {
                res.redirect("/dashboard");
                return "";
            }

            Estudiante estudiante = Estudiante.findFirst("dni_Persona = ?", user.getDNI());

            if (estudiante == null) {
                res.redirect("/dashboard");
                return "";
            }

            CarreraInscripcionService.inscribirCarrera(estudiante.getDni(), idCarrera);
            String message = "Inscripción a carrera realizada con éxito";
            res.redirect("/inscripcion/carrera?message=" + URLEncoder.encode(message, StandardCharsets.UTF_8));
            return "";

        } catch (NumberFormatException e) {
            res.redirect("/inscripcion/carrera?error=" + URLEncoder.encode("ID de carrera inválido", StandardCharsets.UTF_8));
            return "";
        } catch (IllegalArgumentException e) {
            res.redirect("/inscripcion/carrera?error=" + URLEncoder.encode(e.getMessage(), StandardCharsets.UTF_8));
            return "";
        } catch (Exception e) {
            e.printStackTrace();
            res.redirect("/inscripcion/carrera?error=Error interno del servidor");
            return "";
        }
    }

    public static ModelAndView estadoCarrera(Request req, Response res) {
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
            res.redirect("/login");
            return null;
        }

        User user = User.findFirst("nombreUsuario = ?", currentUsername);
        if (user == null) {
            res.redirect("/dashboard");
            return null;
        }

        Estudiante estudiante = Estudiante.findFirst("dni_Persona = ?", user.getDNI());
        if (estudiante == null) {
            res.redirect("/dashboard");
            return null;
        }

        model.put("carreras", CarreraInscripcionService.listarCarrerasDeEstudiante(estudiante.getDni()));
        return new ModelAndView(model, "estado-carrera.mustache");
    }
}
