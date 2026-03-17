
# 🏦 Sistema Financiero — Cooperativa BEB

> Trabajo Final del curso **Programación para Desarrollo de Software with Oracle**
> SENATI — Tecnologías de la Información

---

## 📋 Descripción

Sistema de gestión financiera desarrollado en **Java SE** con interfaz gráfica **Swing**
y base de datos **Oracle**, diseñado para digitalizar y optimizar las operaciones
internas de la Cooperativa "BEB". Permite administrar clientes, cuentas de ahorro,
créditos personales, planes de inversión y el trabajo de asesores financieros desde
una sola plataforma de escritorio.

---

## 🎯 Objetivos del proyecto

- Rediseñar la base de datos con un modelo relacional normalizado en Oracle
- Implementar historificación de datos para auditoría de cambios
- Desarrollar una interfaz moderna y funcional con Java Swing + FlatLaf
- Conectar la aplicación a Oracle mediante JDBC con pool de conexiones (HikariCP)
- Generar reportes exportables en PDF (iText) y Excel (Apache POI)
- Aplicar consultas avanzadas: subconsultas, JOINs, GROUP BY, ROLLUP, CUBE

---

## 🧩 Entidades principales

| Entidad               | Descripción                                      |
|-----------------------|--------------------------------------------------|
| 👤 Cliente            | Datos personales y estado de cuenta              |
| 💰 Cuenta de Ahorro   | Saldos, movimientos y tasas de interés           |
| 📈 Plan de Inversión  | CDTs y productos de inversión con proyecciones   |
| 💳 Transacción        | Historial completo de operaciones financieras    |
| 🏷️ Producto Financiero| Catálogo de productos ofrecidos por la coop.     |
| 🧑‍💼 Asesor Financiero  | Gestión de cartera y asignación de clientes      |

---

## 🛠️ Tecnologías utilizadas

- **Lenguaje:** Java SE 17+
- **UI:** Java Swing + FlatLaf (tema moderno)
- **Base de datos:** Oracle Database
- **Conectividad:** JDBC + HikariCP (pool de conexiones)
- **Reportes:** iText (PDF) · Apache POI (Excel)
- **Gráficos:** JFreeChart
- **Logging:** Log4j2
- **Testing:** JUnit 5
- **Build:** Apache Maven
- **Control de versiones:** Git + GitHub

---

## 📁 Estructura del proyecto

```
CooperativaBEB/
├── src/
│   ├── main/java/com/cooperativabeb/
│   │   ├── model/          # POJOs: Cliente, Cuenta, Transaccion...
│   │   ├── view/           # JFrames y JPanels Swing
│   │   ├── controller/     # Lógica entre view y dao
│   │   ├── dao/            # Acceso a datos con JDBC
│   │   ├── service/        # Lógica financiera y simuladores
│   │   ├── connection/     # Singleton JDBC + HikariCP
│   │   ├── report/         # Generación PDF y Excel
│   │   ├── util/           # Utilidades y helpers
│   │   └── exception/      # Excepciones personalizadas
│   └── test/               # Pruebas JUnit 5
├── database/
│   ├── 01_ddl_tablas.sql
│   ├── 02_dml_datos.sql
│   ├── 03_historificacion.sql
│   ├── 04_procedures.sql
│   ├── 05_triggers.sql
│   ├── 06_vistas.sql
│   ├── 07_consultas_adv.sql
│   └── diagrams/           # Modelo ER y diagramas
├── docs/                   # Manual de usuario y arquitectura
├── resources/
│   ├── db.properties.example
│   ├── log4j2.xml
│   ├── icons/
│   └── templates/
└── pom.xml
```

---

## ⚙️ Configuración inicial

1. Clona el repositorio
   ```bash
   git clone https://github.com/tu-usuario/CooperativaBEB.git
   ```

2. Copia y configura las credenciales de Oracle
   ```bash
   cp resources/db.properties.example resources/db.properties
   ```
   Edita `db.properties` con tu usuario, contraseña y URL de Oracle.

3. Ejecuta los scripts SQL en orden
   ```bash
   # En SQL*Plus o SQL Developer, ejecuta:
   01_ddl_tablas.sql → 02_dml_datos.sql → 03_historificacion.sql ...
   ```

4. Compila y ejecuta con Maven
   ```bash
   mvn clean install
   mvn exec:java -Dexec.mainClass="com.cooperativabeb.Main"
   ```

---

## 📌 Estado del proyecto

| Módulo              | Estado        |
|---------------------|---------------|
| Modelo ER y BD      | 🔄 En progreso |
| Scripts SQL         | 🔄 En progreso |
| Conexión JDBC       | ⏳ Pendiente   |
| Interfaz Swing      | ⏳ Pendiente   |
| Reportes PDF/Excel  | ⏳ Pendiente   |
| Dashboard gráfico   | ⏳ Pendiente   |
| Testing JUnit       | ⏳ Pendiente   |

---

## 👨‍🎓 Autor

**ANTHONY DORLY HUILAHUAÑA CHATA**
Estudiante — SENATI, Tecnologías de la Información
Curso: Programación para Desarrollo de Software with Oracle

---

## 📄 Licencia

Este proyecto es de uso académico — SENATI 2025.
