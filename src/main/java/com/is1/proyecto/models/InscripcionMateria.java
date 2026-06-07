package com.is1.proyecto.models;

import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.annotations.BelongsTo;
import org.javalite.activejdbc.annotations.BelongsToParents;
import org.javalite.activejdbc.annotations.Table;

@Table("inscripcion_Materia")
@BelongsToParents({
    @BelongsTo(foreignKeyName = "dni_Estudiante", parent = Estudiante.class),
    @BelongsTo(foreignKeyName = "id_Materia", parent = Materia.class)
})
public class InscripcionMateria extends Model {

    public Integer getDniEstudiante() {
        return getInteger("dni_Estudiante");
    }

    public void setDniEstudiante(Integer dniEstudiante) {
        set("dni_Estudiante", dniEstudiante);
    }

    public Integer getIdMateria() {
        return getInteger("id_Materia");
    }

    public void setIdMateria(Integer idMateria) {
        set("id_Materia", idMateria);
    }

    public String getEstado() {
        return getString("estado");
    }

    public void setEstado(String estado) {
        set("estado", estado);
    }

    public String getFechaInscripcion() {
        return getString("fecha_Inscripcion");
    }

    public void setFechaInscripcion(String fechaInscripcion) {
        set("fecha_Inscripcion", fechaInscripcion);
    }

    public Estudiante getEstudiante() {
        return parent(Estudiante.class);
    }

    public Materia getMateria() {
        return parent(Materia.class);
    }
}
