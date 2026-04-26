package com.is1.proyecto.controller;

import java.util.HashMap;
import java.util.Map;

import spark.Request;
import spark.Response;
import spark.ModelAndView;

public class DashboardController {
    
  public static ModelAndView dashboard(Request req, Response res) {
    Map<String, Object> model = new HashMap<>(); // Modelo para la plantilla del dashboard.

        // Intenta obtener el nombre de usuario y la bandera de login de la sesión.
        String currentUsername = req.session().attribute("currentUserUsername");
        Boolean loggedIn = req.session().attribute("loggedIn");
        Boolean esAdministrador = req.session().attribute("esAdministrador"); 
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
        model.put("username", currentUsername);
           if(esAdministrador != null && esAdministrador) {
            model.put("esAdministrador", true);
        }
        // 3. Renderiza la plantilla del dashboard con el nombre de usuario.
        return new ModelAndView(model, "dashboard.mustache");
  }    
}

