-- =====================================================
-- COOPERATIVA BEB - Triggers adicionales
-- Oracle 11g XE
-- =====================================================

-- Trigger: validar monto minimo al insertar cuenta
CREATE OR REPLACE TRIGGER trg_valida_saldo_minimo
BEFORE INSERT ON CUENTA_AHORRO
FOR EACH ROW
DECLARE
    v_minimo NUMBER := 100;
BEGIN
    IF :NEW.saldo < v_minimo THEN
        RAISE_APPLICATION_ERROR(-20002,
            'Saldo inicial minimo es S/. ' || v_minimo);
    END IF;
END;
/

-- Trigger: validar monto minimo plan de inversion
CREATE OR REPLACE TRIGGER trg_valida_monto_plan
BEFORE INSERT ON PLAN_INVERSION
FOR EACH ROW
DECLARE
    v_minimo NUMBER;
BEGIN
    SELECT monto_minimo INTO v_minimo
    FROM PRODUCTO_FINANCIERO
    WHERE id_producto = :NEW.id_producto;

    IF :NEW.monto_invertido < v_minimo THEN
        RAISE_APPLICATION_ERROR(-20003,
            'Monto minimo para este producto es S/. ' || v_minimo);
    END IF;
END;
/

-- Trigger: actualizar estado cuenta al cerrarla
CREATE OR REPLACE TRIGGER trg_cierre_cuenta
BEFORE UPDATE OF estado ON CUENTA_AHORRO
FOR EACH ROW
BEGIN
    IF :NEW.estado = 'CERRADA' AND :OLD.estado = 'ACTIVA' THEN
        :NEW.fecha_cierre := SYSDATE;
    END IF;
END;
/

-- Trigger: no permitir retiro en cuenta bloqueada
CREATE OR REPLACE TRIGGER trg_bloqueo_cuenta
BEFORE INSERT ON TRANSACCION
FOR EACH ROW
DECLARE
    v_estado VARCHAR2(10);
BEGIN
    IF :NEW.id_cuenta IS NOT NULL AND :NEW.tipo = 'RETIRO' THEN
        SELECT estado INTO v_estado
        FROM CUENTA_AHORRO
        WHERE id_cuenta = :NEW.id_cuenta;

        IF v_estado != 'ACTIVA' THEN
            RAISE_APPLICATION_ERROR(-20004,
                'No se puede retirar de una cuenta ' || v_estado);
        END IF;
    END IF;
END;
/

-- Trigger: fecha vencimiento plan automatica
CREATE OR REPLACE TRIGGER trg_fecha_vencimiento
BEFORE INSERT ON PLAN_INVERSION
FOR EACH ROW
DECLARE
    v_plazo NUMBER;
BEGIN
    IF :NEW.fecha_vencimiento IS NULL THEN
        SELECT plazo_meses INTO v_plazo
        FROM PRODUCTO_FINANCIERO
        WHERE id_producto = :NEW.id_producto;
        :NEW.fecha_vencimiento := ADD_MONTHS(SYSDATE, v_plazo);
    END IF;
END;
/

COMMIT;

-- Verificar todos los triggers
SELECT trigger_name, table_name, trigger_type, status
FROM user_triggers
ORDER BY table_name;
