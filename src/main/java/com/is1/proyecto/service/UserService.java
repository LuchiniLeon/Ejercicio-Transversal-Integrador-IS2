package com.is1.proyecto.service;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

import org.javalite.activejdbc.Base;
import org.mindrot.jbcrypt.BCrypt;

import com.is1.proyecto.models.Persona;
import com.is1.proyecto.models.User;
import com.is1.proyecto.models.Email;

public class UserService {
    public static void createUser(String name, String password, String nombre, String apellido, String fechaNacimiento, Integer dni, String email) {

        // VALIDACIONES
        if (name == null || name.isEmpty() ||
            password == null || password.isEmpty() ||
            nombre == null || nombre.isEmpty() ||
            apellido == null || apellido.isEmpty() ||
            fechaNacimiento == null || fechaNacimiento.isEmpty() ||
            email ==  null || email.isEmpty() ||
            dni == null || dni == 0 ) {

            throw new IllegalArgumentException("Todos los campos son requeridos");
        }

        //Validación de fecha (Formato y fecha existente)
        try{
            LocalDate.parse(fechaNacimiento);
        }catch(DateTimeParseException e){
            throw new IllegalArgumentException("La fecha de nacimiento debe estar en formato YYYY-MM-DD y ser una fecha válida");
        }

        //Abre la transacción acá
        Base.openTransaction();

        try{
            //CREACION DE PERSONA
            Persona persona = new Persona();
            persona.setNombre(nombre);
            persona.setApellido(apellido);
            persona.setFechaNac(fechaNacimiento);
            persona.setDNI(dni);
            persona.insert();

            //CREACION DE EMAIL
            Email modelEmail = new Email();
            modelEmail.setDni(persona.getDNI());
            modelEmail.setMail(email);
            modelEmail.insert();


            // CREACIÓN DE USUARIO
            User user = new User();

            String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());

            user.setName(name);
            user.setPassword(hashedPassword);

            //dni del usuario es el que ya fue agregado a la tabla persona (Debe ser creada la presona primero para poder asignarlo)
            user.set("dni_Persona", persona.getDNI());

            user.insert();

            //Si todo salió bien dentro del try, confirma para que pase a la bd
            Base.commitTransaction(); 
        }catch (Exception e){
            //Si falló algo en el try, deshacemos todo para no dejar nada a medias en la bd
            Base.rollbackTransaction();
            throw e;
        }
    }   
}
