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
Docentes (Profesores)
Estudiantes (Aún no implementados)
Administrativos (esAdministrador)

## Funcionalidades principales:

//Proyecto hasta ahora.
Crear usuarios normales y administradores
Crear usuarios de profesores desde un usuario administrador
Visualizar los datos del usuario profesor en la sección de “Ver perfil”.

### Funcionalidades para administradores
Crear usuarios de profesores.
Crear materias.
Asignar profesores a materias.
Gestión de usuarios(eliminar usuarios, modificar rol, ver lista de usuarios)

### Funcionalidades para estudiantes
Inscribirse en materias.
Inscribirse a cursos.
Ver cursos disponibles.
Visualizar Materias.
Visualizar notas.
Visualizar y editar los datos del usuario estudiantes en la sección de “Ver perfil”.

### Funcionalidades para profesores
Visualizar estudiantes del curso/materias.
Cargar notas.
Crear cursos.
Editar perfil.

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












