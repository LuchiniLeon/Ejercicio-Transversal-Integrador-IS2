**Departamento de Computación** 
**Asignatura: Ingeniería de Software II** 
**Año 2026**
**Integrantes:**
# Sistema de Gestión Estudiantil

## Descripción del problema
Desarrollo de un sistema de gestión estudiantil para instituciones universitarias.
El sistema permitirá administrar información relacionada con estudiantes, profesores y materias, facilitando la organización, comunicación y acceso a los datos según el rol.
El sistema se basa en un código inicial proporcionado por la materia, el cual incluye funcionalidades básicas como el inicio de sesión y la gestión de usuarios. A partir de esta base se agregarán nuevas funcionalidades.

## El problema que se quiere resolver
La desorganización y la lentitud para acceder a información importante. Y la seguridad de los datos, asegurando que cada usuario acceda solo a las datos/información que le corresponda.

## Usuarios del sistema
- Docentes (Profesores)
- Estudiantes (Aún no implementados)
- Administrativos (esAdministrador)

## Funcionalidades principales:

#### Proyecto hasta ahora.
- Crear usuarios normales y administradores
- Crear usuarios de profesores desde un usuario administrador
- Visualizar los datos del usuario profesor en la sección de “Ver perfil”.

### Funcionalidades para administradores
- Crear usuarios de profesores.
- Crear materias.
- Asignar profesores a materias.
- Gestión de usuarios(eliminar usuarios, modificar rol, ver lista de usuarios)

### Funcionalidades para estudiantes
- Inscribirse en materias.
- Inscribirse a cursos.
- Ver cursos disponibles.
- Visualizar Materias.
- Visualizar notas.
- Visualizar y editar los datos del usuario estudiantes en la sección de “Ver perfil”.

### Funcionalidades para profesores
- Visualizar estudiantes del curso/materias.
- Cargar notas.
- Crear cursos.
- Editar perfil.

### Otras funcionalidades
- Diferenciación de campos fijos y editables en perfiles según el rol.
- Control de roles y permisos.
- Personalización de interfaz: colores, fondos, nombre, logo de universidad.

### No funcionales
- Todas las contraseñas deben estar encriptadas usando BCrypt.
- Acceso a datos según roles.
- Doble verificación al crear usuarios y contraseñas.
- Recuperación de contraseña.

## Restricciones técnicas
- **Base de datos:** Sqlite (Limitación para expansión del sistema y no está pensado para mantener un gran volumen de usuarios)
- **Seguridad:** Uso de BCrypt para encriptado de contraseñas (Seguridad a cambio de lentitud y mal rendimiento en sistemas con muchas solicitudes simultáneas)
- **Vistas:** Uso de Mustache como motor de plantillas para la generación de vistas. (La estructura de vista debe adaptarse lo que ofrece el motor de plantilla)
El desarrollo debe integrarse con la estructura existente del proyecto.

## Tamaño del equipo: 5

## Tecnologias utilizadas y justificación
- **Maven:**
Para la gestión de dependencias y la organización del proyecto.
- **Sqlite (3):**
Como sistema de base de datos para almacenar la información del sistema. Elegida por su facilidad de uso, configuración sencilla y porque no requiere un servidor de base de datos.
- **ActiveJDBC:**
Como framework ORM para facilitar la interacción entre la aplicación y la base de datos.
- **Mustache:**
Motor de plantillas para la generación de las vistas del sistema web. Permite separar la lógica de la aplicación de la interfaz de usuario.
- **Spark Java:**
Como framework web para desarrollar la aplicación. Facilita la creación de rutas, controladores y manejo de peticiones HTTP de forma sencilla.
- **JUnit:**
Realiza pruebas unitarias del sistema.

## Plazo estimado: 
12 semanas

## Cambios de alcance ocurridos
### Inicial
En base a la estructura inicial que nos dieron creamos usuario administrador para que genere los usuarios Profesores para aportar seguridad al sistema y no cualquier individuo se registre como un profesor de la institución. A demás, se implemento la visualización de los datos del usuario profesor en la sección de “Ver perfil”.
### Evolución
También se ampliaron funcionalidades del sistema agregando:
- Incorporación del rol de estudiante dentro del sistema.
- Funcionalidades de inscripción a materias y cursos.
- Asignación de profesores a materias.
- Gestión de materias/cursos.
- Mejora en control de roles y seguridad.
- Interfaz.

## Problemas encontrados:
- Inseguridad al ingresar la contraseña al crear nuevos usuarios (Agregar doble verificación)
- Inseguridad al ingresar cargos docentes (Agregar opciones con los cargos posibles)
- Cambio brusco de páginas (flash)
- No hay usuarios Estudiantes.
- No hay control de roles completo.
- Si un usuario no es profesor no muestra sus datos en la sección “Ver perfil”

## Forma de organización del equipo: 
Se adopto la metodología ágil SCRUM, uso de GIT y GITHUB para el control de versiones del proyecto, Comunicación entre los integrantes del grupo mediante reuniones presenciales y virtuales. Coordinación del avance del proyecto mediante la división de tareas dentro del equipo.

---
# Riesgos identificados por el equipo
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

---
# Riesgos identificados por el equipo
| Tipo de Riesgo     | Descripción                          | Probabilidad | Impacto | Identificado por|
|--------------------|--------------------------------------|--------------|---------|-----|
|Técnico|Curva de aprendizaje en tecnologías utilizadas (Spark Java, ActiveJDBC)|Media|Alto|IA|
|Técnico|Limitaciones de SQLite en escenarios de mayor concurrencia|Media|Alto|IA|
|Técnico|Posibles vulnerabilidades en el manejo de sesiones y autenticación|Media|Crítico|IA|
|Técnico|Falta de validación de datos de entrada (inputs del usuario)|Alta|Alto|IA|
|Técnico|Modelo de dominio incompleto (ausencia de entidad Estudiante)|Alta|Medio|IA|
|Técnico|Acoplamiento entre lógica de negocio y presentación|Media|Medio|IA|
|Organizacional|Falta de definición formal de roles dentro del equipo|Media|Medio|IA|
|Organizacional|Comunicación informal puede generar desalineación|Media|Medio|IA|
|Planificación|Subestimación del esfuerzo necesario para funcionalidades futuras|Alta|Crítico|IA|
|Planificación|Requerimientos incompletos o poco definidos (ej: gestión de materias)|Alta|Alto|IA|
|Planificación|Cambios de alcance sin control formal|Media|Alto|IA|
|Planificación|Falta de backlog estructurado y priorización clara|Media|Alto|IA|
|Humano|Falta de experiencia en desarrollo web o backend|Media|Alto|IA|
|Humano|Sobrecarga de trabajo en alguno de los integrantes|Alta|Alto|IA|
|Humano|Posible desmotivación durante el desarrollo del proyecto|Media|Alto|IA|
|Humano|Errores en la implementación de seguridad (contraseñas, sesiones en el uso del ByCript)|Media|Crítico|IA|

### Conclusión
En resumen, los análisis terminan siendo bastante complementarios. La IA detectó riesgos mucho más técnicos y de fondo, como las limitaciones de SQLite de cara a la escalabilidad, la falta de la entidad Estudiante en el código y los problemas de seguridad en las sesiones; básicamente deducciones directas al analizar nuestro programa.

Pero ambos análisis coincidieron en puntos clave como la dificultad de la curva de aprendizaje de las herramientas nuevas. Sin embargo, el equipo aporto un riesgo que la IA pasó por alto: los problemas y conflictos reales con el uso de Git en el día a día.

La debilidad principal de nuestro análisis como equipo no es la cantidad de riesgos encontrados, sino la falta de profundidad, cosa que la ia con facilidad logro capturar ;)



## Diagrama UML

```mermaid
classDiagram
    %% Notas
    note for Titulo
        La inclusion de la clase Titulo fue debido a que puede implicar una funcionalidad util,
        por ejemplo, asignar profesores a materias segun sus estudios previos
    end note

    note for Usuario
        Consideramos que hay ciertos casos en los que una persona puede tener mas de un usuario
        para utilizar distintas funciones
    end note

    %% Clases Principales
    class Persona {
        -Nombre : String
        -Apellido : String
        -Mail : String
        -Fecha_Nac : int
        -DNI : int
    }
    
    class Usuario {
        -Es_Administrador : boolean
        -Nombre_Usuario : String
        -Clave_Usuario : String
    }
    
    class Titulo {
        -Nombre_Titulo : String
    }
    
    class Docente {
        -Anio_Ingreso : int
        -Cargo : tipo_cargo
    }
    
    class Estudiante {
        -Anio_Ingreso : int
    }
    
    class Materia {
        -Nombre : String
        -Cod_Mat : int
        -Cant_Inscriptos : int
        -Descripcion : String
    }
    
    class Carrera {
        -Nombre : String
        -Cod_Car : int
        -Cant_Inscriptos : int
    }
    
    class PlanDeEstudio {
        -Nombre : String
        -Cod_Plan : int
    }
    
    class Correlatividad {
        -Cod_Mat_Requisito : int
        -Cod_Mat_Bloqueada : int
    }

    %% Enumeraciones
    class tipo_estado {
        <<enumeration>>
        Libre
        Regular
        Promocion
        Aprobado
        Cursando
    }
    
    class tipo_estudiante {
        <<enumeration>>
        Ingresante
        Avanzado
    }
    
    class tipo_cargo {
        <<enumeration>>
        JefeTP
        AyudantePrimera
        AyudanteSegunda
    }

    %% Clases de Asociación
    class Cursa {
        -Estado : tipo_estado
        -Nota_Aprobacion : int
    }
    
    class Participa {
        -Fecha_inicio : int
        -Fecha_fin : int
    }
    
    class Estudia {
        -Estado : tipo_estudiante
    }

    %% Herencia
    Persona <|-- Docente
    Persona <|-- Estudiante

    %% Relaciones Simples
    Persona "1" -- "*" Usuario
    Titulo "0..*" -- "*" Docente : Posee
    Docente "1" -- "0..1" Materia : Responsable
    Materia "*" -- "1..*" Carrera
    Carrera "1" -- "*" PlanDeEstudio
    
    %% Correlatividades (mejor modeladas)
    Correlatividad "*" --> "1" Materia : Requiere
    Correlatividad "*" --> "1" Materia : Bloquea
    PlanDeEstudio "1" -- "*" Correlatividad

    %% Relaciones de las Clases de Asociación
    Docente "*" -- "*" Materia : Participa
    Participa .. Docente
    Participa .. Materia

    Estudiante "*" -- "*" Materia : Cursa
    Cursa .. Estudiante
    Cursa .. Materia

    Estudiante "*" -- "*" Carrera : Estudia
    Estudia .. Estudiante
    Estudia .. Carrera

