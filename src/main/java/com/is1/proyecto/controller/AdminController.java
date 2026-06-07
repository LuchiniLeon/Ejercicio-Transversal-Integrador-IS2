package com.is1.proyecto.controller;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import org.javalite.activejdbc.Base;
import java.util.List;

import com.is1.proyecto.models.Admin;
import com.is1.proyecto.models.Persona;
import com.is1.proyecto.models.User;
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

        return new ModelAndView(new HashMap<>(), "opciones_asignacion.mustache");
    }

    public static ModelAndView formAltaTaller(Request req, Response res) {
        Map<String, Object> model = new HashMap<>();
        
        String currentUsername = req.session().attribute("currentUserUsername");
        User user = User.findFirst("nombreUsuario = ?", currentUsername);
        Admin admin = Admin.findFirst("dni_Persona = ?", user.getDNI());
    
        if (admin == null) {
            res.redirect("/dashboard");
            return null;
        }

        model.put("docentes", AdminService.obtenerDocentes());

        return new ModelAndView(model, "/alta_admin.mustache");
    }

    public static Object altaTaller(Request req, Response res) {
        try {
            String currentUsername = req.session().attribute("currentUserUsername");
            User user = User.findFirst("nombreUsuario = ?", currentUsername);
            Admin admin = Admin.findFirst("dni_Persona = ?", user.getDNI());
        
            if (admin == null) {
                res.redirect("/dashboard");
                return "";
            }

            String titulo = req.queryParams("titulo");
            Integer hora = Integer.parseInt(req.queryParams("hora"));
            Boolean vigente = req.queryParams("vigente").equals("1");
            Integer dniDocente = Integer.parseInt(req.queryParams("dniDocente"));

            AdminService.crearTallerComoAdmin(titulo, hora, vigente, dniDocente);
        
            String msg = "Taller '" + titulo + "' creado con éxito.";
            res.redirect("/admin/taller/lista?message=" + URLEncoder.encode(msg, StandardCharsets.UTF_8));
        
            return "";
        } catch (IllegalArgumentException e) {
            res.redirect("/admin/taller/alta?error=" + URLEncoder.encode(e.getMessage(), StandardCharsets.UTF_8));
            return "";

        } catch (Exception e) {
            e.printStackTrace();
            String errorMsg = e.getMessage() != null ? e.getMessage() : "Error interno del servidor";
            res.redirect("/admin/taller/alta?error=" + URLEncoder.encode(errorMsg, StandardCharsets.UTF_8));
            
            return "";
        }
    }

    public static ModelAndView listaTalleres(Request req, Response res) {
    String currentUsername = req.session().attribute("currentUserUsername");
        User user = User.findFirst("nombreUsuario = ?", currentUsername);
        Admin admin = Admin.findFirst("dni_Persona = ?", user.getDNI());
        if (admin == null) {
            res.redirect("/dashboard");
            return null;
        }

        Map<String, Object> model = new HashMap<>();
        model.put("talleres", AdminService.obtenerTalleres());

        String successMessage = req.queryParams("message");
        if (successMessage != null && !successMessage.isEmpty()) {
            model.put("successMessage", successMessage);
        }

        return new ModelAndView(model, "admin_lista-talleres.mustache");
    }

    public static ModelAndView formAsignarDocenteTaller(Request req, Response res) {
        String currentUsername = req.session().attribute("currentUserUsername");
        User user = User.findFirst("nombreUsuario = ?", currentUsername);
        Admin admin = Admin.findFirst("dni_Persona = ?", user.getDNI());
        if (admin == null) {
            res.redirect("/dashboard");
            return null;
        }

        Map<String, Object> model = new HashMap<>();
        model.put("docentes", AdminService.obtenerDocentes());
        model.put("talleres", AdminService.obtenerTalleres());

        String errorMessage = req.queryParams("error");
        if (errorMessage != null && !errorMessage.isEmpty()) model.put("errorMessage", errorMessage);

        return new ModelAndView(model, "asignar_docente-taller.mustache");
    }

    public static Object asignarDocenteTaller(Request req, Response res) {
        try {
            String currentUsername = req.session().attribute("currentUserUsername");
            User user = User.findFirst("nombreUsuario = ?", currentUsername);
            Admin admin = Admin.findFirst("dni_Persona = ?", user.getDNI());

            if (admin == null) {
                res.redirect("/dashboard");
                return "";
            }

            Integer idTaller = Integer.parseInt(req.queryParams("idTaller"));
            Integer dniDocente = Integer.parseInt(req.queryParams("dniDocente"));

            AdminService.asignarDocenteATaller(idTaller, dniDocente);
            String msg = "Docente asignado al taller con éxito.";
            res.redirect("/admin/taller/lista?message=" + URLEncoder.encode(msg, StandardCharsets.UTF_8));
            return "";
            
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            res.redirect("/admin/taller/asignar?error=" + URLEncoder.encode(e.getMessage(), StandardCharsets.UTF_8));
            return "";
        } catch (Exception e) {
            e.printStackTrace();
            String errorMsg = e.getMessage() != null ? e.getMessage() : "Error interno del servidor";
            res.redirect("/admin/taller/asignar?error=" + URLEncoder.encode(errorMsg, StandardCharsets.UTF_8));
            return "";
        }
    }
}
