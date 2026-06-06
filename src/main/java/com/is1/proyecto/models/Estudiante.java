package com.is1.proyecto.models;

import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.annotations.BelongsTo;
import org.javalite.activejdbc.annotations.IdName;
import org.javalite.activejdbc.annotations.Table;

@Table("estudiante")
@IdName("dni_Persona")
@BelongsTo(foreignKeyName = "dni_Persona", parent = Persona.class)
public class Estudiante extends Model {
    
    public Integer getDniAdministrador(){ return getInteger("dni_Administrador"); }

    public Integer getDni(){ return getInteger("dni_Persona"); }

    public void setDni(Integer dni){ set("dni_Persona", dni); }

    public String getIngreso(){ return getString("ingreso"); }

    public void setIngreso(String ingreso){ set("ingreso", ingreso); }

    public String getEstado(){ return getString("estado_Academico"); }

    public void setEstado(String estado){ set("estado_Academico", estado); }
}
