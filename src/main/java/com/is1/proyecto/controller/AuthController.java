package com.is1.proyecto.controller;

import com.is1.proyecto.models.User;
import com.is1.proyecto.service.AuthService;

import java.util.HashMap;
import java.util.Map;
import spark.Request;
import spark.Response;
import spark.ModelAndView;


public class AuthController {
     public static Object login(Request req, Response res) {

        Map<String, Object> model = new HashMap<>();

        String username = req.queryParams("username");
        String plainTextPassword = req.queryParams("password");

        // validación básica (HTTP)
        if (username == null || username.isEmpty() || plainTextPassword == null || plainTextPassword.isEmpty()) {
            res.status(400);
            model.put("errorMessage", "El nombre de usuario y la contraseña son requeridos.");
            return new ModelAndView(model, "login.mustache");
        }

        User user = AuthService.login(username, plainTextPassword);

        if (user == null) {
            res.status(401);
            model.put("errorMessage", "Usuario o contraseña incorrectos.");
            return new ModelAndView(model, "login.mustache");
        }

        // sesión (esto SI es controller)
        req.session(true).attribute("currentUserUsername", username);
        req.session().attribute("userId", user.getId());
        req.session().attribute("loggedIn", true);

        // admin (esto también es controller porque usa session)
        Object adminValue = user.get("esAdministrador");
        boolean isAdmin = false;

        if (adminValue != null) {
            if (adminValue instanceof Number) {
                isAdmin = ((Number) adminValue).intValue() == 1;
            } else if (adminValue instanceof Boolean) {
                isAdmin = (Boolean) adminValue;
            }
        }

        req.session().attribute("esAdministrador", isAdmin);

        res.redirect("/dashboard");
        return null;
    }
}


