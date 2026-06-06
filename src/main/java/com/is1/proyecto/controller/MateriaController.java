
package com.is1.proyecto.controller;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.is1.proyecto.service.MateriaService;

import spark.ModelAndView;
import spark.Request;
import spark.Response;

public class MateriaController {

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

        return new ModelAndView(model, "materia-alta.mustache");
    }

    public static Object alta(Request req, Response res) {

        try {

            Integer codigo = Integer.parseInt(req.queryParams("codigo"));
            String nombre = req.queryParams("nombre");
            Integer horasTotales = Integer.parseInt(req.queryParams("horasTotales"));
            Integer dniAdministrador = Integer.parseInt(req.queryParams("dniAdministrador"));
            Integer dniDocente = Integer.parseInt(req.queryParams("dniDocente"));

            MateriaService.crearMateria(codigo, nombre, horasTotales, dniAdministrador, dniDocente);

            String msg = "Materia creada con éxito";

            res.redirect("/materia/lista?message="
                    + URLEncoder.encode(msg, StandardCharsets.UTF_8));

            return "";

        } catch (NumberFormatException e) {

            res.redirect("/materia/alta?error="
                    + URLEncoder.encode("Los campos numéricos deben contener números válidos",
                            StandardCharsets.UTF_8));

            return "";

        } catch (IllegalArgumentException e) {

            res.redirect("/materia/alta?error="
                    + URLEncoder.encode(e.getMessage(), StandardCharsets.UTF_8));

            return "";

        } catch (Exception e) {

            e.printStackTrace();

            res.redirect("/materia/alta?error="
                    + URLEncoder.encode("Error interno del servidor",
                            StandardCharsets.UTF_8));

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

        List<Map<String, Object>> materias = MateriaService.listarMaterias();

        Map<String, Object> materiasWrapper = new HashMap<>();
        materiasWrapper.put("lista", materias);

        model.put("materias", materiasWrapper);

        return new ModelAndView(model, "materia-lista.mustache");
    }

    public static ModelAndView formEditar(Request req, Response res) {

        Map<String, Object> model = new HashMap<>();

        String errorMessage = req.queryParams("error");
        if (errorMessage != null && !errorMessage.isEmpty()) {
            model.put("errorMessage", errorMessage);
        }

        try {

            Integer id = Integer.parseInt(req.params(":id"));

            Map<String, Object> materia =
                    MateriaService.obtenerMateria(id);

            if (materia == null) {
                model.put("errorMessage", "Materia no encontrada");
            } else {
                model.putAll(materia);
            }

        } catch (Exception e) {

            model.put("errorMessage", "Materia no encontrada");
        }

        return new ModelAndView(model, "materia-editar.mustache");
    }

    public static Object editar(Request req, Response res) {

        try {

            Integer id = Integer.parseInt(req.params(":id"));
            Integer codigo = Integer.parseInt(req.queryParams("codigo"));
            String nombre = req.queryParams("nombre");
            Integer horasTotales = Integer.parseInt(req.queryParams("horasTotales"));
            Integer dniDocente = Integer.parseInt(req.queryParams("dniDocente"));

            MateriaService.editarMateria(
                    id,
                    codigo,
                    nombre,
                    horasTotales,
                    dniDocente);

            String msg = "Materia actualizada con éxito";

            res.redirect("/materia/lista?message="
                    + URLEncoder.encode(msg, StandardCharsets.UTF_8));

            return "";

        } catch (NumberFormatException e) {

            res.redirect("/materia/editar/"
                    + req.params(":id")
                    + "?error="
                    + URLEncoder.encode("Los campos numéricos deben contener números válidos",
                            StandardCharsets.UTF_8));

            return "";

        } catch (IllegalArgumentException e) {

            res.redirect("/materia/editar/"
                    + req.params(":id")
                    + "?error="
                    + URLEncoder.encode(e.getMessage(), StandardCharsets.UTF_8));

            return "";

        } catch (Exception e) {

            e.printStackTrace();

            res.redirect("/materia/lista?error="
                    + URLEncoder.encode("Error interno del servidor",
                            StandardCharsets.UTF_8));

            return "";
        }
    }

    public static Object eliminar(Request req, Response res) {

        try {

            Integer id = Integer.parseInt(req.params(":id"));

            MateriaService.eliminarMateria(id);

            String msg = "Materia eliminada con éxito";

            res.redirect("/materia/lista?message="
                    + URLEncoder.encode(msg, StandardCharsets.UTF_8));

            return "";

        } catch (IllegalArgumentException e) {

            res.redirect("/materia/lista?error="
                    + URLEncoder.encode(e.getMessage(), StandardCharsets.UTF_8));

            return "";

        } catch (Exception e) {

            e.printStackTrace();

            res.redirect("/materia/lista?error="
                    + URLEncoder.encode("Error interno del servidor",
                            StandardCharsets.UTF_8));

            return "";
        }
    }
}