package com.is1.proyecto.models;

import io.github.cdimascio.dotenv.Dotenv;
import org.javalite.activejdbc.annotations.IdName;
import org.javalite.activejdbc.annotations.Table;

@Table("administrador")
@IdName("dni_Persona")

public class SuperAdmin extends Persona{

    public SuperAdmin(){

    }

    Dotenv dotenv = Dotenv.load();

    public Integer getDni(){ return Integer.parseInt(dotenv.get("SUPERADMIN_DNI")); }

    public String getNombre(){ return dotenv.get("SUPERADMIN_NOMBRE"); }

    public String getApellido(){ return dotenv.get("SUPERADMIN_APELLIDO"); }

    public String getFecha(){ return dotenv.get("SUPERADMIN_FECHA"); }
    
    public String getUsuario(){ return dotenv.get("SUPERADMIN_USER"); }

    public String getCargo(){ return dotenv.get("SUPERADMIN_CARGO"); }

    public String getSector(){ return dotenv.get("SUPERADMIN_SECTOR"); }

    public String getEmail(){ return dotenv.get("SUPERADMIN_EMAIL"); }

    public String getPassword(){ return dotenv.get("SUPERADMIN_PASSWORD"); }
}
