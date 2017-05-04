package com.hxs.websocket;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Callable;

import javax.websocket.Session;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.hxs.pojo.Tank;

public class RunOneRoomGameLogic implements Runnable{
	private static WebSocketDataCenter wSDC;
	private GameDateCenter gDC = GameDateCenter.getInstance();
	ArrayList<Session> palyers;
	/**
	 * 当前游戏房间的ID
	 */
	private int room_id;
	public RunOneRoomGameLogic(WebSocketDataCenter wSDC,int room_id) {
		this.wSDC = wSDC;
		this.room_id=room_id;
	}
	@Override
	public void run() {
		try {
			
			initialGame();
			sendTanksInfo();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void initialGame(){
		wSDC.GDCs.put(room_id, gDC);
		
		System.out.println("游戏初始化开始");
		palyers=wSDC.Rooms.get(room_id);
		for(int i=0;i<palyers.size();i++){
			Tank t= new Tank();
			t.setId(palyers.get(i).getId());
			t.setR(0);
			t.setX(98);
			t.setY(98*(i+1));
			gDC.tanks.add(t);
		}
		System.out.println("游戏初始化结束");
	}
	public void startGame(){
		System.out.println("游戏开始了");
		timer();
	}
	
	final Timer timer = new Timer();
	public void timer() {
		timer.scheduleAtFixedRate(new TimerTask() {
			public void run() {
				if(wSDC.roomSize.get(room_id) != 0){
					System.out.println("游戏运行中........");
				}else{
					
				}
			}
		}, 2000, 20);//2s后开始执行，每次执行20ms
	}
	@SuppressWarnings("unchecked")
	public void sendTanksInfo() throws IOException{
		JSONObject sendNew = new JSONObject();
		JSONArray ja =new JSONArray();
		for(int i=0;i<gDC.tanks.size();i++){
			ja.add(gDC.tanks.get(i).getJsonObject());
		}
		sendNew.put("type", "tanks_info");
		sendNew.put("value", ja.toJSONString());
		String str_sendNew=sendNew.toJSONString();
		System.out.println(str_sendNew);
		
		for(int i=0;i<palyers.size();i++){
			palyers.get(i).getBasicRemote().sendText(str_sendNew);
		}
	}

	public GameDateCenter call() throws Exception {
		
		return gDC;
	}
}
