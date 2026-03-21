# Proyecto Integrador: Especificación, Gestión y planificación

## (Requirements) Describir su proyecto:

- **Problema que se quiere resolver:**
Se requiere crear un sistema de gestión que facilite la comunicación y asignación de materias dentro de un sistema educativo universitario con sus respectivos roles.
Estado del sistema: Versión inicial con gestión de usuarios y profesores.

- **Usuarios del sistema:**
	- Docentes (Profesores)
	- Estudiantes (Aún no implementados)
	- Administrativos (esAdministrador)

- **Funcionalidades principales:**
	- Crear usuarios normales y administradores
	- Crear usuarios de profesores desde un usuario administrador
	- Visualizar los datos del usuario profesor en la sección de “Ver perfil”

- **Restricciones técnicas:**
	- Sqlite (Limitación para expansión del sistema y no está pensado para mantener un gran volumen de usuarios)
	- Uso de BCrypt para encriptado de contraseñas (Seguridad a cambio de lentitud y mal rendimiento en sistemas con muchas solicitudes simultáneas)

- **Tamaño del equipo:**
2

- **Tecnologías utilizadas y justificación:**
	- Sqlite (3)
	- ActiveJDBC
	- Mustache
	- Spark Java
	- JUnit


- **Plazo estimado:**
3 meses

- **Cambios de alcance ocurridos:**
En base a la estructura inicial que nos dieron creamos usuario administrador para que genere los usuarios Profesores para aportar seguridad al sistema y no cualquier individuo se registre como un profesor de la institución.
Visualizar los datos del usuario profesor en la sección de “Ver perfil”

- **Problemas encontrados:**
	- Inseguridad al ingresar la contraseña al crear nuevos usuarios (Agregar doble verificación)
	- Inseguridad al ingresar cargos docentes (Agregar opciones con los cargos posibles)
	- Cambio brusco de páginas (flash)
	- No hay usuarios Estudiantes
	- Es posible crear usuarios genéricos
	- Si un usuario no es profesor no muestra sus datos en la sección “Ver perfil”

- **Forma de organización del equipo:**
Metodología ágil SCRUM, uso de GIT y GITHUB con ramas individuales, comunicación presencial y por Meet.

