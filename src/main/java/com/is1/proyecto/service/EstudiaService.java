package com.is1.proyecto.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.javalite.activejdbc.Base;

import com.is1.proyecto.models.Docente;
import com.is1.proyecto.models.Estudia;
import com.is1.proyecto.models.ParticipaDocenteTaller;
import com.is1.proyecto.models.Persona;
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
        Taller taller = Taller.findById(idtaller);
        
        if (taller == null)
            throw new IllegalArgumentException("Taller no encontrado");
        if (!taller.getVigente())
            throw new IllegalArgumentException("No podés desinscribirte de un taller que no está vigente");

        int borrados = Base.exec(
            "DELETE FROM estudia WHERE dni_Estudiante = ? AND id_taller = ?",
            dni_estudiante, idtaller
        );
        if (borrados == 0)
            throw new IllegalArgumentException("No estás inscripto en este taller");
    }

    public static List<Map<String, Object>> talleresDisponibles(Integer dniEstudiante){
        // Todos los talleres
        List<Taller> talleres = Taller.findAll();

        List<Map<String, Object>> lista = new ArrayList<>();

        for(Taller t : talleres){
            Map<String, Object> map = new HashMap<>();
            map.put("id", t.getId());
            map.put("titulo", t.getTitulo());
            map.put("horas", t.getHoras());
            map.put("vigente", t.getVigente() ? "Si" : "No");

            // Docente titular
            Docente docente = Docente.findFirst("dni_Persona = ?", t.getInteger("dni_Docente"));
            if (docente != null) {
                Persona persona = docente.parent(Persona.class);
                if (persona != null) {
                    map.put("docenteTitular", persona.getString("nombre") + " " + persona.getString("apellido"));
                } else {
                    map.put("docenteTitular", "No asignado");
                }
            } else {
                map.put("docenteTitular", "No asignado");
            }
            // Docentes participantes
            List<ParticipaDocenteTaller> participantes = ParticipaDocenteTaller.where("id_Taller = ?", t.getId());
            List<String> nombres = new ArrayList<>();
            
            for (ParticipaDocenteTaller p : participantes) {
                Docente dp = Docente.findFirst("dni_Persona = ?", p.getDniDocente());
                Persona pp = dp.parent(Persona.class);
                nombres.add(pp.getString("nombre") + " " + pp.getString("apellido"));
            }
        
            map.put("participantes", nombres.isEmpty() ? "—" : String.join(", ", nombres));

            // Verificar si ya está inscripto
            Estudia inscripto = Estudia.findFirst(
                "dni_Estudiante = ? AND id_taller = ?", dniEstudiante, t.getId()
            );
            map.put("inscripto", inscripto != null);
            map.put("puedeInscribirse", t.getVigente() && inscripto == null);

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
                map.put("vigente", taller.getVigente() ? "Si" : "No");

                 // Docente titular
                Docente docente = Docente.findFirst("dni_Persona = ?", taller.getInteger("dni_Docente"));
                if (docente != null) {
                    Persona persona = docente.parent(Persona.class);
                    if (persona != null) {
                        map.put("docenteTitular", persona.getString("nombre") + " " + persona.getString("apellido"));
                    } else {
                        map.put("docenteTitular", "No asignado");
                    }
                } else {
                    map.put("docenteTitular", "No asignado");
                }

                // Docentes participantes
                List<ParticipaDocenteTaller> participantes = ParticipaDocenteTaller.where("id_Taller = ?", taller.getId());
                List<String> nombres = new ArrayList<>();
                
                for (ParticipaDocenteTaller p : participantes) {
                    Docente dp = Docente.findFirst("dni_Persona = ?", p.getDniDocente());
                    Persona pp = dp.parent(Persona.class);
                    nombres.add(pp.getString("nombre") + " " + pp.getString("apellido"));
                }
            
                map.put("participantes", nombres.isEmpty() ? "—" : String.join(", ", nombres));
                
                lista.add(map);
            }
        }
        
        return lista;
    }
}
