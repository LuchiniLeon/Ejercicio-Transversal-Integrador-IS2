/** INSCRIPCION DE ESTUDIANTE A TALLER */
package com.is1.proyecto.models;

import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.annotations.Table;
import org.javalite.activejdbc.annotations.BelongsTo;
import org.javalite.activejdbc.annotations.BelongsToParents;

@Table("estudia")
@BelongsToParents({
    @BelongsTo(foreignKeyName = "dni_Estudiante", parent = Estudiante.class),
    @BelongsTo(foreignKeyName = "id_taller", parent = Taller.class)
})
public class Estudia extends Model{
    public Integer getDni_Estudiante(){
        return getInteger("dni_estudiante");
    }

    public void setDni_Estudiante(Integer dni){
        set("dni_Estudiante", dni);
    }

    public Integer getId_Taller(){
        return getInteger("id_taller");
    }

    public void setId_taller(Integer id){
        set("id_taller", id);
    }

    public Estudiante getEstudiante() {
        return parent(Estudiante.class);
    }

    public Taller getTaller(){
        return parent(Taller.class);
    }
}
