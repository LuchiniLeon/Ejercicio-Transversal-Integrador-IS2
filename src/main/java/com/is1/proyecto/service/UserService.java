package com.is1.proyecto.service;

import org.mindrot.jbcrypt.BCrypt;

import com.is1.proyecto.models.User;

public class UserService {
    public static void createUser(String name, String password) {

        // VALIDACIONES
        if (name == null || name.isEmpty() ||
            password == null || password.isEmpty()) {

            throw new IllegalArgumentException("Nombre y contraseña son requeridos");
        }

        // CREACIÓN
        User user = new User();

        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());

        user.set("name", name);
        user.set("password", hashedPassword);
        user.set("esAdministrador", 0);

        user.saveIt();
    }   
}
