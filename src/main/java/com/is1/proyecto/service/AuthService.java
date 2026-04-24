package com.is1.proyecto.service;

import org.mindrot.jbcrypt.BCrypt;

import com.is1.proyecto.models.User;

public class AuthService {
    public static User login(String username, String password) {

        // buscar usuario
        User user = User.findFirst("name = ?", username);

        if (user == null) {
            return null;
        }

        // verificar password
        String storedHashedPassword = user.getString("password");

        if (!BCrypt.checkpw(password, storedHashedPassword)) {
            return null;
        }

        return user;
    }
}
