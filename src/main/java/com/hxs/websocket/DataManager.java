package com.hxs.websocket;

import org.json.JSONObject;

import java.util.*;

import javax.websocket.Session;

/**
 * Created by xiaoshan on 2017/4/27.
 */
public class DataManager {
	public static final DataManager dataManager=new DataManager();
	
	public int playerNum=0,roomScale=2;
	//房间链表
	public LinkedList<Integer> roomList=new LinkedList<Integer>();
	//房间大小,前为房间编号，后为房间当前人数
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
		//如果没有房间加一个房间
		if(roomList.size()==0){
			roomList.add(1);
			roomSize.put(1, 0);
			Rooms.put(1, new ArrayList<Session>());
		}
		//房间不够加房间
		if(roomList.size()* roomScale < playerNum){
			roomList.add(roomList.getLast()+1);
			SID_Room.put(session.getId(),roomList.getLast());
			Rooms.put(roomList.getLast()+1,new ArrayList<>());
			Rooms.get(roomList.getLast()).add(session);
			roomSize.put(roomList.getLast(),1);
		}else{//往人数不够的房间加人
			for(Map.Entry<Integer, Integer> entry:roomSize.entrySet()){
				if(entry.getValue() < roomScale){
					SID_Room.put(session.getId(), entry.getKey());
					Rooms.get(entry.getKey()).add(session);
					roomSize.put(entry.getKey(), entry.getValue()+1);
				}
			}
		}
		//发送当前房间信息
		JSONObject roomInfo=new JSONObject();
		roomInfo.put("type", "roomInfo");
		int roomid=SID_Room.get(session.getId());




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
