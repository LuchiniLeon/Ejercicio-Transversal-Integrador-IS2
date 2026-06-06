package com.is1.proyecto.service;

import org.javalite.activejdbc.Base;
import org.mindrot.jbcrypt.BCrypt;

import com.is1.proyecto.models.Admin;
import com.is1.proyecto.models.Docente;
import com.is1.proyecto.models.Estudiante;
import com.is1.proyecto.models.Persona;
import com.is1.proyecto.models.User;

public class EstudianteService {
     public static void crearEstudiante(Integer dniInt, String estadoAcademico, String ingreso, Integer dniAdmin) {

        // VALIDACIONES

        if (dniInt == null ||
            dniAdmin == null ||
            ingreso == null || ingreso.isEmpty() ||
            estadoAcademico == null || estadoAcademico.isEmpty()){
            throw new IllegalArgumentException("Faltan campos obligatorios");
        }


        if (Persona.findFirst("dni = ?", dniInt) == null) {
            throw new IllegalStateException("El DNI del estudiante no existe");
        }

        if(Admin.findFirst("dni_Persona = ?", dniAdmin) == null){
            throw new IllegalStateException("El dni no existe o no corresponde a un administrador");
        }
    
              //Abre la transacción acá
        Base.openTransaction();

        try{
            //CREACION DE ESTUDIANTE
            Estudiante estudiante = new Estudiante();
            estudiante.setDni(dniInt);
            estudiante.setEstado(estadoAcademico);
            estudiante.setIngreso(ingreso);
            estudiante.setDniAdministrador(dniAdmin);
            estudiante.insert();

            //Si todo salió bien dentro del try, confirma para que pase a la bd
            Base.commitTransaction(); 
        }catch (Exception e){
            //Si falló algo en el try, deshacemos todo para no dejar nada a medias en la bd
            Base.rollbackTransaction();
            throw e;
        }

    }
}
