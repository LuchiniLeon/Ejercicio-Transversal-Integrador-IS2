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

        //LOGICA PARA ENVIAR EL MAIL (con JavaMail)

        //Configuracion
        java.util.Properties prop = new java.util.Properties();
        prop.put("mail.smtp.auth", "true");
        prop.put("mail.smtp.starttls.enable", "true");
        prop.put("mail.smtp.host", "smtp.gmail.com");
        prop.put("mail.smtp.port", "587");

        //Credenciales
        final String correo = System.getenv("EMAIL_USER"); //Variables de entorno definidas en .dev, no se carga a git
        final String password = System.getenv("EMAIL_PASSWORD");

        if(correo == null || password == null){
            throw new IllegalStateException("Credenciales del correo no estan cargadas en el entorno");
        }

        //CRear sesion con autenticación
        javax.mail.Session session = javax.mail.Session.getInstance(prop, new javax.mail.Authenticator(){
            protected javax.mail.PasswordAuthentication getPasswordAuthentication(){
                return new javax.mail.PasswordAuthentication(correo, password);
            }
        });

        try {
            //Escritura del mail
            javax.mail.Message mensaje = new javax.mail.internet.MimeMessage(session);
            mensaje.setFrom(new javax.mail.internet.InternetAddress(correo));
            mensaje.setRecipients(javax.mail.Message.RecipientType.TO, javax.mail.internet.InternetAddress.parse(emailStr));
            mensaje.setSubject("Recuperación de Contraseña - Sistema de Gestion Universitario");

            String link = "http://localhost:8080/reset-password?token=" + token;
            mensaje.setText("¡Buenos dias!,\n\nPara restablecer tu contraseña, haz clic en el siguiente enlace:\n" + link);

            //Enviar mail
            javax.mail.Transport.send(mensaje);
            System.out.println("Correo enviado exitosamente a " + emailStr);
        }catch(javax.mail.MessagingException e){
            throw new RuntimeException("Error al enviar el correo " + e.getMessage());
        }
    }

    //Validación del token
    public static void validarToken(String token){
        Token tokenPassword = Token.findById(token);

        if(tokenPassword == null || tokenPassword.getInteger("usado") == 1){
            throw new IllegalArgumentException("El link usado es inválido o ya fue usado, por favor solicita uno nuevo.");
        }

        LocalDateTime expiracion = LocalDateTime.parse(tokenPassword.getExpiracion());
        if(LocalDateTime.now().isAfter(expiracion)){
            throw new IllegalArgumentException("El link usado ya expiró, por favor solicita uno nuevo.");
        }
    }

    //Cambio de contraseña
    public static void reestablecerContraseña(String token, String nuevaPassword){
        validarToken(token);

        Token tokenPassword = Token.findById(token);
        
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
