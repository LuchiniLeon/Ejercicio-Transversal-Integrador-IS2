package com.is1.proyecto.models;

import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.annotations.IdName;
import org.javalite.activejdbc.annotations.Table;

@Table("professors")
@IdName("id_prof")
public class Profesores extends Model {

    public void setIdProf(Integer idProf) {
        set("id_prof", idProf); // Establece el valor para la columna 'id_prof'
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

    public String getNombre() {
        return getString("nombre"); // Obtiene el valor de la columna 'nombre'
    }

    public void setNombre(String nombre) {
        set("nombre", nombre); // Establece el valor para la columna 'nombre'
    }

    public Integer getDni() {
        return getInteger("dni"); // Obtiene el valor de la columna 'dni'
    }

    public void setDni(Integer dni) {
        set("dni", dni); // Establece el valor para la columna 'dni'
    }

    public String getApellido() {
        return getString("apellido"); // Obtiene el valor de la columna 'apellido'
    }

    public void setApellido(String apellido) {
        set("apellido", apellido); // Establece el valor para la columna 'apellido'
    }

    public String getDireccion() {
        return getString("direccion"); // Obtiene el valor de la columna 'direccion'
    }

    public void setDireccion(String direccion) {
        set("direccion", direccion); // Establece el valor para la columna 'direccion'
    }

    public Integer getTelefono() {
        return getInteger("telefono"); // Obtiene el valor de la columna 'telefono'
    }

    public void setTelefono(Integer telefono) {
        set("telefono", telefono); // Establece el valor para la columna 'telefono'
    }

    public String getCorreo() {
        return getString("correo");
    }

    public void setCorreo(String correo) {
        set("correo", correo);
    }
}

