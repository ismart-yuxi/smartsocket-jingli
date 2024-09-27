package org.example.util;

public class Util {
    /**
     * 位运算:
     * <p>
     * bytes[0] << 8: 将第一个字节左移8位，放在高位。
     * bytes[1] & 0xFF: 确保第二个字节在转换为 int 时不带符号。
     * |: 使用按位或操作将两个字节合并成一个 short。
     * 检查数组长度: 确保输入的字节数组至少有两个元素，以避免数组越界异常。
     *
     * @param bytes
     * @return short
     */
    public static short bytesToShort(byte[] bytes) {
        if (bytes == null || bytes.length < 2) {
            throw new IllegalArgumentException("Byte array must have at least 2 elements");
        }
        return (short) ((bytes[0] << 8) | (bytes[1] & 0xFF));
    }

    /**
     * 位运算:
     * bytes[1] << 8: 将第二个字节左移8位，放在高位。
     * bytes[0] & 0xFF: 确保第一个字节在转换为 int 时不带符号。
     * |: 使用按位或操作将两个字节合并成一个 short。
     * 这种方法适用于从小端序字节数组中读取16位整数的场景。
     *
     * @param bytes
     * @return short
     */
    public static short bytesToShortLittleEndian(byte[] bytes) {
        if (bytes == null || bytes.length < 2) {
            throw new IllegalArgumentException("Byte array must have at least 2 elements");
        }
        return (short) ((bytes[1] << 8) | (bytes[0] & 0xFF));
    }
}
