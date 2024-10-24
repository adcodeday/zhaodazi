package org.lu.zhaodazi.websocket.service.impl;

import cn.hutool.json.JSONUtil;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import org.lu.zhaodazi.common.constant.RedisKey;
import org.lu.zhaodazi.common.util.RedisUtil;
import org.lu.zhaodazi.websocket.service.WebsocketService;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.concurrent.TimeUnit;
@Service
public class WebsocketServiceImpl implements WebsocketService {

    private static final String LOGIN_CODE = "loginCode";

    private static final Duration EXPIRE_TIME = Duration.ofHours(1);
    private static final Long MAX_MUM_SIZE = 10000L;
    /**
     * 所有请求登录的code与channel关系
     */
    public static final Cache<Integer, Channel> WAIT_LOGIN_MAP = Caffeine.newBuilder()
            .expireAfterWrite(EXPIRE_TIME)
            .maximumSize(MAX_MUM_SIZE)
            .build();
    @Override
    public void removed(Channel channel) {

    }

    @Override
    public void connect(Channel channel) {

    }

//    @Override
//    public void authorize(Channel channel, WSAuthorize wsAuthorize) {
//
//    }

    @Override
    public void handleLoginReq(Channel channel) {
        Integer code = generateLoginCode(channel);
        sendMsg(channel, code);
    }

    private <T> void sendMsg(Channel channel, T object) {
        channel.writeAndFlush(new TextWebSocketFrame(JSONUtil.toJsonStr(object)));
    }

    private Integer generateLoginCode(Channel channel) {
        int inc;
        do {
            //本地cache时间必须比redis key过期时间短，否则会出现并发问题
            //TODO 存在并发问题，但是用户量少就不明显
            inc = RedisUtil.integerInc(RedisKey.getKey(LOGIN_CODE), (int) EXPIRE_TIME.toMinutes(), TimeUnit.MINUTES);
        } while (WAIT_LOGIN_MAP.asMap().containsKey(inc));
        WAIT_LOGIN_MAP.put(inc, channel);
        return inc;
    }
}
