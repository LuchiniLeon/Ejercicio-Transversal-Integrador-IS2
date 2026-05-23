package com.is1.proyecto.models;

import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.annotations.IdName;
import org.javalite.activejdbc.annotations.Table;

@Table("token_password")
@IdName("token") 
public class Token extends Model {

    public String getToken() {
        return getString("token"); // Obtiene el valor de la columna 'name'
    }

    public void setToken(String token) {
        set("token", token); // Establece el valor para la columna 'name'
    }

    public String getmail() {
        return getString("email"); // Obtiene el valor de la columna 'password'
    }

    public void setmail(String mail) {
        set("email", mail); // Establece el valor para la columna 'password'
    }

      public String getExpiracion() {
        return getString("fecha_expiracion"); // Obtiene el valor de la columna 'password'
    }

    public void setExpiracion(String fecha) {
        set("fecha_expiracion", fecha); // Establece el valor para la columna 'password'
    }
}