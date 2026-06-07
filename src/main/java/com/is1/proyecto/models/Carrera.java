package com.is1.proyecto.models;

import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.annotations.IdName;
import org.javalite.activejdbc.annotations.Table;

@Table("carrera")
@IdName("id_Carrera")
public class Carrera extends Model {

    public Integer getIdCarrera() {
        return getInteger("id_Carrera");
    }

    public String getNombre() {
        return getString("nombre");
    }

    public Integer getDuracion() {
        return getInteger("duracion");
    }

    public String getModalidad() {
        return getString("modalidad");
    }

    public void setNombre(String nombre) {
        set("nombre", nombre);
    }

    public void setDuracion(Integer duracion) {
        set("duracion", duracion);
    }

    public void setModalidad(String modalidad) {
        set("modalidad", modalidad);
    }
}
