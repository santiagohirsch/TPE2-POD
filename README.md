# TPE2-POD: Multas de Estacionamiento
## Grupo 12
Implementación una aplicación de consola que utiliza el modelo de programación MapReduce junto con el framework HazelCast para el procesamiento de multas de estacionamiento, basado en datos reales. 

## Instrucciones de Compilación

Para compilar el proyecto se deben ejecutar los siguientes comandos desde la carpeta raíz:

```bash
chmod u+x compile.sh 
./compile.sh 
```

## Instrucciones de Ejecución

Para ejecutar el servidor se deben ejecutar los siguientes comandos desde la carpeta raíz:

```bash
cd tmp/tpe2-g12-server-1.0-SNAPSHOT
./run-server.sh -Dmask='192.168.0.*'
```

Para ejecutar el cliente se deben ejecutar los siguientes comandos desde la carpeta raíz:

```bash
cd tmp/tpe2-g12-client-1.0-SNAPSHOT
sh queryX.sh -Daddresses="xx.xx.xx.xx:XXXX;yy.yy.yy.yy:YYYY" -Dcity=ABC -DinPath=XX -DoutPath=YY [params]
```

donde:

`queryX.sh` es el script que corre la query X.

`Daddresses` refiere a las direcciones IP de los nodos con sus puertos (una o más, separadas por punto y coma).

`Dcity` indica con qué dataset de ciudad se desea trabajar. Los únicos valores posibles son NYC y CHI.

`DinPath` indica el path donde están los archivos de entrada de multas e infracciones

`DoutPath` indica el path donde estarán ambos archivos de salida query1.csv y time1.txt.

`[params]` son los parámetros extras que corresponden para algunas queries.