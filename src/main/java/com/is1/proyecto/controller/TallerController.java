package com.is1.proyecto.controller;

import spark.Request;
import spark.Response;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import com.is1.proyecto.models.Docente;
import com.is1.proyecto.models.User;

import spark.ModelAndView;

public class TallerController {
    
    public static ModelAndView formAlta(Request req, Response res){
        Map<String, Object> model = new HashMap<>();
        String message = req.queryParams("message");
        if(message != null && !message.isEmpty())
                model.put("message", message);

        String errorMessage = req.queryParams("error");
        if(errorMessage != null && !errorMessage.isEmpty())
                model.put("errorMessage", errorMessage);
        
        return new ModelAndView(model, "taller-alta.mustache");
    }

    public static Object alta(Request req, Response res){
        String titulo = req.queryParams("titulo");
        String hora = req.queryParams("hora");
        String vigente = req.queryParams("vigente");
        String currentUsername = req.session().attribute("currentUsername");

        try{
            User user = User.findFirst("nombreUsuario = ?", currentUsername);
            Docente docente = Docente.findFirst("dni_Persona = ?", user.getDNI());

            if(docente == null){
                res.redirect("/dashboard");
                return "";
            }
            TallerService.crearTaller(titulo, hora, vigente, docente.getDni());
            String msg = "Taller '" + titulo + "' creado con éxito.";
            res.redirect("/taller/lista?message=" + URLEncoder.encode(msg, StandardCharsets.UTF_8));
            return "";

        } catch  (IllegalArgumentException e) {
            res.redirect("/taller/alta?error=" + URLEncoder.encode(e.getMessage(), StandardCharsets.UTF_8));
            return "";
        } catch (Exception e) {
            res.redirect("/taller/alta?error=Error interno del servidor");
            return "";
        }
    }

}
