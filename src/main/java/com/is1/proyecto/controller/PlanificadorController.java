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
    
    public static ModelAndView tarea(Request req, Response res){
        Map<String, Object> model = new HashMap<>();

        String username = req.session().attribute("currentUserUsername");
        User user = User.findFirst("nombreUsuario = ?", username);

        LocalDate hoy = LocalDate.now();
        YearMonth mesActual = YearMonth.now();

        // DEJAMOS EL AÑO Y EL MES ANOTADOS EN LA SESIÓN DEL SERVIDOR
        req.session().attribute("anioActualSesion", String.valueOf(hoy.getYear()));
        req.session().attribute("mesActualSesion", hoy.getMonth().toString());

        int cantDia = mesActual.lengthOfMonth();
        LocalDate primerDiaMes = mesActual.atDay(1);
        DayOfWeek diaInicialSem = primerDiaMes.getDayOfWeek();
        int valorSem = diaInicialSem.getValue() - 1;

        List<Map<String, Object>> cal = new LinkedList<>();
        int numeroMesActual = hoy.getMonthValue(); 
        List<PlanificadorTarea> tareasMes = PlanificadorTarea.where("dni_Persona = ? AND mes = ?", user.getDNI(), numeroMesActual);

        for(int i = 1; i <= cantDia; i++) {
            Map<String, Object> diaMap = new HashMap<>();
            diaMap.put("numeroDia", i);
            final int diaActual = i;
            boolean tieneTareas = tareasMes.stream().anyMatch(t -> t.getInteger("dia") == diaActual);
            diaMap.put("tieneTareas", tieneTareas);
            cal.add(diaMap);
        }

        List<String> espacioBlanco = new LinkedList<>();
        for(int i = 0; i <= valorSem; i++)
            espacioBlanco.add("");

        model.put("diaMes", cal); 
        model.put("espacio", espacioBlanco);
        model.put("nombreMes", hoy.getMonth().toString());
        model.put("anio", hoy.getYear()); 
        model.put("valorSem", valorSem);

        return new ModelAndView(model, "planificar-tarea.mustache");
    }

    public static ModelAndView horasDia(Request req, Response res){
        Map<String, Object> model = new HashMap<>();

        String username = req.session().attribute("currentUserUsername");
        User user = User.findFirst("nombreUsuario = ?", username);

        String dia = req.queryParams("dia");
        String form = req.queryParams("accion");
        String hora = req.queryParams("hora_sel");

        // RESCATAMOS EL AÑO Y EL MES DESDE LA SESIÓN SEGURO
        String anio = req.session().attribute("anioActualSesion");
        String mes = req.session().attribute("mesActualSesion");

        // Salvavidas por si la sesión expiró o se borró de memoria
        if (anio == null) anio = String.valueOf(LocalDate.now().getYear());
        if (mes == null) mes = LocalDate.now().getMonth().toString();

        List<Map<String, Object>> horas = new LinkedList<>();
        List<PlanificadorTarea> tareasDia = PlanificadorTarea.where("dni_Persona = ? AND dia = ?", user.getDNI(), dia);
        
        int i = 1;
        while(i <= 24){
            String sufijo = (i < 12 || i == 24) ? "am" : "pm";
            int hora12 = (i == 12 || i == 24) ? 12 : ((i > 12) ? i - 12 : i);
            String horaActualTexto = hora12 + sufijo;

            Map<String, Object> horaMap = new HashMap<>();
            horaMap.put("horaTexto", horaActualTexto);

            final int horaActualInt = i;
            Optional<PlanificadorTarea> tareaEncontrada = tareasDia.stream()
                .filter(t -> horaActualInt >= t.getHoraInicio() && horaActualInt < t.getHoraFin())
                .findFirst();

            if(tareaEncontrada.isPresent()){
                horaMap.put("tieneTarea", true);
                horaMap.put("descripcionTarea", tareaEncontrada.get().getTarea());
            } else {
                horaMap.put("tieneTarea", false);
            }

            horas.add(horaMap);
            i++;
        }

        if("nuevo".equals(form)) model.put("form", true);
        if(hora != null) model.put("horaFormulario", hora);

        model.put("dia", dia);
        model.put("mes", mes);
        model.put("anio", anio); // Se inyecta al HTML para que la tarjeta lo dibuje arriba
        model.put("horas", horas);
        model.put("calendario", true);
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