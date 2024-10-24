package org.lu.zhaodazi.websocket;

import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import cn.hutool.json.JSONUtil;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;
import org.lu.zhaodazi.common.util.RedisUtil;
import org.lu.zhaodazi.websocket.domain.WSBaseReq;
import org.lu.zhaodazi.websocket.enums.WSReqTypeEnum;
import org.lu.zhaodazi.websocket.service.WebsocketService;
import org.lu.zhaodazi.websocket.service.impl.WebsocketServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

/**
 * WebSocket 服务器处理器
 * 处理 WebSocket 连接、消息和事件
 */
@Slf4j // Lombok注解，自动生成日志对象
@Sharable
@Component// Netty注解，表示这个处理器可以在多个Channel中共享
public class NettyWebsocketServerHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    // WebSocketService实例，用于处理业务逻辑
    public WebsocketService websocketService= new WebsocketServiceImpl();

    /**
     * 当web客户端连接后，触发该方法
     * 初始化WebSocketService
     */
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        // 获取WebSocketService实例
        this.websocketService = getService();
    }

    /**
     * 客户端离线时触发
     * 处理用户离线逻辑
     */
    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        // 调用用户离线处理方法
        userOffLine(ctx);
    }

    /**
     * 当连接断开时触发
     * 处理用户离线逻辑
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        // 记录警告日志，表示连接断开
        log.warn("触发 channelInactive 掉线![{}]", ctx.channel().id());
        // 调用用户离线处理方法
        userOffLine(ctx);
    }

    /**
     * 处理用户离线
     * 移除用户连接并关闭通道
     */
    private void userOffLine(ChannelHandlerContext ctx) {
        // 调用WebSocketService的removed方法，处理用户离线逻辑
        this.websocketService.removed(ctx.channel());
        // 关闭通道
        ctx.channel().close();
    }

    /**
     * 处理用户事件
     * 包括心跳检查和WebSocket握手完成事件
     */
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent idleStateEvent = (IdleStateEvent) evt;
            // 读空闲事件，关闭用户连接
            if (idleStateEvent.state() == IdleState.READER_IDLE) {
                userOffLine(ctx);
            }
        } else if (evt instanceof WebSocketServerProtocolHandler.HandshakeComplete) {
            // WebSocket握手完成，处理连接和授权
            this.websocketService.connect(ctx.channel());
            // 获取通道中的token属性
            String token = NettyUtil.getAttr(ctx.channel(), NettyUtil.TOKEN);
            if (StrUtil.isNotBlank(token)) {
                // 如果token不为空，进行授权
//                this.websocketService.authorize(ctx.channel(), new WSAuthorize(token));
            }
        }
        // 调用父类的userEventTriggered方法
        super.userEventTriggered(ctx, evt);
    }

    /**
     * 处理异常
     * 记录异常日志并关闭通道
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        // 记录警告日志，包含异常信息
        log.warn("异常发生，异常消息 ={}", cause);
        // 关闭通道
        ctx.channel().close();
    }

    /**
     * 获取WebSocketService实例
     * @return WebSocketService实例
     */
    private WebsocketService getService() {
        // 使用SpringUtil从Spring上下文中获取WebSocketService实例
        return SpringUtil.getBean(WebsocketService.class);
    }

    /**
     * 读取并处理客户端发送的WebSocket消息
     * @param ctx ChannelHandlerContext
     * @param msg 客户端发送的TextWebSocketFrame消息
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
        // 将消息转换为WSBaseReq对象
        WSBaseReq wsBaseReq = JSONUtil.toBean(msg.text(), WSBaseReq.class);
        // 获取消息类型
        WSReqTypeEnum wsReqTypeEnum = WSReqTypeEnum.of(wsBaseReq.getType());
        // 根据消息类型进行处理
        switch (wsReqTypeEnum) {
            case LOGIN:
                // 处理登录请求
                this.websocketService.handleLoginReq(ctx.channel());
                log.info("请求二维码 = " + msg.text());
                break;
            case HEARTBEAT:
                // 处理心跳请求
                break;
            default:
                // 处理未知类型的消息
                log.info("未知类型");
        }
    }
}

