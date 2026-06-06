package com.is1.proyecto.models;
import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.annotations.BelongsTo;
import org.javalite.activejdbc.annotations.IdName;
import org.javalite.activejdbc.annotations.Table;

@Table("materia")
@IdName("id_Materia")
@BelongsTo(foreignKeyName = "dni_Docente", parent = Docente.class)
public class Materia extends Model {

    public Integer getIdMateria() {
        return getInteger("id_Materia");
    }

    public Integer getCodigo() {
        return getInteger("codigo");
    }

    public void setCodigo(Integer codigo) {
        set("codigo", codigo);
    }

    public Integer getHorasTotales() {
        return getInteger("horasTotales");
    }

    public void setHorasTotales(Integer horasTotales) {
        set("horasTotales", horasTotales);
    }

    public String getNombre() {
        return getString("nombre");
    }

    public void setNombre(String nombre) {
        set("nombre", nombre);
    }

    public Integer getDniAdministrador() {
        return getInteger("dni_Administrador");
    }

    public void setDniAdministrador(Integer dniAdministrador) {
        set("dni_Administrador", dniAdministrador);
    }

    public Integer getDniDocente() {
        return getInteger("dni_Docente");
    }

    public void setDniDocente(Integer dniDocente) {
        set("dni_Docente", dniDocente);
    }
}