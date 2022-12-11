package org.example;

import org.apache.flink.api.common.RuntimeExecutionMode;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.Table;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;
import org.apache.flink.types.Row;
import org.example.sinks.RowLoggerSink;
import org.example.sources.BlockchainWebSocketSource;

import static org.apache.flink.table.api.Expressions.$;

// https://jbcodeforce.github.io/flink-studies/programming/
// https://medium.com/coinmonks/the-journey-of-learning-apache-flink-3-streaming-unconfirmed-bitcoin-transactions-f4d636e77de9
// https://gist.github.com/tonvanbart/17dc93be413f7c53b76567e10b87a141
// https://docs.cloudera.com/csa/1.4.0/development/topics/csa-datastream-dev.html
// https://nightlies.apache.org/flink/flink-docs-master/docs/dev/table/common/#create-a-tableenvironment
public class BlockchainUnconfirmedTransactionsUsingTable {
    private final static String websocketUrl = "wss://ws.blockchain.info/inv";

    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment streamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment();
        streamExecutionEnvironment.setRuntimeMode(RuntimeExecutionMode.STREAMING);
        StreamTableEnvironment tableEnvironment = StreamTableEnvironment.create(streamExecutionEnvironment);
        DataStream dataStream = streamExecutionEnvironment
                .addSource(new BlockchainWebSocketSource(websocketUrl))
                .name("BlockchainWebSocketSource");
        Table transactions = tableEnvironment.fromDataStream(dataStream);
        // ToDo: map JSON to object
        transactions.select($("f0").as("transaction"));
        // https://nightlies.apache.org/flink/flink-docs-stable/docs/dev/datastream/overview/#data-sinks
        // used sink instead of printing with dataStream.print();
        DataStream<Row> resultStream = tableEnvironment.toDataStream(transactions);
        resultStream.addSink(new RowLoggerSink()).name("LoggerSink");
        streamExecutionEnvironment.execute("Blockchain unconfirmed transactions using Table API");
    }
}