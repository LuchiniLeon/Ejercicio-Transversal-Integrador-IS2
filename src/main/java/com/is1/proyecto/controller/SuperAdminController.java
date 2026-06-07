package com.is1.proyecto.controller;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import com.is1.proyecto.service.SuperAdminService;

import spark.ModelAndView;
import spark.Request;
import spark.Response;

public class SuperAdminController {

    public static Object alta(Request req, Response res){
        try{
            AdminController.alta(req, res);//Redirijo al admincontroller para crear el admin
            return "";
        } catch (IllegalArgumentException e) {
            res.status(400);
            res.redirect("/superadmin/alta?error=" + URLEncoder.encode(e.getMessage(), StandardCharsets.UTF_8));
            return "";

        } catch (IllegalStateException e) {
            res.status(409);
            res.redirect("/superadmin/alta?error=" + URLEncoder.encode(e.getMessage(), StandardCharsets.UTF_8));
            return "";

        } catch (Exception e) {
            e.printStackTrace();
            res.status(500);
            res.redirect("/superadmin/alta?error=Error interno del servidor");
            return "";
        }
    }
 
    public static ModelAndView formAlta(Request req, Response res) {

        Map<String, Object> model = new HashMap<>();
        
        String successMessage = req.queryParams("message");
        if (successMessage != null && !successMessage.isEmpty()) {
            model.put("successMessage", successMessage);
        }
    
        String errorMessage = req.queryParams("error");
        if (errorMessage != null && !errorMessage.isEmpty()) {
            model.put("errorMessage", errorMessage);
        }
        
        return new ModelAndView(model, "superAdmin_form.mustache");
    }
}
