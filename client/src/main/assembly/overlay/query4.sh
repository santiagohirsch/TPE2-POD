#!/bin/bash

PATH_TO_CODE_BASE=`pwd`

#JAVA_OPTS="-Djava.rmi.server.codebase=file://$PATH_TO_CODE_BASE/lib/jars/rmi-params-client-1.0-SNAPSHOT.jar"

MAIN_CLASS="ar.edu.itba.pod.tp2.client.Client"

ARGS=("query4")

ARGS+=("$@")

java $JAVA_OPTS -cp 'lib/jars/*'  $MAIN_CLASS "${ARGS[@]}"