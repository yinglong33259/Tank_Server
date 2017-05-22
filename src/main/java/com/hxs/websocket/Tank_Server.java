package com.hxs.websocket;

import java.util.ArrayList;
import java.util.Map;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.hxs.pojo.Ball;
import com.hxs.pojo.Tank;

/**
 * Created by xiaoshan on 2017/4/25.
 */
@ServerEndpoint(value = "/server")
public class Tank_Server {
	private static WebSocketDataCenter WSDC = WebSocketDataCenter.getInstance();
	
	private GameDateCenter gDC;
	private boolean isLoaded_gDC = false;
	private Session mySession;
	private int roomid;
	@OnMessage
	public void onMessage(String message,Session session){
		if(!isLoaded_gDC){
			getIniData();
		}
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
					System.out.println(mySession.getId()+":ssssssssssssssssssssssssssssssssssss");
					gDC.tanks.get(mySession.getId()).setR(Integer.parseInt(msg.get("R").toString()));
					gDC.tanks.get(mySession.getId()).setMove(true);
					System.out.println("ans:"+gDC.tanks.get(mySession.getId()).getR());
					break;
				case "tank_move_Stop"://坦克停止运动
					gDC.tanks.get(mySession.getId()).setR(Integer.parseInt(msg.get("R").toString()));
					gDC.tanks.get(mySession.getId()).setMove(false);
					
					break;
				case "tank_fire_Open"://坦克开火
					System.out.println("fire");
					Tank t=gDC.tanks.get(mySession.getId());
					Ball b = new Ball();
					b.setR(t.getR());
					switch (t.getR()) {
						case 0:
							b.setX(t.getX());
							b.setY(t.getY() + gDC.Seq_width/2);
							break;
						case 1:
							b.setX(t.getX() + gDC.Seq_width/2);
							b.setY(t.getY());
							break;
						case 2:
							b.setX(t.getX() + gDC.Seq_width);
							b.setY(t.getY() + gDC.Seq_width/2);
							break;
						case 3:
							b.setX(t.getX() + gDC.Seq_width/2);
							b.setY(t.getY() + gDC.Seq_width);
							break;
						default:
							break;
					}
					gDC.balls.add(b);
					
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
		System.out.println("尝试连接了>>>>>>>>>>>>>>>>>>>>>>>>>.");
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
			WSDC.Rooms.put(WSDC.roomList.getLast(),new ArrayList<>());
			WSDC.Rooms.get(WSDC.roomList.getLast()).add(mySession);
			WSDC.roomSize.put(WSDC.roomList.getLast(),1);
		}else{//往人数不够的房间加人
			for(Map.Entry<Integer, Integer> entry:WSDC.roomSize.entrySet()){
				if(entry.getValue() < WSDC.roomScale){
					WSDC.SID_Room.put(mySession.getId(), entry.getKey());
					WSDC.Rooms.get(entry.getKey()).add(mySession);
					WSDC.roomSize.put(entry.getKey(), entry.getValue()+1);
					//如果房间人数达到2，开始游戏game
					if(entry.getValue()== WSDC.roomScale){
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
		System.out.println("用户："+mySession.getId()+" 连接正常关闭");
		//总人数减1
		WSDC.playerNum--;
		WSDC.sessions.remove(mySession.getId());
		//移除session与房间的关系
		WSDC.SID_Room.remove(mySession.getId());
		//房间在线人数减一
		int curRoomNum = WSDC.roomSize.get(roomid) - 1;
		if(curRoomNum != 0){
			WSDC.roomSize.put(roomid, curRoomNum);
			WSDC.Rooms.get(roomid).remove(mySession);
		}else{//需要关闭这个房间了
			WSDC.roomSize.remove(roomid);
			WSDC.Rooms.remove(roomid);
			WSDC.roomList.remove(WSDC.roomList.indexOf(roomid));
			WSDC.GDCs.remove(roomid);
			WSDC.isLoaded.remove(roomid);
		}
		
		
	}
	@OnError
	public void onError(Session session, Throwable throwable){
//		System.out.println("用户："+mySession.getId()+" 连接异常关闭");
//		CloseReason reason = null;
//		onClose(mySession, reason);
	}
	
	public void GenOneGame(int room_id){
		RunOneRoomGameLogic RORGl =new RunOneRoomGameLogic(WSDC,room_id);
		Thread S_RORGL = new Thread(RORGl,"tank_room_id-"+room_id);
		S_RORGL.start();
		while( WSDC.isLoaded.get(room_id) == null){
			if(WSDC.GDCs.get(room_id) != null){
				//到此游戏初始化成功
				System.out.println("到此游戏初始化成功");
				WSDC.isLoaded.put(room_id, 1);
			}
		}
	}
	
	public void getIniData(){
		if(WSDC.isLoaded.get(roomid) !=null && WSDC.isLoaded.get(roomid)==1 ){
			this.isLoaded_gDC =true;
			this.gDC=WSDC.GDCs.get(roomid);
		}
		System.out.println("房间："+roomid+" 玩家："+mySession.getId()+"游戏数据中心初始化结束");
	}
}
