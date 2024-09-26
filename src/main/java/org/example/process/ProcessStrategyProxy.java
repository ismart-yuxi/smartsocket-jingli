package org.example.process;

import org.example.protocol.XPacket;
import org.smartboot.socket.MessageProcessor;
import org.smartboot.socket.transport.AioSession;

import java.util.Map;

public class ProcessStrategyProxy implements MessageProcessor<XPacket> {

    private static final Map<String, MessageProcessor<XPacket>> PROCESS_STRATEGY = Map.of(
            "1", new LoginMessageProcessor(),
            "13", new RealTimeDataProcessor()
    );

    /**
     * 处理接收到的消息
     *
     * @param session 通信会话
     * @param msg     待处理的业务消息
     */
    @Override
    public void process(AioSession session, XPacket msg) {
        PROCESS_STRATEGY.get(msg.getSegmentTypeFlagHex()).process(session, msg);
    }
}
