package org.github.socket.protocol.ykq.server.process;

import org.github.socket.protocol.ykq.server.protocol.XPacket;
import org.github.socket.protocol.ykq.util.ChannelCacheManager;
import org.smartboot.socket.StateMachineEnum;
import org.smartboot.socket.extension.processor.AbstractMessageProcessor;
import org.smartboot.socket.transport.AioSession;
import org.tinylog.Logger;

public class ProcessStrategyDispatcher extends AbstractMessageProcessor<XPacket> {


    /**
     * 处理接收到的消息
     */
    @Override
    public void process0(AioSession session, XPacket msg) {
        YunKProtocolProcessorRegister.PROCESS_STRATEGY.get(msg.getSegmentTypeFlagHex()).process(session, msg);
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
