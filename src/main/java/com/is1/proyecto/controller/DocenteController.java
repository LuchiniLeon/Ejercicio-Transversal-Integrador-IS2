package com.is1.proyecto.controller;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import com.is1.proyecto.models.Docente;
import com.is1.proyecto.service.DocenteService;

import spark.ModelAndView;
import spark.Request;
import spark.Response;

public class DocenteController {

  public static Object alta(Request req, Response res) {

        String nombre = req.queryParams("nombre");
        String apellido = req.queryParams("apellido");
        String fecha = req.queryParams("fecha");
        String dniStr = req.queryParams("dni");
        //String direccion = req.queryParams("direccion");
        //String telefonoStr = req.queryParams("telefono");
        String legajoStr = req.queryParams("legajo");
        String cargo = req.queryParams("cargo");
        //String name = req.queryParams("name");
        //String password = req.queryParams("password");
        String dni_adminStr = req.queryParams("dni_Admin");

        try {
            //Creamos un nuevo profesor
            DocenteService.crearDocente(
                dniStr, legajoStr, cargo, dni_adminStr,
                nombre, apellido, fecha
            );

            String msg = "Profesor " + nombre + " " + apellido + " registrado con éxito.";
            res.redirect("/docente/alta?message=" + URLEncoder.encode(msg, StandardCharsets.UTF_8));
            return "";

        } catch (IllegalArgumentException e) {
            res.status(400);
            res.redirect("/docente/alta?error=" + URLEncoder.encode(e.getMessage(), StandardCharsets.UTF_8));
            return "";

        } catch (IllegalStateException e) {
            res.status(409);
            res.redirect("/docente/alta?error=" + URLEncoder.encode(e.getMessage(), StandardCharsets.UTF_8));
            return "";

        } catch (Exception e) {
            res.status(500);
            res.redirect("/docente/alta?error=Error interno del servidor");
            return "";
        }
    }   

    public static ModelAndView formAlta(Request req, Response res) {

        Map<String, Object> model = new HashMap<>();
        
        String successMessage = req.queryParams("message");
        if (successMessage != null && !successMessage.isEmpty()) {
            model.put("successMessage", successMessage);
        }
    
        String errorMessage = req.queryParams("error");
        if (errorMessage != null && !errorMessage.isEmpty()) {
            model.put("errorMessage", errorMessage);
        }
    
        return new ModelAndView(model, "docente_form.mustache");
    }
}
