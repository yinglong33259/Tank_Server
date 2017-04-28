package com.hxs.websocket;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;

/**
 * Created by xiaoshan on 2017/4/25.
 */
@ServerEndpoint(value = "/server")
public class Tank_Server {
	private static DataManager dataManager=DataManager.getInstance();
	
    @OnMessage
    public void onMessage(String message,Session session){
    	
    }
    @OnOpen
    public void onOpen(Session session, EndpointConfig config){
    	//游戏总人数加1
    	dataManager.playerNum++;
    	//Sessions数组加入浏览器Session绘画对象
    	dataManager.sessions.put(session.getId(), session);
    	//
    	System.out.println(dataManager.playerNum+"s飒飒飒飒飒飒撒");
    }
    @OnClose
    public void onClose(Session session, CloseReason reason){
    	//游戏总人数-1
    	dataManager.playerNum--;
    	//Sessions数组加入浏览器Session绘画对象Player
    	System.out.println(dataManager.sessions.get(session.getId()));
    }
    @OnError
    public void onError(Session session, Throwable throwable){
    	
    }
}
