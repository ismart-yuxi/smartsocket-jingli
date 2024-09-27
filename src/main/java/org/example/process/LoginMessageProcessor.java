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

        byte[] chargingType = Arrays.copyOfRange(msg, 7, 8); // 1位
        Logger.info("桩类型%02X".formatted(chargingType[0] & 0xFF));

        byte[] chargingGunNum = Arrays.copyOfRange(msg, 8, 9); // 1位
        Logger.info("充电枪数量%02X".formatted(chargingGunNum[0] & 0xFF));

        byte[] protocolVersion = Arrays.copyOfRange(msg, 9, 10); // 1位
        Logger.info("通信协议版本%02X".formatted(protocolVersion[0] * 10)); //版本号乘以10

        byte[] programmerVersion = Arrays.copyOfRange(msg, 10, 18); //8位
        StringBuilder programmerVersionStr = new StringBuilder();
        for (byte id : programmerVersion) {
            programmerVersionStr.append(String.format("%02X", id & 0xFF));
        }
        Logger.info("程序版本{}", programmerVersionStr); //版本号乘以10

        byte[] networkLinkType = Arrays.copyOfRange(msg, 18, 19); // 1位
        Logger.info("网络链接类型%02X".formatted(networkLinkType[0] & 0xFF)); //版本号乘以10


        byte[] simCardNum = Arrays.copyOfRange(msg, 19, 29); //7位
        StringBuilder simCardNumStr = new StringBuilder();
        for (byte it : simCardNum) {
            simCardNumStr.append(String.format("%02X", it & 0xFF));
        }
        Logger.info("Sim卡 => {}", simCardNumStr); //

        byte[] carrierOperator = Arrays.copyOfRange(msg, 29, 30); // 1位
        Logger.info("运营商%02X".formatted(carrierOperator[0] & 0xFF));

        Logger.info("=====================================登录解析完成===============================================");
    }
}
