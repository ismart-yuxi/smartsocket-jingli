package org.github.socket.protocol.ykq.server.process;

import cn.hutool.core.util.HexUtil;
import org.github.socket.protocol.ykq.server.protocol.XPacket;
import org.smartboot.socket.MessageProcessor;
import org.smartboot.socket.transport.AioSession;
import org.tinylog.Logger;

import java.util.Arrays;

/**
 * 0x09
 * 充电桩计费模型与平台不一致时，都需要请求计费模型，如计费模型请求不成功，则禁止充电
 */
public class BillingModelRequestProcessor implements MessageProcessor<XPacket> {
    /**
     * 处理接收到的消息
     *
     * @param session 通信会话
     * @param packet  待处理的业务消息
     */
    @Override
    public void process(AioSession session, XPacket packet) {
        byte[] msg = packet.getMsg();

        Logger.info("=====================================计费模型协议开始===============================================");
        byte[] chargingId = Arrays.copyOfRange(msg, 0, 7); //7位
        String chargingIdStr = HexUtil.encodeHexStr(chargingId);
        Logger.info("桩编码 {}", chargingIdStr);
        Logger.info("=====================================计费模型协议结束===============================================");

    }
}
