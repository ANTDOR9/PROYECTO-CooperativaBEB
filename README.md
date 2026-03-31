# 🏦 Sistema Financiero — Cooperativa BEB

> Trabajo Final del curso **Programación para Desarrollo de Software with Oracle**
> SENATI — Tecnologías de la Información

---

## 📋 Descripción

Sistema de gestión financiera desarrollado en **Java SE** con interfaz gráfica **Swing**
y base de datos **Oracle 11g XE**, diseñado para digitalizar y optimizar las operaciones
internas de la Cooperativa "BEB". Permite administrar clientes, cuentas de ahorro,
créditos personales, planes de inversión y el trabajo de asesores financieros desde
una sola plataforma de escritorio con estética premium negro + dorado.

---

## 🎯 Objetivos del proyecto

- Rediseñar la base de datos con un modelo relacional normalizado en Oracle 11g
- Implementar historificación de datos para auditoría de cambios
- Desarrollar una interfaz moderna y funcional con Java Swing + FlatLaf
- Conectar la aplicación a Oracle mediante JDBC con pool de conexiones
- Generar reportes exportables en PDF (iText) y Excel (Apache POI)
- Aplicar consultas avanzadas: subconsultas, JOINs, GROUP BY, ROLLUP, CUBE
- Implementar sistema de roles: Admin, Asesor y Cliente

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
| 🔐 Usuario Sistema    | Gestión de accesos con roles diferenciados       |

---

## 🛠️ Tecnologías utilizadas

- **Lenguaje:** Java SE 21 (Eclipse Temurin 21)
- **UI:** Java Swing + FlatLaf Dark (tema negro + dorado premium)
- **Base de datos:** Oracle Database 11g XE
- **Conectividad:** JDBC (ojdbc6)
- **Reportes:** iText 5 (PDF) · Apache POI (Excel)
- **Gráficos:** JFreeChart (dashboard con barras y torta)
- **Logging:** Log4j2
- **Build:** Apache Maven
- **Control de versiones:** Git + GitHub

---

## 👥 Roles del sistema

| Rol | Usuario | Contraseña | Acceso |
|-----|---------|------------|--------|
| Administrador | `admin` | `admin` | Panel completo |
| Asesor | `asesor` | `asesor` | Panel completo |
| Cliente | `cliente` | `cliente` | Portal cliente |

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
│   │   ├── connection/     # Singleton JDBC
│   │   ├── report/         # Generación PDF y Excel
│   │   ├── util/           # Tema visual y helpers
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
│   └── 08_usuarios.sql
├── dist/                   # JAR ejecutable listo para entregar
│   ├── CooperativaBEB.jar
│   ├── libs/
│   ├── EJECUTAR.bat
│   └── LEEME.txt
├── docs/                   # Diagramas y documentación
├── resources/              # Logo, íconos, configuración
└── pom.xml
```

---

## ⚙️ Requisitos para ejecutar

1. **Java 21** (Eclipse Temurin 21)
   - Descargar: https://adoptium.net/temurin/releases/?version=21
   - Verificar: `java -version` debe mostrar versión 21

2. **Oracle Database 11g XE** corriendo en `localhost:1521`

---

## 🚀 Instalación y configuración

**Opción A — JAR ejecutable (recomendado):**
```bash
# 1. Ir a la carpeta dist/
# 2. Editar db.properties con tu contraseña Oracle
# 3. Doble clic en EJECUTAR.bat
```

**Opción B — Desde el código fuente:**
```bash
git clone https://github.com/ANTDOR9/PROYECTO-CooperativaBEB.git
cd PROYECTO-CooperativaBEB
cp src/main/resources/db.properties.example src/main/resources/db.properties
# Editar db.properties con tu contraseña Oracle
mvn clean package
java -jar target/CooperativaBEB.jar
```

**Scripts SQL — ejecutar en orden en SQL Developer:**
```
01_ddl_tablas.sql → 02_dml_datos.sql → 03_historificacion.sql
04_procedures.sql → 05_triggers.sql  → 06_vistas.sql
07_consultas_adv.sql → 08_usuarios.sql
```

---

## ✨ Funcionalidades principales

### Panel Administrador
- Dashboard con gráficos JFreeChart (saldos y transacciones)
- Gestión completa de clientes (CRUD con validaciones)
- Cuentas de ahorro: abrir, depositar, retirar con historial
- Planes de inversión con simulador de ganancias
- Gestión de usuarios y roles desde la interfaz
- Reportes PDF y Excel: clientes, cuentas, transacciones, planes
- Historial de transacciones en tiempo real

### Portal Cliente
- Vista personalizada solo con sus propios datos
- Mis cuentas de ahorro con saldo actual
- Mis transacciones recientes
- Mis planes de inversión con ganancia estimada
- Descarga de reportes PDF y Excel propios
- Fondo animado con partículas y líneas doradas

### Estética premium
- Paleta negro + dorado (#C9A84C)
- Splash screen con logo y barra de carga animada
- Login con fondo animado (líneas doradas + partículas)
- Ícono personalizado de la cooperativa

---

## 📌 Estado del proyecto

| Módulo                | Estado        |
|-----------------------|---------------|
| Modelo ER y BD        | ✅ Completado  |
| Scripts SQL           | ✅ Completado  |
| Historificación       | ✅ Completado  |
| Stored Procedures     | ✅ Completado  |
| Triggers              | ✅ Completado  |
| Vistas y consultas    | ✅ Completado  |
| Conexión JDBC         | ✅ Completado  |
| Interfaz Swing        | ✅ Completado  |
| Reportes PDF/Excel    | ✅ Completado  |
| Dashboard JFreeChart  | ✅ Completado  |
| Roles de usuario      | ✅ Completado  |
| Portal Cliente        | ✅ Completado  |
| Fondo animado         | ✅ Completado  |
| JAR ejecutable        | ✅ Completado  |

---

## 👨‍🎓 Autor

**Anthony Dorly Huilahuaña Chata**
Estudiante — SENATI, Tecnologías de la Información
Curso: Programación para Desarrollo de Software with Oracle

---

## 📄 Licencia

Este proyecto es de uso académico — SENATI 2025.
