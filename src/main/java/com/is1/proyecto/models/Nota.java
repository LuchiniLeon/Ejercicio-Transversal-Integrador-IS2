package com.is1.proyecto.models;

import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.annotations.IdName;
import org.javalite.activejdbc.annotations.Table;

@Table("nota")
@IdName("id_Nota")
public class Nota extends Model {

    public Integer getIdNota() {
        return getInteger("id_Nota");
    }

    public String getCondicion() {
        return getString("condicion");
    }

    public void setCondicion(String condicion) {
        set("condicion", condicion);
    }

    public Integer getNotaFinal() {
        return getInteger("nota_Final");
    }

    public void setNotaFinal(Integer notaFinal) {
        set("nota_Final", notaFinal);
    }

    public String getFechaExamen() {
        return getString("fecha_Examen");
    }

    public void setFechaExamen(String fechaExamen) {
        set("fecha_Examen", fechaExamen);
    }

    public Integer getDniEstudiante() {
        return getInteger("dni_Estudiante");
    }

    public void setDniEstudiante(Integer dniEstudiante) {
        set("dni_Estudiante", dniEstudiante);
    }

    public Integer getIdMateria() {
        return getInteger("id_Materia");
    }

    public void setIdMateria(Integer idMateria) {
        set("id_Materia", idMateria);
    }

    public Integer getIdTaller() {
        return getInteger("id_Taller");
    }

    public void setIdTaller(Integer idTaller) {
        set("id_Taller", idTaller);
    }

    public Integer getDniEstudianteEstudia() {
        return getInteger("dni_Estudiante_Estudia");
    }

    public void setDniEstudianteEstudia(Integer dniEstudianteEstudia) {
        set("dni_Estudiante_Estudia", dniEstudianteEstudia);
    }
}