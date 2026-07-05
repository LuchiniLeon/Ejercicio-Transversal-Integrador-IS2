package com.is1.proyecto.controller;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.is1.proyecto.models.Docente;
import com.is1.proyecto.models.Persona;
import com.is1.proyecto.models.User;
import com.is1.proyecto.service.DocenteService;
import com.is1.proyecto.service.EstudianteService;

import spark.ModelAndView;
import spark.Request;
import spark.Response;

public class EstudianteController {

    //Unicamente usado para reemplazar un printStackTrace
    private static final Logger logger = LoggerFactory.getLogger(EstudianteController.class);

     public static Object alta(Request req, Response res) {

        String dniStr = req.queryParams("dni");
        String estadoAcademico = req.queryParams("estadoAcademico");
        String ingresoStr = req.queryParams("ingreso");

        Integer dniInteger = null;

        String usernameActual = req.session().attribute("currentUserUsername");

        if(usernameActual == null){
            res.redirect("/login?error=" + URLEncoder.encode("Debes iniciar sesión para realizar esta acción.", StandardCharsets.UTF_8));
            return "";
        }

        User usuario = User.findFirst("nombreUsuario = ?", usernameActual);

        Integer dniAdmin = usuario.getDNI();

        try{
            if(dniStr!=null && !dniStr.isEmpty()){
                dniInteger = Integer.parseInt(dniStr);
            }
        } catch (NumberFormatException e) {
            res.redirect("/estudiante/alta?error=" + URLEncoder.encode("El DNI debe ser numérico", StandardCharsets.UTF_8));
            return "";
        }

        try {
            //Creamos un nuevo estudiante
            EstudianteService.crearEstudiante(dniInteger, estadoAcademico, ingresoStr, dniAdmin);

            Persona persona = Persona.findFirst("dni = ?", dniInteger);

            String nombre = "";
            String apellido = "";

            if(persona!=null){
                nombre = persona.getNombre();
                apellido = persona.getApellido();
            }

            String msg = "Estudiante " + nombre + " " + apellido + " registrado con éxito.";
            res.redirect("/estudiante/alta?message=" + URLEncoder.encode(msg, StandardCharsets.UTF_8));
            return "";

        } catch (IllegalArgumentException e) {
            res.status(400);
            res.redirect("/estudiante/alta?error=" + URLEncoder.encode(e.getMessage(), StandardCharsets.UTF_8));
            return "";

        } catch (IllegalStateException e) {
            res.status(409);
            res.redirect("/estudiante/alta?error=" + URLEncoder.encode(e.getMessage(), StandardCharsets.UTF_8));
            return "";

        } catch (Exception e) {
            logger.error("Error interno durante alta de estudiante", e);
            res.status(500);
            res.redirect("/estudiante/alta?error=Error interno del servidor");
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
    
        return new ModelAndView(model, "estudiante_form.mustache");
    }
}
