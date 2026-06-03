package com.is1.proyecto.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        Estudia inscripcion = Estudia.findFirst("dni_Estudiante = ? AND id_Taller = ?", dni_estudiante, idtaller);
        
        if(inscripcion == null){
            throw new IllegalArgumentException("No estas inscriptos en este taller");
        }

        if(!inscripcion.delete()){
            throw new IllegalArgumentException("No se pudo eliminar esta inscripcion");
        }
    }

    public static List<Map<String, Object>> talleresDisponibles(){
        // Todos los talleres disponible
        List<Taller> talleres = Taller.where("vigente = ?", 1);

        List<Map<String, Object>> lista = new ArrayList<>();

        for(Taller t : talleres){
            Map<String, Object> map = new HashMap<>();
            map.put("id_Taller", t.getId());
            map.put("titulo", t.getTitulo());
            map.put("horas", t.getHoras());

            lista.add(map);
        }

        return lista;
    }
}
