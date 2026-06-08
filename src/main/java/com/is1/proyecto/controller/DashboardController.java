package com.is1.proyecto.controller;

import java.util.HashMap;
import java.util.Map;

import com.is1.proyecto.models.Admin;
import com.is1.proyecto.models.Docente;
import com.is1.proyecto.models.Estudiante;
import com.is1.proyecto.models.Persona;
import com.is1.proyecto.models.User;
import com.is1.proyecto.service.CarreraInscripcionService;

import spark.Request;
import spark.Response;
import spark.ModelAndView;

public class DashboardController {
    
  public static ModelAndView dashboard(Request req, Response res) {
    Map<String, Object> model = new HashMap<>(); // Modelo para la plantilla del dashboard.

        // Intenta obtener el nombre de usuario y la bandera de login de la sesión.
        String currentUsername = req.session().attribute("currentUserUsername");
        Boolean loggedIn = req.session().attribute("loggedIn");

        // 1. Verificar si el usuario ha iniciado sesión.
        // Si no hay un nombre de usuario en la sesión, la bandera es nula o falsa,
        // significa que el usuario no está logueado o su sesión expiró.
        if (currentUsername == null || loggedIn == null || !loggedIn) {
            System.out.println("DEBUG: Acceso no autorizado a /dashboard. Redirigiendo a /login.");
            // Redirige al login con un mensaje de error.
            res.redirect("/login?error=Debes iniciar sesión para acceder a esta página.");
            return null; // Importante retornar null después de una redirección.
        }
        // 2. Si el usuario está logueado, añade el nombre de usuario al modelo para la
        // plantilla.
        boolean esSuper = "SuperAdmin55555".equals(req.session().attribute("currentUserUsername"));

        User user = User.findFirst("nombreUsuario = ?", currentUsername);
        Persona person = Persona.findFirst("dni = ?", user.getDNI());

        if(person != null){
            // Usamos findFirst de forma segura. Si no es admin, da null, NO rompe la conexión.
            java.util.List<Admin> admins = Admin.where("dni_Persona = ?", user.getDNI());
            if(!admins.isEmpty()) model.put("esAdministrador", true);

            java.util.List<Docente> docentes = Docente.where("dni_Persona = ?", user.getDNI());
            if(!docentes.isEmpty()) model.put("esDocente", true);

            java.util.List<Estudiante> estudiantes = Estudiante.where("dni_Persona = ?", user.getDNI());
            if(!estudiantes.isEmpty()) {
                model.put("esEstudiante", true);
                model.put("tieneInscripcionCarrera", CarreraInscripcionService.tieneInscripcionCarrera(user.getDNI()));
            }
        }
        
        if(esSuper){
            model.put("esSuperAdmin", esSuper);
            model.replace("esAdministrador", false);
        }

        model.put("username", currentUsername);
        
        // 3. Renderiza la plantilla del dashboard con el nombre de usuario.
        return new ModelAndView(model, "dashboard.mustache");
  }    
}

