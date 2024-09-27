package org.github.socket.protocol.ykq.server.protocol;

import cn.hutool.core.util.HexUtil;
import org.github.socket.protocol.ykq.util.CRC16Modbus;
import org.github.socket.protocol.ykq.util.HexHandleUtil;
import org.smartboot.socket.Protocol;
import org.smartboot.socket.transport.AioSession;
import org.smartboot.socket.util.StringUtils;
import org.tinylog.Logger;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class YunKProtocol implements Protocol<XPacket> {
    /**
     * 对于从Socket流中获取到的数据采用当前Protocol的实现类协议进行解析。
     *
     * @param readBuffer 待处理的读buffer
     * @param session    本次需要解码的session
     * @return 本次解码成功后封装的业务消息对象, 返回null则表示解码未完成
     */
    @Override
    public XPacket decode(ByteBuffer readBuffer, AioSession session) {
        int remaining = readBuffer.remaining();
        if (remaining < Integer.BYTES) {
            return null;
        }
        readBuffer.mark();

        // 不改变 position 的情况下读取数据
        ByteBuffer readOnlyBuffer = readBuffer.duplicate();
        // 获取底层数组
        byte[] dataArray = new byte[readOnlyBuffer.remaining()];
        readOnlyBuffer.get(dataArray);
        // CRC校验的数据
        String allData = HexUtil.encodeHexStr(dataArray);


        XPacket xp = new XPacket();
        String startHex = StringUtils.toHex(readBuffer.get());
        Logger.debug("起始标识符代表一帧数据的开始，固定为 0x68  " + startHex);
        xp.setStartHex(startHex);

        short dataLength = readBuffer.get();
        Logger.debug("数据长度 " + dataLength);
        xp.setLength(dataLength);


        short serialDomain = readBuffer.getShort();
        Logger.debug("序列号域即为数据包的发送顺序号 " + serialDomain);
        xp.setSerialDomain(serialDomain);


        String secFlagHex = StringUtils.toHex(readBuffer.get()); //0x00:不加密   ，0x01:3DES
        Logger.debug("加密标志只针对消息体（数据单元）。0x00:不加密，0x01:3DES " + secFlagHex);
        xp.setSecFlagHex(secFlagHex);

        String segmentTypeFlagHex = StringUtils.toHex(readBuffer.get());
        Logger.debug("帧类型标志定义了上下行数据帧 " + segmentTypeFlagHex);
        xp.setSegmentTypeFlagHex(segmentTypeFlagHex);


        switch (secFlagHex) {
            //不加密的
            case "00" -> {
                byte[] msg = new byte[dataLength - 4]; //4 分别是 序列号域2  加密标志1  帧类型标志1 相加为4  总共长度 -4 刚好为消息体长度
                readBuffer.get(msg);
                StringBuilder msgStr = new StringBuilder();
                for (byte code : msg) {
                    msgStr.append(String.format("%02X", code & 0xFF));
                }
                Logger.debug("原始数据信息 {}", msgStr);
                xp.setMsg(msg);
            }
            //3DES
            case "01" -> {

            }
        }

        short segmentCheckDomain = readBuffer.order(ByteOrder.LITTLE_ENDIAN).getShort();
        Logger.debug("帧校验域 " + segmentCheckDomain);
        xp.setSegmentCheckDomain(segmentCheckDomain);

        byte[] checkData = HexHandleUtil.subBytes(allData.getBytes(), 2, dataLength);
        // CRC校验的结果
        short check = CRC16Modbus.get(checkData);


        Logger.debug("====================================================================================");

        if (true) { //check == segmentCheckDomain
            return xp;
        } else {
            Logger.error("校验都不通过，玩个蛋！");
            return null;
        }
    }
}
