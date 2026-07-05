package com.is1.proyecto.service;
import org.javalite.activejdbc.Base;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.is1.proyecto.models.Admin;
import com.is1.proyecto.models.Docente;
import com.is1.proyecto.models.Estudiante;
import com.is1.proyecto.models.Persona;
import com.is1.proyecto.models.User;

public class EditorServer {

    //Instancia de logger para esta clase específica
    private static final Logger logger = LoggerFactory.getLogger(EditorServer.class);
    
    public static void save(Map<String, Object> datos) {
        try {  
            // 1. Buscamos al usuario por su username
            User user = User.findFirst("nombreUsuario = ?", datos.get("username"));

            if (user != null) {
                // 2. Buscamos a la persona usando el DNI fijo que ya tiene el usuario
                Persona person = Persona.findFirst("dni = ?", user.getDNI());

                if (person != null) {
                    Integer dniFijo = user.getDNI();
                    Boolean esAdminEdicion = datos.get("modificarUsuario") != null && (Boolean) datos.get("modificarUsuario");

                    // ABRIMOS TRANSACCIÓN: Para agrupar los cambios de la persona y sus roles
                    Base.openTransaction();
                    
                    // 3. Modificamos los datos comunes de la Persona (¡El DNI ni se menciona!)
                    person.setNombre(datos.get("newName").toString());
                    person.setApellido(datos.get("newSurname").toString());
                    person.setFechaNac(datos.get("newDate").toString());
                    person.saveIt();

                    // 4. Si es una edición de Administrador, modificamos los campos específicos de los roles
                    if (esAdminEdicion) {
                        
                        if (datos.get("esAdministrador") != null && (Boolean) datos.get("esAdministrador")) {
                            Admin ad = Admin.findFirst("dni_Persona = ?", dniFijo);
                            if (ad != null) {
                                ad.setCargo(datos.get("newCargo").toString());
                                ad.setSector(datos.get("newSec").toString());
                                ad.saveIt();
                            }
                        }

                        if (datos.get("esDocente") != null && (Boolean) datos.get("esDocente")) {
                            Docente doc = Docente.findFirst("dni_Persona = ?", dniFijo);
                            if (doc != null) {
                                doc.setCargo(datos.get("newCargo").toString());
                                doc.setLegajo((Integer) datos.get("newLeg"));
                                doc.saveIt();
                            }
                        }

                        if (datos.get("esEstudiante") != null && (Boolean) datos.get("esEstudiante")) {
                            Estudiante est = Estudiante.findFirst("dni_Persona = ?", dniFijo);
                            if (est != null) {
                                est.setIngreso(datos.get("newIngreso").toString());
                                est.setEstado(datos.get("newEs").toString());
                                est.saveIt();
                            }
                        }
                    }
                    
                    // Consolidamos todos los cambios en un solo viaje al disco
                    Base.commitTransaction();

                    //Reemplazo del system.out.println (Inseguro) por logger.info
                    logger.info("¡Perfil actualizado con éxito para el DNI: {}", dniFijo);
                }
            }
        } catch (Exception e) {
            try { Base.rollbackTransaction(); } catch(Exception ex) {}

            //Reemplazo del system.out.println y printStackTrace (Inseguros) por logger.error
            logger.error("Error al actualizar perfil: ", e);
        }
    }
}
