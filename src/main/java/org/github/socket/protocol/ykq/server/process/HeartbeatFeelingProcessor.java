package org.github.socket.protocol.ykq.server.process;

import cn.hutool.core.util.HexUtil;
import org.github.socket.protocol.ykq.server.protocol.XPacket;
import org.smartboot.socket.MessageProcessor;
import org.smartboot.socket.transport.AioSession;
import org.tinylog.Logger;

import java.util.Arrays;

/**
 * 心跳 用于链路状态判断，3 次未收到心跳包视为网络异常，需要重新登陆
 */
public class HeartbeatFeelingProcessor implements MessageProcessor<XPacket> {
    /**
     * 处理接收到的消息
     *
     * @param session 通信会话
     * @param packet  待处理的业务消息
     */
    @Override
    public void process(AioSession session, XPacket packet) {
        byte[] msg = packet.getMsg();

        Logger.info("=====================================心跳包协议开始===============================================");
        byte[] chargingId = Arrays.copyOfRange(msg, 0, 7); //7位
        String chargingIdStr = HexUtil.encodeHexStr(chargingId);
        Logger.info("桩编码 {}", chargingIdStr);

        byte[] chargingNumber = Arrays.copyOfRange(msg, 7, 8); // 1位
        Logger.info("枪号 %02X".formatted(chargingNumber[0] & 0xFF));


        byte[] chargingStatus = Arrays.copyOfRange(msg, 8, 9); // 1位
        Logger.info("枪状态 %02X".formatted(chargingStatus[0] & 0xFF));
        Logger.info("=====================================心跳包协议结束===============================================");
    }
}
