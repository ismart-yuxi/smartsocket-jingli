package org.github.socket.protocol.ykq.util;


/**
 * @ClassName HexHandleUtil
 * @Description 16进制处理工具类
 * @Author liangbo
 * @Date 2023/2/3 10:00
 * @Version 1.0
 **/

public class HexHandleUtil {

    // TODO-QIU: 2024年8月7日, 0007 迁移

    /**
     * 截取byte[]
     *
     * @param src   源数据
     * @param begin 开始位置
     * @param count 取值数量
     * @return 截取后的byte[]
     */
    public static byte[] subBytes(byte[] src, int begin, int count) {
        byte[] bs = new byte[count];
        for (int i = begin; i < begin + count; i++) {
            bs[i - begin] = src[i];
        }
        return bs;
    }

}


