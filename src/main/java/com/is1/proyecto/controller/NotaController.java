package com.is1.proyecto.controller;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import com.is1.proyecto.models.Docente;
import com.is1.proyecto.models.Nota;
import com.is1.proyecto.models.User;
import com.is1.proyecto.service.TallerService;
import com.is1.proyecto.service.MateriaService;
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

        return new ModelAndView(model, "nota-seleccionar-tipo.mustache");
    }
     
    public static ModelAndView formAltaMateria(Request req, Response res) {
        Map<String, Object> model = new HashMap<>();

        String message = req.queryParams("message");
        if (message != null && !message.isEmpty()) {
            model.put("message", message);
        }

        String errorMessage = req.queryParams("error");
        if (errorMessage != null && !errorMessage.isEmpty()) {
            model.put("errorMessage", errorMessage);
        }

        model.put("materias", MateriaService.listarMaterias());

        return new ModelAndView(model, "nota-alta-materia.mustache");
    }

    public static ModelAndView formAltaTaller(Request req, Response res) {
        Map<String, Object> model = new HashMap<>();

        String message = req.queryParams("message");
        if (message != null && !message.isEmpty()) {
            model.put("message", message);
        }

        String errorMessage = req.queryParams("error");
        if (errorMessage != null && !errorMessage.isEmpty()) {
            model.put("errorMessage", errorMessage);
        }

        String currentUsername = req.session().attribute("currentUserUsername");
        User user = User.findFirst("nombreUsuario = ?", currentUsername);
        Docente docente = Docente.findFirst("dni_Persona = ?", user.getDNI());

        if (docente == null) {
            res.redirect("/dashboard");
            return null;
        }

        model.put("talleres", TallerService.listarTalleresPorDocente(docente.getDni()));

        return new ModelAndView(model, "nota-alta-taller.mustache");
    }

    public static Object altaTaller(Request req, Response res) {
        try {
            Integer dniEstudiante = Integer.parseInt(req.queryParams("dniEstudiante"));
            Integer idTaller = Integer.parseInt(req.queryParams("idTaller"));
            String condicion = req.queryParams("condicion");
            Integer notaFinal = Integer.parseInt(req.queryParams("notaFinal"));
            String fechaExamen = req.queryParams("fechaExamen");

            NotaService.crearNotaTaller(condicion, notaFinal, fechaExamen, dniEstudiante, idTaller);

            res.redirect("/nota/lista?message=" +
                  URLEncoder.encode("Nota de taller creada correctamente", StandardCharsets.UTF_8));
            return "";

        } catch (Exception e) {
            res.redirect("/nota/alta/taller?error=" +
                   URLEncoder.encode(e.getMessage(), StandardCharsets.UTF_8));
            return "";
        }
    }
    

    public static Object altaMateria(Request req, Response res) {
        try {
            Integer dniEstudiante = Integer.parseInt(req.queryParams("dniEstudiante"));
            Integer idMateria = Integer.parseInt(req.queryParams("idMateria"));
            String condicion = req.queryParams("condicion");
            Integer notaFinal = Integer.parseInt(req.queryParams("notaFinal"));
            String fechaExamen = req.queryParams("fechaExamen");

            NotaService.crearNotaMateria(condicion, notaFinal, fechaExamen, dniEstudiante, idMateria);

            res.redirect("/nota/lista?message=" +
                        URLEncoder.encode("Nota de materia creada correctamente", StandardCharsets.UTF_8));
            return "";

        } catch (Exception e) {
            res.redirect("/nota/alta/materia?error=" +
                URLEncoder.encode(e.getMessage(), StandardCharsets.UTF_8));
            return "";
        }
    }

    public static void crearNotaMateria(String condicion, Integer notaFinal, String fechaExamen,
                                        Integer dniEstudiante, Integer idMateria) {

        if (condicion == null || condicion.trim().isEmpty()) {
            throw new IllegalArgumentException("La condición no puede estar vacía");
        }

        if (!condicion.equals("Libre") && !condicion.equals("Regular") && !condicion.equals("Promocional")) {
            throw new IllegalArgumentException("La condición debe ser Libre, Regular o Promocional");
        }

        if (notaFinal == null || notaFinal < 1 || notaFinal > 10) {
           throw new IllegalArgumentException("La nota final debe estar entre 1 y 10");
        }

        if (fechaExamen == null || fechaExamen.trim().isEmpty()) {
            throw new IllegalArgumentException("La fecha de examen no puede estar vacía");
        }

        if (dniEstudiante == null) {
            throw new IllegalArgumentException("Debe ingresar un estudiante");
        }

        if (idMateria == null) {
            throw new IllegalArgumentException("Debe seleccionar una materia");
        }

        Nota nota = new Nota();
        nota.setCondicion(condicion.trim());
        nota.setNotaFinal(notaFinal);
        nota.setFechaExamen(fechaExamen.trim());
        nota.setDniEstudiante(dniEstudiante);
        nota.setIdMateria(idMateria);

        if (!nota.save()) {
           throw new IllegalArgumentException("No se pudo guardar la nota de materia");
        }
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