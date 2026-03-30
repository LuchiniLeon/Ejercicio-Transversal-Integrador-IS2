| Tipo de Riesgo     | Descripción                                         | Probabilidad | Impacto | Identificado por   |
| :-------------	 | :-------------------------------------------------- | :----------- | :------ | :-------------   	 |
| **Técnico**		 | Limitación de las tecnologías usadas.			   | Alta 		  | Medio   | - Equipo			 |
| **Técnico**		 | Falla en la base de datos						   | Medio		  | Alto 	| - Equipo			 |
| **Organizacional** | Mala distribución de tareas.            			   | Medio        | Medio   | - Equipo           |
| **Organizacional** | Problemas con uso del Git.          			       | Alta         | Medio   | - Equipo           |
| **Organizacional** | Mala comunicación del equipo.     			       | Medio        | Alto    | - Equipo           |
| **Planificación**  | Las tareas no están claramente definidas.		   | Medio        | Alto    | - Equipo           |
| **Humano**         | Dificultad para aprender tecnologías nuevas.        | Medio        | Medio   | - Equipo           |
| **Humano**         | Baja de un participante del equipo.				   | Baja         | Alto    | - Equipo           |

**PROMPT**
Actúa como un auditor de proyectos de software con experiencia en Ingeniería de Software.

A partir de la siguiente descripción del proyecto, identifica riesgos relevantes y clasifícalos en las siguientes categorías:
- Riesgos técnicos
- Riesgos organizacionales
- Riesgos de planificación
- Riesgos humanos

Para cada riesgo debes:
- Dar una descripción del riesgo.
- Asignar una probabilidad: Baja | Media | Alta
- Asignar un impacto: Bajo | Medio | Alto | Crítico

Presenta el resultado en formato tabla con las siguientes columnas:
- Tipo de Riesgo
- Descripción
- Probabilidad
- Impacto

Condiciones:
- Generar entre 6 y 10 riesgos relevantes.
- Evitar riesgos genéricos o repetidos.
- Basarte únicamente en la información del proyecto.


# Auditoría de riesgos — Sistema de Gestión Estudiantil

**GEMINI**

# Informe de Auditoría de Riesgos: Sistema de Gestión Estudiantil

## 1. Descripción del Problema
Desarrollo de un sistema de gestión estudiantil para instituciones universitarias orientado a administrar información de estudiantes, profesores y materias. El sistema busca resolver la desorganización, la lentitud en el acceso a la información y garantizar la seguridad de los datos mediante un control de acceso basado en roles. El desarrollo parte de una base de código preexistente con funcionalidades de inicio de sesión y gestión de usuarios.

---

## 2. Análisis de Riesgos Detallado

### A. Riesgos Técnicos

| Riesgo | Descripción | Probabilidad | Impacto |
| :--- | :--- | :--- | :--- |
| **Herencia de Deuda Técnica** | El código base proporcionado podría contener errores latentes, falta de escalabilidad o prácticas de programación obsoletas. | **Alta** | **Alto** |
| **Fallas en el Control de Acceso (RBAC)** | Errores en la implementación de permisos que permitan a un usuario acceder a datos sensibles que no corresponden a su rol. | **Media** | **Crítico** |
| **Incompatibilidad de Integración** | Conflictos técnicos al intentar acoplar las nuevas funcionalidades de gestión con el módulo de usuarios heredado. | **Media** | **Alto** |

### B. Riesgos Organizacionales

| Riesgo | Descripción | Probabilidad | Impacto |
| :--- | :--- | :--- | :--- |
| **Ambigüedad de Requerimientos** | Definiciones imprecisas sobre qué información es "prioritaria", resultando en una herramienta que no satisface las necesidades reales. | **Alta** | **Alto** |
| **Incumplimiento Normativo** | No adecuarse a las leyes vigentes de protección de datos personales (GDPR o leyes locales de educación). | **Baja** | **Crítico** |

### C. Riesgos de Planificación

| Riesgo | Descripción | Probabilidad | Impacto |
| :--- | :--- | :--- | :--- |
| **Subestimación del Esfuerzo** | Asumir que el código inicial reduce drásticamente el trabajo, ignorando el tiempo necesario para auditoría y refactorización. | **Muy Alta** | **Medio** |
| **Corrimiento del Alcance** | Incorporar funciones secundarias (comunicación/reportes) antes de consolidar el núcleo de gestión y seguridad. | **Media** | **Alto** |

### D. Riesgos Humanos

| Riesgo | Descripción | Probabilidad | Impacto |
| :--- | :--- | :--- | :--- |
| **Resistencia al Cambio** | Rechazo de la plataforma por parte de docentes o administrativos acostumbrados a sistemas tradicionales (papel o Excel). | **Media** | **Alto** |
| **Brecha de Conocimiento** | Tiempo invertido por el equipo en comprender la lógica del código fuente ajeno antes de poder producir código nuevo. | **Media** | **Medio** |

---

Tipo de Riesgo,Descripción,Probabilidad,Impacto,Identificado por
Técnico,Limitación de las tecnologías usadas.,Alta,Medio,Equipo
Técnico,Falla en la base de datos (Estructura/Conexión).,Medio,Alto,Equipo
Técnico,Deuda Técnica: Errores ocultos en el código inicial.,Alta,Alto,IA
Técnico,Brecha de Seguridad: Acceso no autorizado a datos.,Media,Crítico,IA
Organizacional,Mala distribución de tareas.,Medio,Medio,Equipo
Organizacional,Problemas con el uso de Git (Conflictos de código).,Alta,Medio,Equipo
Organizacional,Mala comunicación interna del equipo.,Medio,Alto,Equipo
Organizacional,Vacío Legal: Incumplimiento de leyes de privacidad.,Baja,Crítico,IA
Planificación,Tareas no definidas claramente.,Medio,Alto,Equipo
Planificación,Subestimación: Retraso por refactorizar código ajeno.,Muy Alta,Medio,IA
Humano,Dificultad para aprender tecnologías nuevas.,Medio,Medio,Equipo
Humano,Baja de un participante del equipo (Deserción).,Baja,Alto,Equipo
Humano,Resistencia al Cambio: Usuarios no usan el sistema.,Media,Alto,IA


**Comparación del análisis de riesgos**
##Riesgos que encontró la IA y el equipo no
Riesgos relacionados con errores en autenticación y control de acceso, que podrían generar accesos indebidos al sistema.
Riesgos de inconsistencia en los datos, que podrían afectar la información de materias, usuarios o notas.
Riesgos en la validación de datos ingresados, lo que podría provocar registros inválidos.
Riesgos de falta de documentación del sistema, dificultando el mantenimiento.
Riesgos de cambios en los requerimientos, generando retrabajo o retrasos.
Riesgos de acumulación de tareas al final del proyecto, afectando la calidad de la entrega.
##Riesgos que encontró el equipo y la IA no
Limitaciones de las tecnologías utilizadas, especialmente SQLite y su impacto en la escalabilidad.
Fallas en la base de datos, considerando pérdida de información o mal funcionamiento.
Problemas en el uso de Git, que pueden generar conflictos o pérdida de código.
Mala distribución de tareas dentro del equipo, afectando la organización del trabajo.


## Conclusión
En resumen, los análisis son complementarios más que contradictorios. La IA detectó más riesgos de fondo —escalabilidad, seguridad de roles, deuda técnica, cobertura de pruebas— porque los infirió del documento técnico. El equipo, en cambio, capturó dos riesgos que la IA no vio: los problemas reales con Git y la curva de aprendizaje de las tecnologías, que son riesgos vividos, no deducidos.
La debilidad principal del análisis del equipo no es la cantidad sino la profundidad: riesgos como "falla en la base de datos" o "mala comunicación" son válidos pero demasiado genéricos para ser accionables. Un buen análisis de riesgos debería responder no solo qué puede fallar, sino por qué y cómo se mitiga en este proyecto concreto.
