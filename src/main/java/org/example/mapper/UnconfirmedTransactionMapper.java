package org.example.mapper;

import org.apache.flink.api.common.functions.RichMapFunction;
import org.apache.flink.api.java.tuple.Tuple2;

public class UnconfirmedTransactionMapper extends RichMapFunction<String, Tuple2<String, Integer>> {
    // in
    // {"op":"utx","x":{"lock_time":766818,"ver":2,"size":386,"inputs":[{"sequence":38383838,"prev_out":{"spent":true,"tx_index":0,"type":0,"addr":"3ArGd4jYUMweNPAnhxbpjB5YtFFYCf5sWE","value":3524000,"n":86,"script":"a91464792540e3a8b45808d5c54cd2593a593b0de00d87"},"script":"160014656ae10500628e35a70f04fd1916946e56bd5fbf"},{"sequence":4294967294,"prev_out":{"spent":true,"tx_index":0,"type":0,"addr":"36rJygLtpSHgV8CEtTZokr9F4x4TRM2tCL","value":717836,"n":3,"script":"a914389a9bcc4be95632ca8b941d4f30fb9f48be168587"},"script":"160014272aced89e65a131010dd2b4010ebe858bce24a8"}],"time":1670697603,"tx_index":0,"vin_sz":2,"hash":"a350334fe5db4128cb459cf08e3c4ca41ccaa1388152e37e9900f11b9829f96d","vout_sz":1,"relayed_by":"0.0.0.0","out":[{"spent":false,"tx_index":0,"type":0,"addr":"3JM5fPsdUnsCUVBGLNf84AUsYcR1q6Uaot","value":4237788,"n":0,"script":"a914b6b4f0aa258a2ad56dc61636d407401eaa1fdf1587"}]}}
    // {"op":"utx","x":{"lock_time":242424,"ver":2,"size":386,"inputs":[{"sequence":4294967294,"prev_out":{"spent":true,"tx_index":0,"type":0,"addr":"3ArGd4jYUMweNPAnhxbpjB5YtFFYCf5sWE","value":3524000,"n":86,"script":"a91464792540e3a8b45808d5c54cd2593a593b0de00d87"},"script":"160014656ae10500628e35a70f04fd1916946e56bd5fbf"},{"sequence":4294967294,"prev_out":{"spent":true,"tx_index":0,"type":0,"addr":"36rJygLtpSHgV8CEtTZokr9F4x4TRM2tCL","value":717836,"n":3,"script":"a914389a9bcc4be95632ca8b941d4f30fb9f48be168587"},"script":"160014272aced89e65a131010dd2b4010ebe858bce24a8"}],"time":1670697603,"tx_index":0,"vin_sz":2,"hash":"a350334fe5db4128cb459cf08e3c4ca41ccaa1388152e37e9900f11b9829f96d","vout_sz":1,"relayed_by":"0.0.0.0","out":[{"spent":false,"tx_index":0,"type":0,"addr":"3JM5fPsdUnsCUVBGLNf84AUsYcR1q6Uaot","value":4237788,"n":0,"script":"a914b6b4f0aa258a2ad56dc61636d407401eaa1fdf1587"}]}}
    // {"op":"utx","x":{"lock_time":123949,"ver":2,"size":386,"inputs":[{"sequence":29239239,"prev_out":{"spent":true,"tx_index":0,"type":0,"addr":"3ArGd4jYUMweNPAnhxbpjB5YtFFYCf5sWE","value":3524000,"n":86,"script":"a91464792540e3a8b45808d5c54cd2593a593b0de00d87"},"script":"160014656ae10500628e35a70f04fd1916946e56bd5fbf"},{"sequence":4294967294,"prev_out":{"spent":true,"tx_index":0,"type":0,"addr":"36rJygLtpSHgV8CEtTZokr9F4x4TRM2tCL","value":717836,"n":3,"script":"a914389a9bcc4be95632ca8b941d4f30fb9f48be168587"},"script":"160014272aced89e65a131010dd2b4010ebe858bce24a8"}],"time":1670697603,"tx_index":0,"vin_sz":2,"hash":"a350334fe5db4128cb459cf08e3c4ca41ccaa1388152e37e9900f11b9829f96d","vout_sz":1,"relayed_by":"0.0.0.0","out":[{"spent":false,"tx_index":0,"type":0,"addr":"3JM5fPsdUnsCUVBGLNf84AUsYcR1q6Uaot","value":4237788,"n":0,"script":"a914b6b4f0aa258a2ad56dc61636d407401eaa1fdf1587"}]}}
    // {"op":"utx","x":{"lock_time":8548499,"ver":2,"size":386,"inputs":[{"sequence":19294248,"prev_out":{"spent":true,"tx_index":0,"type":0,"addr":"3ArGd4jYUMweNPAnhxbpjB5YtFFYCf5sWE","value":3524000,"n":86,"script":"a91464792540e3a8b45808d5c54cd2593a593b0de00d87"},"script":"160014656ae10500628e35a70f04fd1916946e56bd5fbf"},{"sequence":4294967294,"prev_out":{"spent":true,"tx_index":0,"type":0,"addr":"36rJygLtpSHgV8CEtTZokr9F4x4TRM2tCL","value":717836,"n":3,"script":"a914389a9bcc4be95632ca8b941d4f30fb9f48be168587"},"script":"160014272aced89e65a131010dd2b4010ebe858bce24a8"}],"time":1670697603,"tx_index":0,"vin_sz":2,"hash":"a350334fe5db4128cb459cf08e3c4ca41ccaa1388152e37e9900f11b9829f96d","vout_sz":1,"relayed_by":"0.0.0.0","out":[{"spent":false,"tx_index":0,"type":0,"addr":"3JM5fPsdUnsCUVBGLNf84AUsYcR1q6Uaot","value":4237788,"n":0,"script":"a914b6b4f0aa258a2ad56dc61636d407401eaa1fdf1587"}]}}
    // out
    // Transaction, 1
    // Transaction, 1
    // Transaction, 1
    // Transaction, 1
    @Override
    public Tuple2<String, Integer> map(String unconfirmedTransaction) {
        return new Tuple2<>("Transaction", 1);
    }
}
