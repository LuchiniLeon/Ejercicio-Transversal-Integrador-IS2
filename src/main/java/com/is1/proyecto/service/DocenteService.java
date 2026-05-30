package com.is1.proyecto.service;

import org.javalite.activejdbc.Base;
import org.mindrot.jbcrypt.BCrypt;

import com.is1.proyecto.models.Docente;
import com.is1.proyecto.models.Persona;
import com.is1.proyecto.models.User;

public class DocenteService {
    
     public static void crearDocente(
        String dniStr, String legajoStr, String cargo, 
        String dni_adminStr, String nombre, String apellido,
        String fecha
        ) {

        // VALIDACIONES

        if (
            dniStr == null || dniStr.isEmpty() ||
            legajoStr == null || legajoStr.isEmpty() ||
            dni_adminStr == null || dni_adminStr.isEmpty() ||
            nombre == null || nombre.isEmpty() ||
            apellido == null || apellido.isEmpty() ||
            fecha == null || fecha.isEmpty())
            {

            throw new IllegalArgumentException("Faltan campos obligatorios");
        }

        Integer dni, legajo, dni_Admin;

        try {
            dni = Integer.valueOf(dniStr.trim());
            legajo = Integer.valueOf(legajoStr.trim());
            dni_Admin = Integer.valueOf(dni_adminStr.trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("DNI y Legajo deben ser números válidos");
        }

        if (Persona.findFirst("dni = ?", dni) != null) {
            throw new IllegalStateException("El DNI ya existe");
        }
    
        // CREACIÓN

    }
}
