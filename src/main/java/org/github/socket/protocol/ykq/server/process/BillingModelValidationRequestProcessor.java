package org.github.socket.protocol.ykq.server.process;

import org.github.socket.protocol.ykq.server.protocol.XPacket;
import org.smartboot.socket.MessageProcessor;
import org.smartboot.socket.transport.AioSession;
import org.tinylog.Logger;

import java.util.Arrays;

/**
 * 0x05
 * 充电桩在登陆成功后，都需要对当前计费模型校验，如计费模型与平台当前不一致，则需要向
 * 平台请求新的计费模型
 * 计费模型验证请求
 */
public class BillingModelValidationRequestProcessor implements MessageProcessor<XPacket> {
    /**
     * 处理接收到的消息
     *
     * @param session 通信会话
     * @param packet  待处理的业务消息
     */
    @Override
    public void process(AioSession session, XPacket packet) {
        byte[] pkg = packet.getMsg();

        byte[] chargingId = Arrays.copyOfRange(pkg, 0, 7); //7位
        StringBuilder chargingIdStr = new StringBuilder();
        for (byte id : chargingId) {
            chargingIdStr.append(String.format("%02X", id & 0xFF));
        }
        Logger.info("桩编码 {}", chargingIdStr);


        byte[] billingModelNumber = Arrays.copyOfRange(pkg, 7, 9); // 2位
        Logger.info("计费模型编号 %02X".formatted(billingModelNumber[0] & 0xFF));

    }
}
