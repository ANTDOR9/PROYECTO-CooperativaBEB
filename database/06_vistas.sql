-- =====================================================
-- COOPERATIVA BEB - Vistas
-- Oracle 11g XE
-- =====================================================

-- Vista: resumen de clientes con sus cuentas
CREATE OR REPLACE VIEW v_clientes_cuentas AS
SELECT 
    c.id_cliente,
    c.nombres || ' ' || c.apellidos AS nombre_completo,
    c.dni,
    c.email,
    c.estado AS estado_cliente,
    COUNT(ca.id_cuenta) AS total_cuentas,
    NVL(SUM(ca.saldo), 0) AS saldo_total,
    MAX(ca.fecha_apertura) AS ultima_apertura
FROM CLIENTE c
LEFT JOIN CUENTA_AHORRO ca ON c.id_cliente = ca.id_cliente
GROUP BY c.id_cliente, c.nombres, c.apellidos, c.dni, c.email, c.estado;

-- Vista: detalle de transacciones con datos del cliente
CREATE OR REPLACE VIEW v_transacciones_detalle AS
SELECT
    t.id_transaccion,
    t.fecha_transaccion,
    t.tipo,
    t.monto,
    t.saldo_anterior,
    t.saldo_posterior,
    t.descripcion,
    t.estado,
    ca.nro_cuenta,
    c.nombres || ' ' || c.apellidos AS cliente,
    c.dni
FROM TRANSACCION t
LEFT JOIN CUENTA_AHORRO ca ON t.id_cuenta = ca.id_cuenta
LEFT JOIN CLIENTE c ON ca.id_cliente = c.id_cliente
WHERE t.id_cuenta IS NOT NULL;

-- Vista: planes de inversion activos con proyeccion
CREATE OR REPLACE VIEW v_planes_activos AS
SELECT
    p.id_plan,
    c.nombres || ' ' || c.apellidos AS cliente,
    c.dni,
    pf.nombre AS producto,
    p.monto_invertido,
    p.tasa_pactada,
    p.plazo_meses,
    p.fecha_inicio,
    p.fecha_vencimiento,
    ROUND(p.monto_invertido * (p.tasa_pactada/100) * (p.plazo_meses/12), 2) AS ganancia_estimada,
    ROUND(p.monto_invertido + (p.monto_invertido * (p.tasa_pactada/100) * (p.plazo_meses/12)), 2) AS monto_final,
    a.nombres || ' ' || a.apellidos AS asesor,
    p.estado
FROM PLAN_INVERSION p
JOIN CLIENTE c ON p.id_cliente = c.id_cliente
JOIN PRODUCTO_FINANCIERO pf ON p.id_producto = pf.id_producto
LEFT JOIN ASESOR_FINANCIERO a ON p.id_asesor = a.id_asesor
WHERE p.estado = 'ACTIVO';

-- Vista: resumen por asesor
CREATE OR REPLACE VIEW v_resumen_asesores AS
SELECT
    a.id_asesor,
    a.nombres || ' ' || a.apellidos AS asesor,
    a.especialidad,
    COUNT(DISTINCT ca.id_cliente) AS total_clientes,
    COUNT(DISTINCT ca.id_cuenta) AS total_cuentas,
    NVL(SUM(ca.saldo), 0) AS cartera_cuentas,
    COUNT(DISTINCT p.id_plan) AS total_planes,
    NVL(SUM(p.monto_invertido), 0) AS cartera_inversiones
FROM ASESOR_FINANCIERO a
LEFT JOIN CUENTA_AHORRO ca ON a.id_asesor = ca.id_asesor
LEFT JOIN PLAN_INVERSION p ON a.id_asesor = p.id_asesor
GROUP BY a.id_asesor, a.nombres, a.apellidos, a.especialidad;

-- Vista: dashboard general cooperativa
CREATE OR REPLACE VIEW v_dashboard AS
SELECT
    (SELECT COUNT(*) FROM CLIENTE WHERE estado = 'ACTIVO') AS clientes_activos,
    (SELECT COUNT(*) FROM CUENTA_AHORRO WHERE estado = 'ACTIVA') AS cuentas_activas,
    (SELECT NVL(SUM(saldo),0) FROM CUENTA_AHORRO WHERE estado = 'ACTIVA') AS total_ahorros,
    (SELECT COUNT(*) FROM PLAN_INVERSION WHERE estado = 'ACTIVO') AS planes_activos,
    (SELECT NVL(SUM(monto_invertido),0) FROM PLAN_INVERSION WHERE estado = 'ACTIVO') AS total_inversiones,
    (SELECT COUNT(*) FROM TRANSACCION WHERE TRUNC(fecha_transaccion) = TRUNC(SYSDATE)) AS transacciones_hoy
FROM DUAL;

COMMIT;

-- Verificar vistas
SELECT view_name FROM user_views ORDER BY view_name;
