package com.is1.proyecto.models;

import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.annotations.IdName;
import org.javalite.activejdbc.annotations.Table;
import org.javalite.activejdbc.annotations.BelongsTo;

@Table("planificador_tarea") // Tu tabla real en singular
@IdName("id_Tarea")          // Tu clave primaria real
@BelongsTo(foreignKeyName = "dni_Persona", parent = Persona.class)
public class PlanificadorTarea extends Model{
    
    public int getDni(){ return getInteger("dni_Persona");}
    public void setDni(int dninew){ set("dni_Persona", dninew);}

    public int getIdTarea(){ return getInteger("id_Tarea");}
    public void setIdTarea(int idnew){ set("id_Tarea", idnew);}

    public int getHoraInicio(){ return getInteger("hora_inicio");}
    public void setHoraInicio(int horaInicio){ set("hora_inicio", horaInicio);}

    public int getHoraFin(){ return getInteger("hora_fin");}
    public void setHoraFin(int horaFin){ set("hora_fin", horaFin);}

    // === EL ÚNICO CAMBIO REAL QUE LE HACÍA FALTA A TU MODELO ===
    public String getTarea(){ return getString("tarea");} // <-- Cambiado de 'int' a 'String' y de 'getInteger' a 'getString'
    public void setTarea(String tarea){ set("tarea", tarea);}

    public String getMes(){ return getString("mes");}
    public void setMes(String mes){ set("mes", mes);}

    public int getAnio(){ return getInteger("anio");}
    public void setAnio(int anio){ set("anio", anio);}

    public int getDia(){ return getInteger("dia");}
    public void setDia(int dia){ set("dia", dia);}
}
