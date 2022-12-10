package org.example.mapper;

import org.apache.flink.api.common.functions.RichMapFunction;
import org.apache.flink.api.java.tuple.Tuple2;

public class UnconfirmedTransactionMapper extends RichMapFunction<String, Tuple2<String, Integer>> {
    @Override
    public Tuple2<String, Integer> map(String s) {
        return new Tuple2<String, Integer>("Transaction", 1);
    }
}
