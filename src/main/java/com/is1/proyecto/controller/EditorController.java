package com.is1.proyecto.controller;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.print.Doc;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import com.is1.proyecto.service.ProfileService;
import com.is1.proyecto.models.Admin;
import com.is1.proyecto.models.Docente;
import com.is1.proyecto.models.Email;
import com.is1.proyecto.models.Estudiante;
import com.is1.proyecto.models.Persona;
import com.is1.proyecto.models.SuperAdmin;
import com.is1.proyecto.models.User;
import com.is1.proyecto.service.EditorServer;

import spark.ModelAndView;
import spark.Request;
import spark.Response;

public class EditorController {
    
    public static ModelAndView editar(Request req, Response res){
        Map<String, Object> model = new HashMap<>();

        // Verificación de sesión
        if (req.session().attribute("loggedIn") == null) {
            res.redirect("/");
            System.out.println("El logueo es nulo");
            return null;
        }

        // Obtener datos básicos del usuario
        String username = req.session().attribute("currentUserUsername");

        model.put("name", username);

        try {
            Map<String, Object> profileData = ProfileService.getProfileData(username);
           
            model.putAll(profileData);

            String modo = req.queryParams("modo");
            String buscarUsuario = req.queryParams("buscarUsuario");

            if("oculto".equals(modo) || buscarUsuario != null){
                model.put("modificarUsuario", true);

               if(buscarUsuario != null && !buscarUsuario.trim().isEmpty()){
                    Map<String, Object> datosUser = ProfileService.getProfileData(buscarUsuario);

                    if(datosUser != null && !datosUser.isEmpty()){
                        model = datosUser;
                        model.put("modificarUsuario", true);
                        model.put("busquedaActual", buscarUsuario);
                    }
               }
            }
    
        } catch (Exception e) {
            e.printStackTrace(); 
            model.put("errorMessage", "Error al cargar el perfil");
        }
        return new ModelAndView(model, "editor.mustache");
    }

    public static ModelAndView saveUser(Request req, Response res){

        if (req.session().attribute("loggedIn") == null) {
            res.redirect("/");
            System.out.println("El logueo es nulo");
            return null;
        }

        Map<String, Object> datos = new HashMap<>();

        String user = req.session().attribute("currentUserUsername");

        datos.put("username", user);
        datos.put("newName", req.queryParams("nombre"));
        datos.put("newSurname", req.queryParams("apellido"));
        datos.put("newDate", req.queryParams("fecha"));

        datos.put("modificarUsuario", false);

        EditorServer.save(datos);

        res.redirect("/profile");
        return null;
    }

    public static ModelAndView saveAdmin(Request req, Response res){

        if (req.session().attribute("loggedIn") == null) {
            res.redirect("/");
            System.out.println("El logueo es nulo");
            return null;
        }

        Map<String, Object> datos = new HashMap<>();

        String user = req.queryParams("buscarUsuario");
        if (user == null || user.isEmpty()) {
            user = req.session().attribute("currentUserUsername");
        }

        datos.put("username", user);
        datos.put("newName", req.queryParams("nombre"));
        datos.put("newSurname", req.queryParams("apellido"));
        datos.put("newDate", req.queryParams("fecha"));

        datos.put("modificarUsuario", true);
        datos.put("newDni", req.queryParams("dni"));

        // Procesamos los roles específicos según lo que vino en el formulario
        if (req.queryParams("legajo") != null) {
            datos.put("esDocente", true);
            datos.put("newCargo", req.queryParams("cargoDoc"));
            datos.put("newLeg", Integer.parseInt(req.queryParams("legajo")));
        }
        if (req.queryParams("sector") != null) {
            datos.put("esAdministrador", true);
            datos.put("newCargo", req.queryParams("cargoAdm"));
            datos.put("newSec", req.queryParams("sector"));
        }
        if (req.queryParams("ingreso") != null) {
            
            datos.put("esEstudiante", true);
            datos.put("newIngreso", req.queryParams("ingreso"));
            datos.put("newEs", req.queryParams("estado"));
        }

        EditorServer.save(datos);

        res.redirect("/editor?modo=oculto");
        return null;
    }

    public static ModelAndView pedido(Request req, Response res) {
        if (req.session().attribute("loggedIn") == null) {
            res.redirect("/");
            return null;
        }

        String username = req.session().attribute("currentUserUsername");
        User user = User.findFirst("nombreUsuario = ?", username);
        Persona person = Persona.findFirst("dni = ?", user.getDNI());

        if (person != null) {
            Map<String, Object> model = new HashMap<>();
            model.put("dni", person.getDNI());
            model.put("nombre", person.getNombre());
            model.put("apellido", person.getApellido());

            // Identificamos al administrador a cargo
            Object dniAdminObj = null;

            Admin ad = Admin.findFirst("dni_Persona = ?", person.getDNI());
            if (ad != null) {
                SuperAdmin sadmin = new SuperAdmin();
                dniAdminObj = sadmin.getDni();
            }

            Docente doc = Docente.findFirst("dni_Persona = ?", person.getDNI());
            if (doc != null) {
                dniAdminObj = doc.get("dni_Persona"); // Usamos el nombre de columna de la BD
            }

            Estudiante est = Estudiante.findFirst("dni_Persona = ?", person.getDNI());
            if (est != null) {
                dniAdminObj = est.get("dni_Persona");
            }

            // Si encontramos al admin responsable, armamos el mailto
            if (dniAdminObj != null) {
                Persona perAd = Persona.findFirst("dni = ?", dniAdminObj);
                Email adminEmail = Email.findFirst("dni_Persona = ?", dniAdminObj);

                if (perAd != null && adminEmail != null) {
                    // 1. Armamos la redacción limpia
                    String cuerpoMail = "Buenas Administrador " + perAd.getNombre() + " " + perAd.getApellido() + " (DNI: " + dniAdminObj.toString() + "),\n\n" +
                    "Me comunico para informarle que solicito la modificación de mis datos sensibles en el sistema.\n\n" +
                    "Datos del Solicitante:\n" +
                    "- Usuario: " + username + "\n" +
                    "- Nombre Completo: " + person.getNombre() + " " + person.getApellido() + "\n" +
                    "- DNI: " + person.getDNI() + "\n\n" +
                    "He leído los términos y me comprometo a presentarme en las oficinas en un plazo máximo de 3 días hábiles junto con la documentación física correspondiente.\n\n" +
                    "Campos a modificar (completar aquí):\n" +
                    "- ";

                    // 2. Usamos URLEncoder (este sí le gusta a la web de Google)
                    String asuntoCodificado = URLEncoder.encode("Solicitud de Cambio de Datos - " + username, StandardCharsets.UTF_8);
                    String cuerpoCodificado = URLEncoder.encode(cuerpoMail, StandardCharsets.UTF_8);

                    // 3. ¡EL TRUCO! Armamos la URL web directa para redactar en Gmail
                    String linkGmailWeb = "https://gmail.google.com/mail/?view=cm&fs=1&to=" + adminEmail.getMail() +
                                        "&su=" + asuntoCodificado +
                                        "&body=" + cuerpoCodificado;

                    // 4. Redirigimos al navegador a la web de Gmail
                    res.redirect(linkGmailWeb);
                    return null;
                }
            }
        }
        
        // Si algo falló en las búsquedas, lo mandamos al dashboard para que no quede la pantalla en negro
        res.redirect("/dashboard");
        return null;
    }

    public static ModelAndView redireccion(Request req, Response res){
        
        Map<String, Object> model = new HashMap<>();

        String username = req.session().attribute("currentUserUsername");

        User user = User.findFirst("nombreUsuario = ?", username);

        Persona person = Persona.findFirst("dni = ?", user.getDNI());
        if(person != null){
            model.put("dni", person.getDNI());
            model.put("nombre", person.getNombre());
            model.put("apellido", person.getApellido());
        
            Admin ad = Admin.findFirst("dni_Persona = ?", person.getDNI());
            if(ad != null){
                SuperAdmin sadmin = new SuperAdmin();
                model.put("dniAdministrador", sadmin.getDni());
            }

            Docente doc = Docente.findFirst("dni_Persona = ?", person.getDNI());
            if(doc != null)
                model.put("dniAdministrador", doc.getDniAdministrador());
            

            Estudiante est = Estudiante.findFirst("dni_Persona = ?", person.getDNI());
            if(est != null)
                model.put("dniAdministrador", est.getDniAdministrador());
        }

        Admin miAdmin = Admin.findFirst("dni_Persona = ?", model.get("dniAdministrador"));
            
        Email adminEmail = Email.findFirst("dni_Persona = ?", miAdmin.getDni());
        model.put("email", adminEmail.getMail());
        
        return new ModelAndView(model, "mensaje.mustache");
    }
}
