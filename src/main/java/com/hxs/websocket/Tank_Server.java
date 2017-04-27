package com.hxs.websocket;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;

/**
 * Created by xiaoshan on 2017/4/25.
 */
@ServerEndpoint(value = "/server")
public class Tank_Server {

    @OnMessage
    public void onMessage(String message,Session session){

    }
    @OnOpen
    public void onOpen(Session session, EndpointConfig config){

    }
    @OnClose
    public void onClose(Session session, CloseReason reason){

    }
    @OnError
    public void onError(Session session, Throwable throwable){

    }
}
