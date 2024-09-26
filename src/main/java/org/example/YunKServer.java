package org.example;

import org.example.process.ProcessStrategyProxy;
import org.example.protocol.YunKProtocol;
import org.smartboot.socket.transport.AioQuickServer;
import org.tinylog.provider.ProviderRegistry;

import java.io.IOException;

public class YunKServer {

    public static void main(String[] args) throws IOException {
        AioQuickServer server = new AioQuickServer(9530, new YunKProtocol(), new ProcessStrategyProxy());
        server.start();

        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                try {
                    ProviderRegistry.getLoggingProvider().shutdown();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }
}
