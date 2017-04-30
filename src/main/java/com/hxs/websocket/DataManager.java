package com.hxs.websocket;



import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.util.*;

import javax.websocket.CloseReason;
import javax.websocket.EndpointConfig;
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
	
	public void onMessage(String message,Session session){
		ArrayList<Session> CurRoom=Rooms.get(SID_Room.get(session.getId()));
		try{
			JSONObject msg= (JSONObject) new JSONParser().parse(message);
			switch (msg.get("type").toString()){
				case "roomInfo":
					for(int i=0;i<CurRoom.size();i++){
						Session s=CurRoom.get(i);
						s.getBasicRemote().sendText(message);
					}
					break;
				case "tank_Position":
					System.out.println(message);
					for(int i=0;i<CurRoom.size();i++){
						Session s=CurRoom.get(i);
						s.getBasicRemote().sendText(message);
					}
					break;
			}
		}catch (Exception e){
			e.printStackTrace();
		}
	}
	
	public void onOpen(Session session, EndpointConfig config){
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
		ArrayList<Session> curRoomSessions=Rooms.get(roomid);
		roomInfo.put("room_ID",roomid);
		roomInfo.put("playerNum", curRoomSessions.size());
		String Str_SIDs="";
		//给房间里的每个玩家都发送消息
		for(int i = 0 ; i < curRoomSessions.size() ; i++){
			Session s = curRoomSessions.get(i);
			Str_SIDs=Str_SIDs+"*"+s.getId();
		}
		roomInfo.put("Str_SIDs",Str_SIDs);
		roomInfo.put("mySession_ID",session.getId());
		System.out.println(Str_SIDs);
		onMessage(roomInfo.toJSONString(), session);

	}
	public void onClose(Session session, CloseReason reason){
		playerNum--;
		sessions.remove(session.getId());
	}
	public void onError(Session session, Throwable throwable){
		
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
