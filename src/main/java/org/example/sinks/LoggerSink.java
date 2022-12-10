package org.example.sinks;

import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.functions.sink.RichSinkFunction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// using a custom sink to write to the output using a logger
// otherwise when running inside a Docker container the following Exception will occur
// FlinkException: The file STDOUT does not exist on the TaskExecutor.
// https://stackoverflow.com/questions/54036010/apache-flink-the-file-stdout-is-not-available-on-the-taskexecutor
public class LoggerSink extends RichSinkFunction<Tuple2<String, Integer>> {
    private final static Logger logger = LoggerFactory.getLogger(LoggerSink.class);

    @Override
    public void invoke(Tuple2<String, Integer> value, Context context) {
        logger.info("{} unconfirmed transactions.", value.f1);
    }
}
