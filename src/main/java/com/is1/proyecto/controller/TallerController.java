package com.is1.proyecto.controller;

import spark.Request;
import spark.Response;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import com.is1.proyecto.models.Docente;
import com.is1.proyecto.models.User;
import com.is1.proyecto.service.TallerService;

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

        try{
            String titulo = req.queryParams("titulo");
            Integer hora = Integer.parseInt(req.queryParams("hora"));
            Boolean vigente = req.queryParams("vigente").equals("1");
            String currentUsername = req.session().attribute("currentUserUsername");
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
            e.printStackTrace();
            String errorMsg = e.getMessage() != null ? e.getMessage() : "Error interno del servidor";
            res.redirect("/taller/alta?error=" + URLEncoder.encode(errorMsg, StandardCharsets.UTF_8));
            return "";
        }
    }

}
