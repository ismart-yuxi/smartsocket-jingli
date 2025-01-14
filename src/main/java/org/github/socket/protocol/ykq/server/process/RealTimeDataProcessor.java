package org.github.socket.protocol.ykq.server.process;

import cn.hutool.core.util.HexUtil;
import cn.hutool.core.util.NumberUtil;
import org.github.socket.protocol.ykq.server.protocol.XPacket;
import org.github.socket.protocol.ykq.util.Util;
import org.smartboot.socket.MessageProcessor;
import org.smartboot.socket.transport.AioSession;
import org.tinylog.Logger;

import java.util.Arrays;

public class RealTimeDataProcessor implements MessageProcessor<XPacket> {
    /**
     * 处理接收到的消息
     *
     * @param session 通信会话
     * @param packet  待处理的业务消息
     */
    @Override
    public void process(AioSession session, XPacket packet) {

        byte[] msg = packet.getMsg();

        Logger.info("=====================================实时数据处理协议开始===============================================");

        byte[] orderId = Arrays.copyOfRange(msg, 0, 16); //16位
        String orderIdStr = HexUtil.encodeHexStr(orderId);
        Logger.info("交易流水号 {}", orderIdStr);


        byte[] chargingId = Arrays.copyOfRange(msg, 16, 16 + 7); // 7位
        String chargingIdStr = HexUtil.encodeHexStr(chargingId);
        Logger.info("桩编号 {}", chargingIdStr);


        byte[] gunId = Arrays.copyOfRange(msg, 16 + 7, 16 + 7 + 1); // 1位
        Logger.info("枪号 %02X".formatted(gunId[0] & 0xFF));


        byte[] status = Arrays.copyOfRange(msg, 16 + 7 + 1, 16 + 7 + 1 + 1); // 1位
        Logger.info("状态 %02X".formatted(status[0] & 0xFF));


        byte[] goBack = Arrays.copyOfRange(msg, 16 + 7 + 1 + 1, 16 + 7 + 1 + 1 + 1); // 1位
        Logger.info("枪是否归位 %02X".formatted(goBack[0] & 0xFF));


        byte[] insertGun = Arrays.copyOfRange(msg, 16 + 7 + 1 + 1 + 1, 16 + 7 + 1 + 1 + 1 + 1); // 1位
        Logger.info("是否插枪 %02X".formatted(insertGun[0] & 0xFF));


        byte[] voltage = Arrays.copyOfRange(msg, 16 + 7 + 1 + 1 + 1 + 1, 16 + 7 + 1 + 1 + 1 + 1 + 2); // 2位
        Logger.info("输出电压 {}", NumberUtil.mul(Util.bytesToShortLittleEndian(voltage), 0.1));


        byte[] electricity = Arrays.copyOfRange(msg, 16 + 7 + 1 + 1 + 1 + 1 + 2, 16 + 7 + 1 + 1 + 1 + 1 + 2 + 2); // 2位
        Logger.info("输出电流 {}", NumberUtil.mul(Util.bytesToShortLittleEndian(electricity), 0.1));


        Logger.info("=====================================实时数据处理协议结束===============================================");

    }
}
