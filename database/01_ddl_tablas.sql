-- =====================================================
-- COOPERATIVA BEB - DDL Tablas principales
-- Oracle 11g XE
-- =====================================================

DROP TABLE TRANSACCION CASCADE CONSTRAINTS PURGE;
DROP TABLE PLAN_INVERSION CASCADE CONSTRAINTS PURGE;
DROP TABLE CUENTA_AHORRO CASCADE CONSTRAINTS PURGE;
DROP TABLE PRODUCTO_FINANCIERO CASCADE CONSTRAINTS PURGE;
DROP TABLE ASESOR_FINANCIERO CASCADE CONSTRAINTS PURGE;
DROP TABLE CLIENTE CASCADE CONSTRAINTS PURGE;

DROP SEQUENCE seq_cliente;
DROP SEQUENCE seq_asesor;
DROP SEQUENCE seq_producto;
DROP SEQUENCE seq_cuenta;
DROP SEQUENCE seq_plan;
DROP SEQUENCE seq_transaccion;

CREATE SEQUENCE seq_cliente     START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE seq_asesor      START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE seq_producto    START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE seq_cuenta      START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE seq_plan        START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE seq_transaccion START WITH 1 INCREMENT BY 1;

CREATE TABLE CLIENTE (
    id_cliente       NUMBER         PRIMARY KEY,
    dni              VARCHAR2(8)    NOT NULL UNIQUE,
    nombres          VARCHAR2(100)  NOT NULL,
    apellidos        VARCHAR2(100)  NOT NULL,
    telefono         VARCHAR2(15),
    email            VARCHAR2(100)  UNIQUE,
    fecha_nacimiento DATE,
    direccion        VARCHAR2(200),
    estado           VARCHAR2(10)   DEFAULT 'ACTIVO',
    fecha_registro   DATE           DEFAULT SYSDATE NOT NULL
);

CREATE TABLE ASESOR_FINANCIERO (
    id_asesor    NUMBER         PRIMARY KEY,
    dni          VARCHAR2(8)    NOT NULL UNIQUE,
    nombres      VARCHAR2(100)  NOT NULL,
    apellidos    VARCHAR2(100)  NOT NULL,
    especialidad VARCHAR2(100),
    telefono     VARCHAR2(15),
    email        VARCHAR2(100)  UNIQUE,
    estado       VARCHAR2(10)   DEFAULT 'ACTIVO'
);

CREATE TABLE PRODUCTO_FINANCIERO (
    id_producto  NUMBER         PRIMARY KEY,
    nombre       VARCHAR2(100)  NOT NULL,
    tipo         VARCHAR2(20)   NOT NULL,
    descripcion  VARCHAR2(300),
    tasa_base    NUMBER(5,2)    NOT NULL,
    monto_minimo NUMBER(12,2)   DEFAULT 0,
    monto_maximo NUMBER(12,2),
    plazo_meses  NUMBER(3),
    estado       VARCHAR2(10)   DEFAULT 'ACTIVO'
);

CREATE TABLE CUENTA_AHORRO (
    id_cuenta      NUMBER        PRIMARY KEY,
    nro_cuenta     VARCHAR2(20)  NOT NULL UNIQUE,
    id_cliente     NUMBER        NOT NULL,
    id_asesor      NUMBER,
    saldo          NUMBER(12,2)  DEFAULT 0 NOT NULL,
    tasa_interes   NUMBER(5,2)   DEFAULT 0.5,
    tipo_cuenta    VARCHAR2(20)  DEFAULT 'BASICA',
    estado         VARCHAR2(10)  DEFAULT 'ACTIVA',
    fecha_apertura DATE          DEFAULT SYSDATE NOT NULL,
    fecha_cierre   DATE,
    CONSTRAINT fk_cuenta_cliente FOREIGN KEY (id_cliente) REFERENCES CLIENTE(id_cliente),
    CONSTRAINT fk_cuenta_asesor  FOREIGN KEY (id_asesor)  REFERENCES ASESOR_FINANCIERO(id_asesor)
);

CREATE TABLE PLAN_INVERSION (
    id_plan           NUMBER        PRIMARY KEY,
    id_cliente        NUMBER        NOT NULL,
    id_producto       NUMBER        NOT NULL,
    id_asesor         NUMBER,
    monto_invertido   NUMBER(12,2)  NOT NULL,
    tasa_pactada      NUMBER(5,2)   NOT NULL,
    plazo_meses       NUMBER(3)     NOT NULL,
    fecha_inicio      DATE          DEFAULT SYSDATE NOT NULL,
    fecha_vencimiento DATE          NOT NULL,
    estado            VARCHAR2(10)  DEFAULT 'ACTIVO',
    CONSTRAINT fk_plan_cliente  FOREIGN KEY (id_cliente)  REFERENCES CLIENTE(id_cliente),
    CONSTRAINT fk_plan_producto FOREIGN KEY (id_producto) REFERENCES PRODUCTO_FINANCIERO(id_producto),
    CONSTRAINT fk_plan_asesor   FOREIGN KEY (id_asesor)   REFERENCES ASESOR_FINANCIERO(id_asesor)
);

CREATE TABLE TRANSACCION (
    id_transaccion    NUMBER        PRIMARY KEY,
    id_cuenta         NUMBER,
    id_plan           NUMBER,
    tipo              VARCHAR2(20)  NOT NULL,
    monto             NUMBER(12,2)  NOT NULL,
    saldo_anterior    NUMBER(12,2)  NOT NULL,
    saldo_posterior   NUMBER(12,2)  NOT NULL,
    fecha_transaccion DATE          DEFAULT SYSDATE NOT NULL,
    descripcion       VARCHAR2(300),
    estado            VARCHAR2(10)  DEFAULT 'EXITOSA',
    CONSTRAINT fk_trans_cuenta FOREIGN KEY (id_cuenta) REFERENCES CUENTA_AHORRO(id_cuenta),
    CONSTRAINT fk_trans_plan   FOREIGN KEY (id_plan)   REFERENCES PLAN_INVERSION(id_plan)
);

CREATE INDEX idx_cuenta_cliente ON CUENTA_AHORRO(id_cliente);
CREATE INDEX idx_plan_cliente   ON PLAN_INVERSION(id_cliente);
CREATE INDEX idx_trans_cuenta   ON TRANSACCION(id_cuenta);
CREATE INDEX idx_trans_fecha    ON TRANSACCION(fecha_transaccion);
CREATE INDEX idx_cliente_dni    ON CLIENTE(dni);

COMMIT;

SELECT table_name FROM user_tables
WHERE table_name IN ('CLIENTE','ASESOR_FINANCIERO','PRODUCTO_FINANCIERO','CUENTA_AHORRO','PLAN_INVERSION','TRANSACCION')
ORDER BY table_name;
