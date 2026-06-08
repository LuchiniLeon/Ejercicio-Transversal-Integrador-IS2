package com.is1.proyecto.models;

import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.annotations.BelongsTo;
import org.javalite.activejdbc.annotations.BelongsToParents;
import org.javalite.activejdbc.annotations.Table;

@Table("inscripcion_Carrera")
@BelongsToParents({
    @BelongsTo(foreignKeyName = "dni_Estudiante", parent = Estudiante.class),
    @BelongsTo(foreignKeyName = "id_Carrera", parent = Carrera.class)
})
public class InscripcionCarrera extends Model {

    public Integer getDniEstudiante() {
        return getInteger("dni_Estudiante");
    }

    public void setDniEstudiante(Integer dniEstudiante) {
        set("dni_Estudiante", dniEstudiante);
    }

    public Integer getIdCarrera() {
        return getInteger("id_Carrera");
    }

    public void setIdCarrera(Integer idCarrera) {
        set("id_Carrera", idCarrera);
    }

    public String getEstado() {
        return getString("estado");
    }

    public void setEstado(String estado) {
        set("estado", estado);
    }

    public String getFechaIngreso() {
        return getString("fecha_Ingreso");
    }

    public void setFechaIngreso(String fechaIngreso) {
        set("fecha_Ingreso", fechaIngreso);
    }

    public String getAnioIngreso() {
        return getString("anio_Ingreso");
    }

    public void setAnioIngreso(String anioIngreso) {
        set("anio_Ingreso", anioIngreso);
    }

    public Estudiante getEstudiante() {
        return parent(Estudiante.class);
    }

    public Carrera getCarrera() {
        return parent(Carrera.class);
    }
}
