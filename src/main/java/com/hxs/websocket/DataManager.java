package com.hxs.websocket;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.LinkedList;

import javax.websocket.Session;

/**
 * Created by xiaoshan on 2017/4/27.
 */
public class DataManager {
	public static final DataManager dataManager=new DataManager();
	
	public int playerNum=0;
	//房间链表
	public LinkedList<Integer> roomList=new LinkedList<Integer>();
	//房间大小
	public Hashtable<Integer,Integer> roomSize=new Hashtable<Integer,Integer>();
	//
	public Hashtable<String, Session> sessions=new Hashtable<String, Session>();
	//保存用户在哪房间
	public Hashtable<String,Integer> SID_Room=new Hashtable<String, Integer>();
	//房间信息,前为房间编号
	public Hashtable<Integer,ArrayList<Session>> Rooms=new Hashtable<Integer,ArrayList<Session>>();
	
	public void onMessage(Session session){
			
		}
	
	public void onOpen(Session session){
		//房间总人数++
		playerNum++;
		//
		sessions.put(session.getId(), session);
		//如果是第一个人
		if(roomList.size()==0){
			roomList.add(1);
			roomSize.put(1, 0);
			Rooms.put(1, new ArrayList<Session>());
		}
		//房间不够加房间
	}
	public void onClose(Session session){
		playerNum--;
		sessions.remove(session.getId());
	}
	public void onError(Session session){
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	public int getPlayerNum() {
        return playerNum;
    }

    public void setPlayerNum(int playerNum) {
        this.playerNum = playerNum;
    }
    
    public Hashtable<String, Session> getSessions() {
		return sessions;
	}

	public void setSessions(Hashtable<String, Session> sessions) {
		this.sessions = sessions;
	}

	private DataManager() {
    	//其他类不可生成新的对象
	}
    public static DataManager  getInstance(){
    	return dataManager;
    }
}
