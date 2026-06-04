package com.is1.proyecto.controller;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import com.is1.proyecto.service.NotaService;

import spark.ModelAndView;
import spark.Request;
import spark.Response;

public class NotaController {

    public static ModelAndView formAlta(Request req, Response res) {

        Map<String, Object> model = new HashMap<>();

        String message = req.queryParams("message");
        if (message != null && !message.isEmpty()) {
            model.put("message", message);
        }

        String errorMessage = req.queryParams("error");
        if (errorMessage != null && !errorMessage.isEmpty()) {
            model.put("errorMessage", errorMessage);
        }

        return new ModelAndView(model, "nota-alta.mustache");
    }

    public static Object alta(Request req, Response res) {

        try {

            String condicion = req.queryParams("condicion");
            Integer notaFinal = Integer.parseInt(req.queryParams("notaFinal"));
            String fechaExamen = req.queryParams("fechaExamen");

            NotaService.crearNota(
                    condicion,
                    notaFinal,
                    fechaExamen,
                    null,
                    null,
                    null,
                    null
            );

            String msg = "Nota creada correctamente";

            res.redirect("/nota/lista?message=" +
                    URLEncoder.encode(msg, StandardCharsets.UTF_8));

            return "";

        } catch (Exception e) {

            res.redirect("/nota/alta?error=" +
                    URLEncoder.encode(e.getMessage(), StandardCharsets.UTF_8));

            return "";
        }
    }
}
