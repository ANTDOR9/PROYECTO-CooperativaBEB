-- =====================================================
-- COOPERATIVA BEB - Tabla de usuarios del sistema
-- Oracle 11g XE
-- =====================================================

DROP TABLE USUARIO_SISTEMA CASCADE CONSTRAINTS PURGE;
DROP SEQUENCE seq_usuario;

CREATE SEQUENCE seq_usuario START WITH 1 INCREMENT BY 1;

CREATE TABLE USUARIO_SISTEMA (
    id_usuario   NUMBER         PRIMARY KEY,
    username     VARCHAR2(50)   NOT NULL UNIQUE,
    password     VARCHAR2(100)  NOT NULL,
    rol          VARCHAR2(20)   NOT NULL CHECK (rol IN ('ADMIN','ASESOR','CLIENTE')),
    id_cliente   NUMBER,
    nombre_completo VARCHAR2(200),
    email        VARCHAR2(100),
    estado       VARCHAR2(10)   DEFAULT 'ACTIVO' CHECK (estado IN ('ACTIVO','INACTIVO')),
    fecha_creacion DATE         DEFAULT SYSDATE,
    ultimo_acceso  DATE,
    CONSTRAINT fk_usuario_cliente FOREIGN KEY (id_cliente)
        REFERENCES CLIENTE(id_cliente)
);

-- Usuarios iniciales del sistema
INSERT INTO USUARIO_SISTEMA VALUES (
    seq_usuario.NEXTVAL,'admin','admin','ADMIN',
    NULL,'Administrador del Sistema','admin@beb.com','ACTIVO',SYSDATE,NULL);

INSERT INTO USUARIO_SISTEMA VALUES (
    seq_usuario.NEXTVAL,'asesor','asesor','ASESOR',
    NULL,'Asesor Financiero','asesor@beb.com','ACTIVO',SYSDATE,NULL);

INSERT INTO USUARIO_SISTEMA VALUES (
    seq_usuario.NEXTVAL,'cliente','cliente','CLIENTE',
    1,'Juan Perez Garcia','juan.perez@gmail.com','ACTIVO',SYSDATE,NULL);

COMMIT;

SELECT id_usuario, username, rol, nombre_completo, estado
FROM USUARIO_SISTEMA ORDER BY rol;
