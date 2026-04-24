package com.is1.proyecto.controller;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import com.is1.proyecto.service.UserService;

import spark.Request;
import spark.Response;

public class UserController {
    public static Object create(Request req, Response res) {

        String name = req.queryParams("name");
        String password = req.queryParams("password");

        try {

            UserService.createUser(name, password);

            res.status(201);
            res.redirect("/user/create?message=" +
                URLEncoder.encode("Cuenta creada exitosamente para " + name + "!", StandardCharsets.UTF_8)
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

            return "";
        }   
    }
}
