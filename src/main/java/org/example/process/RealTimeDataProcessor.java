package org.example.process;

import cn.hutool.core.util.NumberUtil;
import org.example.Util;
import org.example.protocol.XPacket;
import org.smartboot.socket.MessageProcessor;
import org.smartboot.socket.transport.AioSession;

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

        System.out.print("交易流水号 ===>");
        byte[] orderId = Arrays.copyOfRange(msg, 0, 16); //16位
        for (byte id : orderId) {
            System.out.printf("%02X", id & 0xFF);
        }
        System.out.print("\n");
        System.out.print("桩编号 ===>");

        byte[] chargingId = Arrays.copyOfRange(msg, 16, 16 + 7); // 7位
        for (byte id : chargingId) {
            System.out.printf("%02X", id & 0xFF);
        }
        System.out.print("\n");

        byte[] gunId = Arrays.copyOfRange(msg, 16 + 7, 16 + 7 + 1); // 1位
        System.out.printf("枪号 %02X", gunId[0] & 0xFF);

        System.out.print("\n");

        byte[] status = Arrays.copyOfRange(msg, 16 + 7 + 1, 16 + 7 + 1 + 1); // 1位
        System.out.printf("状态 %02X", status[0] & 0xFF);

        System.out.print("\n");

        byte[] goBack = Arrays.copyOfRange(msg, 16 + 7 + 1 + 1, 16 + 7 + 1 + 1 + 1); // 1位
        System.out.printf("枪是否归位 %02X", goBack[0] & 0xFF);
        System.out.print("\n");

        byte[] insertGun = Arrays.copyOfRange(msg, 16 + 7 + 1 + 1 + 1, 16 + 7 + 1 + 1 + 1 + 1); // 1位
        System.out.printf("是否插枪 %02X", insertGun[0] & 0xFF);
        System.out.print("\n");

        byte[] voltage = Arrays.copyOfRange(msg, 16 + 7 + 1 + 1 + 1 + 1, 16 + 7 + 1 + 1 + 1 + 1 + 2); // 2位
        System.out.print("输出电压 ===>" + NumberUtil.mul(Util.bytesToShortLittleEndian(voltage), 0.1));
        System.out.print("\n");

        byte[] electricity = Arrays.copyOfRange(msg, 16 + 7 + 1 + 1 + 1 + 1 + 2, 16 + 7 + 1 + 1 + 1 + 1 + 2 + 2); // 2位
        System.out.print("输出电流 ===>" + NumberUtil.mul(Util.bytesToShortLittleEndian(electricity), 0.1));
        System.out.print("\n");
    }
}
