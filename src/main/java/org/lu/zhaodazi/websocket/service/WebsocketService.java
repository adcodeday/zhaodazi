package org.lu.zhaodazi.websocket.service;

import io.netty.channel.Channel;
import org.springframework.stereotype.Service;


public interface WebsocketService {

    void removed(Channel channel);

    void connect(Channel channel);

//    void authorize(Channel channel, WSAuthorize wsAuthorize);

    void handleLoginReq(Channel channel);
}
