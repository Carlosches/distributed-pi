Esta es la version distribuida de la tarea final del curso de arquitectura de software.

Implementado por:
 - Felipe Sanchez
 - Carlos Restrepo
 - César Canales


En esta version el proyecto se ejecuta en varias máquinas (nodos de procesamiento). 
El proyecto consta de tres componentes (Client, Broker y PointsGenerator)


Comandos para compilar:
 
   Componente Broker (ubicarse dentro de la carpeta Broker)
    - frascati compile src broker
    
    Componente PointsGenerator (ubicarse dentro de la carpeta PointsGenerator)
    - frascati compile src server

    Componente client  (ubicarse dentro de la carpeta Client)
    - frascati compile src client

Comandos para ejecutar componentes. Ejecute los componentes en el orden que sigue:

    Componente Broker (ubicarse dentro de la carpeta Broker)
    - frascati run distributed-pi-broker -libpath broker.jar run
    
    Componente PointsGenerator (ubicarse dentro de la carpeta PointsGenerator)
    - frascati run distributed-pi-generator -libpath server.jar -s r -m run

    Componente client  (ubicarse dentro de la carpeta Client)
    - frascati run distributed-pi-client -libpath client.jar -s r -m run

