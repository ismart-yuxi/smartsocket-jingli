package org.example;

import cn.hutool.core.util.NumberUtil;
import org.example.protocol.XPacket;
import org.smartboot.socket.MessageProcessor;
import org.smartboot.socket.Protocol;
import org.smartboot.socket.transport.AioQuickServer;
import org.smartboot.socket.transport.AioSession;
import org.smartboot.socket.util.StringUtils;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Arrays;

public class StringServer {

    public static void main(String[] args) throws IOException {
        MessageProcessor<byte[]> processor = new MessageProcessor<byte[]>() {

            /**
             * 处理接收到的消息
             *
             * @param session 通信会话
             * @param msg     待处理的业务消息
             */
            @Override
            public void process(AioSession session, byte[] msg) {

                System.out.print("交易流水号 ===>");
                byte[] orderId = Arrays.copyOfRange(msg, 0, 16); //16位
                for (byte id : orderId) {
                    System.out.printf("%02X", id & 0xFF);
                }
                System.out.print("\n");
                System.out.print("桩编号 ===>");

                byte[] chargingId = Arrays.copyOfRange(msg, 16, 16+7); // 7位
                for (byte id : chargingId) {
                    System.out.printf("%02X", id & 0xFF);
                }
                System.out.print("\n");

                byte[] gunId = Arrays.copyOfRange(msg, 16+7, 16+7+1); // 1位
                System.out.printf("枪号 %02X", gunId[0] & 0xFF);

                System.out.print("\n");

                byte[] status = Arrays.copyOfRange(msg, 16+7+1, 16+7+1+1); // 1位
                System.out.printf("状态 %02X", status[0] & 0xFF);

                System.out.print("\n");

                byte[] goBack = Arrays.copyOfRange(msg, 16+7+1+1, 16+7+1+1+1); // 1位
                System.out.printf("枪是否归位 %02X", goBack[0] & 0xFF);
                System.out.print("\n");

                byte[] insertGun = Arrays.copyOfRange(msg, 16+7+1+1+1, 16+7+1+1+1+1); // 1位
                System.out.printf("是否插枪 %02X", insertGun[0] & 0xFF);
                System.out.print("\n");

                byte[] voltage = Arrays.copyOfRange(msg, 16+7+1+1+1+1, 16+7+1+1+1+1+2); // 2位
                System.out.print("输出电压 ===>" + NumberUtil.mul(Util.bytesToShortLittleEndian(voltage), 0.1));
                System.out.print("\n");

                byte[] electricity = Arrays.copyOfRange(msg, 16+7+1+1+1+1+2, 16+7+1+1+1+1+2+2); // 2位
                System.out.print("输出电流 ===>" + NumberUtil.mul(Util.bytesToShortLittleEndian(electricity), 0.1));
                System.out.print("\n");

            }
        };

        AioQuickServer server = new AioQuickServer(9530, new Protocol<byte[]>() {
            @Override
            public byte[] decode(ByteBuffer readBuffer, AioSession session) {
                int remaining = readBuffer.remaining();
                if (remaining < Integer.BYTES) {
                    return null;
                }
                readBuffer.mark();

                System.out.println("起始标识符代表一帧数据的开始，固定为 0x68  " + StringUtils.toHex(readBuffer.get()));

                short dataLength = readBuffer.get();
                System.out.println("数据长度 " + dataLength);
                System.out.println("序列号域即为数据包的发送顺序号 " + readBuffer.getShort());
                System.out.println("加密标志只针对消息体（数据单元）。0x00:不加密，0x01:3DES " + StringUtils.toHex(readBuffer.get()));
                System.out.println("帧类型标志定义了上下行数据帧 "+ StringUtils.toHex(readBuffer.get()));


                byte[] msg = new byte[dataLength-4];
                readBuffer.get(msg);
                for (byte code : msg) {
                    System.out.printf("%02X", code & 0xFF);
                }

                System.out.println();
                System.out.println("帧校验域 "+ readBuffer.getShort());

                readBuffer.mark();

                return msg;
            }
        }, processor);
        server.start();
    }
}
