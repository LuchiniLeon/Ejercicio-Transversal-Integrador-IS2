package com.is1.proyecto.models;

import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.annotations.Table;

@Table("users") // Esta anotación asocia explícitamente el modelo 'User' con la tabla 'users' en la DB.
public class User extends Model {

    // ActiveJDBC mapea automáticamente las columnas de la tabla 'users'
    // (como 'id', 'name', 'password', etc.) a los atributos de esta clase.
    // No necesitas declarar los campos (id, name, password) aquí como variables de instancia,
    // ya que la clase Model base se encarga de la interacción con la base de datos.

    // Opcional: Puedes agregar métodos getters y setters si prefieres un acceso más tipado,
    // aunque los métodos genéricos de Model (getString(), set(), getInteger(), etc.) ya funcionan.

    public String getName() {
        return getString("name"); // Obtiene el valor de la columna 'name'
    }

    public void setName(String name) {
        set("name", name); // Establece el valor para la columna 'name'
    }

    public String getPassword() {
        return getString("password"); // Obtiene el valor de la columna 'password'
    }

    public void setPassword(String password) {
        set("password", password); // Establece el valor para la columna 'password'
    }

     public Integer getesAdministrador() {
        return getInteger("esAdministrador"); // Obtiene el valor de la columna 'password'
    }

    public void setesAdministrador(Integer esAdmin) {
        set("esAdministrador", esAdmin); // Establece el valor para la columna 'password'
    }

    public Integer getId() {
        return getInteger("id"); // Obtiene el valor de la columna 'id'
    }

}