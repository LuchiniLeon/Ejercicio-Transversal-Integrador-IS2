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


        res.redirect("/dashboard");
        return null;
    }

    public static Object logout(Request req, Response res) {
        // Invalida completamente la sesión del usuario.
        // Esto elimina todos los atributos guardados en la sesión y la marca como
        // inválida.
        // La cookie JSESSIONID en el navegador también será gestionada para
        // invalidarse.
        req.session().invalidate();
        System.out.println("DEBUG: Sesión cerrada. Redirigiendo a /login.");
        // Redirige al usuario a la página de login con un mensaje de éxito.
        res.redirect("/");
        
        return null; // Importante retornar null después de una redirección.
    }

    // Nota: Esta ruta debería ser capaz de leer también mensajes de error/éxito de
    // los query params
    // si se la usa como destino de redirecciones. (Tu código de /user/create ya lo
    // hace, aplicar similar).
    public static ModelAndView vistaLogin(Request req, Response res) {

        Map<String, Object> model = new HashMap<>();

        String errorMessage = req.queryParams("error");
        if (errorMessage != null && !errorMessage.isEmpty()) {
            model.put("errorMessage", errorMessage);
        }

        String successMessage = req.queryParams("message");
        if (successMessage != null && !successMessage.isEmpty()) {
            model.put("successMessage", successMessage);
        }

        return new ModelAndView(model, "login.mustache");
    }
}


