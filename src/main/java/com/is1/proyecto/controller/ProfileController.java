package com.is1.proyecto.controller;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.is1.proyecto.service.ProfileService;

import spark.ModelAndView;
import spark.Request;
import spark.Response;

public class ProfileController {

    private static final Logger logger = LoggerFactory.getLogger(ProfileController.class);

    public static ModelAndView profile(Request req, Response res) {

        Map<String, Object> model = new HashMap<>();

        // Verificación de sesión
        if (req.session().attribute("loggedIn") == null) {
            logger.warn("Intento de acceso a /profile sin iniciar sesión. Redirigiendo al inicio.");
            res.redirect("/");
            return null;
        }

        // Obtener datos básicos del usuario
        String username = req.session().attribute("currentUserUsername");

        model.put("name", username);

        try {
            Map<String, Object> profileData = ProfileService.getProfileData(username);

            model.putAll(profileData);
    
        } catch (Exception e) {
            logger.error("Error al cargar el perfil", e);
    
            model.put("errorMessage", "Error al cargar el perfil");
        }
        return new ModelAndView(model, "profile.mustache");
    }
}
