#!/bin/bash
echo "Start: Sleep 30 seconds"
sleep 30;

# Creando el topic 'created.transaction'
echo "Creando el topic  =>> 'ms.sling.search.write.v1'"
kafka-topics --create --if-not-exists --zookeeper zookeeper:2181 --partitions 1 --replication-factor 1 --topic 'ms.sling.search.write.v1'
echo "topic 'ms.sling.search.write.v1' creado"

# Comando de espera infinita para mantener el contenedor en ejecución
tail -f /dev/null