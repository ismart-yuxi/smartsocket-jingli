package org.example;

import org.example.process.ProcessStrategyProxy;
import org.example.protocol.YunKProtocol;
import org.smartboot.socket.transport.AioQuickServer;

import java.io.IOException;

public class StringServer {

    public static void main(String[] args) throws IOException {
        AioQuickServer server = new AioQuickServer(9530, new YunKProtocol(), new ProcessStrategyProxy());
        server.start();
    }
}
