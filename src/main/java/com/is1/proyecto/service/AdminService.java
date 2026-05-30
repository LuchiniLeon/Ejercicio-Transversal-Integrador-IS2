package com.is1.proyecto.service;

import org.javalite.activejdbc.Base;
import org.mindrot.jbcrypt.BCrypt;

import com.is1.proyecto.models.Docente;
import com.is1.proyecto.models.Persona;
import com.is1.proyecto.models.User;


public class AdminService {
    
    public static void crearAdmin(
        Integer dni_Persona, 
        String cargo,
        String sector
    ){
        if(dni_Persona == null || dni_Persona == 0) throw new IllegalArgumentException("Faltan campos obligatorios");

        if (Persona.findFirst("dni = ?", dni_Persona) != null) throw new IllegalStateException("El DNI ya existe");

        String sql = "INSERT INTO administrador (dni_Persona, cargo, sector) VALUES (?, ?, ?)";

        Base.exec(sql, dni_Persona, cargo, sector);
    }
}
