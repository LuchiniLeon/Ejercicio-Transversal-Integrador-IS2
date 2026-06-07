package com.is1.proyecto.controller;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import com.is1.proyecto.models.Persona;
import com.is1.proyecto.service.AdminService;

import spark.ModelAndView;
import spark.Request;
import spark.Response;

public class AdminController {
   
    public static Object alta(Request req, Response res){

        String dni = req.queryParams("dni");
        String cargo = req.queryParams("cargo");
        String sector = req.queryParams("sector");

        Integer dni_Persona = null;
        try {
            if (dni != null && !dni.isEmpty()) {
                dni_Persona = Integer.parseInt(dni);
            }
        } catch (NumberFormatException e) {
            res.redirect("/admin/alta?error=" + URLEncoder.encode("El DNI debe ser numérico", StandardCharsets.UTF_8));
            return "";
        }

        try{
            AdminService.crearAdmin(dni_Persona, cargo, sector);

            Persona persona = Persona.findFirst("dni = ?", dni_Persona);
            
            String nombre = "";
            String apellido = "";

            if(persona!=null){
                nombre = persona.getNombre();
                apellido = persona.getApellido();
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
            e.printStackTrace();
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

    //Metodo que recibe la peticion para mostrar la plantilla de opciones de asignacion a profesores
    public static ModelAndView opcionesAsignacion(Request req, Response res) {
        //model.put("materias", MateriaService.listarMaterias());
        //model.put("docentes", DocenteService.listarDocentes());
        return new ModelAndView(new HashMap<>(), "opciones_asignacion.mustache");
    }

    
}
