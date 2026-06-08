package com.is1.proyecto.models;

import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.annotations.IdName;
import org.javalite.activejdbc.annotations.Table;

@Table("token_password")
@IdName("token") 
public class Token extends Model {

    public String getToken() {
        return getString("token"); // Obtiene el valor de la columna 'token'
    }

    public void setToken(String token) {
        set("token", token); // Establece el valor para la columna 'token'
    }

    public String getmail() {
        return getString("email"); // Obtiene el email asociado al token
    }

    public void setmail(String mail) {
        set("email", mail); // Guarda el email asociado al token
    }

      public String getExpiracion() {
        return getString("fecha_expiracion"); // Obtiene la fecha de expiración del token
    }

    public void setExpiracion(String fecha) {
        set("fecha_expiracion", fecha); // Guarda la fecha de expiración del token
    }
}