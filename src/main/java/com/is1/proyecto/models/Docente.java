package com.is1.proyecto.models;

import org.javalite.activejdbc.annotations.BelongsTo;
import org.javalite.activejdbc.annotations.IdName;
import org.javalite.activejdbc.annotations.Table;

@Table("docente")
@IdName("dni_Persona")
@BelongsTo(foreignKeyName = "dni_Persona", parent = Persona.class)
public class Docente extends Persona {

    public Integer getDniAdministrador(){
        return getInteger("dni_Administrador");
    }

    public Integer getDni(){
        return getInteger("dni_Persona");
    }

    public void setDni(Integer dni_Persona){
        set("dni_Persona", dni_Persona);
    }

    public Integer getLegajo() {
        return getInteger("legajo"); // Obtiene el valor de la columna 'legajo'
    }

    public void setLegajo(Integer legajo) {
        set("legajo", legajo); // Establece el valor para la columna 'legajo'
    }

    public String getCargo() {
        return getString("cargo"); // Obtiene el valor de la columna 'cargo'
    }

    public void setCargo(String cargo) {
        set("cargo", cargo); // Establece el valor para la columna 'cargo'
    }
}

