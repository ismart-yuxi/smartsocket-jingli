package org.example.process;

import org.example.protocol.XPacket;
import org.smartboot.socket.MessageProcessor;
import org.smartboot.socket.transport.AioSession;
import org.tinylog.Logger;

import java.util.Arrays;

public class LoginMessageProcessor implements MessageProcessor<XPacket> {
    /**
     * 处理接收到的消息
     *
     * @param session 通信会话
     * @param packet  待处理的业务消息
     */
    @Override
    public void process(AioSession session, XPacket packet) {
        byte[] msg = packet.getMsg();


        byte[] chargingId = Arrays.copyOfRange(msg, 0, 7); //7位
        StringBuilder chargingIdStr = new StringBuilder();
        for (byte id : chargingId) {
            chargingIdStr.append(String.format("%02X", id & 0xFF));
        }
        Logger.info("桩编码 ===> {}", chargingIdStr);


        Logger.info("=====================================登录解析完成===============================================");
    }
}
