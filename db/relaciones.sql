PRAGMA foreign_keys = OFF;

DROP TABLE IF EXISTS pertenece;
DROP TABLE IF EXISTS nota;
DROP TABLE IF EXISTS estudia;
DROP TABLE IF EXISTS taller;
DROP TABLE IF EXISTS participa;
DROP TABLE IF EXISTS rinde;
DROP TABLE IF EXISTS inscripcion_Materia;
DROP TABLE IF EXISTS correlatividad;
DROP TABLE IF EXISTS materia_Plan;
DROP TABLE IF EXISTS inscripcion_Carrera;

CREATE TABLE IF NOT EXISTS inscripcion_Carrera (
    dni_Estudiante INTEGER,
    id_Carrera INTEGER,
    estado TEXT NOT NULL CHECK (estado IN ('Suspendido', 'Baja', 'Egresado', 'Activo')),
    fecha_Ingreso TEXT NOT NULL, -- No existe el tipo date, por lo que se escribe asi: 'YYYY-MM-DD'
    anio_Ingreso TEXT NOT NULL, -- No existe el tipo date, por lo que se escribe asi: 'YYYY'
    CONSTRAINT pk_est_carrera PRIMARY KEY (id_Carrera, dni_Estudiante),
    CONSTRAINT fk_dni_estudiante_insc FOREIGN KEY (dni_Estudiante) REFERENCES estudiante(dni_Persona) ON UPDATE CASCADE ON DELETE CASCADE,
    CONSTRAINT fk_id_Carrera_insc FOREIGN KEY (id_Carrera) REFERENCES carrera(id_Carrera) ON DELETE CASCADE
);


CREATE TABLE IF NOT EXISTS materia_Plan (
    id_Plan INTEGER,
    id_Materia INTEGER,
    anio TEXT NOT NULL, -- No existe el tipo date, por lo que se escribe así: 'YYYY'
    periodo TEXT NOT NULL CHECK (periodo IN ('1 er Cuatrimestre', '2 do Cuatrimestre', '1 er Bimestre', '2 do Bimestre', 'Anual')),
    CONSTRAINT pk_mat_plan PRIMARY KEY (id_Plan, id_Materia),
    CONSTRAINT fk_id_Plan_matP FOREIGN KEY (id_Plan) REFERENCES planEstudio(id_Plan) ON DELETE CASCADE,
    CONSTRAINT fk_id_Materia_matP FOREIGN KEY (id_Materia) REFERENCES materia(id_Materia) ON DELETE CASCADE
);


CREATE TABLE IF NOT EXISTS correlatividad (
    id_Materia_Primaria INTEGER,
    id_Materia_Previa INTEGER,
    condicion TEXT NOT NULL CHECK (condicion IN ('Libre', 'Regular', 'Promocional')),
    CONSTRAINT pk_corre PRIMARY KEY (id_Materia_Primaria, id_Materia_Previa),
    CONSTRAINT fk_materia_primaria FOREIGN KEY (id_Materia_Primaria) REFERENCES materia(id_Materia) ON DELETE CASCADE,
    CONSTRAINT fk_materia_previa FOREIGN KEY (id_Materia_Previa) REFERENCES materia(id_Materia) ON DELETE CASCADE
);

DROP TABLE IF EXISTS inscripcion_Materia;

CREATE TABLE IF NOT EXISTS inscripcion_Materia (
    id_Materia INTEGER,
    dni_Estudiante INTEGER,
    estado TEXT NOT NULL CHECK (estado IN ('Condicional', 'Regular')),
    fecha_Inscripcion TEXT NOT NULL, --No existe el tipo date, por lo que se escribe asi: 'YYYY-MM-DD'
    CONSTRAINT pk_inscMat PRIMARY KEY (id_Materia, dni_Estudiante),
    CONSTRAINT fk_id_Mat_InscMat FOREIGN KEY (id_Materia) REFERENCES materia(id_Materia) ON DELETE CASCADE,
    CONSTRAINT fk_dni_Est_InscMat FOREIGN KEY (dni_Estudiante) REFERENCES estudiante(dni_Persona) ON UPDATE CASCADE ON DELETE CASCADE
);


CREATE TABLE IF NOT EXISTS rinde (
    id_Materia INTEGER,
    dni_Estudiante INTEGER,
    CONSTRAINT pk_rinde PRIMARY KEY (id_Materia, dni_Estudiante),
    CONSTRAINT fk_dni_estudiante_rinde FOREIGN KEY (dni_Estudiante) REFERENCES estudiante(dni_Persona) ON UPDATE CASCADE ON DELETE CASCADE,
    CONSTRAINT fk_id_Mat_rinde FOREIGN KEY (id_Materia) REFERENCES materia(id_Materia) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS participa (
    id_Materia INTEGER,
    dni_Docente INTEGER,
    fecha_Inicio TEXT NOT NULL, -- No existe el tipo date, por lo cual se escribe asi: 'YYYY-MM-DD'
    fecha_Fin TEXT NOT NULL, -- No existe el tipo date, por lo cual se escribe asi: 'YYYY-MM-DD'
    CONSTRAINT pk_participa PRIMARY KEY (id_Materia, dni_Docente),
    CONSTRAINT fk_id_Materia_participa FOREIGN key (id_Materia) REFERENCES materia(id_Materia) ON DELETE CASCADE,
    CONSTRAINT fk_dni_Est_participa FOREIGN KEY (dni_Docente) REFERENCES docente(dni_Persona) ON UPDATE CASCADE ON DELETE CASCADE
);


CREATE TABLE IF NOT EXISTS participa_docente_taller (
    id_Taller INTEGER,
    dni_Docente INTEGER,
    fecha_Inicio TEXT NOT NULL, -- No existe el tipo date, por lo cual se escribe asi: 'YYYY-MM-DD'
    fecha_Fin TEXT NOT NULL, -- No existe el tipo date, por lo cual se escribe asi: 'YYYY-MM-DD'
    CONSTRAINT pk_participa_docente PRIMARY KEY (id_Taller, dni_Docente),
    CONSTRAINT fk_id_Taller_participa FOREIGN key (id_Taller) REFERENCES taller(id_Taller) ON DELETE CASCADE,
    CONSTRAINT fk_dni_Doc_participa FOREIGN KEY (dni_Docente) REFERENCES docente(dni_Persona) ON UPDATE CASCADE ON DELETE CASCADE
);


CREATE TABLE IF NOT EXISTS taller (
    id_Taller INTEGER,
    vigente INTEGER NOT NULL DEFAULT 0 CHECK (vigente IN (0, 1)),
    hora INTEGER NOT NULL,
    titulo TEXT NOT NULL,
    dni_Docente INTEGER NOT NULL,
    CONSTRAINT pk_taller PRIMARY KEY (id_Taller),
    CONSTRAINT fk_doc_Taller FOREIGN KEY (dni_Docente) REFERENCES docente(dni_Persona) ON UPDATE CASCADE ON DELETE CASCADE
);



CREATE TABLE IF NOT EXISTS estudia (
    dni_Estudiante INTEGER,
    id_Taller INTEGER,
    CONSTRAINT pk_estudia PRIMARY KEY (id_Taller, dni_Estudiante),
    CONSTRAINT fk_taller_estudia FOREIGN KEY (id_Taller) REFERENCES taller(id_Taller) ON DELETE CASCADE,
    CONSTRAINT fk_ESt_Estudia FOREIGN KEY (dni_Estudiante) REFERENCES estudiante(dni_Persona) ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS nota (
    id_Nota INTEGER,
    condicion TEXT NOT NULL CHECK (condicion IN ('Libre', 'Regular', 'Promocional')),
    nota_Final INTEGER NOT NULL,
    fecha_Examen TEXT NOT NULL, --No existe el tipo date, por lo cual se escribe asi: 'YYYY-MM-DD'
    dni_Estudiante INTEGER,
    id_Materia INTEGER,
    CONSTRAINT pk_nota PRIMARY KEY (id_Nota),
    CONSTRAINT fk_id_Mat_Nota FOREIGN KEY (id_Materia, dni_Estudiante) REFERENCES rinde(id_Materia, dni_Estudiante) ON DELETE CASCADE
);



CREATE TABLE IF NOT EXISTS nota_taller (
    id_Nota INTEGER,
    dni_Estudiante INTEGER,
    id_Taller INTEGER,
    CONSTRAINT pk_pertenece PRIMARY KEY (id_Nota, dni_Estudiante, id_Taller),
    CONSTRAINT fk_Est_pertenece FOREIGN KEY (dni_Estudiante) REFERENCES estudiante(dni_Persona) ON UPDATE CASCADE ON DELETE CASCADE,
    CONSTRAINT fk_Nota_Pertenece FOREIGN KEY (id_Nota) REFERENCES nota(id_Nota) ON DELETE CASCADE,
    CONSTRAINT fk_Taller_pertenece FOREIGN KEY (id_Taller) REFERENCES taller(id_Taller) ON DELETE CASCADE
);
PRAGMA foreign_keys = ON;
