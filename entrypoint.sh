#!/usr/bin/env sh
java \
    -javaagent:dd-java-agent.jar \
    -Ddd.trace.db.client.split-by-instance \
    -Ddd.service=ps-backend \
    -Ddd.profiling.enabled=false \
    -XX:FlightRecorderOptions=stackdepth=256 \
    -Ddd.version=TAG_VERSION \
    -jar "app.jar"

