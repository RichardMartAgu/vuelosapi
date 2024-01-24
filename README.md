Descripción del Proyecto
Este proyecto implementa un sistema de gestión para una aerolínea, centrado en el control de diversas características relacionadas con vuelos.
La aplicación permite realizar operaciones CRUD completas, incluyendo la posibilidad de añadir, listar, editar y borrar registros en diferentes entidades. 
Las entidades gestionadas y sus respectivas características son las siguientes:

Aeropuertos:

Ubicación
Nombre
Ciudad
Estado actual de actividad
Año de fundación

Aerolíneas:

Nombre
Teléfono
Número de flota
Media de vuelos puntuales
Estado actual de actividad
Año de fundación

Aviones:

Modelo
Capacidad de pasajeros
Velocidad máxima
Estado actual de actividad
Fecha de manufacturación

Tiendas de Aeropuertos:

Nombre
Tipo de establecimiento
Teléfono
Beneficio
Estado actual de actividad
Día de apertura

Vuelos:

Nombre
Puerta de embarque
Duración del vuelo
Aeropuerto de origen o destino
Fecha de salida

Pasajeros:

Nombre
Apellido
DNI
Día de cumpleaños
Edad

Tickets:

Número de asiento
Fecha de expedición
Equipaje
Premium o no

Relaciones entre Entidades
Listar vuelos asociados a un aeropuerto.
Listar aviones asociados a una aerolínea.
Listar tiendas asociadas a un aeropuerto.
Listar tickets asociados a un pasajero.
Listar tickets asociados a un vuelo.

Puesta en Marcha
- Clonar el Repositorio:

- Abrir en IntelliJ:

Abre IntelliJ IDEA.
Selecciona "File" -> "Open" y elige la carpeta del proyecto clonado.

- Configuración del Proyecto:

Asegúrate de tener configurado el SDK de Java y las dependencias de Spring Boot.
Actualiza las configuraciones de la base de datos según tu entorno en el archivo application.properties.
Ejecutar la Aplicación:

- Ejecuta la aplicación usando el comando Spring Boot:

./mvnw spring-boot:run

Documentación de la API:
Accede a la documentación de la API para conocer y utilizar todos los métodos proporcionados. La documentación estará disponible en el propio repositiorio en el ymel.

Tambien he añadido una colección Postman para probar todas las operaciones desarrolladas.





