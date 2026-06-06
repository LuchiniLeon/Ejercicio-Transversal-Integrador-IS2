package com.is1.proyecto.models;

import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.annotations.BelongsTo;
import org.javalite.activejdbc.annotations.BelongsToParents;
import org.javalite.activejdbc.annotations.Table;

@Table("participa_docente_taller")
@BelongsToParents({
    @BelongsTo(foreignKeyName = "id_Taller", parent = Taller.class),
    @BelongsTo(foreignKeyName = "dni_Docente", parent = Docente.class)
})
public class ParticipaDocenteTaller extends Model {

    public Integer getIdTaller() { 
        return getInteger("id_Taller");
    }
    
    public void setIdTaller(Integer idTaller) {
         set("id_Taller", idTaller); 
    }

    public Integer getDniDocente() {
         return getInteger("dni_Docente"); 
    }
    
    public void setDniDocente(Integer dniDocente) { 
        set("dni_Docente", dniDocente); 
    }
}