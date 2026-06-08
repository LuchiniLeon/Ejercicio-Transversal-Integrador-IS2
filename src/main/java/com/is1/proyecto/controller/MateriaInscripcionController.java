package com.is1.proyecto.controller;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.is1.proyecto.models.Estudiante;
import com.is1.proyecto.models.User;
import com.is1.proyecto.service.CarreraInscripcionService;
import com.is1.proyecto.service.MateriaInscripcionService;

import spark.ModelAndView;
import spark.Request;
import spark.Response;

public class MateriaInscripcionController {

    public static ModelAndView listaMaterias(Request req, Response res) {
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

        if (!CarreraInscripcionService.tieneInscripcionCarrera(estudiante.getDni())) {
            model.put("errorMessage", "Primero inscribite a una carrera");
            model.put("hayMaterias", false);
            return new ModelAndView(model, "lista-materias-estudiantes.mustache");
        }

        List<Map<String, Object>> materias = MateriaInscripcionService.materiasDisponibles(estudiante.getDni());
        model.put("materias", materias);
        model.put("hayMaterias", !materias.isEmpty());

        return new ModelAndView(model, "lista-materias-estudiantes.mustache");
    }

    public static ModelAndView misMaterias(Request req, Response res) {
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

        List<Map<String, Object>> lista = MateriaInscripcionService.listarMateriasDeEstudiante(estudiante.getDni());
        model.put("materias", lista);

        return new ModelAndView(model, "estudiante-mis-materias.mustache");
    }

    public static Object inscribir(Request req, Response res) {
        try {
            Integer idMateria = Integer.parseInt(req.params(":id"));
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

            MateriaInscripcionService.inscribirMateria(estudiante.getDni(), idMateria);
            String message = "Inscripción a materia realizada con éxito";
            res.redirect("/inscripcion/materia?message=" + URLEncoder.encode(message, StandardCharsets.UTF_8));
            return "";

        } catch (NumberFormatException e) {
            res.redirect("/inscripcion/materia?error=" + URLEncoder.encode("ID de materia inválido", StandardCharsets.UTF_8));
            return "";
        } catch (IllegalArgumentException e) {
            res.redirect("/inscripcion/materia?error=" + URLEncoder.encode(e.getMessage(), StandardCharsets.UTF_8));
            return "";
        } catch (Exception e) {
            e.printStackTrace();
            res.redirect("/inscripcion/materia?error=Error interno del servidor");
            return "";
        }
    }

    public static Object desincribir(Request req, Response res) {
        try {
            Integer idMateria = Integer.parseInt(req.params(":id"));
            String currentUsername = req.session().attribute("currentUserUsername");
            User user = User.findFirst("nombreUsuario = ?", currentUsername);
            Estudiante estudiante = Estudiante.findFirst("dni_Persona = ?", user.getDNI());

            if (estudiante == null) {
                res.redirect("/dashboard");
                return "";
            }

            MateriaInscripcionService.desinscribirMateria(estudiante.getDni(), idMateria);
            String message = "Te desinscribiste de la materia con éxito.";
            res.redirect("/estudiante/mis-materias?message=" + URLEncoder.encode(message, StandardCharsets.UTF_8));
            return "";

        } catch (NumberFormatException e) {
            res.redirect("/estudiante/mis-materias?error=" + URLEncoder.encode("ID de materia inválido", StandardCharsets.UTF_8));
            return "";
        } catch (IllegalArgumentException e) {
            res.redirect("/estudiante/mis-materias?error=" + URLEncoder.encode(e.getMessage(), StandardCharsets.UTF_8));
            return "";
        } catch (Exception e) {
            e.printStackTrace();
            res.redirect("/estudiante/mis-materias?error=Error interno del servidor");
            return "";
        }
    }
}
