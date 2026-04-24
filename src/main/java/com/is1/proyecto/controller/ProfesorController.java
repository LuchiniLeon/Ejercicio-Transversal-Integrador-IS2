package com.is1.proyecto.controller;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import com.is1.proyecto.service.ProfesorService;

import spark.Request;
import spark.Response;

public class ProfesorController {

  public static Object alta(Request req, Response res) {

        String nombre = req.queryParams("nombre");
        String apellido = req.queryParams("apellido");
        String correo = req.queryParams("correo");
        String dniStr = req.queryParams("dni");
        String direccion = req.queryParams("direccion");
        String telefonoStr = req.queryParams("telefono");
        String legajoStr = req.queryParams("legajo");
        String cargo = req.queryParams("cargo");
        String name = req.queryParams("name");
        String password = req.queryParams("password");

        try {
            //Creamos un nuevo profesor
            ProfesorService.crearProfesor(
                nombre, apellido, correo,
                dniStr, direccion, telefonoStr,
                legajoStr, cargo, name, password
            );

            String msg = "Profesor " + nombre + " " + apellido + " registrado con éxito.";
            res.redirect("/profesor/alta?message=" + URLEncoder.encode(msg, StandardCharsets.UTF_8));
            return "";

        } catch (IllegalArgumentException e) {
            res.status(400);
            res.redirect("/profesor/alta?error=" + URLEncoder.encode(e.getMessage(), StandardCharsets.UTF_8));
            return "";

        } catch (IllegalStateException e) {
            res.status(409);
            res.redirect("/profesor/alta?error=" + URLEncoder.encode(e.getMessage(), StandardCharsets.UTF_8));
            return "";

        } catch (Exception e) {
            res.status(500);
            res.redirect("/profesor/alta?error=Error interno del servidor");
            return "";
        }
    }   
}
