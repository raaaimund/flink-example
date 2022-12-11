package org.example;

import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.assigners.TumblingProcessingTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.example.mapper.UnconfirmedTransactionMapper;
import org.example.sinks.LoggerSink;
import org.example.sources.BlockchainWebSocketSource;

// https://jbcodeforce.github.io/flink-studies/programming/
// https://medium.com/coinmonks/the-journey-of-learning-apache-flink-3-streaming-unconfirmed-bitcoin-transactions-f4d636e77de9
// https://gist.github.com/tonvanbart/17dc93be413f7c53b76567e10b87a141
// https://docs.cloudera.com/csa/1.4.0/development/topics/csa-datastream-dev.html
public class BlockchainUnconfirmedTransactions {
    private final static String fieldNameToSum = "f1";
    private final static Integer windowSizeInSeconds = 5;
    private final static String websocketUrl = "wss://ws.blockchain.info/inv";

    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        DataStream<Tuple2<String, Integer>> dataStream = env
                // https://nightlies.apache.org/flink/flink-docs-stable/docs/dev/datastream/overview/#data-sources
                .addSource(new BlockchainWebSocketSource(websocketUrl))
                .map(new UnconfirmedTransactionMapper())
                .keyBy(transaction -> transaction.f0)
                .window(TumblingProcessingTimeWindows.of(Time.seconds(windowSizeInSeconds)))
                .sum(fieldNameToSum);
        // https://nightlies.apache.org/flink/flink-docs-stable/docs/dev/datastream/overview/#data-sinks
        // used sink instead of printing with dataStream.print();
        dataStream.addSink(new LoggerSink()).name("LoggerSink");
        env.execute("Blockchain unconfirmed transactions");
    }
}