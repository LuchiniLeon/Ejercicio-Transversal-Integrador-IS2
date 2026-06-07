package com.is1.proyecto.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.javalite.activejdbc.Base;

import com.is1.proyecto.models.Docente;
import com.is1.proyecto.models.InscripcionCarrera;
import com.is1.proyecto.models.InscripcionMateria;
import com.is1.proyecto.models.Materia;

public class MateriaInscripcionService {

    public static void inscribirMateria(Integer dniEstudiante, Integer idMateria) {
        if (dniEstudiante == null) {
            throw new IllegalArgumentException("El DNI no es válido");
        }

        if (InscripcionCarrera.where("dni_Estudiante = ?", dniEstudiante).isEmpty()) {
            throw new IllegalArgumentException("Primero inscribite a una carrera");
        }

        Materia materia = Materia.findById(idMateria);
        if (materia == null) {
            throw new IllegalArgumentException("Materia no encontrada");
        }

        List<Integer> carrerasInscriptas = CarreraInscripcionService.obtenerCarrerasDeEstudiante(dniEstudiante);
        if (!carrerasInscriptas.contains(materia.getIdCarrera())) {
            throw new IllegalArgumentException("La materia no pertenece a ninguna de tus carreras inscriptas");
        }

        InscripcionMateria existente = InscripcionMateria.findFirst(
            "dni_Estudiante = ? AND id_Materia = ?", dniEstudiante, idMateria);

        if (existente != null) {
            throw new IllegalArgumentException("Ya estás inscripto en esta materia");
        }

        InscripcionMateria inscripcion = new InscripcionMateria();
        inscripcion.setDniEstudiante(dniEstudiante);
        inscripcion.setIdMateria(idMateria);
        inscripcion.setEstado("Regular");
        inscripcion.setFechaInscripcion(LocalDate.now().toString());

        if (!inscripcion.save()) {
            throw new IllegalArgumentException("No se puede guardar la inscripción");
        }
    }

    public static void desinscribirMateria(Integer dniEstudiante, Integer idMateria) {
        Materia materia = Materia.findById(idMateria);
        if (materia == null) {
            throw new IllegalArgumentException("Materia no encontrada");
        }

        int borrados = Base.exec(
            "DELETE FROM inscripcion_Materia WHERE dni_Estudiante = ? AND id_Materia = ?",
            dniEstudiante, idMateria
        );

        if (borrados == 0) {
            throw new IllegalArgumentException("No estás inscripto en esta materia");
        }
    }

    public static List<Map<String, Object>> materiasDisponibles(Integer dniEstudiante) {
        List<Integer> idsCarreras = CarreraInscripcionService.obtenerCarrerasDeEstudiante(dniEstudiante);
        if (idsCarreras.isEmpty()) {
            return new ArrayList<>();
        }

        Set<Integer> materiasAgregadas = new HashSet<>();
        List<Map<String, Object>> lista = new ArrayList<>();

        for (Integer idCarrera : idsCarreras) {
            List<Materia> materias = Materia.where("id_Carrera = ?", idCarrera);
            for (Materia materia : materias) {
                if (!materiasAgregadas.add(materia.getIdMateria())) {
                    continue;
                }
                lista.add(mapearMateriaDisponible(materia, dniEstudiante));
            }
        }

        return lista;
    }

    private static Map<String, Object> mapearMateriaDisponible(Materia materia, Integer dniEstudiante) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", materia.getIdMateria());
        map.put("codigo", materia.getCodigo());
        map.put("nombre", materia.getNombre());
        map.put("horasTotales", materia.getHorasTotales());

        Docente docente = materia.parent(Docente.class);
        if (docente != null) {
            com.is1.proyecto.models.Persona persona = docente.parent(com.is1.proyecto.models.Persona.class);
            if (persona != null) {
                map.put("docenteTitular", persona.getString("nombre") + " " + persona.getString("apellido"));
            } else {
                map.put("docenteTitular", "No asignado");
            }
        } else {
            map.put("docenteTitular", "No asignado");
        }

        InscripcionMateria inscripto = InscripcionMateria.findFirst(
            "dni_Estudiante = ? AND id_Materia = ?", dniEstudiante, materia.getIdMateria());

        map.put("inscripto", inscripto != null);
        map.put("puedeInscribirse", inscripto == null);
        return map;
    }

    public static List<Map<String, Object>> listarMateriasDeEstudiante(Integer dniEstudiante) {
        List<InscripcionMateria> inscripciones = InscripcionMateria.where("dni_Estudiante = ?", dniEstudiante);
        List<Map<String, Object>> lista = new ArrayList<>();

        for (InscripcionMateria inscripcion : inscripciones) {
            Materia materia = inscripcion.parent(Materia.class);
            if (materia != null) {
                Map<String, Object> map = new HashMap<>();
                map.put("id", materia.getIdMateria());
                map.put("codigo", materia.getCodigo());
                map.put("nombre", materia.getNombre());
                map.put("horasTotales", materia.getHorasTotales());
                map.put("estado", inscripcion.getEstado());
                map.put("fechaInscripcion", inscripcion.getFechaInscripcion());

                Docente docente = materia.parent(Docente.class);
                if (docente != null) {
                    com.is1.proyecto.models.Persona persona = docente.parent(com.is1.proyecto.models.Persona.class);
                    if (persona != null) {
                        map.put("docenteTitular", persona.getString("nombre") + " " + persona.getString("apellido"));
                    } else {
                        map.put("docenteTitular", "No asignado");
                    }
                } else {
                    map.put("docenteTitular", "No asignado");
                }

                lista.add(map);
            }
        }

        return lista;
    }
}
