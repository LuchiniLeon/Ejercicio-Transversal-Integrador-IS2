package com.is1.proyecto.service;

import java.time.LocalDateTime;
import java.util.UUID;

import org.javalite.activejdbc.Base;
import org.mindrot.jbcrypt.BCrypt;

import com.is1.proyecto.models.Email;
import com.is1.proyecto.models.Token;
import com.is1.proyecto.models.User;

public class PasswordRecoveryService {
    
    //Generar token y enviar correo
    public static void SolicitarRecuperación(String emailStr){
        Email emailModel = Email.findFirst("email = ?", emailStr);
        if(emailModel == null){
            return; //Si el mail no existe no hace nada
        }

        String token = UUID.randomUUID().toString();
        LocalDateTime expiracion = LocalDateTime.now().plusMinutes(10);

        Token tokenPassword = new Token();
        tokenPassword.setToken(token);
        tokenPassword.setmail(emailStr);
        tokenPassword.setExpiracion(expiracion.toString());
        tokenPassword.set("usado", 0);
        tokenPassword.insert();

        //ACA AÑADIR LOGICA CUANDO VEA COMO ENVIAR EL MAIL
        //ESTO ES DE PRUEBA
        String link = "http://localhost:8080/reset-password?token=" + token;
        System.out.println("-------------------------------------------------");
        System.out.println("EMAIL SIMULADO PARA: " + emailStr);
        System.out.println("Para restablecer tu contraseña haz click aquí:");
        System.out.println(link);
        System.out.println("-------------------------------------------------");
    }

    //Validacion del token y cambio de contraseña
    public static void reestablecerContraseña(String token, String nuevaPassword){
        Token tokenPassword = Token.findById(token);

        if(tokenPassword == null || tokenPassword.getInteger("usado") == 1){
            throw new IllegalArgumentException("El link usado es inválido o ya fue usado");
        }

        LocalDateTime expiracion = LocalDateTime.parse(tokenPassword.getExpiracion());
        if(LocalDateTime.now().isAfter(expiracion)){
            throw new IllegalArgumentException("El link usado ya expiró");
        }

        Base.openTransaction();
        try{
            String emailStr = tokenPassword.getmail();
            Email emailModel = Email.findFirst("email = ?", emailStr);
            User usuario = User.findFirst("dni_Persona = ?", emailModel.getDni());

            //Actualizacion de contraseña
            String hashedPassword = BCrypt.hashpw(nuevaPassword, BCrypt.gensalt());
            usuario.setPassword(hashedPassword);
            usuario.saveIt();

            tokenPassword.set("usado", 1); //Marcar token como ya usado
            tokenPassword.saveIt();

            Base.commitTransaction();
        }catch(Exception e){
            Base.rollbackTransaction(); //Si ocurrió un error, no modifica nada de la bd
            throw e;
        }
    }
}
