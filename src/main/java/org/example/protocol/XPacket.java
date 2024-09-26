package org.example.protocol;

import java.io.Serializable;

public class XPacket implements Serializable {
    private String start;  // 起始标志
    private Short length; //数据长度
    private Short serialDomain; // 序列号域
    private String secFlag;  //加密标志
    private String segmentTypeFlag; //帧类型标志
    private byte[] msg; //解析的消息体
    private Short segmentCheckDomain; //帧校验域

    private String startHex; // 起始标志 16进制字符串
    private String secFlagHex; //加密标志 16进制字符串
    private String segmentTypeFlagHex; //帧类型标志 16进制字符串

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public Short getLength() {
        return length;
    }

    public void setLength(Short length) {
        this.length = length;
    }

    public Short getSerialDomain() {
        return serialDomain;
    }

    public void setSerialDomain(Short serialDomain) {
        this.serialDomain = serialDomain;
    }

    public String getSecFlag() {
        return secFlag;
    }

    public void setSecFlag(String secFlag) {
        this.secFlag = secFlag;
    }

    public String getSegmentTypeFlag() {
        return segmentTypeFlag;
    }

    public void setSegmentTypeFlag(String segmentTypeFlag) {
        this.segmentTypeFlag = segmentTypeFlag;
    }

    public byte[] getMsg() {
        return msg;
    }

    public void setMsg(byte[] msg) {
        this.msg = msg;
    }

    public Short getSegmentCheckDomain() {
        return segmentCheckDomain;
    }

    public void setSegmentCheckDomain(Short segmentCheckDomain) {
        this.segmentCheckDomain = segmentCheckDomain;
    }

    public String getStartHex() {
        return startHex;
    }

    public void setStartHex(String startHex) {
        this.startHex = startHex;
    }

    public String getSecFlagHex() {
        return secFlagHex;
    }

    public void setSecFlagHex(String secFlagHex) {
        this.secFlagHex = secFlagHex;
    }

    public String getSegmentTypeFlagHex() {
        return segmentTypeFlagHex;
    }

    public void setSegmentTypeFlagHex(String segmentTypeFlagHex) {
        this.segmentTypeFlagHex = segmentTypeFlagHex;
    }
}
