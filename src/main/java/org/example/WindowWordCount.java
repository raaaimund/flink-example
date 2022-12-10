package org.example;

import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.assigners.TumblingProcessingTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.util.Collector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// tutorial - https://nightlies.apache.org/flink/flink-docs-master/docs/dev/datastream/overview/
// run "ncat -lk 9999" first on command line
public class WindowWordCount {
    private final static Logger logger = LoggerFactory.getLogger(WindowWordCount.class);
    private final static String host = "localhost";
    private final static int port = 9999;
    private final static String fieldNameToSum = "f1";
    private final static Integer windowSizeInSeconds = 5;

    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        DataStream<Tuple2<String, Integer>> dataStream = env
                .socketTextStream(host, port)
                .flatMap(new WordSplitter())
                .keyBy(word -> word.f0)
                .window(TumblingProcessingTimeWindows.of(Time.seconds(windowSizeInSeconds)))
                .sum(fieldNameToSum);
        dataStream.print();
        logger.info("Listening to ws://{}:{}.", host, port);
        env.execute("WordCount");
    }

    // if there are multiple words in a line, split them into separate words
    public static class WordSplitter implements FlatMapFunction<String, Tuple2<String, Integer>> {
        @Override
        public void flatMap(String sentence, Collector<Tuple2<String, Integer>> out) {
            for (String word: sentence.split(" ")) {
                out.collect(new Tuple2(word, 1));
            }
        }
    }
}