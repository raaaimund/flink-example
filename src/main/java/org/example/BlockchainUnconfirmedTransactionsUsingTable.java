package org.example;

import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.Table;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;
import org.apache.flink.types.Row;
import org.example.sinks.RowLoggerSink;
import org.example.sources.BlockChainStreamingSource;

import static org.apache.flink.table.api.Expressions.$;

// https://jbcodeforce.github.io/flink-studies/programming/
// https://medium.com/coinmonks/the-journey-of-learning-apache-flink-3-streaming-unconfirmed-bitcoin-transactions-f4d636e77de9
// https://gist.github.com/tonvanbart/17dc93be413f7c53b76567e10b87a141
// https://docs.cloudera.com/csa/1.4.0/development/topics/csa-datastream-dev.html
// https://nightlies.apache.org/flink/flink-docs-master/docs/dev/table/common/#create-a-tableenvironment
public class BlockchainUnconfirmedTransactionsUsingTable {
    private final static Integer windowSizeInSeconds = 5;
    private final static String websocketUrl = "wss://ws.blockchain.info/inv";

    // {"op":"utx","x":{"lock_time":0,"ver":2,"size":151,"inputs":[{"sequence":4294967295,"prev_out":{"spent":true,"tx_index":0,"type":0,"addr":"bc1p59v673aezjuyg5gnxfftwxkqnz059j5te7j2p9gjqnyvcduy82yskgtgku","value":2500,"n":0,"script":"5120a159af47b914b84451133252b71ac0989f42ca8bcfa4a0951204c8cc37843a89"},"script":""}],"time":1670627753,"tx_index":0,"vin_sz":1,"hash":"7731b35a8724b66314102b6f2f71601c2acfe62f0cb6319cfc525292480a0650","vout_sz":1,"relayed_by":"0.0.0.0","out":[{"spent":false,"tx_index":0,"type":0,"addr":"bc1qqpapk39nhlghh0kyjm5j8dh9zsljr0wrw9h0cf","value":2349,"n":0,"script":"0014007a1b44b3bfd17bbec496e923b6e5143f21bdc3"}]}}
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment streamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment();
        StreamTableEnvironment tableEnvironment = StreamTableEnvironment.create(streamExecutionEnvironment);
        DataStream dataStream = streamExecutionEnvironment.addSource(new BlockChainStreamingSource(websocketUrl));
        Table transactions = tableEnvironment.fromDataStream(dataStream);
        // ToDo: map JSON to object
        transactions.select($("f0"));
        // https://nightlies.apache.org/flink/flink-docs-stable/docs/dev/datastream/overview/#data-sinks
        // used sink instead of printing with dataStream.print();
        DataStream<Row> resultStream = tableEnvironment.toDataStream(transactions);
        resultStream.addSink(new RowLoggerSink()).name("LoggerSink");
        streamExecutionEnvironment.execute("Blockchain unconfirmed transactions");
    }
}