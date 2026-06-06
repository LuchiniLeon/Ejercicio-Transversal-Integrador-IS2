package com.is1.proyecto.service;

import org.javalite.activejdbc.Base;
import org.mindrot.jbcrypt.BCrypt;

import com.is1.proyecto.models.Admin;
import com.is1.proyecto.models.Docente;
import com.is1.proyecto.models.Persona;
import com.is1.proyecto.models.User;

public class DocenteService {
    
     public static void crearDocente(Integer dniInt, Integer legajoInt, String cargo, Integer dniAdmin) {

        // VALIDACIONES

        if (dniInt == null ||
            dniAdmin == null ||
            legajoInt == null ||
            cargo == null || cargo.isEmpty()){
            throw new IllegalArgumentException("Faltan campos obligatorios");
        }


        if (Persona.findFirst("dni = ?", dniInt) == null) {
            throw new IllegalStateException("El DNI del docente no existe");
        }

        if(Admin.findFirst("dni_Persona = ?", dniAdmin) == null){
            throw new IllegalStateException("El dni no existe o no corresponde a un administrador");
        }
    
              //Abre la transacción acá
        Base.openTransaction();

        try{
            //CREACION DE DOCENTE
            Docente docente = new Docente();
            docente.setCargo(cargo);
            docente.setLegajo(legajoInt);
            docente.setDni(dniInt);
            docente.setDniAdministrador(dniAdmin);
            docente.insert();

            //Si todo salió bien dentro del try, confirma para que pase a la bd
            Base.commitTransaction(); 
        }catch (Exception e){
            //Si falló algo en el try, deshacemos todo para no dejar nada a medias en la bd
            Base.rollbackTransaction();
            throw e;
        }

    }
}
