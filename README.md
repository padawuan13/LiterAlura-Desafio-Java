LiterAlura - Catálogo de Libros Inteligente 📚

LiterAlura es una aplicación de consola desarrollada en Java utilizando Spring Boot. El proyecto consume datos de la API de Gutendex para buscar libros y autores, ofreciendo una experiencia bilingüe mediante la integración de un servicio de traducción automática. Los datos se persisten en una base de datos relacional PostgreSQL.

🚀 Características principales
El sistema cuenta con un menú interactivo que permite realizar las siguientes acciones:

Búsqueda inteligente por título: Permite buscar libros en español. La App traduce la búsqueda al inglés automáticamente para ampliar los resultados en el catálogo de Gutendex y luego guarda el libro y su autor en la base de datos.

Listado de libros registrados: Muestra todos los libros que has guardado localmente.

Listado de autores registrados: Muestra los escritores almacenados, evitando duplicados en la base de datos.

Filtro de autores vivos: Consulta qué autores estaban vivos en un año específico ingresado por el usuario.

Listado por idioma: Filtra tus libros guardados por idioma (español, inglés, francés, portugués).

Top 10 Libros más descargados: Muestra los 10 libros más populares de la API, traduciendo sus títulos al español en tiempo real mediante Java Streams.

🛠️ Tecnologías utilizadas
Java 17: Lenguaje principal de desarrollo.

Spring Boot 3.x: Framework para la gestión de dependencias y configuración.

Spring Data JPA: Para la persistencia de datos y mapeo objeto-relacional (ORM).

PostgreSQL: Motor de base de datos relacional.

Jackson: Para el procesamiento de datos JSON de las APIs.

Gutendex API: Fuente de datos de libros de dominio público.

MyMemory API: Servicio de traducción utilizado como puente lingüístico.

🏗️ Lógica de Desarrollo Destacada
Traducción y Limpieza de Datos
Se implementó un servicio de traducción dinámica que maneja errores y limpia los resultados mediante Expresiones Regulares (Regex), asegurando que los títulos se muestren sin caracteres basura o índices numéricos innecesarios.

Persistencia Inteligente
El sistema utiliza una lógica de guardado que verifica la existencia previa de un autor antes de crear un nuevo registro. Esto garantiza la integridad referencial y evita la redundancia de datos en PostgreSQL.

Uso de Java Streams
Se aplicaron operaciones de flujo (filter, map, sorted, limit) para procesar grandes volúmenes de datos de la API de forma eficiente y elegante.

📦 Configuración e Instalación
Clona este repositorio.

Asegúrate de tener instalado PostgreSQL y crear una base de datos llamada literalura.

Configura tus credenciales de base de datos en el archivo src/main/resources/application.properties:

Properties
spring.datasource.url=jdbc:postgresql://localhost:5432/literalura
spring.datasource.username=TU_USUARIO
spring.datasource.password=TU_CONTRASEÑA
spring.jpa.hibernate.ddl-auto=update
Ejecuta la aplicación desde tu IDE favorito (IntelliJ IDEA recomendado).

Desarrollado por:

Felipe Hernández - Analista Programador.
