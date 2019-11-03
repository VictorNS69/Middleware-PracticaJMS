# Servidor de Noticias

## Requisitos
- Maven 3.6.1
- Java 1.8.0
- OpenJDK 8
- OpenMQ 5.1.1

**Nota**: OpenMQ está autocontenido en este repositorio.

## Contenido del repositorio
### Estructura general
```
Middleware-PracticaJMS/
├── ClienteSN
│   ├── pom.xml
│   └── src
│       ├── main
│       │   └── java
│       │       └── es
│       │           └── upm
│       │               └── middleware
│       │                   ├── App.java
│       │                   └── Client.java
│       └── test
│           └── java
│               └── es
│                   └── upm
│                       └── middleware
│                           └── AppTest.java
├── docs
│   ├── formato_noticia.txt
│   └── Practica-JMS.pdf
├── noticias_ejemplo
│   ├── noticia_deportes.txt
│   ├── noticia_economia.txt
│   └── noticia_politica.txt
├── README.md
├── scripts
│   ├── install_mq.sh
│   ├── openmq5_1_1.zip
│   ├── run_broker.sh
│   ├── run_client.sh
│   └── run_server.sh
└── ServidorNoticias
    ├── pom.xml
    └── src
        ├── main
        │   └── java
        │       └── es
        │           └── upm
        │               └── middleware
        │                   ├── App.java
        │                   ├── ListHandler.java
        │                   ├── Noticia.java
        │                   └── Server.java
        └── test
            └── java
                └── es
                    └── upm
                        └── middleware
                            └── AppTest.java

```
- **ClienteSN**: Código fuente del cliente
- **docs**: Documentación relevante
- **scripts**: Scripts para la ejecución de los distintos componentes del proyecto
- **ServidorNoticias**: Código fuente del servidor
### Definición de clases
- Las clases **App.java** y **AppTest.java** tienen como función la de comprobar la correcta construcción del proyecto en Maven.
- Cliente:
  - **Client.java**: Contiene todo el código fuente del cliente
- Servidor:
  - **Noticia.java**: Contiene el objeto java _Noticia_
  - **ListHandler.java**: Contiene la funcionalidad de manejar todas las noticias
  - **Server.java**: Es el cuerpo principal del servidor, y el encargado de escuchar la llegada de mensajes
## Instalar el broker
Si no tienes instalado el broker, puedes instalarlo ejecutando el siguiente script desde el directorio raiz del proyecto:
```sh
./scripts/install_mq.sh
```
Esto creará la carpeta _mq_ en el directorio raiz del proyecto y te permitirá ejecutar el script _run_broker.sh_.

## Ejecución de la aplicación
Cada comando ha de ejecutarse en **distintos terminales**.

**Primero, ejecutar el broker**
```sh
./scripts/run_broker.sh
```
**Segundo, ejecutar el servidor**
```sh
./scripts/run_server.sh
```
**Por ultimo, ejecutar tantos clientes como se desee**
```sh
./scripts/run_cliente.sh
```

## Formato de las noticias para importar desde un archivo
Para que la aplicación funcione correctamente, las noticias a importar desde un archivo tienen que seguir el formato de [este fichero](/docs/formato_noticia.txt).

No es necesaria ninguna extensión de archivo, ni ninguna ruta específica, ya que a la hora de ingresar la noticia desde un archivo, se tendrá que **escribir la ruta absoluta del archivo**.

Para mayor comodidad, se ha añadido una carpeta [_noticias_ejemplo_](/noticias_ejemplo) donde hay varios ejemplos de noticias para importar directamente.
## Autores
- [Víctor Nieves Sánchez](https://github.com/VictorNS69/) (Scrum Master & Developer)
- Ruben Garcia Garcia (Product Owner & Developer)
- Eduardo Freyre Gomez (Developer)
- Miguel Gonzalo Anton (Developer)
