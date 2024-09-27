package org.github.socket.protocol.ykq.util;

import cn.hutool.core.map.MapBuilder;
import cn.hutool.core.map.MapUtil;
import org.smartboot.socket.transport.AioSession;
import org.smartboot.socket.transport.WriteBuffer;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


public class ChannelCacheManager {

    /**
     * 通道缓存，string为通道信息，ChannelHandlerContext 为通道信息
     * 使用 final 关键字使得该引用不可修改
     */
    private static final Map<String, AioSession> CHANNEL_CACHE = new ConcurrentHashMap<>(16);

    // 私有构造器，防止实例化
    private ChannelCacheManager() {
    }

    /**
     * 添加或更新通道信息
     *
     * @param channelId 通道信息
     * @param context   通道信息
     */
    public static void putChannel(String channelId, AioSession context) {
        CHANNEL_CACHE.put(channelId, context);
    }

    /**
     * 根据通道信息获取通道信息
     *
     * @param channelId 通道信息
     * @return 通道信息
     */
    public static AioSession getChannel(String channelId) {
        return CHANNEL_CACHE.get(channelId);
    }

    /**
     * 移除通道信息
     *
     * @param channelId 通道信息
     */
    public static void removeChannel(String channelId) {
        CHANNEL_CACHE.remove(channelId);
    }

    /**
     * 检查通道缓存中是否包含某个通道
     *
     * @param channelId 通道信息
     * @return 是否存在
     */
    public static boolean containsChannel(String channelId) {
        return CHANNEL_CACHE.containsKey(channelId);
    }

    /**
     * 获取所有通道信息，并返回不可变的 Multimap 副本
     */
    public static Map<String, AioSession> getAllChannels() {
        MapBuilder<String, AioSession> builder = MapUtil.builder();
        CHANNEL_CACHE.forEach(builder::put);
        // 构建并返回不可变的
        return builder.build();
    }

    /**
     * 服务端给客户端发送消息
     *
     * @param channelId 连接通道唯一id
     * @param msg       需要发送的消息内容
     */
    public static void channelWrite(String channelId, byte[] msg) {
        AioSession channel = getChannel(channelId);
        if (channel == null) {
            System.err.printf("通道【%s】不存在", channelId);
            return;
        }
        if (msg == null || msg.length == 0) {
            System.out.println("服务端响应空的消息");
            return;
        }

        try (WriteBuffer wb = channel.writeBuffer()) {
            wb.write(msg);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
//            wb.flush();
            channel.close();
        }

    }

}
