package org.example.sources;

import org.asynchttpclient.ws.WebSocket;
import org.asynchttpclient.ws.WebSocketListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.BlockingQueue;

public class BlockchainWebSocketListener implements WebSocketListener {
    private final static Logger logger = LoggerFactory.getLogger(BlockchainWebSocketListener.class);

    private final BlockingQueue<String> unconfirmedTransactions;

    public BlockchainWebSocketListener(BlockingQueue<String> unconfirmedTransactions) {
        this.unconfirmedTransactions = unconfirmedTransactions;
    }

    @Override
    public void onOpen(WebSocket webSocket) {
        logger.info("Connected to {}.", webSocket.getRemoteAddress());
        webSocket.sendTextFrame("{\"op\":\"unconfirmed_sub\"}");
    }

    @Override
    public void onClose(WebSocket webSocket, int i, String s) {
        logger.info("Disconnected from {}.", webSocket.getRemoteAddress());
        webSocket.sendTextFrame("{\"op\":\"unconfirmed_unsub\"}");
    }

    // an example transaction
    // {"op":"utx","x":{"lock_time":0,"ver":2,"size":151,"inputs":[{"sequence":4294967295,"prev_out":{"spent":true,"tx_index":0,"type":0,"addr":"bc1p59v673aezjuyg5gnxfftwxkqnz059j5te7j2p9gjqnyvcduy82yskgtgku","value":2500,"n":0,"script":"5120a159af47b914b84451133252b71ac0989f42ca8bcfa4a0951204c8cc37843a89"},"script":""}],"time":1670627753,"tx_index":0,"vin_sz":1,"hash":"7731b35a8724b66314102b6f2f71601c2acfe62f0cb6319cfc525292480a0650","vout_sz":1,"relayed_by":"0.0.0.0","out":[{"spent":false,"tx_index":0,"type":0,"addr":"bc1qqpapk39nhlghh0kyjm5j8dh9zsljr0wrw9h0cf","value":2349,"n":0,"script":"0014007a1b44b3bfd17bbec496e923b6e5143f21bdc3"}]}}
    @Override
    public void onTextFrame(String transaction, boolean finalFragment, int rsv) {
        if (transaction != null) {
            try {
                unconfirmedTransactions.put(transaction);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void onError(Throwable throwable) {
        logger.error(throwable.getMessage());
    }
}
