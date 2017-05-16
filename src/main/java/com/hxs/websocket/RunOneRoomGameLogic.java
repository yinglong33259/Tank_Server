package com.hxs.websocket;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
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
			startGame();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void initialGame(){
		wSDC.GDCs.put(room_id, gDC);
		System.out.println("房间"+room_id+":游戏初始化开始" );
		palyers=wSDC.Rooms.get(room_id);
		for(int i=0;i<palyers.size();i++){
			Tank t= new Tank();
			t.setId(palyers.get(i).getId());
			t.setR(0);
			t.setF(0);
			t.setX(98);
			t.setY(98*(i+1));
			t.setMove(false);
			gDC.tanks.put(palyers.get(i).getId(), t);
		}
		System.out.println("游戏初始化结束");
	}
	public void startGame(){
		System.out.println("房间："+room_id+"游戏开始了");
		timer();
		System.out.println("房间："+room_id+" 游戏结束了");
	}
	
	final Timer timer = new Timer();
	public void timer() {
		timer.scheduleAtFixedRate(new TimerTask() {
			public void run() {
				if(wSDC.roomSize.get(room_id) != null){
					//System.out.println("游戏运行中........");
					refreshTankStau();
					try {
						sendTanksInfo();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}else{
					timer.cancel();
					System.out.println("房间"+room_id+"：游戏运行结束........");
				}
			}
		}, 50, 50);//2s后开始执行，每次执行20ms
	}
	@SuppressWarnings("unchecked")
	public void sendTanksInfo() throws IOException{
		JSONObject sendNew = new JSONObject();
		JSONArray ja =new JSONArray();
		for(Map.Entry<String, Tank> entry:gDC.tanks.entrySet()){
			ja.add(entry.getValue().getJsonObject());
		}
		sendNew.put("type", "tanks_info");
		sendNew.put("value", ja.toJSONString());
		String str_sendNew=sendNew.toJSONString();
		//System.out.println(str_sendNew);
		
		for(int i=0;i<palyers.size();i++){
			palyers.get(i).getBasicRemote().sendText(str_sendNew);
		}
	}

	public void refreshTankStau(){
		for(Map.Entry<String, Tank> entry:gDC.tanks.entrySet()){
			Tank t = entry.getValue();
			checkTankPosition(t);
		}
	}
	/**
	 * 检验坦克下一步运动位置是否合法
	 * @param t 坦克
	 */
	public void checkTankPosition(Tank t){
		int tank_x = t.getX();
		int tank_y = t.getY();
		
		if(t.isMove()){
			switch (t.getR()) {
			case 0:
				if(t.getX() > 0){
					int col= Math.floorDiv(tank_x - 1,32) ;
					int row1= Math.floorDiv(tank_y,32) ;
					int row2= Math.floorDiv((tank_y+31),32)	 ;
					if( row1 >=0 && row2 >= 0){
						if(gDC.tileMap[row1][col] == 0 && gDC.tileMap[row2][col] ==0)
		                      t.setX(tank_x-2);
					}
				}
				break;
			case 1:
				if(t.getY() > 0){
					 int row =(int) Math.floorDiv(tank_y - 1,32);
					 int col1=(int) Math.floorDiv(tank_x,32);
					 int col2=(int) Math.floorDiv(tank_x+31,32);
					 if(gDC.tileMap[row][col1] == 0 && gDC.tileMap[row][col2] ==0)
						 t.setY(tank_y-2);
				}
				break;
			case 2:
				if(t.getX() < (gDC.mapCols-1) * gDC.Seq_width){
					int col=(int) Math.floorDiv((tank_x+31)+1,32);
					int row1=(int) Math.floorDiv(tank_y,32);
					int row2=(int) Math.floorDiv((tank_y+31),32);
					if(gDC.tileMap[row1][col] == 0 && gDC.tileMap[row2][col] ==0)
						t.setX(tank_x + 2);
				}
				break;
			case 3:
				if(t.getY() < (gDC.mapRows-1) * gDC.Seq_width){
					int row = Math.floorDiv((tank_y+31)+1,32);
					int col1= Math.floorDiv(tank_x,32);
					int col2= Math.floorDiv((tank_x+31),32);
	                if(gDC.tileMap[row][col1] == 0 && gDC.tileMap[row][col2] ==0)
	                	t.setY(tank_y + 2);
				}
				break;
			default:
				break;
			}
		}
	}
}