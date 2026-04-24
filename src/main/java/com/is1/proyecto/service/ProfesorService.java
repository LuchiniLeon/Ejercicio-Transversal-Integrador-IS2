package com.is1.proyecto.service;

import org.javalite.activejdbc.Base;
import org.mindrot.jbcrypt.BCrypt;

import com.is1.proyecto.models.Profesores;
import com.is1.proyecto.models.User;

public class ProfesorService {
    
     public static void crearProfesor(
        String nombre, String apellido, String correo,
        String dniStr, String direccion, String telefonoStr,
        String legajoStr, String cargo, String name, String password
    ) {

        // VALIDACIONES

        if (nombre == null || nombre.isEmpty() ||
            apellido == null || apellido.isEmpty() ||
            correo == null || correo.isEmpty() ||
            dniStr == null || dniStr.isEmpty() ||
            legajoStr == null || legajoStr.isEmpty() ||
            password == null || password.isEmpty() ||
            name == null || name.isEmpty()) {

            throw new IllegalArgumentException("Faltan campos obligatorios");
        }

        if (!correo.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$")) {
            throw new IllegalArgumentException("Correo inválido");
        }

        Integer dni, legajo;

        try {
            dni = Integer.valueOf(dniStr.trim());
            legajo = Integer.valueOf(legajoStr.trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("DNI y Legajo deben ser números válidos");
        }

        // DUPLICADOS

        if (Profesores.findFirst("correo = ?", correo) != null) {
            throw new IllegalStateException("El correo ya existe");
        }

        if (Profesores.findFirst("dni = ?", dni) != null) {
            throw new IllegalStateException("El DNI ya existe");
        }

        if (Profesores.findFirst("legajo = ?", legajo) != null) {
            throw new IllegalStateException("El legajo ya existe");
        }

        if (User.findFirst("name = ?", name) != null) {
            throw new IllegalStateException("El username ya existe");
        }

        // CREACIÓN

        User newUser = new User();
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
        newUser.set("name", name);
        newUser.set("password", hashedPassword);
        newUser.saveIt();

        Object userId = newUser.getId();

        String sql = "INSERT INTO professors (id_prof, nombre, apellido, dni, legajo, correo, cargo, direccion, telefono) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        Object dirVal = (direccion != null && !direccion.isEmpty()) ? direccion : null;

        Object telVal = null;
        if (telefonoStr != null && !telefonoStr.isEmpty()) {
            try {
                telVal = Integer.valueOf(telefonoStr.trim());
            } catch (Exception ignored) {}
        }

        Base.exec(sql, userId, nombre, apellido, dni, legajo, correo, cargo, dirVal, telVal);
    }
}
