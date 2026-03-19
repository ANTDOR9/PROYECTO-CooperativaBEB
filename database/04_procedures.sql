-- =====================================================
-- COOPERATIVA BEB - Stored Procedures
-- Oracle 11g XE
-- =====================================================

-- =====================================================
-- PROCEDURE: Registrar deposito
-- =====================================================
CREATE OR REPLACE PROCEDURE sp_deposito(
    p_id_cuenta  IN NUMBER,
    p_monto      IN NUMBER,
    p_descripcion IN VARCHAR2
) AS
    v_saldo_anterior NUMBER;
    v_saldo_nuevo    NUMBER;
BEGIN
    SELECT saldo INTO v_saldo_anterior
    FROM CUENTA_AHORRO WHERE id_cuenta = p_id_cuenta;

    v_saldo_nuevo := v_saldo_anterior + p_monto;

    UPDATE CUENTA_AHORRO SET saldo = v_saldo_nuevo
    WHERE id_cuenta = p_id_cuenta;

    INSERT INTO TRANSACCION VALUES (
        seq_transaccion.NEXTVAL, p_id_cuenta, NULL,
        'DEPOSITO', p_monto, v_saldo_anterior,
        v_saldo_nuevo, SYSDATE, p_descripcion, 'EXITOSA'
    );
    COMMIT;
    DBMS_OUTPUT.PUT_LINE('Deposito exitoso. Nuevo saldo: ' || v_saldo_nuevo);
EXCEPTION
    WHEN NO_DATA_FOUND THEN
        DBMS_OUTPUT.PUT_LINE('Error: Cuenta no encontrada');
    WHEN OTHERS THEN
        ROLLBACK;
        DBMS_OUTPUT.PUT_LINE('Error: ' || SQLERRM);
END;
/

-- =====================================================
-- PROCEDURE: Registrar retiro
-- =====================================================
CREATE OR REPLACE PROCEDURE sp_retiro(
    p_id_cuenta  IN NUMBER,
    p_monto      IN NUMBER,
    p_descripcion IN VARCHAR2
) AS
    v_saldo_anterior NUMBER;
    v_saldo_nuevo    NUMBER;
BEGIN
    SELECT saldo INTO v_saldo_anterior
    FROM CUENTA_AHORRO WHERE id_cuenta = p_id_cuenta;

    IF v_saldo_anterior < p_monto THEN
        RAISE_APPLICATION_ERROR(-20001, 'Saldo insuficiente');
    END IF;

    v_saldo_nuevo := v_saldo_anterior - p_monto;

    UPDATE CUENTA_AHORRO SET saldo = v_saldo_nuevo
    WHERE id_cuenta = p_id_cuenta;

    INSERT INTO TRANSACCION VALUES (
        seq_transaccion.NEXTVAL, p_id_cuenta, NULL,
        'RETIRO', p_monto, v_saldo_anterior,
        v_saldo_nuevo, SYSDATE, p_descripcion, 'EXITOSA'
    );
    COMMIT;
    DBMS_OUTPUT.PUT_LINE('Retiro exitoso. Nuevo saldo: ' || v_saldo_nuevo);
EXCEPTION
    WHEN OTHERS THEN
        ROLLBACK;
        DBMS_OUTPUT.PUT_LINE('Error: ' || SQLERRM);
END;
/

-- =====================================================
-- PROCEDURE: Abrir cuenta de ahorro
-- =====================================================
CREATE OR REPLACE PROCEDURE sp_abrir_cuenta(
    p_id_cliente  IN NUMBER,
    p_id_asesor   IN NUMBER,
    p_tipo_cuenta IN VARCHAR2,
    p_saldo_inicial IN NUMBER
) AS
    v_nro_cuenta VARCHAR2(20);
    v_id_cuenta  NUMBER;
BEGIN
    v_id_cuenta  := seq_cuenta.NEXTVAL;
    v_nro_cuenta := 'BEB-' || LPAD(v_id_cuenta, 4, '0') || '-' || TO_CHAR(SYSDATE,'YYYY');

    INSERT INTO CUENTA_AHORRO VALUES (
        v_id_cuenta, v_nro_cuenta, p_id_cliente,
        p_id_asesor, p_saldo_inicial, 0.5,
        p_tipo_cuenta, 'ACTIVA', SYSDATE, NULL
    );

    INSERT INTO TRANSACCION VALUES (
        seq_transaccion.NEXTVAL, v_id_cuenta, NULL,
        'APERTURA', p_saldo_inicial, 0,
        p_saldo_inicial, SYSDATE, 'Apertura de cuenta', 'EXITOSA'
    );
    COMMIT;
    DBMS_OUTPUT.PUT_LINE('Cuenta creada: ' || v_nro_cuenta);
EXCEPTION
    WHEN OTHERS THEN
        ROLLBACK;
        DBMS_OUTPUT.PUT_LINE('Error: ' || SQLERRM);
END;
/

-- =====================================================
-- PROCEDURE: Contratar plan de inversion
-- =====================================================
CREATE OR REPLACE PROCEDURE sp_contratar_plan(
    p_id_cliente  IN NUMBER,
    p_id_producto IN NUMBER,
    p_id_asesor   IN NUMBER,
    p_monto       IN NUMBER
) AS
    v_tasa      NUMBER;
    v_plazo     NUMBER;
    v_id_plan   NUMBER;
BEGIN
    SELECT tasa_base, plazo_meses INTO v_tasa, v_plazo
    FROM PRODUCTO_FINANCIERO WHERE id_producto = p_id_producto;

    v_id_plan := seq_plan.NEXTVAL;

    INSERT INTO PLAN_INVERSION VALUES (
        v_id_plan, p_id_cliente, p_id_producto,
        p_id_asesor, p_monto, v_tasa, v_plazo,
        SYSDATE, ADD_MONTHS(SYSDATE, v_plazo), 'ACTIVO'
    );

    INSERT INTO TRANSACCION VALUES (
        seq_transaccion.NEXTVAL, NULL, v_id_plan,
        'APERTURA', p_monto, 0, p_monto,
        SYSDATE, 'Contratacion plan inversion', 'EXITOSA'
    );
    COMMIT;
    DBMS_OUTPUT.PUT_LINE('Plan contratado exitosamente. ID: ' || v_id_plan);
EXCEPTION
    WHEN OTHERS THEN
        ROLLBACK;
        DBMS_OUTPUT.PUT_LINE('Error: ' || SQLERRM);
END;
/

COMMIT;

-- Verificar procedures creados
SELECT object_name, object_type, status
FROM user_objects
WHERE object_type = 'PROCEDURE'
ORDER BY object_name;
