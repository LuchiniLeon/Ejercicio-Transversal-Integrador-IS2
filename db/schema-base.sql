PRAGMA foreign_keys = ON;

DROP TABLE IF EXISTS persona;

CREATE TABLE IF NOT EXISTS persona (
    dni INTEGER,
    nombre TEXT NOT NULL,
    apellido TEXT NOT NULL,
    fecha_Nacimiento TEXT NOT NULL, -- No existe en sqlite las fechas, por lo que se deben ingresar así "YYYY-MM-DD"
    edad INTEGER GENERATED ALWAYS AS (
        (strftime('%Y', 'now') - strftime('%Y', fecha_Nacimiento)) -
        (strftime('%m-%d', 'now') < strftime('%m-%d', fecha_Nacimiento))
    ) VIRTUAL,
    CONSTRAINT pk_dni_persona PRIMARY KEY (dni)
);

DROP TABLE IF EXISTS usuario;

CREATE TABLE IF NOT EXISTS usuario (
    nombreUsuario TEXT NOT  NULL,
    contraseña TEXT NOT NULL,
    dni_Persona INTEGER,
    CONSTRAINT pk_nombre_usuario PRIMARY KEY (nombreUsuario),
    CONSTRAINT fk_dni_persona_usuario FOREIGN KEY (dni_Persona) REFERENCES persona(dni) ON DELETE CASCADE
);

DROP TABLE IF EXISTS administrador;

CREATE TABLE IF NOT EXISTS administrador (
    dni_Persona INTEGER, 
    cargo TEXT NOT NULL,
    sector TEXT NOT NULL,

    CONSTRAINT pk_dni_persona_administrador PRIMARY KEY (dni_Persona),
    CONSTRAINT fk_dni_persona_administrador FOREIGN KEY (dni_Persona) REFERENCES persona(dni) ON DELETE CASCADE
);