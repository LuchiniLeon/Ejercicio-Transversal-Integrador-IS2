package com.is1.proyecto.service;

import java.util.HashMap;
import java.util.Map;

import com.is1.proyecto.models.Profesores;

public class ProfileService {
 
      public static Map<String, Object> getProfileData(Integer userId) {

        Map<String, Object> data = new HashMap<>();

        Profesores profe = Profesores.findById(userId);

        if (profe != null) {
            data.put("isProfessor", true);

            data.put("nombre", profe.getString("nombre"));
            data.put("apellido", profe.getString("apellido"));
            data.put("dni", profe.get("dni"));
            data.put("legajo", profe.get("legajo"));
            data.put("cargo", profe.getString("cargo"));
            data.put("correo", profe.getString("correo"));

            Object tel = profe.get("telefono");
            if (tel != null) data.put("telefono", tel);

            String dir = profe.getString("direccion");
            if (dir != null && !dir.isEmpty()) data.put("direccion", dir);

        } else {
            data.put("isProfessor", false);
        }

        return data;
    }
}
