# flink-example

This is a simple example of how to use Apache Flink with Java 1.8.

## WindowWordCount
This example is based on the [WindowWordCount](https://nightlies.apache.org/flink/flink-docs-master/docs/dev/datastream/overview/#example-program)
example from the Flink documentation. The [Apache Spark documentation](https://spark.apache.org/docs/latest/streaming-programming-guide.html#a-quick-example)
also covers this example. It listens to a socket and counts the words inside a 5 seconds window. You can find the example in the [WindowWordCount.java](src/main/java/org/example/WindowWordCount.java)
file. This example is not dockerized.

- [socketTextStream](https://nightlies.apache.org/flink/flink-docs-master/docs/dev/datastream/overview/) listens to a socket
- [flatMap](https://nightlies.apache.org/flink/flink-docs-master/docs/dev/datastream/operators/overview/#flatmap) splits the lines into words
- [keyBy](https://nightlies.apache.org/flink/flink-docs-master/docs/dev/datastream/operators/overview/#keyby) groups the words by the word itself and creates a [Keyed DataStream](https://nightlies.apache.org/flink/flink-docs-master/docs/dev/datastream/fault-tolerance/state/)
- [window](https://nightlies.apache.org/flink/flink-docs-master/docs/dev/datastream/operators/windows/) splits the stream into "buckets" of 5 seconds
- sum sums the words using an [AggregateFunction](https://nightlies.apache.org/flink/flink-docs-master/docs/dev/datastream/operators/windows/#aggregatefunction)

You can use netcat to write to a socket.

Linux
```bash
nc -lk 9999
```

Windows
```bash
ncat -lk 9999
```

You can get the installation of netcat for windows from [here](https://nmap.org/dist/nmap-7.93-setup.exe).

Other useful links
- https://stackoverflow.com/questions/55018206/flink-streaming-what-exactly-does-sum-do

## BlockchainUnconfirmedTransactions
In this example, we listen to the [Blockchain WebSocket API](https://www.blockchain.com/explorer/api/api_websocket) and
subscribe to the unconfirmed transactions. Again, we count the unconfirmed transactions within a 5 seconds window.
You can find the example in the [BlockchainUnconfirmedTransactions.java](src/main/java/org/example/BlockchainUnconfirmedTransactions.java)
file.

- [addSource](https://nightlies.apache.org/flink/flink-docs-master/docs/dev/datastream/sources/) adds a source to the stream which connects to a WebSocket and subscribes to the unconfirmed transactions
- [map](https://nightlies.apache.org/flink/flink-docs-master/docs/dev/datastream/operators/overview/#map) maps the JSON string to a simple key value pair of type Tuple2 ("Transaction", 1)
- [keyBy](https://nightlies.apache.org/flink/flink-docs-master/docs/dev/datastream/operators/overview/#keyby) groups the tuples by the key itself and creates a [Keyed DataStream](https://nightlies.apache.org/flink/flink-docs-master/docs/dev/datastream/fault-tolerance/state/)
- [window](https://nightlies.apache.org/flink/flink-docs-master/docs/dev/datastream/operators/windows/) splits the stream into "buckets" of 5 seconds
- sum sums the transactions using an [AggregateFunction](https://nightlies.apache.org/flink/flink-docs-master/docs/dev/datastream/operators/windows/#aggregatefunction)
- [addSink](https://nightlies.apache.org/flink/flink-docs-master/docs/dev/datastream/overview/#data-sinks) adds a sink to the stream which uses the [logger](src/main/java/org/example/sinks/LoggerSink.java) to print the results
   - when using print and running inside a Docker container an ``FlinkException: The file STDOUT does not exist on the TaskExecutor`` will be thrown (https://stackoverflow.com/questions/54036010/apache-flink-the-file-stdout-is-not-available-on-the-taskexecutor)
   - the logger sink is a simple workaround for this issue

The following tutorials were used creating this example:
- https://jbcodeforce.github.io/flink-studies/programming/
- https://medium.com/coinmonks/the-journey-of-learning-apache-flink-3-streaming-unconfirmed-bitcoin-transactions-f4d636e77de9
- https://gist.github.com/tonvanbart/17dc93be413f7c53b76567e10b87a141
- https://docs.cloudera.com/csa/1.4.0/development/topics/csa-datastream-dev.html
- https://github.com/okkam-it/flink-examples
- https://github.com/mushketyk/flink-examples
- https://github.com/apache/flink/tree/master/flink-examples

### Docker

The example is dockerized and can be run using the following command:

```bash
docker-compose up
```

This will start one task manager and one job manager. The job manager will start the BlockchainUnconfirmedTransactions.
The Flink UI can be accessed at http://localhost:8081.

Don't forget to build, if any code changes are made.

```bash
docker-compose build
```