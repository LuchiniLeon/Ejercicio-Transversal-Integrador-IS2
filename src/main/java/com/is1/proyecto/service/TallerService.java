package com.is1.proyecto.service;

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
}
