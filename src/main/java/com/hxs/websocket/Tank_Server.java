package com.hxs.websocket;

import java.util.ArrayList;
import java.util.Map;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/**
 * Created by xiaoshan on 2017/4/25.
 */
@ServerEndpoint(value = "/server")
public class Tank_Server {
	private static WebSocketDataCenter WSDC = WebSocketDataCenter.getInstance();
	
	private GameDateCenter gDC;
	private boolean isLoaded_GDC = false;
	private Session mySession;
	private int roomid;
	@OnMessage
	public void onMessage(String message,Session session){
		ArrayList<Session> CurRoom=WSDC.Rooms.get(WSDC.SID_Room.get(mySession.getId()));
		System.out.println("roompeople:"+CurRoom.size());
		try{
			JSONObject msg= (JSONObject) new JSONParser().parse(message);
			switch (msg.get("type").toString()){
				case "roomInfo":
					for(int i=0;i<CurRoom.size();i++){
						Session s=CurRoom.get(i);
						s.getBasicRemote().sendText(message);
					}
					break;
				case "tank_move_R"://坦克运动方向改变
					System.out.println(gDC.mapCols);
					break;
				case "tank_move_Stop"://坦克停止运动
					
					break;
				case "tank_fire_Open"://坦克开火
					
					break;
				case "tank_fire_Stop"://坦克停火
					
					break;
			}
		}catch (Exception e){
			e.printStackTrace();
		}
	}
	@OnOpen
	public void onOpen(Session session, EndpointConfig config){
		this.mySession=session;
		//房间总人数++
		WSDC.playerNum++;
		//
		WSDC.sessions.put(mySession.getId(), mySession);
		//如果没有房间加一个房间
		if(WSDC.roomList.size()==0){
			WSDC.roomList.add(1);
			WSDC.roomSize.put(1, 0);
			WSDC.Rooms.put(1, new ArrayList<Session>());
		}
		//房间不够加房间
		if(WSDC.roomList.size()* WSDC.roomScale < WSDC.playerNum){
			WSDC.roomList.add(WSDC.roomList.getLast()+1);
			WSDC.SID_Room.put(mySession.getId(),WSDC.roomList.getLast());
			WSDC.Rooms.put(WSDC.roomList.getLast()+1,new ArrayList<>());
			WSDC.Rooms.get(WSDC.roomList.getLast()).add(mySession);
			WSDC.roomSize.put(WSDC.roomList.getLast(),1);
		}else{//往人数不够的房间加人
			for(Map.Entry<Integer, Integer> entry:WSDC.roomSize.entrySet()){
				if(entry.getValue() < WSDC.roomScale){
					WSDC.SID_Room.put(mySession.getId(), entry.getKey());
					WSDC.Rooms.get(entry.getKey()).add(mySession);
					WSDC.roomSize.put(entry.getKey(), entry.getValue()+1);
					//如果房间人数达到2，开始游戏game
					if(entry.getValue()==2){
						GenOneGame(entry.getKey());
					}
				}
			}
		}
		//发送当前房间信息
		JSONObject roomInfo=new JSONObject();
		roomInfo.put("type", "roomInfo");
		roomid=WSDC.SID_Room.get(mySession.getId());
		ArrayList<Session> curRoomSessions=WSDC.Rooms.get(roomid);
		roomInfo.put("room_ID",roomid);
		roomInfo.put("playerNum", curRoomSessions.size());
		String Str_SIDs="";
		//给房间里的每个玩家都发送消息
		for(int i = 0 ; i < curRoomSessions.size() ; i++){
			Session s = curRoomSessions.get(i);
			Str_SIDs=Str_SIDs+"*"+s.getId();
		}
		roomInfo.put("Str_SIDs",Str_SIDs);
		roomInfo.put("mySession_ID",mySession.getId());
		System.out.println(Str_SIDs);
		onMessage(roomInfo.toJSONString(), mySession);

	}

	@OnClose
	public void onClose(Session session, CloseReason reason){
		WSDC.playerNum--;
		WSDC.sessions.remove(mySession.getId());
	}
	@OnError
	public void onError(Session session, Throwable throwable){
		
	}
	
	public void GenOneGame(int room_id){
		RunOneRoomGameLogic RORGl =new RunOneRoomGameLogic(WSDC,room_id);
		Thread S_RORGL = new Thread(RORGl,"tank_room_id-"+room_id);
		S_RORGL.start();
		this.gDC = WSDC.GDCs.get(room_id);
		while( !isLoaded_GDC){
			if(WSDC.GDCs.get(room_id) != null){
				//到此游戏初始化成功
				System.out.println("到此游戏初始化成功");
				isLoaded_GDC=true;
			}
		}
	}
}
