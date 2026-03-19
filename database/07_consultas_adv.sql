-- =====================================================
-- COOPERATIVA BEB - Consultas avanzadas
-- Oracle 11g XE
-- =====================================================

-- =====================================================
-- 1. JOINs
-- =====================================================

-- INNER JOIN: clientes con sus cuentas activas
SELECT c.nombres || ' ' || c.apellidos AS cliente,
       c.dni, ca.nro_cuenta, ca.saldo, ca.tipo_cuenta
FROM CLIENTE c
INNER JOIN CUENTA_AHORRO ca ON c.id_cliente = ca.id_cliente
WHERE ca.estado = 'ACTIVA'
ORDER BY ca.saldo DESC;

-- LEFT JOIN: todos los clientes aunque no tengan cuenta
SELECT c.nombres || ' ' || c.apellidos AS cliente,
       c.dni,
       NVL(ca.nro_cuenta, 'SIN CUENTA') AS cuenta,
       NVL(ca.saldo, 0) AS saldo
FROM CLIENTE c
LEFT JOIN CUENTA_AHORRO ca ON c.id_cliente = ca.id_cliente
ORDER BY c.id_cliente;

-- RIGHT JOIN: todos los asesores aunque no tengan clientes
SELECT NVL(c.nombres || ' ' || c.apellidos, 'SIN CLIENTE') AS cliente,
       a.nombres || ' ' || a.apellidos AS asesor,
       a.especialidad
FROM CLIENTE c
RIGHT JOIN CUENTA_AHORRO ca ON c.id_cliente = ca.id_cliente
RIGHT JOIN ASESOR_FINANCIERO a ON ca.id_asesor = a.id_asesor
ORDER BY a.id_asesor;

-- CROSS JOIN: combinacion productos con asesores
SELECT a.nombres AS asesor, pf.nombre AS producto, pf.tasa_base
FROM ASESOR_FINANCIERO a
CROSS JOIN PRODUCTO_FINANCIERO pf
WHERE pf.estado = 'ACTIVO'
ORDER BY a.nombres, pf.tipo;

-- NATURAL JOIN: transacciones con cuentas
SELECT t.id_transaccion, t.tipo, t.monto, t.fecha_transaccion
FROM TRANSACCION t
JOIN CUENTA_AHORRO ca ON t.id_cuenta = ca.id_cuenta
ORDER BY t.fecha_transaccion DESC;

-- =====================================================
-- 2. GROUP BY y HAVING
-- =====================================================

-- Total de saldo por tipo de cuenta
SELECT tipo_cuenta,
       COUNT(*) AS total_cuentas,
       SUM(saldo) AS saldo_total,
       AVG(saldo) AS saldo_promedio,
       MAX(saldo) AS saldo_maximo,
       MIN(saldo) AS saldo_minimo
FROM CUENTA_AHORRO
WHERE estado = 'ACTIVA'
GROUP BY tipo_cuenta
ORDER BY saldo_total DESC;

-- Asesores con mas de 1 cliente
SELECT a.nombres || ' ' || a.apellidos AS asesor,
       COUNT(DISTINCT ca.id_cliente) AS total_clientes,
       SUM(ca.saldo) AS cartera_total
FROM ASESOR_FINANCIERO a
JOIN CUENTA_AHORRO ca ON a.id_asesor = ca.id_asesor
GROUP BY a.id_asesor, a.nombres, a.apellidos
HAVING COUNT(DISTINCT ca.id_cliente) > 1
ORDER BY cartera_total DESC;

-- Transacciones por tipo con monto total
SELECT tipo,
       COUNT(*) AS cantidad,
       SUM(monto) AS monto_total,
       AVG(monto) AS monto_promedio
FROM TRANSACCION
WHERE estado = 'EXITOSA'
GROUP BY tipo
HAVING SUM(monto) > 1000
ORDER BY monto_total DESC;

-- =====================================================
-- 3. ROLLUP - subtotales por jerarquia
-- =====================================================

-- Resumen de saldos por asesor y tipo de cuenta
SELECT NVL(a.nombres || ' ' || a.apellidos, 'TOTAL GENERAL') AS asesor,
       NVL(ca.tipo_cuenta, 'SUBTOTAL') AS tipo_cuenta,
       COUNT(*) AS cuentas,
       SUM(ca.saldo) AS saldo_total
FROM CUENTA_AHORRO ca
LEFT JOIN ASESOR_FINANCIERO a ON ca.id_asesor = a.id_asesor
GROUP BY ROLLUP(a.nombres || ' ' || a.apellidos, ca.tipo_cuenta)
ORDER BY asesor, tipo_cuenta;

-- =====================================================
-- 4. CUBE - todas las combinaciones
-- =====================================================

-- Analisis de transacciones por tipo y estado
SELECT NVL(tipo, 'TODOS') AS tipo,
       NVL(estado, 'TODOS') AS estado,
       COUNT(*) AS cantidad,
       SUM(monto) AS monto_total
FROM TRANSACCION
GROUP BY CUBE(tipo, estado)
ORDER BY tipo, estado;

-- =====================================================
-- 5. Subconsultas correlacionadas
-- =====================================================

-- Clientes con saldo mayor al promedio general
SELECT c.nombres || ' ' || c.apellidos AS cliente,
       ca.nro_cuenta,
       ca.saldo
FROM CLIENTE c
JOIN CUENTA_AHORRO ca ON c.id_cliente = ca.id_cliente
WHERE ca.saldo > (SELECT AVG(saldo) FROM CUENTA_AHORRO WHERE estado = 'ACTIVA')
ORDER BY ca.saldo DESC;

-- Clientes que tienen al menos un plan de inversion
SELECT c.nombres || ' ' || c.apellidos AS cliente,
       c.dni,
       (SELECT COUNT(*) FROM PLAN_INVERSION p
        WHERE p.id_cliente = c.id_cliente) AS total_planes,
       (SELECT NVL(SUM(monto_invertido),0) FROM PLAN_INVERSION p
        WHERE p.id_cliente = c.id_cliente) AS total_invertido
FROM CLIENTE c
WHERE EXISTS (
    SELECT 1 FROM PLAN_INVERSION p
    WHERE p.id_cliente = c.id_cliente
    AND p.estado = 'ACTIVO'
)
ORDER BY total_invertido DESC;

-- Producto mas contratado
SELECT pf.nombre AS producto,
       pf.tipo,
       pf.tasa_base,
       (SELECT COUNT(*) FROM PLAN_INVERSION pi
        WHERE pi.id_producto = pf.id_producto) AS veces_contratado
FROM PRODUCTO_FINANCIERO pf
WHERE (SELECT COUNT(*) FROM PLAN_INVERSION pi
       WHERE pi.id_producto = pf.id_producto) =
      (SELECT MAX(COUNT(*)) FROM PLAN_INVERSION
       GROUP BY id_producto);

-- =====================================================
-- 6. Consulta usando las VISTAS
-- =====================================================

-- Dashboard general
SELECT * FROM v_dashboard;

-- Top clientes por saldo total
SELECT nombre_completo, dni, total_cuentas, saldo_total
FROM v_clientes_cuentas
ORDER BY saldo_total DESC;

-- Planes activos con ganancia estimada
SELECT cliente, producto, monto_invertido,
       tasa_pactada, ganancia_estimada, monto_final
FROM v_planes_activos
ORDER BY ganancia_estimada DESC;

-- Resumen por asesor
SELECT asesor, especialidad, total_clientes,
       cartera_cuentas, total_planes, cartera_inversiones
FROM v_resumen_asesores
ORDER BY cartera_inversiones DESC;

