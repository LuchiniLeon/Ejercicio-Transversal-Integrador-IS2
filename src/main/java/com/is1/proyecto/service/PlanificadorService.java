package com.is1.proyecto.service;

import java.util.Map;
import org.javalite.activejdbc.Base;
import com.is1.proyecto.models.Persona;
import com.is1.proyecto.models.PlanificadorTarea;
import com.is1.proyecto.models.User;

public class PlanificadorService {
    
    public static void save(Map<String, Object> model){
        try {
            // Buscamos el usuario por su username (ojo si la columna en DB es 'username' o 'nombreUsuario')
            User user = User.findFirst("nombreUsuario = ?", model.get("username"));
            
            if (user == null) {
                System.out.println("Error: No se encontró el usuario " + model.get("username"));
                return;
            }

            Persona person = Persona.findFirst("dni = ?", user.getDNI());

            Integer hora_inicio = Integer.valueOf(model.get("hora_inicio").toString());
            Integer hora_fin = Integer.valueOf(model.get("hora_fin").toString());
            String tarea = model.get("tarea").toString();
            
            // Si tu DB espera un entero para el mes, acordate de transformarlo antes de mandarlo acá
            String mes = model.get("mes").toString(); 
            Integer dia = Integer.valueOf(model.get("dia").toString());
            Integer anio = Integer.valueOf(model.get("anio").toString());

            if(person != null){
                Base.openTransaction();

                Integer dni = person.getDNI();

                PlanificadorTarea plaTa = new PlanificadorTarea();
                plaTa.setDni(dni);
                plaTa.setDia(dia);
                plaTa.setMes(mes);
                plaTa.setAnio(anio);
                plaTa.setHoraInicio(hora_inicio);
                plaTa.setHoraFin(hora_fin);
                plaTa.setTarea(tarea); // Ahora compila perfecto porque el modelo acepta el String de la tarea

                plaTa.insert(); // Usa tu método insert() original

                Base.commitTransaction();
                System.out.println("¡Tarea guardada con éxito para el DNI: " + dni + "!");
            } else {
                System.out.println("Error: No se encontró la persona asociada al usuario.");
            }
        } catch (Exception e) {
            try { Base.rollbackTransaction(); } catch(Exception ex) {}
            System.out.println("Error al cargar una tarea: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
