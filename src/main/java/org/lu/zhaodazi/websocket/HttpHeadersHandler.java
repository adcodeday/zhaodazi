package org.lu.zhaodazi.websocket;


import cn.hutool.core.net.url.UrlBuilder;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpHeaders;
import org.apache.commons.lang3.StringUtils;

import java.net.InetSocketAddress;
import java.util.Optional;
//这个类的作用是处理HTTP请求，提取请求中的token和IP，并将它们设置为通道的属性
public class HttpHeadersHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        // 检查消息是否是HTTP请求
        if (msg instanceof FullHttpRequest) {
            FullHttpRequest request = (FullHttpRequest) msg;
            UrlBuilder urlBuilder = UrlBuilder.ofHttp(request.uri());

            // 从URL查询参数中提取token
            String token = Optional.ofNullable(urlBuilder.getQuery()).map(k->k.get("token")).map(CharSequence::toString).orElse("");
            // 将token设置为通道的属性
            NettyUtil.setAttr(ctx.channel(), NettyUtil.TOKEN, token);

            // 更新请求的URI，只保留路径部分
            request.setUri(urlBuilder.getPath().toString());

            HttpHeaders headers = request.headers();
            // 尝试从"X-Real-IP"头中获取客户端IP
            String ip = headers.get("X-Real-IP");
            if (StringUtils.isEmpty(ip)) {
                // 如果"X-Real-IP"头不存在，则直接从通道的远程地址获取IP
                InetSocketAddress address = (InetSocketAddress) ctx.channel().remoteAddress();
                ip = address.getAddress().getHostAddress();
            }
            // 将获取到的IP设置为通道的属性
            NettyUtil.setAttr(ctx.channel(), NettyUtil.IP, ip);

            // 从管道中移除当前处理器，因为它的任务已经完成
            ctx.pipeline().remove(this);

            // 将处理后的请求传递给管道中的下一个处理器
            ctx.fireChannelRead(request);
        } else {
            // 如果消息不是HTTP请求，则直接将原始消息传递给下一个处理器
            ctx.fireChannelRead(msg);
        }
    }
}