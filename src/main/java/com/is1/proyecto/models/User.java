package com.is1.proyecto.models;

import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.annotations.IdName;
import org.javalite.activejdbc.annotations.Table;

@Table("usuario") // Esta anotación asocia explícitamente el modelo 'User' con la tabla 'users' en la DB.
@IdName("nombreUsuario") //Le digo que el nombre de usuario es clave primaria (Es su id)
public class User extends Model {

    // ActiveJDBC mapea automáticamente las columnas de la tabla 'users'
    // (como 'id', 'name', 'password', etc.) a los atributos de esta clase.
    // No necesitas declarar los campos (id, name, password) aquí como variables de instancia,
    // ya que la clase Model base se encarga de la interacción con la base de datos.

    // Opcional: Puedes agregar métodos getters y setters si prefieres un acceso más tipado,
    // aunque los métodos genéricos de Model (getString(), set(), getInteger(), etc.) ya funcionan.

    public String getName() {
        return getString("nombreUsuario"); // Obtiene el valor de la columna 'name'
    }

    public void setName(String name) {
        set("nombreUsuario", name); // Establece el valor para la columna 'name'
    }

    public String getPassword() {
        return getString("contraseña"); // Obtiene el valor de la columna 'password'
    }

    public void setPassword(String password) {
        set("contraseña", password); // Establece el valor para la columna 'password'
    }

    public Integer getDNI() {
        return getInteger("dni_Persona"); // Obtiene el valor de la columna 'id'
    }

}