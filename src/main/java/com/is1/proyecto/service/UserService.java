package com.is1.proyecto.service;

import org.mindrot.jbcrypt.BCrypt;

import com.is1.proyecto.models.Persona;
import com.is1.proyecto.models.User;

public class UserService {
    public static void createUser(String name, String password, String nombre, String apellido, String fechaNacimiento, Integer dni) {

        // VALIDACIONES
        if (name == null || name.isEmpty() ||
            password == null || password.isEmpty() ||
            nombre == null || nombre.isEmpty() ||
            apellido == null || apellido.isEmpty() ||
            fechaNacimiento == null || fechaNacimiento.isEmpty() ||
            dni == null || dni == 0 ) {

            throw new IllegalArgumentException("Todos los campos son requeridos");
        }

        //CREACION DE PERSONA
        Persona persona = new Persona();
        persona.setNombre(nombre);
        persona.setApellido(apellido);
        persona.setFechaNac(fechaNacimiento);
        persona.setDNI(dni);

        persona.saveIt();

        // CREACIÓN DE USUARIO
        User user = new User();

        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());

        user.setName(name);
        user.setPassword(hashedPassword);

        //dni del usuario es el que ya fue agregado a la tabla persona (Debe ser creada la presona primero para poder asignarlo)
        user.set("dni_Persona", persona.getDNI());

        user.saveIt();
    }   
}
