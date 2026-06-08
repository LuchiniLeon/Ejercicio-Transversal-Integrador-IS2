package com.is1.proyecto.service;


import com.is1.proyecto.models.NotaTaller;
import org.javalite.activejdbc.Base;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.is1.proyecto.models.Nota;
import com.is1.proyecto.models.Taller;
import com.is1.proyecto.models.Materia;

public class NotaService {

    public static void crearNota(String condicion, Integer notaFinal, String fechaExamen,
                                 Integer dniEstudiante, Integer idMateria, Integer idTaller,
                                 Integer dniEstudianteEstudia) {

        if (condicion == null || condicion.trim().isEmpty()) {
            throw new IllegalArgumentException("La condición no puede estar vacía");
        }

        if (!condicion.equals("Libre") && !condicion.equals("Regular") && !condicion.equals("Promocional")) {
            throw new IllegalArgumentException("La condición debe ser Libre, Regular o Promocional");
        }

        if (notaFinal == null || notaFinal < 1 || notaFinal > 10) {
            throw new IllegalArgumentException("La nota final debe estar entre 1 y 10");
        }

        if (fechaExamen == null || fechaExamen.trim().isEmpty()) {
            throw new IllegalArgumentException("La fecha de examen no puede estar vacía");
        }

        try {
            java.time.LocalDate fecha = java.time.LocalDate.parse(fechaExamen);

            if (fecha.getYear() < 1900) {
                throw new IllegalArgumentException("El año debe ser mayor o igual a 1900");
            }

            } catch (java.time.format.DateTimeParseException e) {
                    throw new IllegalArgumentException("La fecha debe tener formato YYYY-MM-DD y ser válida");
            }

        if (dniEstudiante == null) {
            throw new IllegalArgumentException("Debe seleccionar un estudiante");
        } 

        if (dniEstudiante < 1000000 || dniEstudiante > 99999999) {
             throw new IllegalArgumentException("El DNI debe tener entre 7 y 8 números");
        }
           
        if (idMateria != null && idTaller != null) {
             throw new IllegalArgumentException("La nota no puede pertenecer a una materia y a un taller");
        }


        if (idMateria == null && idTaller == null) {
           throw new IllegalArgumentException("Debe seleccionar una materia o un taller");
        }

        if (idTaller != null) {
            Taller taller = Taller.findById(idTaller);

            if (taller == null) {
               throw new IllegalArgumentException("El taller seleccionado no existe");
            }
        }
        
        Nota nota = new Nota();
        nota.setCondicion(condicion.trim());
        nota.setNotaFinal(notaFinal);
        nota.setFechaExamen(fechaExamen.trim());
        nota.setDniEstudiante(dniEstudiante);
        nota.setIdMateria(idMateria);
        nota.setIdTaller(idTaller);
        nota.setDniEstudianteEstudia(dniEstudianteEstudia);

        if (!nota.save()) {
            throw new IllegalArgumentException("No se pudo guardar la nota");
        }
    }

    public static void crearNotaTaller(String condicion, Integer notaFinal, String fechaExamen,
                                     Integer dniEstudiante, Integer idTaller) {

        if (condicion == null || condicion.trim().isEmpty()) {
            throw new IllegalArgumentException("La condición no puede estar vacía");
        }

        if (!condicion.equals("Libre") && !condicion.equals("Regular") && !condicion.equals("Promocional")) {
            throw new IllegalArgumentException("La condición debe ser Libre, Regular o Promocional");
        }

        if (notaFinal == null || notaFinal < 1 || notaFinal > 10) {
            throw new IllegalArgumentException("La nota final debe estar entre 1 y 10");
        }

        if (fechaExamen == null || fechaExamen.trim().isEmpty()) {
            throw new IllegalArgumentException("La fecha de examen no puede estar vacía");
        }

        if (dniEstudiante == null) {
            throw new IllegalArgumentException("Debe ingresar un estudiante");
        }

        if (idTaller == null) {
            throw new IllegalArgumentException("Debe ingresar un taller");
        }

        Base.openTransaction();

        try {
            Nota nota = new Nota();
            nota.setCondicion(condicion.trim());
            nota.setNotaFinal(notaFinal);
            nota.setFechaExamen(fechaExamen.trim());
            nota.setDniEstudiante(dniEstudiante);
            nota.setIdMateria(null);

            if (!nota.save()) {
                throw new IllegalArgumentException("No se pudo guardar la nota");
            }

            NotaTaller notaTaller = new NotaTaller();
            notaTaller.setIdNota(nota.getIdNota());
            notaTaller.setDniEstudiante(dniEstudiante);
            notaTaller.setIdTaller(idTaller);

            if (!notaTaller.save()) {
                throw new IllegalArgumentException("No se pudo guardar la relación nota-taller");
            }

            Base.commitTransaction();

        } catch (Exception e) {
            Base.rollbackTransaction();
            throw e;
        }
    }


 

    public static List<Map<String, Object>> listarNotas() {
        List<Nota> notas = Nota.findAll();
        List<Map<String, Object>> lista = new ArrayList<>();

        for (Nota nota : notas) {
            Map<String, Object> n = new HashMap<>();
            n.put("id", nota.getIdNota());
            n.put("condicion", nota.getCondicion());
            n.put("notaFinal", nota.getNotaFinal());
            n.put("fechaExamen", nota.getFechaExamen());
            n.put("dniEstudiante", nota.getDniEstudiante());
            n.put("idMateria", nota.getIdMateria());
            
            Integer idMateria = nota.getIdMateria();
            if (idMateria != null) {
                Materia materia = Materia.findById(idMateria);
                n.put("nombre", materia != null ? materia.getNombre() : "Materia no encontrada");
            } else {
                NotaTaller notaTaller = NotaTaller.findFirst("id_nota = ?", nota.getIdNota());
                if (notaTaller != null) {
                    Taller taller = Taller.findById(notaTaller.getIdTaller());
                    n.put("nombre", taller != null ? taller.getTitulo() : "Taller no encontrado");
                } else {
                    n.put("nombre", "-");
                }
            }
         
            lista.add(n);
        }

        return lista;
    }

    public static List<Map<String, Object>> listarNotasDeEstudiante(Integer dniEstudiante) {
       List<Map<String, Object>> lista = new ArrayList<>();

       List<Nota> notas = Nota.where("dni_Estudiante = ?", dniEstudiante);

       for (Nota nota : notas) {
            Map<String, Object> n = new HashMap<>();

            n.put("condicion", nota.getCondicion());
            n.put("notaFinal", nota.getNotaFinal());
            n.put("fechaExamen", nota.getFechaExamen());

            Integer idMateria = nota.getIdMateria();

            if (idMateria != null) {
                Materia materia = Materia.findById(idMateria);

                n.put("tipo", "Materia");

                if (materia != null) {
                   n.put("nombre", materia.getNombre());
                } else {
                   n.put("nombre", "Materia no encontrada");
                }

            } else {
                NotaTaller notaTaller = NotaTaller.findFirst(
                    "id_Nota = ? AND dni_Estudiante = ?",
                    nota.getIdNota(),
                    dniEstudiante
                );

                n.put("tipo", "Taller");

                if (notaTaller != null) {
                    Taller taller = Taller.findById(notaTaller.getIdTaller());

                    if (taller != null) {
                        n.put("nombre", taller.getTitulo());
                    } else {
                        n.put("nombre", "Taller no encontrado");
                    }
                } else {
                    n.put("nombre", "Taller no encontrado");
                }
            }

            lista.add(n);
        }

        return lista;
    }
 
    
    public static Map<String, Object> obtenerNota(Integer idNota) {

        Nota nota = Nota.findById(idNota);

        if (nota == null) {
            throw new IllegalArgumentException("Nota no encontrada");
        }

        Map<String, Object> n = new HashMap<>();
        n.put("id", nota.getIdNota());
        n.put("condicion", nota.getCondicion());
        n.put("notaFinal", nota.getNotaFinal());
        n.put("fechaExamen", nota.getFechaExamen());
        n.put("dniEstudiante", nota.getDniEstudiante());
        n.put("idMateria", nota.getIdMateria());
        n.put("idTaller", nota.getIdTaller());
        n.put("dniEstudianteEstudia", nota.getDniEstudianteEstudia());

        return n;
    }

public static void editarNota(Integer idNota, String condicion, Integer notaFinal, String fechaExamen) {

    if (condicion == null || condicion.trim().isEmpty()) {
        throw new IllegalArgumentException("La condición no puede estar vacía");
    }

    if (!condicion.equals("Libre") && !condicion.equals("Regular") && !condicion.equals("Promocional")) {
        throw new IllegalArgumentException("La condición debe ser Libre, Regular o Promocional");
    }

    if (notaFinal == null || notaFinal < 1 || notaFinal > 10) {
        throw new IllegalArgumentException("La nota final debe estar entre 1 y 10");
    }

    if (fechaExamen == null || fechaExamen.trim().isEmpty()) {
        throw new IllegalArgumentException("La fecha de examen no puede estar vacía");
    }

    Nota nota = Nota.findById(idNota);

    if (nota == null) {
        throw new IllegalArgumentException("Nota no encontrada");
    }

    nota.setCondicion(condicion.trim());
    nota.setNotaFinal(notaFinal);
    nota.setFechaExamen(fechaExamen.trim());

    if (!nota.save()) {
        throw new IllegalArgumentException("No se pudo guardar la modificación de la nota");
    }
}

public static void eliminarNota(Integer idNota) {

    Nota nota = Nota.findById(idNota);

    if (nota == null) {
        throw new IllegalArgumentException("Nota no encontrada");
    }

    if (!nota.delete()) {
        throw new IllegalArgumentException("No se pudo eliminar la nota");
    }
}

public static void crearNotaMateria(String condicion, Integer notaFinal, String fechaExamen,
                                    Integer dniEstudiante, Integer idMateria) {

    if (condicion == null || condicion.trim().isEmpty()) {
        throw new IllegalArgumentException("La condición no puede estar vacía");
    }

    if (!condicion.equals("Libre") && !condicion.equals("Regular") && !condicion.equals("Promocional")) {
        throw new IllegalArgumentException("La condición debe ser Libre, Regular o Promocional");
    }

    if (notaFinal == null || notaFinal < 1 || notaFinal > 10) {
        throw new IllegalArgumentException("La nota final debe estar entre 1 y 10");
    }

    if (fechaExamen == null || fechaExamen.trim().isEmpty()) {
        throw new IllegalArgumentException("La fecha de examen no puede estar vacía");
    }

    if (dniEstudiante == null) {
        throw new IllegalArgumentException("Debe ingresar un estudiante");
    }

    if (idMateria == null) {
        throw new IllegalArgumentException("Debe seleccionar una materia");
    }

    Nota nota = new Nota();
    nota.setCondicion(condicion.trim());
    nota.setNotaFinal(notaFinal);
    nota.setFechaExamen(fechaExamen.trim());
    nota.setDniEstudiante(dniEstudiante);
    nota.setIdMateria(idMateria);

    if (!nota.save()) {
        throw new IllegalArgumentException("No se pudo guardar la nota de materia");
    }
}

}

