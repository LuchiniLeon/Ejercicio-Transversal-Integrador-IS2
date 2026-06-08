package com.is1.proyecto.models;

import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.annotations.Table;

@Table("nota_taller")
public class NotaTaller extends Model {

    public Integer getIdNota() {
        return getInteger("id_Nota");
    }

    public void setIdNota(Integer idNota) {
        set("id_Nota", idNota);
    }

    public Integer getDniEstudiante() {
        return getInteger("dni_Estudiante");
    }

    public void setDniEstudiante(Integer dniEstudiante) {
        set("dni_Estudiante", dniEstudiante);
    }

    public Integer getIdTaller() {
        return getInteger("id_Taller");
    }

    public void setIdTaller(Integer idTaller) {
        set("id_Taller", idTaller);
    }
}