package com.is1.proyecto.models;


import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.annotations.IdName;
import org.javalite.activejdbc.annotations.Table;

@Table("persona")
@IdName("dni") //Le digo que dni es clave primaria (Es su id)
public class Persona extends Model{
    
    public String getNombre() {
        return getString("nombre"); // Obtiene el valor de la columna 'name'
    }

    public void setNombre(String name) {
        set("nombre", name); // Establece el valor para la columna 'name'
    }

    public String getApellido() {
        return getString("apellido"); // Obtiene el valor de la columna 
    }

    public void setApellido(String apellido) {
        set("apellido", apellido); // Establece el valor para la columna 
    }

    public String getFechaNac() {
        return getString("fecha_Nacimiento"); // Obtiene el valor de la columna 
    }

    public void setFechaNac(String fechaNac) {
        set("fecha_Nacimiento", fechaNac); // Establece el valor para la columna 
    }

    public Integer getDNI() {
        return getInteger("dni"); // Obtiene el valor de la columna 
    }

    public void setDNI(Integer dni){
        set("dni", dni);
    }
}
