package com.is1.proyecto.controller;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import org.javalite.activejdbc.Base;
import java.util.List;

import com.is1.proyecto.models.Admin;
import com.is1.proyecto.models.Docente;
import com.is1.proyecto.models.Persona;
import com.is1.proyecto.service.AdminService;
import com.is1.proyecto.service.DocenteService;

import spark.ModelAndView;
import spark.Request;
import spark.Response;

public class AdminController {
   
    public static Object alta(Request req, Response res){

        Integer dni_Persona = req.attribute("dni");
        String cargo = req.attribute("cargo");
        String sector = req.attribute("sector");

        try{
            AdminService.crearAdmin(dni_Persona, cargo, sector);
            
            List<Map> result = Base.findAll("SELECT name, apellido FROM persona WHERE(persona.dni = ?)");

            String nombre = "";
            String apellido = "";

            if(!result.isEmpty()){
                Map persona = result.get(0);
                nombre = (String) persona.get("nombre");
                apellido = (String) persona.get("apellido");
            }

            String msg = "Administrador: " + nombre + " " + apellido + " habilitado con éxito.";
            res.redirect("/admin/alta?message=" + URLEncoder.encode(msg, StandardCharsets.UTF_8));
            return "";
        } catch (IllegalArgumentException e) {
            res.status(400);
            res.redirect("/admin/alta?error=" + URLEncoder.encode(e.getMessage(), StandardCharsets.UTF_8));
            return "";

        } catch (IllegalStateException e) {
            res.status(409);
            res.redirect("/admin/alta?error=" + URLEncoder.encode(e.getMessage(), StandardCharsets.UTF_8));
            return "";

        } catch (Exception e) {
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
}
