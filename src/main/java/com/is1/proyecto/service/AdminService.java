package com.is1.proyecto.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public static List<Map<String, Object>> obtenerDocentes() {
        List<Docente> docentes = Docente.findAll();
        List<Map<String, Object>> resultado = new ArrayList<>();

        for (Docente d : docentes) {

            Persona persona = d.parent(Persona.class);
            Map<String, Object> map = new HashMap<>();

            map.put("dni", d.getInteger("dni_Persona"));
            map.put("legajo", d.getInteger("legajo"));
            map.put("cargo", d.getString("cargo"));
            map.put("nombre", persona.getString("nombre"));
            map.put("apellido", persona.getString("apellido"));
            map.put("fechaNacimiento", persona.getString("fecha_Nacimiento"));
            
            resultado.add(map);
        }
        return resultado;
    }
}
