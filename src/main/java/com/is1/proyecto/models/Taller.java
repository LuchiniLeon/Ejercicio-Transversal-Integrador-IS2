package com.is1.proyecto.models;
import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.annotations.BelongsTo;
import org.javalite.activejdbc.annotations.IdName;
import org.javalite.activejdbc.annotations.Table;


@Table("taller")
@IdName("id_taller")
@BelongsTo(foreignKeyName = "dni_Docente", parent = Docente.class)
public class Taller extends Model{

    public Integer getId(){
        return getInteger("id_taller");
    }

    public void setId(Integer id) { 
        set("id_taller", id); 
    }

    public Boolean getVigente(){
        return getInteger("vigente") == 1;
    }

    // error a otro numero.
    public void setVigente(Boolean vigente) { 
        set("vigente", vigente ? 1 : 0); 
    }

    public Integer getHoras(){
        return getInteger("hora");
    }
    
    public void setHora(Integer hora) { 
        set("hora", hora); 

    }
    public String getTitulo(){
        return getString("titulo");
    }

    public void setTitulo(String titulo) {
        set("titulo", titulo); 
    }

    public Integer getDniDocente(){
        return getInteger("dni_Docente");
    }
    
    public void setDniDocente(Integer dni) {
        set("dni_Docente", dni); 
    }

    // Para obtener el docente asociado
    public Docente getDocente() { return parent(Docente.class); }
}