package com.is1.proyecto.models;

import org.javalite.activejdbc.annotations.IdName;
import org.javalite.activejdbc.annotations.Table;

@Table("administrador")
@IdName("dni_Persona")
public class Admin extends Persona{
    
    public Integer getDni(){ return getInteger("dni_Persona"); }

    public void setDni(Integer dni_Persona){ set("dni_Persona", dni_Persona); }

    public String getCargo(){ return getString("cargo"); }

    public void setCargo(String cargo){ set("cargo", cargo); }

    public String getSector(){ return getString("sector"); }

    public void setSector(String sector){ set("sector", sector); }
}
