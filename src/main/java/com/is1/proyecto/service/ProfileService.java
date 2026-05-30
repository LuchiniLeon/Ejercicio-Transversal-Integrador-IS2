package com.is1.proyecto.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.javalite.activejdbc.Base;

import com.is1.proyecto.models.Admin;
import com.is1.proyecto.models.Docente;
import com.is1.proyecto.models.Estudiante;
import com.is1.proyecto.models.Persona;
import com.is1.proyecto.models.User;

public class ProfileService {
 
    public static Map<String, Object> getProfileData(String username) {

        Map<String, Object> data = new HashMap<>();
    
        User user = User.findFirst("nombreUsuario = ?", username);
        data.put("name", username);
       
        Persona person = Persona.findFirst("dni = ?", user.getDNI());

        if(person != null){
            data.put("dni", person.getDNI());
            data.put("nombre", person.getNombre());
            data.put("apellido", person.getApellido());
            data.put("fecha", person.getFechaNac());

            Docente doc = Docente.findFirst("dni_Persona = ?", user.getDNI());
            if(doc != null){
                data.put("esDocente", true);
                data.put("cargoDoc", doc.getCargo());
                data.put("legajo", doc.getLegajo());
            } 
            
            Admin ad = Admin.findFirst("dni_Persona = ?", user.getDNI());
            if(ad != null){
                data.put("esAdministrador", true);
                data.put("cargoAdm", ad.getCargo());
                data.put("sector", ad.getSector());
            }

            Estudiante est = Estudiante.findFirst("dni_Persona = ?", user.getDNI());
            if(est != null){
                data.put("esEstudiante", true);
                data.put("estado", est.getEstado());
                data.put("ingreso", est.getIngreso());
            }
        }

        return data;
    }
}
