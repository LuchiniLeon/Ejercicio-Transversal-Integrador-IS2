package com.is1.proyecto.service;

import org.javalite.activejdbc.Base;

import com.is1.proyecto.models.Admin;
import com.is1.proyecto.models.Persona;


public class AdminService {
    
    public static void crearAdmin(Integer dni_Persona, String cargo, String sector)
    {
        if(dni_Persona == null || dni_Persona == 0) throw new IllegalArgumentException("Faltan campos obligatorios");

        //Esto no deberia ir ya que se supone que se asugna a un usuario ya existente, pero no sé si rompe algo harcodeado
        //if (Persona.findFirst("dni = ?", dni_Persona) != null) throw new IllegalStateException("El DNI ya existe");
        if (Persona.findFirst("dni = ?", dni_Persona) == null) {
            throw new IllegalStateException("El DNI no existe");
        }

             //Abre la transacción acá
        Base.openTransaction();

        try{
            //CREACION DE ADMINISTRADOR
            Admin admin = new Admin();
            admin.setDni(dni_Persona);
            admin.setCargo(cargo);
            admin.setSector(sector);
            admin.insert();

            //Si todo salió bien dentro del try, confirma para que pase a la bd
            Base.commitTransaction(); 
        }catch (Exception e){
            //Si falló algo en el try, deshacemos todo para no dejar nada a medias en la bd
            Base.rollbackTransaction();
            throw e;
        }
    }
}
