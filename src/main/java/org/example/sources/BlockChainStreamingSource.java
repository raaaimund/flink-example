package org.example.sources;

import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.source.RichSourceFunction;
import org.asynchttpclient.AsyncHttpClient;
import org.asynchttpclient.BoundRequestBuilder;
import org.asynchttpclient.Dsl;
import org.asynchttpclient.ws.WebSocketUpgradeHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

// https://www.blockchain.com/explorer/api/api_websocket
public class BlockChainStreamingSource extends RichSourceFunction<String> {
    private final static Logger logger = LoggerFactory.getLogger(BlockChainStreamingSource.class);

    private transient AsyncHttpClient httpClient;
    private transient BoundRequestBuilder boundRequestBuilder;
    private transient WebSocketUpgradeHandler.Builder websockerBuilder;

    private boolean isRunning;
    private final BlockingQueue<String> unconfirmedTransactions;
    private final String url;

    public BlockChainStreamingSource(String url) {
        this.url = url;
        this.isRunning = true;
        this.unconfirmedTransactions = new ArrayBlockingQueue<>(100);
    }

    @Override
    public void run(SourceContext<String> sourceContext) throws Exception {
        WebSocketUpgradeHandler webSocketUpgradeHandler = websockerBuilder
                .addWebSocketListener(new BlockchainWebSocketListener(unconfirmedTransactions)).build();
        boundRequestBuilder.execute(webSocketUpgradeHandler).get();

        while (isRunning) {
            sourceContext.collect(unconfirmedTransactions.take());
        }

        isRunning = false;
    }

    @Override
    public void cancel() {
        logger.info("Websocket connection {} cancelled.", url);
        isRunning = false;
    }

    @Override
    public void open(Configuration parameters) {
        this.httpClient = Dsl.asyncHttpClient(Dsl.config().setWebSocketMaxFrameSize(1024000));
        this.boundRequestBuilder = httpClient.prepareGet(url);
        this.websockerBuilder = new WebSocketUpgradeHandler.Builder();
    }
}
