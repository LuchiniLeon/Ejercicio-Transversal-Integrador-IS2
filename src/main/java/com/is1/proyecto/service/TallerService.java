package com.is1.proyecto.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.is1.proyecto.models.Taller;

public class TallerService {
    

    public static void crearTaller(String titulo, Integer hora, Boolean vigente, Integer dniDocente){

        // Validaciones
        if(titulo == null || titulo.trim().isEmpty()){
            throw new IllegalArgumentException("El titulo no puede estar vacio");
        }

        if(hora <= 0){
            throw new IllegalArgumentException("La hora tiene que ser mayor a cero");
        } 

        if(dniDocente == null){
            throw new IllegalArgumentException("Debe asignar un docente");
        }

        // Crear el taller
        Taller taller = new Taller();
        taller.setTitulo(titulo.trim());
        taller.setHora(hora);
        taller.setVigente(vigente);
        taller.setDniDocente(dniDocente);

        if (!taller.save()){
            throw new IllegalArgumentException("No se pudo guardar el taller");
        }
    }

    // Lista todos los talleres que le pertenece a un docente.
    public static List<Map<String, Object>> listarTalleresPorDocente(Integer dniDocente){
        // Talleres de un docente en particular
        List<Taller> talleres = Taller.where("dni_Docente = ?", dniDocente);
        List<Map<String, Object>> lista = new ArrayList<>();

        for(Taller taller : talleres){
            Map<String, Object> t = new HashMap<>();
            t.put("id", taller.getId());
            t.put("titulo", taller.getTitulo());
            t.put("horas", taller.getHoras());
            t.put("vigente", taller.getVigente()? "Si" : "No");

            lista.add(t);
        }

        return lista;

    }
    
    public static Map<String, Object> obtenerTaller(Integer idTaller){

        Taller taller = Taller.findById(idTaller);

        if(taller == null){
            throw new IllegalArgumentException("No se encontro el taller");
        }

        Map<String, Object> t = new HashMap<>();
        t.put("id", taller.getId());
        t.put("titulo", taller.getTitulo());
        t.put("hora", taller.getHoras());
        t.put("vigente", taller.getVigente());

        return t;
    }

    public static void editarTaller(Integer id, String titulo, Integer hora, Boolean vigente){

        if(titulo == null || titulo.trim().isEmpty()){
            throw new IllegalArgumentException("El titulo no puede estar vacio");
        }

        if(hora <= 0){
            throw new IllegalArgumentException("La hora debe ser mayor a 0");
        }

        Taller taller = Taller.findById(id);
        if(taller == null){
            throw new IllegalArgumentException("Taller no encontrado");
        }

        taller.setTitulo(titulo);
        taller.setHora(hora);
        taller.setVigente(vigente);

        if(!taller.save()){
            throw new IllegalArgumentException("No se pudo guardar los cambios de taller");
        }
    }   
}
