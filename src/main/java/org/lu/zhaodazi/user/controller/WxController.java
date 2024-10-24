package org.lu.zhaodazi.user.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.lu.zhaodazi.auth.authtication.WxAuthenticationToken;
import org.lu.zhaodazi.user.dao.UserMapper;
import org.lu.zhaodazi.user.domain.vo.req.WxTxtMsgReqVo;
import org.lu.zhaodazi.user.service.AuthService;
import org.lu.zhaodazi.websocket.service.WebsocketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Component
@RequestMapping("/wx")
public class WxController {
    @Autowired
    WebsocketService websocketService;
    @Autowired
    AuthService authService;


    /**
     * 微信的公众号接入 token 验证，即返回echostr的参数值
     *
     * @param request
     * @return
     */
    @GetMapping(path = "/callback")
    @ResponseBody
    public String check(HttpServletRequest request) {
        String echoStr = request.getParameter("echostr");
        if (StringUtils.isNoneEmpty(echoStr)) {
            return echoStr;
        }
        return "";
    }

    /**
     * fixme: 需要做防刷校验
     * 微信的响应返回
     * 本地测试访问: curl -X POST 'http://localhost:8080/wx/callback' -H 'content-type:application/xml' -d '<xml><URL><![CDATA[https://hhui.top]]></URL><ToUserName><![CDATA[一灰灰blog]]></ToUserName><FromUserName><![CDATA[demoUser1234]]></FromUserName><CreateTime>1655700579</CreateTime><MsgType><![CDATA[text]]></MsgType><Content><![CDATA[login]]></Content><MsgId>11111111</MsgId></xml>' -i
     *
     * @param msg
     * @return
     */
    @PostMapping(path = "/callback",
            consumes = {"application/xml", "text/xml"},
            produces = "application/xml;charset=utf-8")
    public void callBack(@RequestBody WxTxtMsgReqVo msg) {
        String content = msg.getContent();
        String openId = msg.getFromUserName();
        Integer code;
        try {
            code = Integer.valueOf(content);

        } catch (NumberFormatException e) {
            throw new RuntimeException(e);
        }
        authService.login(WxAuthenticationToken.unauthenticated(openId,code));
    }
}
