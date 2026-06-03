package com.is1.proyecto.controller;

import spark.Request;
import spark.Response;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
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

    public static ModelAndView listaPorDocente(Request req, Response res){
        Map<String, Object> model = new HashMap<>();


        String message = req.queryParams("message");
        if (message != null && !message.isEmpty())
            model.put("successMessage", message);

        String errorMessage = req.queryParams("error");
        if (errorMessage != null && !errorMessage.isEmpty())
            model.put("errorMessage", errorMessage);

        // Usuario que esta logeado
        String currentUsername = req.session().attribute("currentUserUsername");
        
        User user = User.findFirst("nombreUsuario = ?", currentUsername);
        Docente docente = Docente.findFirst("dni_Persona = ?", user.getDNI());

        if (docente == null) {
            res.redirect("/dashboard");
            return null;
        }

        List<Map<String, Object>> lista = TallerService.listarTalleresPorDocente(docente.getDni());

        if(!lista.isEmpty()){
            Map<String, Object> talleres = new HashMap<>();
            talleres.put("lista", lista);
            model.put("talleres", talleres);
        }

        return new ModelAndView(model, "taller-lista.mustache");
    }

    public static ModelAndView formEditar(Request req, Response res) {
        Map<String, Object> model = new HashMap<>();

        try{
            Integer id = Integer.parseInt(req.params(":id"));
            Map<String, Object> taller = TallerService.obtenerTaller(id);

            model.putAll(taller);

        } catch (Exception e){
             model.put("errorMessage", "Taller no encontrado");
        }

        return new ModelAndView(model, "taller-editar.mustache");
    }

    public static Object editar(Request req, Response res){
        try{
            Integer id = Integer.parseInt(req.params("id"));
            String titulo = req.queryParams("titulo");
            Integer hora = Integer.parseInt(req.queryParams("hora"));
            Boolean vigente = req.queryParams("vigente").equals("1");

            TallerService.editarTaller(id, titulo, hora, vigente);

            String msg = "Taller actualizado con éxito.";
            res.redirect("/taller/lista?message=" + URLEncoder.encode(msg, StandardCharsets.UTF_8));
            return "";

        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            res.redirect("/taller/editar/" + req.params(":id") + "?error=" + URLEncoder.encode(e.getMessage(), StandardCharsets.UTF_8));
            return "";
        } catch (Exception e) {
            e.printStackTrace();
            res.redirect("/taller/editar/" + req.params(":id") + "?error=Error interno del servidor");
            return "";
        }
    }

    public static Object eliminar(Request req, Response res) {
        try {
            Integer id = Integer.parseInt(req.params(":id"));
            TallerService.eliminarTaller(id);
            String msg = "Taller eliminado con éxito.";
            res.redirect("/taller/lista?message=" + URLEncoder.encode(msg, StandardCharsets.UTF_8));
            return "";
        } catch (IllegalArgumentException e) {
            res.redirect("/taller/lista?error=" + URLEncoder.encode(e.getMessage(), StandardCharsets.UTF_8));
            return "";
        } catch (Exception e) {
            e.printStackTrace();
            res.redirect("/taller/lista?error=Error interno del servidor");
            return "";
        }
    }
}
