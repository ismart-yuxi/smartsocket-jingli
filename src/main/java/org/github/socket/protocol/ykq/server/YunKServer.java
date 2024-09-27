package org.github.socket.protocol.ykq.server;

import org.github.socket.protocol.ykq.server.process.ProcessStrategyDispatcher;
import org.github.socket.protocol.ykq.server.protocol.YunKProtocol;
import org.smartboot.socket.extension.plugins.StreamMonitorPlugin;
import org.smartboot.socket.transport.AioQuickServer;
import org.tinylog.provider.ProviderRegistry;

import java.io.IOException;

public class YunKServer {

    public static void main(String[] args) throws IOException {

        ProcessStrategyDispatcher processor = new ProcessStrategyDispatcher();
//        processor.addPlugin(new IdleStatePlugin<>(15 * 1000)); //15秒
        processor.addPlugin(new StreamMonitorPlugin<>());

        AioQuickServer server = new AioQuickServer(9530, new YunKProtocol(), processor);
        server.start();

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                ProviderRegistry.getLoggingProvider().shutdown();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }));
    }
}
