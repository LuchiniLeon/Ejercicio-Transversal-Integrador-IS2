package com.is1.proyecto.controller;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import com.is1.proyecto.models.User;
import com.is1.proyecto.service.UserService;

import spark.ModelAndView;
import spark.Request;
import spark.Response;

public class UserController {
    public static Object create(Request req, Response res) {

        String name = req.queryParams("name");
        String password = req.queryParams("password");
        String passwordVerificacion = req.queryParams("passwordConfirmada");
        String nombre = req.queryParams("nombre");
        String apellido = req.queryParams("apellido");
        String fechaNacimiento = req.queryParams("fechaNacimiento");
        String email = req.queryParams("email");

        Integer dni = null; //Para obtener el valor de dni del formulario, lo hacemos dentro de un try catch por si se pasó un valor distinto a Integer
        try{

            String dniForm = req.queryParams("dni");
            if(dniForm == null || dniForm.isEmpty()){
                throw new NumberFormatException("El DNI está vacío");
            }
            dni = Integer.parseInt(dniForm);

        } catch (NumberFormatException e) {
            System.err.println("Formato incorrecto para campo DNI:" + e.getMessage());

            res.redirect("/user/create?error=" +
                URLEncoder.encode("DNI debe ser un número sin puntos ni espacios", StandardCharsets.UTF_8)
            );

            return null;

        }

        if(password == null || !(password.equals(passwordVerificacion))){
             res.redirect("/user/create?error=" +
                URLEncoder.encode("La contraseña debe coincidir", StandardCharsets.UTF_8)
            );
            return null;
        }


        try {

            UserService.createUser(name, password, nombre, apellido, fechaNacimiento, dni, email);

            res.status(201);
            res.redirect("/user/create?message=" +
                URLEncoder.encode("Cuenta creada exitosamente para " + nombre + "!", StandardCharsets.UTF_8)
            );

            return "";

        } catch (IllegalArgumentException e) {

            res.status(400);
            res.redirect("/user/create?error=" +
                URLEncoder.encode(e.getMessage(), StandardCharsets.UTF_8)
            );

            return "";

        } catch (Exception e) {

            res.status(500);
            res.redirect("/user/create?error=Error interno al crear la cuenta.");
            e.printStackTrace();

            return null;
        }   
    }

    // Soporta la visualización de mensajes de éxito o error pasados como query
    // parameters.
    public static ModelAndView formCreate(Request req, Response res) {

        Map<String, Object> model = new HashMap<>();

        // Obtener y añadir mensaje de éxito de los query parameters (ej.
        // ?message=Cuenta creada!)
        String successMessage = req.queryParams("message");
        if (successMessage != null && !successMessage.isEmpty()) {
            model.put("successMessage", successMessage);
        }


        // Obtener y añadir mensaje de error de los query parameters (ej. ?error=Campos
        // vacíos)
        String errorMessage = req.queryParams("error");
        if (errorMessage != null && !errorMessage.isEmpty()) {
            model.put("errorMessage", errorMessage);
        }

        return new ModelAndView(model, "user_form.mustache");
    }

    // Advertencia: Esta ruta tiene un propósito diferente a las de formulario HTML.
    public static Object addUser(Request req, Response res) throws Exception {

        res.type("application/json");

        String name = req.queryParams("name");
        String password = req.queryParams("password");

        // Validaciones básicas
        if (name == null || name.isEmpty() || password == null || password.isEmpty()) {
            res.status(400);
            return new com.fasterxml.jackson.databind.ObjectMapper()
                    .writeValueAsString(Map.of("error", "Nombre y contraseña son requeridos."));
        }

        try {

            User newUser = new User();
            newUser.set("nombreUsuario", name);
            newUser.set("contraseña", password);
            newUser.saveIt();

            res.status(201);

            return new com.fasterxml.jackson.databind.ObjectMapper()
                    .writeValueAsString(
                            Map.of("message", "Usuario '" + name + "' registrado con exito.", "id", newUser.getId()));

        } catch (Exception e) {
            System.err.println("Error al registrar usuario: " + e.getMessage());
            e.printStackTrace();
            res.status(500);

            return new com.fasterxml.jackson.databind.ObjectMapper()
                    .writeValueAsString(
                            Map.of("error", "Error interno al registrar usuario: " + e.getMessage()));
        }
    }  
    
    // En una aplicación real, probablemente querrías unificar con '/user/create'
    // para evitar duplicidad.
    public static ModelAndView formNew(Request req, Response res) {
        return new ModelAndView(new HashMap<>(), "user_form.mustache");
    }
}
