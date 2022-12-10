# https://nightlies.apache.org/flink/flink-docs-master/docs/deployment/resource-providers/standalone/docker/
FROM maven:3.8-jdk-8-slim AS builder

COPY ./pom.xml /opt/pom.xml
COPY ./src /opt/src
RUN cd /opt; mvn clean install -Dmaven.test.skip

FROM flink:java8
COPY --from=builder /opt/target/flink-example-*.jar /opt/flink/usrlib/flink-example.jar
RUN echo "execution.checkpointing.interval: 10s" >> /opt/flink/conf/flink-conf.yaml; \
    echo "pipeline.object-reuse: true" >> /opt/flink/conf/flink-conf.yaml; \
    echo "pipeline.time-characteristic: EventTime" >> /opt/flink/conf/flink-conf.yaml; \
    echo "taskmanager.memory.jvm-metaspace.size: 256m" >> /opt/flink/conf/flink-conf.yaml; \
# entry point in docker-compose.yaml file
