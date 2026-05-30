package com.is1.proyecto.service;

import org.javalite.activejdbc.Base;

import com.is1.proyecto.models.*;

public class SuperAdminService {
 
    public static void cargaSuperAdmin(){
        
        
        SuperAdmin admin = new SuperAdmin();
        
        try{
          
            User exist = User.findFirst("nombreUsuario = ?", admin.getUsuario());

            if(exist != null){

                System.out.println("Saltando creación de Super Admin");
                return;
            }

            UserService.createUser(admin.getUsuario(), admin.getPassword(), admin.getNombre(), admin.getApellido(), admin.getFecha(), admin.getDni(), admin.getEmail());
            //Hardcodeo
            //UserService.createUser("Leo4", "Leo4", "Leonel", "Campos", "1900-05-23", 25325125, "mechylacruz06@gmail.com");
            //UserService.createUser("UriCocos4", "UriCocos4", "Uriel", "Luchinni", "1900-05-23", 54118344, "mechylacruz06@gmail.com");
            //UserService.createUser("Agos100", "Agos100", "Agostina", "Dios Sabe", "1900-05-23", 48458455, "mechylacruz06@gmail.com");
            Base.openTransaction();
            
            String sql = "INSERT INTO administrador (dni_Persona, cargo, sector) VALUES (?, ?, ?)";

            Base.exec(sql, admin.getDni(), admin.getCargo(), admin.getSector());
            
            //Hardcodeo
            String sqlAdm = "INSERT INTO administrador (dni_Persona, cargo, sector) VALUES (?, ?, ?)";
            String sqlDocente = "INSERT INTO docente (dni_Persona, legajo, cargo, dni_Administrador) VALUES(?, ?, ?, ?)";
            String sqlEst = "INSERT INTO estudiante (dni_Persona, estado_Academico, ingreso, dni_administrador) VALUES (?, ?, ?, ?)";

            
            Base.exec(sqlAdm, 48458455, "EXPERIENCIA", "GRUPO");
            Base.exec(sqlDocente, 54118344, 452182584, "AYUDANTE", 48458455);
            Base.exec(sqlEst, 25325125, "REGULAR", "2020-03-01", 48458455);

            Base.commitTransaction();

        } catch (Exception e) {
        
            Base.rollbackTransaction();
            
            throw e;
        } 
    }
}
