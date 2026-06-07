package com.is1.proyecto.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.javalite.activejdbc.Base;

import com.is1.proyecto.models.Admin;
import com.is1.proyecto.models.Docente;
import com.is1.proyecto.models.Estudia;
import com.is1.proyecto.models.ParticipaDocenteTaller;
import com.is1.proyecto.models.Persona;
import com.is1.proyecto.models.Taller;


public class AdminService {
    
    public static void crearAdmin(Integer dni_Persona, String cargo, String sector)
    {
        if(dni_Persona == null || dni_Persona == 0) throw new IllegalArgumentException("Faltan campos obligatorios");

        //Esto no deberia ir ya que se supone que se asugna a un usuario ya existente, pero no sé si rompe algo harcodeado
        //if (Persona.findFirst("dni = ?", dni_Persona) != null) throw new IllegalStateException("El DNI ya existe");
        if (Persona.findFirst("dni = ?", dni_Persona) == null) {
            throw new IllegalStateException("El DNI no existe");
        }

             //Abre la transacción acá
        Base.openTransaction();

        try{
            //CREACION DE ADMINISTRADOR
            Admin admin = new Admin();
            admin.setDni(dni_Persona);
            admin.setCargo(cargo);
            admin.setSector(sector);
            admin.insert();

            //Si todo salió bien dentro del try, confirma para que pase a la bd
            Base.commitTransaction(); 
        }catch (Exception e){
            //Si falló algo en el try, deshacemos todo para no dejar nada a medias en la bd
            Base.rollbackTransaction();
            throw e;
        }
    }

    public static List<Map<String, Object>> obtenerDocentes() {
        List<Docente> docentes = Docente.findAll();
        List<Map<String, Object>> resultado = new ArrayList<>();

        for (Docente d : docentes) {

            Persona persona = d.parent(Persona.class);
            Map<String, Object> map = new HashMap<>();

            map.put("dni", d.getInteger("dni_Persona"));
            map.put("legajo", d.getInteger("legajo"));
            map.put("cargo", d.getString("cargo"));
            map.put("nombre", persona.getString("nombre"));
            map.put("apellido", persona.getString("apellido"));
            map.put("fechaNacimiento", persona.getString("fecha_Nacimiento"));
            
            resultado.add(map);
        }
        return resultado;
    }
//TALLERES
    public static void crearTallerComoAdmin(String titulo, Integer hora, Boolean vigente, Integer dniDocente) {
        if (Docente.findFirst("dni_Persona = ?", dniDocente) == null) {
            throw new IllegalArgumentException("El docente asignado no existe");
        }
        TallerService.crearTaller(titulo, hora, vigente, dniDocente);
    }   

    public static List<Map<String, Object>> obtenerTalleres() {
        List<Taller> talleres = Taller.findAll();
        List<Map<String, Object>> resultado = new ArrayList<>();

        for (Taller t : talleres) {
           
            Docente docente = t.parent(Docente.class);
            Persona persona = docente.parent(Persona.class);
            Map<String, Object> map = new HashMap<>();
            map.put("idTaller", t.getInteger("id_Taller"));
            map.put("titulo", t.getString("titulo"));
            map.put("hora", t.getInteger("hora"));
            map.put("vigente", t.getBoolean("vigente") ? "Sí" : "No");
            map.put("nombreDocente", persona.getString("nombre") + " " + persona.getString("apellido"));
            
             // Participantes
            List<ParticipaDocenteTaller> participantes = ParticipaDocenteTaller.where("id_Taller = ?", t.getInteger("id_Taller"));
            List<String> nombresParticipantes = new ArrayList<>();
            
            for (ParticipaDocenteTaller p : participantes) {
                Docente docenteP = Docente.findFirst("dni_Persona = ?", p.getDniDocente());
                Persona personaP = docenteP.parent(Persona.class);
            
                nombresParticipantes.add(personaP.getString("nombre") + " " + personaP.getString("apellido"));
            }
            map.put("participantes", nombresParticipantes.isEmpty() ? "—" : String.join(", ", nombresParticipantes));

            long cantAlumnos = Estudia.count("id_taller = ?", t.getInteger("id_Taller"));
            map.put("cantAlumnos", cantAlumnos);
            
            resultado.add(map);
        }

        return resultado;
    }

    public static void asignarDocenteATaller(Integer idTaller, Integer dniDocente) {
       
        if (idTaller == null) throw new IllegalArgumentException("Debe seleccionar un taller");
        if (dniDocente == null) throw new IllegalArgumentException("Debe seleccionar un docente");

        Taller taller = Taller.findFirst("id_Taller = ?", idTaller);
        if (taller == null) throw new IllegalArgumentException("El taller no existe");
        if (taller.getInteger("dni_Docente").equals(dniDocente))
            throw new IllegalArgumentException("El docente ya es el titular de este taller");

        if (Docente.findFirst("dni_Persona = ?", dniDocente) == null)
            throw new IllegalArgumentException("El docente no existe");

        if (ParticipaDocenteTaller.findFirst("id_Taller = ? AND dni_Docente = ?", idTaller, dniDocente) != null)
            throw new IllegalArgumentException("El docente ya está asignado a este taller");

        ParticipaDocenteTaller p = new ParticipaDocenteTaller();

        p.setIdTaller(idTaller);
        p.setDniDocente(dniDocente);

        if (!p.save()){
            throw new IllegalArgumentException("No se pudo guardar la asignación");
        } 
    }

    //MATERIAS
    public static void crearMateriaComoAdmin(Integer codigo,String nombre,Integer horasTotales,Integer dniAdministrador,Integer dniDocente) {
        if (Docente.findFirst("dni_Persona = ?", dniDocente) == null) {
            throw new IllegalArgumentException("El docente asignado no existe");
        }

        MateriaService.crearMateria(codigo,nombre,horasTotales,dniAdministrador,dniDocente);

    }

    public static List<Map<String, Object>> obtenerMaterias() {
        return MateriaService.listarMaterias();
    }

    public static void asignarDocenteAMateria(Integer idMateria,Integer dniDocente) {
        if (idMateria == null) {
         throw new IllegalArgumentException("Debe seleccionar una materia");
        }

        if (dniDocente == null) {
            throw new IllegalArgumentException("Debe seleccionar un docente");
        }

        MateriaService.asignarDocenteAMateria(idMateria,dniDocente);
    }
}
