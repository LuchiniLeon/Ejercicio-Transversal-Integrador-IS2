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
            Integer dniEstudiante = Integer.parseInt(req.queryParams("dniEstudiante"));
            String condicion = req.queryParams("condicion");
            Integer notaFinal = Integer.parseInt(req.queryParams("notaFinal"));
            String fechaExamen = req.queryParams("fechaExamen");

        
            Integer idMateria = null;
            if (req.queryParams("idMateria") != null && !req.queryParams("idMateria").isEmpty()) {
                idMateria = Integer.parseInt(req.queryParams("idMateria"));
            }

            Integer idTaller = null;
            if (req.queryParams("idTaller") != null && !req.queryParams("idTaller").isEmpty()) {
                idTaller = Integer.parseInt(req.queryParams("idTaller"));
            }

            NotaService.crearNota(condicion, notaFinal, fechaExamen, dniEstudiante, idMateria, idTaller, null);
            
            res.redirect("/nota/lista?message=" +
                    URLEncoder.encode("Nota creada correctamente", StandardCharsets.UTF_8));
            return "";

        } catch (Exception e) {
            res.redirect("/nota/alta?error=" +
                    URLEncoder.encode(e.getMessage(), StandardCharsets.UTF_8));
            return "";
        }
    }

    public static ModelAndView lista(Request req, Response res) {
        Map<String, Object> model = new HashMap<>();

        String message = req.queryParams("message");
        if (message != null && !message.isEmpty()) {
            model.put("successMessage", message);
        }

        String errorMessage = req.queryParams("error");
        if (errorMessage != null && !errorMessage.isEmpty()) {
            model.put("errorMessage", errorMessage);
        }

        model.put("notas", NotaService.listarNotas());

        return new ModelAndView(model, "nota-lista.mustache");
    }

    public static ModelAndView formEditar(Request req, Response res) {
        Map<String, Object> model = new HashMap<>();

        try {
            Integer id = Integer.parseInt(req.params(":id"));
            Map<String, Object> nota = NotaService.obtenerNota(id);
            model.putAll(nota);

        } catch (Exception e) {
            model.put("errorMessage", "Nota no encontrada");
        }

        return new ModelAndView(model, "nota-editar.mustache");
    }

    public static Object editar(Request req, Response res) {
        try {
            Integer id = Integer.parseInt(req.params(":id"));
            String condicion = req.queryParams("condicion");
            Integer notaFinal = Integer.parseInt(req.queryParams("notaFinal"));
            String fechaExamen = req.queryParams("fechaExamen");

            NotaService.editarNota(id, condicion, notaFinal, fechaExamen);

            res.redirect("/nota/lista?message=" +
                    URLEncoder.encode("Nota actualizada correctamente", StandardCharsets.UTF_8));
            return "";

        } catch (Exception e) {
            res.redirect("/nota/lista?error=" +
                    URLEncoder.encode(e.getMessage(), StandardCharsets.UTF_8));
            return "";
        }
    }

    public static Object eliminar(Request req, Response res) {
        try {
            Integer id = Integer.parseInt(req.params(":id"));
            NotaService.eliminarNota(id);

            res.redirect("/nota/lista?message=" +
                    URLEncoder.encode("Nota eliminada correctamente", StandardCharsets.UTF_8));
            return "";

        } catch (Exception e) {
            res.redirect("/nota/lista?error=" +
                    URLEncoder.encode(e.getMessage(), StandardCharsets.UTF_8));
            return "";
        }
    }
}