package com.is1.proyecto.controller;

import java.util.HashMap;
import java.util.Map;

import com.is1.proyecto.service.ProfileService;

import spark.ModelAndView;
import spark.Request;
import spark.Response;

public class ProfileController {

    public static ModelAndView profile(Request req, Response res) {

        Map<String, Object> model = new HashMap<>();

        // Verificación de sesión
        if (req.session().attribute("loggedIn") == null) {
            res.redirect("/");
            System.out.println("El logueo es nulo");
            return null;
        }

        // Obtener datos básicos del usuario
        String username = req.session().attribute("currentUserUsername");

        model.put("name", username);

        try {
            Map<String, Object> profileData = ProfileService.getProfileData(username);
            System.out.println("Sigo en el try");
            model.putAll(profileData);
    
        } catch (Exception e) {
            e.printStackTrace(); 
            model.put("errorMessage", "Error al cargar el perfil");
        }
        return new ModelAndView(model, "profile.mustache");
    }
}
