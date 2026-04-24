package com.is1.proyecto.controller;

import java.util.HashMap;
import java.util.Map;

import com.is1.proyecto.service.ProfileService;

import spark.ModelAndView;
import spark.Request;
import spark.Response;

public abstract class ProfileController {


public static Object profile(Request req, Response res) {

        Map<String, Object> model = new HashMap<>();

        // Verificación de sesión
        if (req.session().attribute("loggedIn") == null) {
            res.redirect("/");
            return null;
        }

        // Obtener datos básicos del usuario
        Integer userId = req.session().attribute("userId");
        String username = req.session().attribute("currentUserUsername");

        model.put("name", username);

        try {
            Map<String, Object> profileData = ProfileService.getProfileData(userId);

            model.putAll(profileData);

        } catch (Exception e) {
            model.put("errorMessage", "Error al cargar el perfil");
        }

        return new ModelAndView(model, "profile.mustache");
    }
}
