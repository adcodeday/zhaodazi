package org.lu.zhaodazi.websocket;


import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshaker;
import io.netty.util.Attribute;
import io.netty.util.AttributeKey;

/**
 * Description: netty工具类
 * Author: <a href="https://github.com/zongzibinbin">abin</a>
 * Date: 2023-04-18
 */
//这个类
public class NettyUtil {

    // AttributeKey 是 Netty 中用于标识 Channel 属性的键
    // 这里定义了一个用于存储用户令牌（token）的 AttributeKey
    // 在 WebSocket 连接中，我们可以用这个 key 来存储和获取用户的认证令牌
    public static AttributeKey<String> TOKEN = AttributeKey.valueOf("token");

    // 定义用于存储用户 IP 地址的 AttributeKey
    // 这可以用来记录连接的客户端 IP，便于日志记录或者 IP 限制等功能
    public static AttributeKey<String> IP = AttributeKey.valueOf("ip");

    // 定义用于存储用户 ID 的 AttributeKey
    // 一旦用户认证成功，可以将用户的唯一标识存储在 Channel 中，方便后续使用
    public static AttributeKey<Long> UID = AttributeKey.valueOf("uid");

    // 定义用于存储 WebSocket 握手器的 AttributeKey
    // WebSocketServerHandshaker 用于完成 WebSocket 的握手过程
    // 存储这个对象可以在需要时方便地完成 WebSocket 的关闭等操作
    public static AttributeKey<WebSocketServerHandshaker> HANDSHAKER_ATTR_KEY = AttributeKey.valueOf(WebSocketServerHandshaker.class, "HANDSHAKER");

    /**
     * 设置 Channel 的属性值
     * @param channel 要设置属性的 Netty Channel
     * @param attributeKey 属性的键，使用上面定义的 AttributeKey
     * @param data 要存储的数据
     * @param <T> 数据的类型，使用泛型来适应不同类型的数据
     */
    public static <T> void setAttr(Channel channel, AttributeKey<T> attributeKey, T data) {
        // 获取 Channel 的属性对象
        Attribute<T> attr = channel.attr(attributeKey);
        // 设置属性值
        attr.set(data);
    }

    /**
     * 获取 Channel 的属性值
     * @param channel 要获取属性的 Netty Channel
     * @param attributeKey 属性的键，使用上面定义的 AttributeKey
     * @param <T> 数据的类型，使用泛型来适应不同类型的数据
     * @return 存储在 Channel 中的属性值
     */
    public static <T> T getAttr(Channel channel, AttributeKey<T> attributeKey) {
        // 获取并返回属性值
        return channel.attr(attributeKey).get();
    }
}
