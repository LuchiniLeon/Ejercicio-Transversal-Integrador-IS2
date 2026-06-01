package com.is1.proyecto.models;

import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.annotations.IdName;
import org.javalite.activejdbc.annotations.Table;

@Table("email") // Esta anotación asocia explícitamente el modelo 'Email' con la tabla 'email' en la DB.
@IdName("dni_Persona") //Le digo que el dni de la persona es clave primaria (Es su id)
public class Email extends Model {

    public String getMail() {
        return getString("email"); // Obtiene el correo electrónico
    }

    public void setMail(String name) {
        set("email", name); //  Guarda el correo electrónico
    }

    public Integer getDni() {
        return getInteger("dni_Persona"); //Obtiene el DNI asociado al email
    }

    public void setDni(Integer dni) {
        set("dni_Persona", dni); // Guarda el DNI asociado al email
    }

}