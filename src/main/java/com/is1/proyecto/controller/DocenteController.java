package com.is1.proyecto.controller;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import com.is1.proyecto.models.Docente;
import com.is1.proyecto.models.Persona;
import com.is1.proyecto.service.DocenteService;

import spark.ModelAndView;
import spark.Request;
import spark.Response;

public class DocenteController {

  public static Object alta(Request req, Response res) {

        String dniStr = req.queryParams("dni");
        String legajoStr = req.queryParams("legajo");
        String cargo = req.queryParams("cargo");
        String dniAdminStr = req.queryParams("dniAdmin");

        Integer dniInteger = null;
        Integer legajoInteger = null;
        Integer dniAdminInt = null;

        try{
            if(dniStr!=null && !dniStr.isEmpty()){
                dniInteger = Integer.parseInt(dniStr);
            }
        } catch (NumberFormatException e) {
            res.redirect("/docente/alta?error=" + URLEncoder.encode("El DNI debe ser numérico", StandardCharsets.UTF_8));
            return "";
        }

         try{
            if(legajoStr!=null && !legajoStr.isEmpty()){
                legajoInteger = Integer.parseInt(legajoStr);
            }
        } catch (NumberFormatException e) {
            res.redirect("/docente/alta?error=" + URLEncoder.encode("El legajo debe ser numérico", StandardCharsets.UTF_8));
            return "";
        }

        try{
            if(dniAdminStr!=null && !dniAdminStr.isEmpty()){
                dniAdminInt = Integer.parseInt(dniAdminStr);
            }
        } catch (NumberFormatException e) {
            res.redirect("/docente/alta?error=" + URLEncoder.encode("El DNI del administrador debe ser numérico", StandardCharsets.UTF_8));
            return "";
        }

        try {
            //Creamos un nuevo profesor
            DocenteService.crearDocente(dniInteger, legajoInteger, cargo, dniAdminInt);

            Persona persona = Persona.findFirst("dni = ?", dniInteger);

            String nombre = "";
            String apellido = "";

            if(persona!=null){
                nombre = persona.getNombre();
                apellido = persona.getApellido();
            }

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
            e.printStackTrace();
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
