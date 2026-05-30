PRAGMA foreign_keys = ON;

DROP TABLE IF EXISTS estudiante;

CREATE TABLE IF NOT EXISTS estudiante (
    dni_Persona INTEGER,
    estado_Academico TEXT NOT NULL,
    ingreso TEXT NOT NULL, -- No existe el tipo date, por lo que se debe escribir así: 'YYYY-MM-DD'
    dni_Administrador INTEGER,
    CONSTRAINT pk_dni_Estudiante PRIMARY KEY (dni_Persona),
    CONSTRAINT fk_dni_persona_Estudiante FOREIGN KEY (dni_Persona) REFERENCES persona(dni) ON UPDATE CASCADE ON DELETE CASCADE,
    CONSTRAINT fk_dni_persona_administrador_Est FOREIGN KEY (dni_Administrador) REFERENCES administrador(dni_Persona) ON UPDATE CASCADE ON DELETE CASCADE
);

DROP TABLE IF EXISTS docente;

CREATE TABLE IF NOT EXISTS docente (
    dni_Persona INTEGER,
    legajo INTEGER NOT NULL,
    cargo TEXT NOT NULL,
    dni_Administrador INTEGER,
    CONSTRAINT pk_dni_docente PRIMARY KEY (dni_Persona),
    CONSTRAINT fk_dni_persona_docente FOREIGN KEY (dni_Persona) REFERENCES persona(dni) ON UPDATE CASCADE ON DELETE CASCADE,
    CONSTRAINT fk_dni_persona_administrador_doc FOREIGN KEY (dni_Administrador) REFERENCES administrador(dni_Persona) ON UPDATE CASCADE ON DELETE CASCADE
);

DROP TABLE IF EXISTS telefono;

CREATE TABLE IF NOT EXISTS telefono (
    dni_Persona INTEGER,
    telefono TEXT NOT NULL,
    CONSTRAINT pk_dni_telefono PRIMARY KEY (dni_Persona),
    CONSTRAINT fk_dni_persona_tel FOREIGN KEY (dni_Persona) REFERENCES persona(dni) ON UPDATE CASCADE ON DELETE CASCADE
);

DROP TABLE IF EXISTS email;

CREATE TABLE IF NOT EXISTS email(
    dni_Persona INTEGER,
    email TEXT NOT NULL,
    CONSTRAINT pk_dni_email PRIMARY KEY (dni_Persona),
    CONSTRAINT fk_dni_persona_email FOREIGN KEY (dni_Persona) REFERENCES persona(dni) ON UPDATE CASCADE ON DELETE CASCADE
);

DROP TABLE IF EXISTS titulo;

CREATE TABLE IF NOT EXISTS titulo (
    dni_Docente INTEGER,
    titulo TEXT NOT NULL,
    CONSTRAINT pk_dni_titulo PRIMARY KEY (dni_Docente),
    CONSTRAINT fk_dni_docente_titulo FOREIGN KEY (dni_Docente) REFERENCES docente(dni_Persona) ON UPDATE CASCADE ON DELETE CASCADE
);

DROP TABLE IF EXISTS carrera;

CREATE TABLE IF NOT EXISTS carrera (
    id_Carrera INTEGER,
    duracion INTEGER NOT NULL,
    modalidad TEXT NOT NULL CHECK (modalidad IN ('Virtual', 'Presencial', 'Hibrido')),
    CONSTRAINT pk_id_carrera PRIMARY KEY (id_Carrera)
);

DROP TABLE IF EXISTS planEstudio;

CREATE TABLE IF NOT EXISTS planEstudio (
    id_Plan INTEGER,
    aniVigencia TEXT NOT NULL, --Solo colocar el año, así: 'YYYY'
    activo INTEGER NOT NULL DEFAULT 0 CHECK (activo IN (0, 1)),
    resolucion TEXT NOT NULL,
    id_Carrera INTEGER NOT NULL, 
    CONSTRAINT pk_id_Plan PRIMARY KEY (id_Plan),
    CONSTRAINT fk_id_Carrera FOREIGN KEY (id_Carrera) REFERENCES carrera(id_Carrera) ON DELETE CASCADE
);

DROP TABLE IF EXISTS materia;

CREATE TABLE IF NOT EXISTS materia (
    id_Materia INTEGER,
    codigo INTEGER UNIQUE NOT NULL,
    horasTotales INTEGER NOT NULL,
    nombre TEXT NOT NULL,
    dni_Administrador INTEGER NOT NULL,
    dni_Docente INTEGER NOT NULL,
    CONSTRAINT pk_id_materia PRIMARY KEY (id_Materia),
    CONSTRAINT fk_dni_docente_materia FOREIGN KEY (dni_Docente) REFERENCES docente(dni_Persona) ON UPDATE CASCADE ON DELETE CASCADE,
    CONSTRAINT fk_dni_administrador_Mat FOREIGN KEY (dni_Administrador) REFERENCES administrador(dni_Persona) ON UPDATE CASCADE ON DELETE CASCADE
);

DROP TABLE IF EXISTS token_password;
CREATE TABLE IF NOT EXISTS token_password(
    token TEXT,
    email TEXT NOT NULL,
    fecha_expiracion TEXT NOT NULL,
    usado INTEGER DEFAULT 0, -- 0 indica no usado
    CONSTRAINT pk_token PRIMARY KEY (token),
    CONSTRAINT fk_token_email FOREIGN KEY (email) REFERENCES email(email) ON DELETE CASCADE
);