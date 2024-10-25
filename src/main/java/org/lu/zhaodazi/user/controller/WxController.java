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
    @GetMapping(path = "/callback")
    @ResponseBody
    public String check(HttpServletRequest request) {
        String echoStr = request.getParameter("echoStr");
        if (StringUtils.isNoneEmpty(echoStr)) {
            return echoStr;
        }
        return "";
    }
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
