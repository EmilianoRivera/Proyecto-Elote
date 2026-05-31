# 🚀 Proyecto Eskiloko 

Este es el repositorio del backend de la aplicación **Eskilokos**. El proyecto está construido sobre **Spring Boot** y utiliza **PostgreSQL** montado en un contenedor de **Docker** para la persistencia de datos. Mediante Hibernate/JPA, las tablas de la base de datos se generan automáticamente al iniciar la aplicación.

A continuación, se detallan los pasos necesarios para configurar el entorno local y ejecutar el proyecto desde cero.

---

## 🛠️ Prerrequisitos

Antes de comenzar, asegúrate de tener instaladas las siguientes herramientas en tu sistema operativo:

* **Java Development Kit (JDK):** Versión 21 o superior.
* **Apache Maven:** Para la gestión de dependencias y construcción del proyecto.
* **Docker & Docker Compose:** Necesarios para levantar el contenedor de la base de datos.
* **IDE de desarrollo:** IntelliJ IDEA (recomendado), Eclipse o VS Code.

---

## 🏎️ Pasos para la Ejecución del Proyecto

Sigue estos tres sencillos pasos en orden cronológico para desplegar la aplicación:

### Paso 1: Configurar las Variables de Entorno (`.env`)
El proyecto utiliza un archivo `.env` para externalizar las credenciales de la base de datos y evitar exponer información sensible en el código de configuración de Docker.

1. En la raíz principal del proyecto (donde se encuentra el archivo `compose.yaml`), crea un archivo de texto y nómbralo exactamente como:
   ```env
   .env

Abre IntelliJ ve a  ` Edit Configurations > Modify options > Environment Variables`. Posteriormente regresa a la ventana de Run/Debug Configurations y añade en formato VAR=VALUE;VAR1=VALUE1
las siguientes variables de entorno:
   ```Environment Variables
      POSTGRES_DB=text;DB_USER=text;DB_PASSWORD=text;INTERNAL_PORT=text;DB_URL=text
  ```

También en vez de escribirlo puede subir un archivo .env


### Paso 2: Levantar el Contenedor de la Base de Datos (Docker)

Una vez configuradas las credenciales, procedemos a inicializar el contenedor que hospedará el motor de PostgreSQL.

Abre una terminal o consola de comandos en la raíz del proyecto.

Ejecuta la instrucción de Docker Compose para descargar la imagen y levantar el servicio en segundo plano (detached mode):
    
  ``` bash
    docker-compose up -d
    docker ps
 ```

### Paso 3: Ejecutar la Aplicación de Spring Boot (Generación de Tablas)

Con la base de datos en línea y escuchando peticiones, es momento de arrancar el servidor web de desarrollo.

Abre el proyecto en tu IDE preferido (ej. IntelliJ IDEA).

Ejecuta la clase principal de la aplicación (la que contiene la anotación @SpringBootApplication).


   
