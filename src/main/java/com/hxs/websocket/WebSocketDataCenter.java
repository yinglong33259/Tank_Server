package com.hxs.websocket;




import java.util.*;

import javax.websocket.*;

/**
 * Created by xiaoshan on 2017/4/27.
 * Websocket数据定义中心
 */
public class WebSocketDataCenter {
	private static WebSocketDataCenter WSDC=new WebSocketDataCenter();
	/**
	 * 当前玩家总数
	 */
	int playerNum=0;
	/**
	 * 每个房间容纳的最大人数
	 */
	int roomScale=2;
	/**
	 * 房间链表
	 */
	LinkedList<Integer> roomList=new LinkedList<Integer>();
	/**
	 * 房间大小,前为房间编号，后为房间当前人数
	 */
	Hashtable<Integer,Integer> roomSize=new Hashtable<Integer,Integer>();
	/**
	 *保存所有连接玩家的session（Session），前为Session.id,后为Session
	 */
	Hashtable<String, Session> sessions=new Hashtable<String, Session>();
	/**
	 * 保存用户在哪房间
	 */
	Hashtable<String,Integer> SID_Room=new Hashtable<String, Integer>();
	/**
	 * 房间信息,前为房间编号
	 */
	Hashtable<Integer,ArrayList<Session>> Rooms=new Hashtable<Integer,ArrayList<Session>>();
	/**
	 * 保存每个房间的游戏中心数据，前为房间ID，后为游戏房间数据
	 */
	Hashtable<Integer, GameDateCenter> GDCs=new Hashtable<Integer, GameDateCenter>();
	
	public static WebSocketDataCenter getInstance(){
		return WSDC;
	}


}
