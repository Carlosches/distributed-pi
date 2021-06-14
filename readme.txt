Comandos para compilar:

    - frascati compile src broker
    - frascati compile src server
    - frascati compile src client

Comandos para ejecutar componentes. Ejecute los componentes en el orden que sigue:

    Componente Broker
    - frascati run distributed-pi-broker -libpath broker.jar run
    
    Componente PointsGenerator
    - frascati run distributed-pi-generator -libpath server.jar -s r -m run

    Componente client
    - frascati run distributed-pi-client -libpath client.jar -s r -m run

