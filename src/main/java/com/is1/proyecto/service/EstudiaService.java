package com.is1.proyecto.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.javalite.activejdbc.Base;
import com.is1.proyecto.models.Estudia;
import com.is1.proyecto.models.Taller;

public class EstudiaService {
    
    public static void inscribir(Integer dniEstudiante, Integer idTaller){
        if(dniEstudiante == null){
            throw new IllegalArgumentException("El DNI no es valido");
        }

        Taller taller = Taller.findById(idTaller);
        if(taller == null){
            throw new IllegalArgumentException("Taller no encontrado");
        }

        // verificacion de duplicado
        Estudia existente = Estudia.findFirst("dni_Estudiante = ? AND id_Taller = ?", dniEstudiante, idTaller);

        if(existente != null){
            throw new IllegalArgumentException("Ya estas inscripto en este taller");
        }

        Estudia inscripcion = new Estudia();
        inscripcion.setDni_Estudiante(dniEstudiante);
        inscripcion.setId_taller(idTaller);

        if(!inscripcion.save()){
            throw new IllegalArgumentException("No se puede guardar la inscripcion");
        }
    }

    public static void desincribir(Integer dni_estudiante, Integer idtaller) {
        int borrados = Base.exec(
            "DELETE FROM estudia WHERE dni_Estudiante = ? AND id_taller = ?",
            dni_estudiante, idtaller
        );
        if (borrados == 0)
            throw new IllegalArgumentException("No estás inscripto en este taller");
    }

    public static List<Map<String, Object>> talleresDisponibles(Integer dniEstudiante){
        // Todos los talleres disponible
        List<Taller> talleres = Taller.where("vigente = ?", 1);

        List<Map<String, Object>> lista = new ArrayList<>();

        for(Taller t : talleres){
            Map<String, Object> map = new HashMap<>();
            map.put("id", t.getId());
            map.put("titulo", t.getTitulo());
            map.put("horas", t.getHoras());

            // Verificar si ya está inscripto
            Estudia inscripto = Estudia.findFirst(
                "dni_Estudiante = ? AND id_taller = ?", dniEstudiante, t.getId()
            );
            map.put("inscripto", inscripto != null);

            lista.add(map);
        }

        return lista;
    }

    public static List<Map<String, Object>> listarTalleresDeEstudiante(Integer dniEstudiante) {
        List<Estudia> inscripciones = Estudia.where("dni_Estudiante = ?", dniEstudiante);
        List<Map<String, Object>> lista = new ArrayList<>();

        for (Estudia e : inscripciones) {
            Taller taller = Taller.findById(e.getId_Taller());
            
            if (taller != null) {
                Map<String, Object> map = new HashMap<>();
                map.put("id", taller.getId());
                map.put("titulo", taller.getTitulo());
                map.put("horas", taller.getHoras());
                lista.add(map);
            }
        }
        
        return lista;
    }
}
