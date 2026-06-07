package com.is1.proyecto.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.is1.proyecto.models.Carrera;
import com.is1.proyecto.models.InscripcionCarrera;

public class CarreraInscripcionService {

    public static void inscribirCarrera(Integer dniEstudiante, Integer idCarrera) {
        if (dniEstudiante == null) {
            throw new IllegalArgumentException("El DNI no es válido");
        }

        Carrera carrera = Carrera.findById(idCarrera);
        if (carrera == null) {
            throw new IllegalArgumentException("Carrera no encontrada");
        }

        InscripcionCarrera existente = InscripcionCarrera.findFirst(
            "dni_Estudiante = ? AND id_Carrera = ?", dniEstudiante, idCarrera);

        if (existente != null) {
            throw new IllegalArgumentException("Ya estás inscripto en esta carrera");
        }

        InscripcionCarrera inscripcion = new InscripcionCarrera();
        inscripcion.setDniEstudiante(dniEstudiante);
        inscripcion.setIdCarrera(idCarrera);
        inscripcion.setEstado("Activo");
        String fecha = LocalDate.now().toString();
        inscripcion.setFechaIngreso(fecha);
        inscripcion.setAnioIngreso(String.valueOf(LocalDate.now().getYear()));

        if (!inscripcion.save()) {
            throw new IllegalArgumentException("No se puede guardar la inscripción");
        }
    }

    public static List<Map<String, Object>> carrerasDisponibles(Integer dniEstudiante) {
        List<Carrera> carreras = Carrera.findAll();
        List<Map<String, Object>> lista = new ArrayList<>();

        for (Carrera carrera : carreras) {
            Map<String, Object> map = new HashMap<>();
            map.put("id", carrera.getIdCarrera());
            map.put("nombre", carrera.getNombre());
            map.put("duracion", carrera.getDuracion());
            map.put("modalidad", carrera.getModalidad());

            InscripcionCarrera inscripto = InscripcionCarrera.findFirst(
                    "dni_Estudiante = ? AND id_Carrera = ?", dniEstudiante, carrera.getIdCarrera());

            map.put("inscripto", inscripto != null);
            map.put("puedeInscribirse", inscripto == null);
            lista.add(map);
        }

        return lista;
    }

    public static void crearCarrera(Integer idCarrera, String nombre, Integer duracion, String modalidad) {
        if (idCarrera == null) {
            throw new IllegalArgumentException("El ID de carrera es obligatorio");
        }

        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre de la carrera es obligatorio");
        }

        if (duracion == null || duracion <= 0) {
            throw new IllegalArgumentException("La duración de la carrera debe ser mayor a cero");
        }

        if (modalidad == null || modalidad.trim().isEmpty()) {
            throw new IllegalArgumentException("La modalidad de la carrera es obligatoria");
        }

        if (Carrera.findById(idCarrera) != null) {
            throw new IllegalArgumentException("Ya existe una carrera con ese ID");
        }

        Carrera carrera = new Carrera();
        carrera.set("id_Carrera", idCarrera);
        carrera.setNombre(nombre.trim());
        carrera.setDuracion(duracion);
        carrera.setModalidad(modalidad.trim());

        if (!carrera.insert()) {
            throw new IllegalArgumentException("No se pudo guardar la carrera");
        }
    }

    public static List<Map<String, Object>> listarCarreras() {
        List<Carrera> carreras = Carrera.findAll();
        List<Map<String, Object>> lista = new ArrayList<>();

        for (Carrera carrera : carreras) {
            Map<String, Object> map = new HashMap<>();
            map.put("id", carrera.getIdCarrera());
            map.put("nombre", carrera.getNombre());
            map.put("duracion", carrera.getDuracion());
            map.put("modalidad", carrera.getModalidad());
            lista.add(map);
        }

        return lista;
    }

    public static Integer obtenerCarreraPrincipalDeEstudiante(Integer dniEstudiante) {
        List<InscripcionCarrera> inscripciones = InscripcionCarrera.where("dni_Estudiante = ?", dniEstudiante);
        if (inscripciones.isEmpty()) {
            return null;
        }
        return inscripciones.get(0).getIdCarrera();
    }

    public static boolean tieneInscripcionCarrera(Integer dniEstudiante) {
        return !InscripcionCarrera.where("dni_Estudiante = ?", dniEstudiante).isEmpty();
    }

    public static List<Integer> obtenerCarrerasDeEstudiante(Integer dniEstudiante) {
        List<InscripcionCarrera> inscripciones = InscripcionCarrera.where("dni_Estudiante = ?", dniEstudiante);
        List<Integer> carreras = new ArrayList<>();
        for (InscripcionCarrera inscripcion : inscripciones) {
            carreras.add(inscripcion.getIdCarrera());
        }
        return carreras;
    }

    public static List<Map<String, Object>> listarCarrerasDeEstudiante(Integer dniEstudiante) {
        List<InscripcionCarrera> inscripciones = InscripcionCarrera.where("dni_Estudiante = ?", dniEstudiante);
        List<Map<String, Object>> lista = new ArrayList<>();

        for (InscripcionCarrera inscripcion : inscripciones) {
            Carrera carrera = inscripcion.parent(Carrera.class);
            if (carrera != null) {
                Map<String, Object> map = new HashMap<>();
                map.put("id", carrera.getIdCarrera());
                map.put("nombre", carrera.getNombre());
                map.put("duracion", carrera.getDuracion());
                map.put("modalidad", carrera.getModalidad());
                map.put("estado", inscripcion.getEstado());
                map.put("fechaIngreso", inscripcion.getFechaIngreso());
                map.put("anioIngreso", inscripcion.getAnioIngreso());
                lista.add(map);
            }
        }

        return lista;
    }
}
