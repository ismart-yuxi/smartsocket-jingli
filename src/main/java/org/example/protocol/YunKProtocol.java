package org.example.protocol;

import org.example.Util;
import org.smartboot.socket.Protocol;
import org.smartboot.socket.transport.AioSession;
import org.smartboot.socket.util.StringUtils;
import org.tinylog.Logger;

import java.nio.ByteBuffer;

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


        String secFlagHex = StringUtils.toHex(readBuffer.get());
        Logger.debug("加密标志只针对消息体（数据单元）。0x00:不加密，0x01:3DES " + secFlagHex);
        xp.setSecFlagHex(secFlagHex);

        String segmentTypeFlagHex = StringUtils.toHex(readBuffer.get());
        Logger.debug("帧类型标志定义了上下行数据帧 " + segmentTypeFlagHex);
        xp.setSegmentTypeFlagHex(segmentTypeFlagHex);



        byte[] msg = new byte[dataLength - 4];
        readBuffer.get(msg);
        StringBuilder msgStr = new StringBuilder();
        for (byte code : msg) {
            msgStr.append(String.format("%02X", code & 0xFF));
        }
        Logger.debug("原始数据信息 ===> {}", msgStr);
        xp.setMsg(msg);

//        short segmentCheckDomain = readBuffer.getShort();
//        Logger.debug("帧校验域 " + segmentCheckDomain);

        byte[] segmentCheckDomainByte = new byte[2];
        readBuffer.get(segmentCheckDomainByte);
        short segmentCheckDomain = Util.bytesToShortLittleEndian(segmentCheckDomainByte);
        Logger.debug("帧校验域 " + segmentCheckDomain);
        xp.setSegmentCheckDomain(segmentCheckDomain);

        Logger.debug("====================================================================================");

        readBuffer.mark();

        return xp;
    }
}
