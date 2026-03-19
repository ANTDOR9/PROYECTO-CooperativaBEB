-- =====================================================
-- COOPERATIVA BEB - Historificacion de datos
-- Oracle 11g XE
-- =====================================================

-- Tabla de auditoria general del sistema
CREATE TABLE AUDITORIA (
                           id_auditoria    NUMBER PRIMARY KEY,
                           tabla_afectada  VARCHAR2(50)  NOT NULL,
                           operacion       VARCHAR2(10)  NOT NULL,
                           id_registro     NUMBER        NOT NULL,
                           usuario_bd      VARCHAR2(50)  DEFAULT USER,
                           fecha_cambio    DATE          DEFAULT SYSDATE,
                           datos_anteriores VARCHAR2(4000),
                           datos_nuevos     VARCHAR2(4000)
);

CREATE SEQUENCE seq_auditoria START WITH 1 INCREMENT BY 1;

-- Historico de saldos de cuentas
CREATE TABLE HIST_CUENTA_AHORRO (
                                    id_hist         NUMBER PRIMARY KEY,
                                    id_cuenta       NUMBER        NOT NULL,
                                    nro_cuenta      VARCHAR2(20),
                                    saldo_anterior  NUMBER(12,2),
                                    saldo_nuevo     NUMBER(12,2),
                                    fecha_cambio    DATE          DEFAULT SYSDATE,
                                    motivo          VARCHAR2(200)
);

CREATE SEQUENCE seq_hist_cuenta START WITH 1 INCREMENT BY 1;

-- Historico de estados de clientes
CREATE TABLE HIST_CLIENTE (
                              id_hist         NUMBER PRIMARY KEY,
                              id_cliente      NUMBER        NOT NULL,
                              dni             VARCHAR2(8),
                              estado_anterior VARCHAR2(10),
                              estado_nuevo    VARCHAR2(10),
                              fecha_cambio    DATE          DEFAULT SYSDATE,
                              usuario_bd      VARCHAR2(50)  DEFAULT USER
);

CREATE SEQUENCE seq_hist_cliente START WITH 1 INCREMENT BY 1;

-- Historico de planes de inversion
CREATE TABLE HIST_PLAN_INVERSION (
                                     id_hist           NUMBER PRIMARY KEY,
                                     id_plan           NUMBER        NOT NULL,
                                     id_cliente        NUMBER,
                                     estado_anterior   VARCHAR2(10),
                                     estado_nuevo      VARCHAR2(10),
                                     monto_invertido   NUMBER(12,2),
                                     fecha_cambio      DATE          DEFAULT SYSDATE,
                                     usuario_bd        VARCHAR2(50)  DEFAULT USER
);

CREATE SEQUENCE seq_hist_plan START WITH 1 INCREMENT BY 1;

-- =====================================================
-- TRIGGERS de historificacion
-- =====================================================

-- Trigger: cambios en saldo de cuenta
CREATE OR REPLACE TRIGGER trg_hist_saldo
AFTER UPDATE OF saldo ON CUENTA_AHORRO
    FOR EACH ROW
BEGIN
INSERT INTO HIST_CUENTA_AHORRO VALUES (
                                          seq_hist_cuenta.NEXTVAL,
                                          :OLD.id_cuenta,
                                          :OLD.nro_cuenta,
                                          :OLD.saldo,
                                          :NEW.saldo,
                                          SYSDATE,
                                          'Actualizacion de saldo'
                                      );
END;
/

-- Trigger: cambios en estado de cliente
CREATE OR REPLACE TRIGGER trg_hist_cliente
AFTER UPDATE OF estado ON CLIENTE
    FOR EACH ROW
BEGIN
INSERT INTO HIST_CLIENTE VALUES (
                                    seq_hist_cliente.NEXTVAL,
                                    :OLD.id_cliente,
                                    :OLD.dni,
                                    :OLD.estado,
                                    :NEW.estado,
                                    SYSDATE,
                                    USER
                                );
END;
/

-- Trigger: cambios en estado de plan inversion
CREATE OR REPLACE TRIGGER trg_hist_plan
AFTER UPDATE OF estado ON PLAN_INVERSION
    FOR EACH ROW
BEGIN
INSERT INTO HIST_PLAN_INVERSION VALUES (
                                           seq_hist_plan.NEXTVAL,
                                           :OLD.id_plan,
                                           :OLD.id_cliente,
                                           :OLD.estado,
                                           :NEW.estado,
                                           :OLD.monto_invertido,
                                           SYSDATE,
                                           USER
                                       );
END;
/

-- Trigger: auditoria general en TRANSACCION
CREATE OR REPLACE TRIGGER trg_audit_transaccion
AFTER INSERT ON TRANSACCION
FOR EACH ROW
BEGIN
INSERT INTO AUDITORIA VALUES (
                                 seq_auditoria.NEXTVAL,
                                 'TRANSACCION',
                                 'INSERT',
                                 :NEW.id_transaccion,
                                 USER,
                                 SYSDATE,
                                 NULL,
                                 'Monto: ' || :NEW.monto || ' | Tipo: ' || :NEW.tipo
                             );
END;
/

COMMIT;

-- Verificar triggers creados
SELECT trigger_name, table_name, status
FROM user_triggers
ORDER BY table_name;