package com.is1.proyecto.controller;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.is1.proyecto.models.Estudiante;
import com.is1.proyecto.models.User;
import com.is1.proyecto.service.EstudiaService;

import spark.ModelAndView;
import spark.Request;
import spark.Response;

public class EstudiaController {
    
    //Lista talleres disponibles para que el estudiante se inscriba
    public static ModelAndView listaTaller(Request req, Response res){
        Map<String, Object> model = new HashMap<>();

        String message = req.queryParams("message");
        if (message != null && !message.isEmpty())
            model.put("successMessage", message);

        String errorMessage = req.queryParams("error");
        if (errorMessage != null && !errorMessage.isEmpty())
            model.put("errorMessage", errorMessage);

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

        List<Map<String, Object>> lista = EstudiaService.talleresDisponibles(estudiante.getDni());
        Map<String, Object> talleres = new HashMap<>();
        talleres.put("lista", lista);
        model.put("talleres", talleres);

        return new ModelAndView(model, "lista-talleres-estudiantes.mustache");
    }

    public static ModelAndView misTalleres(Request req, Response res) {
        Map<String, Object> model = new HashMap<>();

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

        List<Map<String, Object>> lista = EstudiaService.listarTalleresDeEstudiante(estudiante.getDni());
        model.put("talleres", lista);

        return new ModelAndView(model, "estudiante-mis-talleres.mustache");
    }

    //Inscribir al estudiante logueado al taller
    public static Object inscribir(Request req, Response res){
        try{
            Integer id_taller = Integer.parseInt(req.params(":id"));
            // Estudiante logueado
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

            if(estudiante == null){
                res.redirect("/dashboard");
                return "";
            }

            EstudiaService.inscribir(estudiante.getDni(), id_taller);
            String message = "Se a realizado la inscripcion con exito";
            res.redirect("/estudiante/talleres?message=" + URLEncoder.encode(message, StandardCharsets.UTF_8));
            return "";
        } catch (IllegalArgumentException e){
            res.redirect("/estudiante/talleres?error=" + URLEncoder.encode(e.getMessage(), StandardCharsets.UTF_8));
            return "";
        } catch(Exception e){
            e.printStackTrace();
            res.redirect("/estudiante/talleres?error=Error interno del servidor");
            return "";
        }
    }

    //Desincribir al estudiante de un taller
    public static Object desincribir(Request req, Response res){
        try{
            Integer id_taller = Integer.parseInt(req.params(":id"));
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

            EstudiaService.desincribir(estudiante.getDni(), id_taller);
            String msg = "Te desinscribiste del taller con éxito.";
            res.redirect("/estudiante/talleres?message=" + URLEncoder.encode(msg, StandardCharsets.UTF_8));
            return "";

        } catch (IllegalArgumentException e) {
            res.redirect("/estudiante/talleres?error=" + URLEncoder.encode(e.getMessage(), StandardCharsets.UTF_8));
            return "";
        } catch (Exception e) {
            e.printStackTrace();
            res.redirect("/estudiante/talleres?error=Error interno del servidor");
            return "";
        }
    }
}
