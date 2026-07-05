package com.is1.proyecto.controller;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

import com.is1.proyecto.service.PasswordRecoveryService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import spark.ModelAndView;
import spark.Request;
import spark.Response;

public class PasswordRecoveryController {

    private static final Logger logger = LoggerFactory.getLogger(PasswordRecoveryController.class);

    //Recibe el mail y solicida al service que cree el token y envie el mail
    public static Object forgotPasswordPost(Request req, Response res){
        String mail = req.queryParams("email");
        try{
            PasswordRecoveryService.SolicitarRecuperación(mail);
            logger.info("Solicitud de recuperación procesada para el correo: {}", mail);
        } catch (Exception e){
            logger.error("Error interno al intentar enviar correo de recuperación a: " + mail, e);
        }
        res.redirect("/?successMessage=" + URLEncoder.encode("Si el correo está registrado, recibirás un link de recuperación.", StandardCharsets.UTF_8));
        return null;
    }

    //Muestra el formulario para la nueva contraseña
    public static ModelAndView resetPasswordGet(Request req, Response res){
        String token = req.queryParams("token");
        HashMap<String, Object> model = new HashMap<>();

        try{
            PasswordRecoveryService.validarToken(token);
        }catch(IllegalArgumentException e){
            logger.warn("Intento de recuperación con token inválido o expirado. Token: {}", token);
            res.redirect("/forgot-password?errorMessage=" + 
                URLEncoder.encode(e.getMessage(), StandardCharsets.UTF_8));
            return new ModelAndView(model, "forgot_password.mustache");
        }
        model.put("token", token);
        return new ModelAndView(model, "password_recovery.mustache");
    }

    //Guarda la nueva contraseña
    public static Object resetPasswordPost(Request req, Response res){
        String token = req.queryParams("token");
        String password = req.queryParams("password");
        String confirmacion = req.queryParams("confirmacionPassword");

        if(!password.equals(confirmacion)){
            logger.info("Las contraseñas ingresadas no coinciden.");
            res.redirect("/reset-password?token=" + token + "&error=Las contraseñas no coinciden");
            return null;
        }

        try{
            PasswordRecoveryService.reestablecerContraseña(token, password);
            logger.info("Contraseña restablecida exitosamente mediante token.");
            res.redirect("/?successMessage=" + URLEncoder.encode("Contraseña actualizada exitosamente", StandardCharsets.UTF_8));
            return null;
        }catch(IllegalArgumentException e){
            logger.warn("Fallo de validación al intentar restablecer contraseña: {}", e.getMessage());
            res.redirect("/reset-password?token=" + token + "&error=" + e.getMessage());
            return null;
        }catch(Exception e){
            //Posibles fallos de base de datos
            logger.error("Error crítico de base de datos al intentar guardar nueva contraseña.", e);
            res.redirect("/reset-password?token=" + token + "&error=" + URLEncoder.encode("Error interno del servidor.", StandardCharsets.UTF_8));
            return null;
        }
    }

    public static ModelAndView formNew(Request req, Response res) {
        HashMap<String, Object> model = new HashMap<>();

        String errorMessage = req.queryParams("errorMessage");
        if(errorMessage!=null && !errorMessage.isEmpty()){
            model.put("errorMessage", errorMessage);
        }
        return new ModelAndView(model, "forgot_password.mustache");
    }
}
