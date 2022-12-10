package org.example.sinks;

import org.apache.flink.streaming.api.functions.sink.RichSinkFunction;
import org.apache.flink.types.Row;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RowLoggerSink extends RichSinkFunction<Row> {
    private final static Logger logger = LoggerFactory.getLogger(RowLoggerSink.class);

    @Override
    public void invoke(Row value, Context context) {
        logger.info("{}", value.toString());
    }
}
