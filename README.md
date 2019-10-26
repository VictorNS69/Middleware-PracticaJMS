# Servidor de Noticias

## Requisitos
- Maven 3.6.1
- Java 1.8.0
- OpenJDK 8
- OpenMQ 5.1.1

## Instalar el broker
Si no tienes instalado el broker, puedes instalarlo ejecutando el siguiente comando:
```sh
unzip scripts/openmq5_1_1.zip
```
Esto creará la carpeta _mq_ en el directorio raiz y te permitirá ejecutar el script _run_broker.sh_.

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
## Autores
- Víctor Nieves Sánchez (Scrum Master & Developer)
- Ruben Garcia Garcia (Product Owner & Developer)
- Eduardo Freyre Gomez (Developer)
- Miguel Gonzalo Anton (Developer)
