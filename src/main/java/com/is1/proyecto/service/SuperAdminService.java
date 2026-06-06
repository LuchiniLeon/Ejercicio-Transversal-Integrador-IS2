package com.is1.proyecto.service;

import org.javalite.activejdbc.Base;

import com.is1.proyecto.models.SuperAdmin;
import com.is1.proyecto.models.User;

public class SuperAdminService {
 
    public static void cargaSuperAdmin(){
        
        
        SuperAdmin admin = new SuperAdmin();
            
        Base.openTransaction();
        
        try{
          
            User exist = User.findFirst("nombreUsuario = ?", admin.getUsuario());

            if(exist != null){
                System.out.println("Saltando creación de Super Admin");
                Base.commitTransaction();
                return;
            }

            UserService.createUser(admin.getUsuario(), admin.getPassword(), admin.getNombre(), admin.getApellido(), admin.getFecha(), admin.getDni(), admin.getEmail());

            AdminService.crearAdmin(admin.getDni(), admin.getCargo(), admin.getSector());

            // Hardcodeo — solo si no existen
            if (User.findFirst("nombreUsuario = ?", "Agos100") == null){
                UserService.createUser("Agos100", "Agos100", "Agostina", "Dios Sabe", "1900-05-23", 48458455, "agos100@gmail.com");
                Base.exec("INSERT INTO administrador (dni_Persona, cargo, sector) VALUES (?, ?, ?)", 48458455, "EXPERIENCIA", "GRUPO");
            }
            if (User.findFirst("nombreUsuario = ?", "UriCocos4") == null){
                UserService.createUser("UriCocos4", "UriCocos4", "Uriel", "Luchinni", "1900-05-23", 54118344, "uricocos4@gmail.com");
                Base.exec("INSERT INTO docente (dni_Persona, legajo, cargo, dni_Administrador) VALUES (?, ?, ?, ?)", 54118344, 452182584, "AYUDANTE", 48458455);
            }
            if (User.findFirst("nombreUsuario = ?", "Leo4") == null){
                UserService.createUser("Leo4", "Leo4", "Leonel", "Campos", "1900-05-23", 25325125, "leo4@gmail.com");
                Base.exec("INSERT INTO estudiante (dni_Persona, estado_Academico, ingreso, dni_Administrador) VALUES (?, ?, ?, ?)", 25325125, "REGULAR", "2020-03-01", 48458455);
            }

            Base.commitTransaction();

        } catch (Exception e) {
        
            Base.rollbackTransaction();
            
            throw e;
        } 
    }
}
