package com.is1.proyecto.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.is1.proyecto.models.Docente;
import com.is1.proyecto.models.Materia;
import com.is1.proyecto.models.Persona;

public class MateriaService {

    public static void crearMateria(Integer codigo, String nombre, Integer horasTotales, Integer dniAdministrador, Integer dniDocente) {

        // Sus respectivas validaciones ;)
        if (codigo == null) {
            throw new IllegalArgumentException("El codigo es obligatorio");
        }

        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre es obligatorio");
        }

        if (horasTotales == null || horasTotales <= 0) {
            throw new IllegalArgumentException("Las horas totales deben ser mayores a cero");
        }

        if (dniAdministrador == null) {
            throw new IllegalArgumentException("Debe existir un administrador");
        }

        if (dniDocente == null) {
            throw new IllegalArgumentException("Debe asignarse un docente");
        }

        //Crear la materia 
        Materia materia = new Materia();

        materia.setCodigo(codigo);
        materia.setNombre(nombre.trim());
        materia.setHorasTotales(horasTotales);
        materia.setDniAdministrador(dniAdministrador);
        materia.setDniDocente(dniDocente);

        if (!materia.save()) {
            throw new IllegalArgumentException("No se pudo guardar la materia");
        }
    }

    //Lista todas las materias 
    public static List<Map<String, Object>> listarMaterias() {

        List<Materia> materias = Materia.findAll();
        List<Map<String, Object>> lista = new ArrayList<>();

        for (Materia materia : materias) {

            Map<String, Object> m = new HashMap<>();

            m.put("id", materia.getIdMateria());
            m.put("codigo", materia.getCodigo());
            m.put("nombre", materia.getNombre());
            m.put("horasTotales", materia.getHorasTotales());
            m.put("dniDocente", materia.getDniDocente());

            if (materia.parent(Docente.class) != null) {
                Docente docente = materia.parent(Docente.class);
                m.put("nombreDocente", docente.parent(Persona.class).getString("nombre") + " " + docente.parent(Persona.class).getString("apellido"));
            } else {
                m.put("nombreDocente", "Sin docente asignado");
            }

            lista.add(m);
        }

        return lista;
    }

    public static List<Map<String, Object>> listarMateriasPorDocente(Integer dniDocente) {
        List<Materia> materias = Materia.where("dni_Docente = ?", dniDocente);
        List<Map<String, Object>> lista = new ArrayList<>();

        for (Materia materia : materias) {
            Map<String, Object> m = new HashMap<>();
            m.put("id", materia.getIdMateria());
            m.put("codigo", materia.getCodigo());
            m.put("nombre", materia.getNombre());
            m.put("horasTotales", materia.getHorasTotales());
            m.put("dniDocente", materia.getDniDocente());

            if (materia.parent(Docente.class) != null) {
                Docente docente = materia.parent(Docente.class);
                m.put("nombreDocente", docente.parent(Persona.class).getString("nombre") + " " + docente.parent(Persona.class).getString("apellido"));
            } else {
                m.put("nombreDocente", "Sin docente asignado");
            }

            lista.add(m);
        }

        return lista;
    }

    public static Map<String, Object> obtenerMateria(Integer idMateria) {

        Materia materia = Materia.findById(idMateria);

        if (materia == null) {
            throw new IllegalArgumentException("Materia no encontrada");
        }

        Map<String, Object> m = new HashMap<>();

        m.put("id", materia.getIdMateria());
        m.put("codigo", materia.getCodigo());
        m.put("nombre", materia.getNombre());
        m.put("horasTotales", materia.getHorasTotales());
        m.put("dniAdministrador", materia.getDniAdministrador());
        m.put("dniDocente", materia.getDniDocente());

        return m;
    }

    public static void editarMateria(Integer idMateria, Integer codigo, String nombre, Integer horasTotales, Integer dniDocente) {

        Materia materia = Materia.findById(idMateria);
        //verifico primero si la materia existe, sino no tiene sentido 
        if (materia == null) {
            throw new IllegalArgumentException("Materia no encontrada");
        }

        if (codigo == null) {
            throw new IllegalArgumentException("El codigo es obligatorio");
        }

        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre es obligatorio");
        }

        if (horasTotales == null || horasTotales <= 0) {
            throw new IllegalArgumentException("Las horas totales deben ser mayores a cero");
        }

        materia.setCodigo(codigo);
        materia.setNombre(nombre.trim());
        materia.setHorasTotales(horasTotales);
        materia.setDniDocente(dniDocente);

        if (!materia.save()) {
            throw new IllegalArgumentException("No se pudieron guardar los cambios");
        }
    }

    public static void eliminarMateria(Integer idMateria) {

        Materia materia = Materia.findById(idMateria);

        if (materia == null) {
            throw new IllegalArgumentException("Materia no encontrada");
        }

        if (!materia.delete()) {
            throw new IllegalArgumentException("No se pudo eliminar la materia");
        }
    }

    public static void asignarDocenteAMateria(Integer idMateria, Integer dniDocente) {
        Materia m = Materia.findById(idMateria);

        if(m == null){
            throw new IllegalArgumentException("Materia no encontrada");
        } 

        Docente d = Docente.findFirst("dni_Persona = ?", dniDocente);

        if(d == null){
            throw new IllegalArgumentException("Docente no encontrado");
        } 

        m.setDniDocente(dniDocente);
        if(!m.save()){
            throw new IllegalArgumentException("No se pudo asignar el docente");
        } 
    }

}
