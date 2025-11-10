# Proyecto: Excel2Database

Este proyecto Java (Maven) permite leer un archivo Excel (.xlsx) y volcar sus datos en una base de datos MySQL, o viceversa (exportar la base de datos a un archivo Excel).
Está diseñado como una herramienta educativa para comprender la interacción entre Java, JDBC, Apache POI y MySQL en un entorno controlado con Docker Desktop.

## Estructura del proyecto
```bash 
excel2database/
├── src/main/java/com/iesvdc/dam/acceso/
│   ├── Excel2Database.java              # Clase principal (menú)
│   ├── conexion/
│   │   ├── Conexion.java                # Conexión con MySQL mediante JDBC
│   │   └── Config.java                  # Carga del fichero config.properties
│   └── excelutil/
│       ├── ExcelReader.java             # Lee un Excel y genera tablas en la BBDD
│       └── ExcelWriter.java             # (opcional) Exporta la base de datos a Excel
├── datos/
│   ├── personasPrueba.xlsx              # Archivo Excel de entrada
│   └── agenda.xlsx                      # Archivo Excel de salida
├── db/
│   └── init.sql                         # Script inicial de la base de datos (agenda)
├── docker-compose.yml                   # Contenedor MySQL + Adminer
├── config.properties                    # Configuración de conexión a la BBDD
└── pom.xml                              # Dependencias y configuración Maven
```

## Configuración del entorno con Docker Desktop

Este proyecto utiliza Docker Compose para crear y administrar la base de datos MySQL y la herramienta de administración Adminer.

## Contenedor de base de datos
```sql 
version: '3.1'

services:
  db-excel:
    image: mysql:latest
    restart: "no"
    environment:
      MYSQL_ROOT_PASSWORD: s83n38DGB8d72
    ports:
      - 33307:3306
    volumes:
      - ./db:/docker-entrypoint-initdb.d

  adminer:
    image: adminer:latest
    restart: "no"
    ports:
      - 8181:8080
```

## Explicación

db-excel → crea una base de datos MySQL.

Se inicia con el script `db/init.sql` (si existe).

Contraseña de root: s83n38DGB8d72

Puerto externo: 33307

adminer → interfaz web para gestionar la base de datos.

Accesible en: http://localhost:8181

## Ejecución

Desde la raíz del proyecto, ejecuta:
```bash
docker-compose up
```


Esto levantará la base de datos y Adminer.
Una vez esté en marcha, podrás entrar a Adminer y conectarte con estos datos:

| Parámetro     | Valor                   |
|----------------|--------------------------|
| Sistema        | MySQL                   |
| Servidor       | db-excel o localhost    |
| Usuario        | root                    |
| Contraseña     | s83n38DGB8d72           |
| Base de datos  | agenda                  |

## Archivo config.properties

El archivo config.properties contiene la configuración que Java usa para conectarse a la base de datos y localizar los archivos Excel:
```properties
user=root
password=s83n38DGB8d72
useUnicode=yes
useJDBCCompliantTimezoneShift=true
port=33307
database=agenda
host=localhost
driver=MySQL
outputFile=datos/agenda.xlsx
inputFile=datos/personasPrueba.xlsx
useSSL=false
serverTimezone=Europe/Madrid
allowPublicKeyRetrieval=true
```

## Funcionamiento general

El programa principal es Excel2Database.java, que presenta un menú con dos opciones:
```csharp
Selecciona la opción que desea realizar:
1) Agregar la base de datos desde excel.
2) Guardar la base de datos en un excel.
```

### Opción 1: Cargar Excel en la base de datos

El método `ExcelReader.loadToDataBase():`

Lee el archivo Excel indicado en config.properties (inputFile).

Por cada hoja del Excel:

Crea una tabla en la base de datos con el mismo nombre.

Usa la primera fila como nombres de columnas.

Usa la segunda fila para inferir el tipo de dato.

Inserta todas las filas restantes como registros.

| nombre | apellidos | teléfono   | género    |
|--------|-----------|------------|-----------|
| texto  | texto     | texto      | texto     |
| Ana    | López     | 654321987  | FEMENINO  |
| Juan   | Pérez     | 678912345  | MASCULINO | 

Esto generará automáticamente:
```sql
CREATE TABLE Hoja1 (
  nombre VARCHAR(300) NOT NULL,
  apellidos VARCHAR(300) NOT NULL,
  teléfono VARCHAR(12),
  género ENUM('FEMENINO','MASCULINO','NEUTRO','OTRO') NOT NULL
) ENGINE=InnoDB;

INSERT INTO Hoja1 (nombre, apellidos, teléfono, género)
VALUES ('Ana','López','654321987','FEMENINO'),
       ('Juan','Pérez','678912345','MASCULINO');
```

### Opción 2: Exportar la base de datos a Excel

El método ExcelWriter.loadDatabaseDatos() (si está implementado) realiza la operación inversa:

Lee las tablas de la base de datos.

Crea un archivo Excel (outputFile) con los datos exportados.

## Dependencias principales

Definidas en el archivo pom.xml:

 | Librería          |	Descripción          |
 |-------------------------------------------|
 | mysql-connector-j | Conexión JDBC a MySQL |
 | poi y poi-ooxml |	Lectura/escritura de archivos Excel (Apache POI) | 
 | junit	| Pruebas unitarias (opcional) |
## Ejecución del programa

Inicia los contenedores:

`docker-compose up`


Abre el proyecto en tu IDE (Eclipse, IntelliJ o VS Code).

Ejecuta la clase Excel2Database.java.

Selecciona la opción del menú:

1 → Leer Excel y cargar datos en MySQL.

2 → Exportar datos de MySQL a Excel.

## Conceptos clave del proyecto

Apache POI: Librería para manipular ficheros .xlsx.

JDBC: API de Java para conectar y ejecutar comandos en una base de datos.

Docker Compose: Permite crear entornos reproducibles para bases de datos.

Config.properties: Centraliza toda la configuración del proyecto.

Automatización SQL: El programa genera las sentencias CREATE TABLE e INSERT INTO dinámicamente según el contenido del Excel.