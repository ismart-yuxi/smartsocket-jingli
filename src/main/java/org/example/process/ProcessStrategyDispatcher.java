package org.example.process;

import org.example.protocol.XPacket;
import org.example.util.ChannelCacheManager;
import org.smartboot.socket.MessageProcessor;
import org.smartboot.socket.StateMachineEnum;
import org.smartboot.socket.extension.processor.AbstractMessageProcessor;
import org.smartboot.socket.transport.AioSession;
import org.tinylog.Logger;

import java.util.Map;

public class ProcessStrategyDispatcher extends AbstractMessageProcessor<XPacket> {

    private static final Map<String, MessageProcessor<XPacket>> PROCESS_STRATEGY = Map.of(
            "01", new LoginMessageProcessor(),
            "13", new RealTimeDataProcessor()
    );

    /**
     * 处理接收到的消息
     */
    @Override
    public void process0(AioSession session, XPacket msg) {
        PROCESS_STRATEGY.get(msg.getSegmentTypeFlagHex()).process(session, msg);
    }


    @Override
    public void stateEvent0(AioSession session, StateMachineEnum stateMachineEnum, Throwable throwable) {
        if (throwable != null) {
            Logger.error(stateMachineEnum + " exception:", throwable);
        }

        switch (stateMachineEnum) {
            case NEW_SESSION -> ChannelCacheManager.putChannel(session.getSessionID(), session);
            case SESSION_CLOSED -> ChannelCacheManager.removeChannel(session.getSessionID());
            default -> Logger.error("emmmmmmm ??????!!!!!");
        }
    }
}
