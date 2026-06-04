package com.is1.proyecto.service;



import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.is1.proyecto.models.Nota;

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

        if (dniEstudiante == null) {
            throw new IllegalArgumentException("Debe seleccionar un estudiante");
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
            n.put("idTaller", nota.getIdTaller());

            lista.add(n);
        }

        return lista;
    }
}

