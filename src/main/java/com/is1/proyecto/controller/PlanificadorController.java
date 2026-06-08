package com.is1.proyecto.controller;

import spark.Request;
import spark.Response;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.DayOfWeek;
import java.util.List;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Optional;
import spark.ModelAndView;
import com.is1.proyecto.models.PlanificadorTarea;
import com.is1.proyecto.models.User;
import com.is1.proyecto.service.PlanificadorService;

public class PlanificadorController {
    
    public static ModelAndView tarea(Request req, Response res) {
        Map<String, Object> model = new HashMap<>();

        String username = req.session().attribute("currentUserUsername");
        User user = User.findFirst("nombreUsuario = ?", username);

        // 1. LEER LOS PARÁMETROS DE LAS FLECHITAS DESDE LA URL (si no están, usamos el año y mes actuales)
        String paramMes = req.queryParams("mes");
        String paramAnio = req.queryParams("anio");

        int numeroMesActual;
        int anioActual;

        if (paramMes != null && paramAnio != null) {
            numeroMesActual = Integer.parseInt(paramMes);
            anioActual = Integer.parseInt(paramAnio);
        } else {
            LocalDate hoy = LocalDate.now();
            numeroMesActual = hoy.getMonthValue();
            anioActual = hoy.getYear();
        }

        // 2. CREAR EL OBJETO YearMonth DINÁMICO SEGÚN LA NAVEGACIÓN
        YearMonth mesActual = YearMonth.of(anioActual, numeroMesActual);

        // Guardamos en la sesión lo que estamos viendo actualmente
        req.session().attribute("anioActualSesion", String.valueOf(anioActual));
        req.session().attribute("mesActualSesion", mesActual.getMonth().toString());

        // 3. CALCULAR LOS MESES ANTERIOR Y SIGUIENTE PARA LAS FLECHITAS
        YearMonth mesAnterior = mesActual.minusMonths(1);
        YearMonth mesSiguiente = mesActual.plusMonths(1);

        // Pasamos los datos de las flechas al modelo de Mustache
        model.put("mesAnt", mesAnterior.getMonthValue());
        model.put("anioAnt", mesAnterior.getYear());
        model.put("mesSig", mesSiguiente.getMonthValue());
        model.put("anioSig", mesSiguiente.getYear());

        // 4. LÓGICA DE LA GRILLA DEL CALENDARIO (Se mantiene tu lógica pero con las variables dinámicas)
        int cantDia = mesActual.lengthOfMonth();
        LocalDate primerDiaMes = mesActual.atDay(1);
        DayOfWeek diaInicialSem = primerDiaMes.getDayOfWeek();
        
        // El -1 que tenías para acomodar el inicio de la semana
        int valorSem = diaInicialSem.getValue() - 1; 

        List<Map<String, Object>> cal = new LinkedList<>();
        // Buscamos las tareas en la BD usando el mes y año que se están navegando
        // Usamos el nombre del mes en lugar del número
        String nombreMes = mesActual.getMonth().toString(); 
        List<PlanificadorTarea> tareasMes = PlanificadorTarea.where("dni_Persona = ? AND mes = ?", user.getDNI(), nombreMes);

        for (int i = 1; i <= cantDia; i++) {
            Map<String, Object> diaMap = new HashMap<>();
            diaMap.put("numeroDia", i);
            
            List<Map<String, String>> listaTareasMustache = new LinkedList<>();
            for (PlanificadorTarea t : tareasMes) {
                if (t.getInteger("dia") == i) { 
                    Map<String, String> m = new HashMap<>();
                    // USAMOS EL MÉTODO getTarea() QUE SABEMOS QUE FUNCIONA
                    m.put("descripcionTarea", t.getTarea()); 
                    listaTareasMustache.add(m);
                }
            }
            diaMap.put("listaTareas", listaTareasMustache); 
            diaMap.put("tieneTareas", !listaTareasMustache.isEmpty());
            
            cal.add(diaMap);
        }
        List<String> espacioBlanco = new LinkedList<>();
        for (int i = 0; i <= valorSem; i++) { // Corregido a < para que coincida exactamente con los espacios
            espacioBlanco.add("");
        }

        // 5. CARGAR LAS VARIABLES EN EL MODELO PARA MUSTACHE
        model.put("diaMes", cal); 
        model.put("espacio", espacioBlanco);
        model.put("nombreMes", mesActual.getMonth().toString()); // Nombre del mes dinámico
        model.put("anio", anioActual);                           // Año dinámico
        model.put("valorSem", valorSem);

        return new ModelAndView(model, "planificar-tarea.mustache");
    }

    public static ModelAndView horasDia(Request req, Response res) {
        Map<String, Object> model = new HashMap<>();

        String username = req.session().attribute("currentUserUsername");
        User user = User.findFirst("nombreUsuario = ?", username);

        String dia = req.queryParams("dia");
        String form = req.queryParams("accion");
        String hora = req.queryParams("hora_sel");

        String anio = req.session().attribute("anioActualSesion");
        String mes = req.session().attribute("mesActualSesion");

        if (anio == null) anio = String.valueOf(LocalDate.now().getYear());
        if (mes == null) mes = LocalDate.now().getMonth().toString();

        // GENERAR LAS HORAS (Esto se ejecuta SIEMPRE, haya form o no)
        List<Map<String, Object>> horas = new LinkedList<>();
        List<PlanificadorTarea> tareasDia = PlanificadorTarea.where("dni_Persona = ? AND dia = ?", user.getDNI(), dia);
        
        int i = 0;
        while (i < 24) {
            String sufijo = (i < 12) ? "am" : "pm";
            int hora12 = (i == 0 || i == 12) ? 12 : (i > 12 ? i - 12 : i);
            String horaActualTexto = String.format("%02d:00 %s", hora12, sufijo);

            Map<String, Object> horaMap = new HashMap<>();
            horaMap.put("horaTexto", horaActualTexto);
            horaMap.put("horaMilitar", i); 
            horaMap.put("dia", dia); // Mantenemos el link atado al día actual

            final int horaActualInt = i;
            Optional<PlanificadorTarea> tareaEncontrada = tareasDia.stream()
                .filter(t -> horaActualInt >= t.getHoraInicio() && horaActualInt <= t.getHoraFin())
                .findFirst();

            if (tareaEncontrada.isPresent()) {
                horaMap.put("tieneTarea", true);
                horaMap.put("descripcionTarea", tareaEncontrada.get().getTarea());
            } else {
                horaMap.put("tieneTarea", false);
            }

            horas.add(horaMap);
            i++;
        }

        // SI SE SOLICITÓ EL FORMULARIO, PREPARAMOS LOS FLAGS
        if ("nuevo".equals(form)) {
            model.put("form", true);
        }
        if (hora != null) {
            model.put("horaFormulario", hora);
        }

        // INYECTAMOS TODO JUNTO AL MODELO
        model.put("dia", dia);
        model.put("mes", mes);
        model.put("nombreMes", mes); // Para el título del header
        model.put("anio", anio); 
        model.put("horas", horas);
        model.put("calendario", true); // Indica al HTML que muestre la sección de horas de fondo

        return new ModelAndView(model, "planificar-tarea.mustache");
    }

    public static ModelAndView save(Request req, Response res){
        Map<String, Object> model = new HashMap<>();

        String username = req.session().attribute("currentUserUsername");
        String hora_inicio = req.queryParams("hora_inicio");
        String hora_fin = req.queryParams("hora_fin");
        String tarea = req.queryParams("tarea");
        String dia = req.queryParams("dia"); 

        // SACAMOS EL AÑO Y EL MES DE LA SESIÓN. Ya nunca más van a venir como ""
        String anio = req.session().attribute("anioActualSesion");
        String mes = req.session().attribute("mesActualSesion");

        if (anio == null) anio = String.valueOf(LocalDate.now().getYear());
        if (mes == null) mes = LocalDate.now().getMonth().toString();

        Integer horaReal_inicio = Integer.parseInt(hora_inicio.replace("am", "").replace("pm", ""));
        Integer horaReal_fin = Integer.parseInt(hora_fin.replace("am", "").replace("pm", ""));

        model.put("username", username);
        model.put("hora_inicio", horaReal_inicio);
        model.put("hora_fin", horaReal_fin);
        model.put("tarea", tarea);
        model.put("mes", mes);
        model.put("anio", Integer.valueOf(anio)); // ¡Solucionado! Pase limpio a entero
        model.put("dia", Integer.valueOf(dia));

        PlanificadorService.save(model);

        res.redirect("/planificador/calendario");
        return null;
    }
}