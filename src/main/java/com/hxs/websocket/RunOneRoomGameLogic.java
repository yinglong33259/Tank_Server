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

import com.hxs.pojo.Ball;
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
			t.setF(2);
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
					refreshBallStau();
					try {
						sendBallInfo();
						sendTanksInfo();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}else{
					timer.cancel();
					System.out.println("房间"+room_id+"：游戏运行结束........");
				}
			}

		}, 10, 10);//2s后开始执行，每次执行20ms
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
	public void refreshBallStau(){
		for(int i=0;i<gDC.balls.size();i++){
			Ball b = gDC.balls.get(i);
			checkBallPosition(b,i);
		}
	}
	/**
	 * 检验坦克下一步运动位置是否合法
	 * @param t 坦克
	 */
	private void checkTankPosition(Tank t){
		if(!checkTankCrash(t))
			return;
		int tank_x = t.getX();
		int tank_y = t.getY();
		int tank_f = t.getF();
		if(t.isMove()){
			if(tank_f<9){
				t.setF(++tank_f);
			}else{
				t.setF(3);
			}
			switch (t.getR()) {
			case 0:
				if(tank_x > 0){
					int col= Math.floorDiv(tank_x - gDC.tank_Speed,32) ;
					int row1= Math.floorDiv(tank_y,32) ;
					int row2= Math.floorDiv((tank_y+31),32);
					if(gDC.tileMap[row1][col] == 0 && gDC.tileMap[row2][col] ==0)
	                      t.setX(tank_x - gDC.tank_Speed);
				}
				break;
			case 1:
				if(tank_y > 0){
					 int row = Math.floorDiv(tank_y - gDC.tank_Speed,32);
					 int col1= Math.floorDiv(tank_x,32);
					 int col2= Math.floorDiv(tank_x+31,32);
					 if(gDC.tileMap[row][col1] == 0 && gDC.tileMap[row][col2] ==0)
						 t.setY(tank_y - gDC.tank_Speed);
				}
				break;
			case 2:
				if(tank_x < (gDC.mapCols-1) * gDC.Seq_width){
					int col=Math.floorDiv((tank_x+31)+gDC.tank_Speed,32);
					int row1=Math.floorDiv(tank_y,32);
					int row2=Math.floorDiv((tank_y+31),32);
					if(gDC.tileMap[row1][col] == 0 && gDC.tileMap[row2][col] ==0)
						t.setX(tank_x + gDC.tank_Speed);
				}
				break;
			case 3:
				if(tank_y < (gDC.mapRows-1) * gDC.Seq_width){
					int row = Math.floorDiv((tank_y+31)+gDC.tank_Speed,32);
					int col1= Math.floorDiv(tank_x,32);
					int col2= Math.floorDiv((tank_x+31),32);
	                if(gDC.tileMap[row][col1] == 0 && gDC.tileMap[row][col2] ==0)
	                	t.setY(tank_y + gDC.tank_Speed);
				}
				break;
			default:
				break;
			}
		}else{
			t.setF(2);
		}
	}
	/**
	 * 检查坦克是否发生碰撞
	 * r坦克运动方向
	 */
	private boolean checkTankCrash(Tank t){
		int tank_x = t.getX();
		int tank_y = t.getY();
		switch (t.getR()) {
			case 0:
				tank_x -= gDC.tank_Speed;
				break;
			case 1:
				tank_y -= gDC.tank_Speed;
				break;
			case 2:
				tank_x += gDC.tank_Speed;
				break;
			case 3:
				tank_y += gDC.tank_Speed;
				break;
		}
		for(Map.Entry<String, Tank> entry:gDC.tanks.entrySet()){
			if(entry.getKey()!=t.getId()){
				if( Math.abs(tank_y - entry.getValue().getY()) < 30 &&
						Math.abs(tank_x - entry.getValue().getX()) < 30
						)
					return false;
			}
		}
		return true;
	}

	@SuppressWarnings("unchecked")
	private void sendBallInfo() throws IOException {
		JSONObject sendNew = new JSONObject();
		JSONArray ja =new JSONArray();
		for(int i=0 ;i<gDC.balls.size();i++){
			ja.add(gDC.balls.get(i).getJsonObject());
		}
		sendNew.put("type", "balls_info");
		sendNew.put("value", ja.toJSONString());
		String str_sendNew=sendNew.toJSONString();
		
		for(int i=0;i<palyers.size();i++){
			palyers.get(i).getBasicRemote().sendText(str_sendNew);
		}
	}
	public void checkBallPosition(Ball b,int index){
		int ball_x=b.getX();
		int ball_y=b.getY();
		int ball_r=b.getR();
		switch (ball_r){
			case 0:
				if(ball_x - gDC.ball_radius - gDC.ball_Speed> 0){
					int col= Math.floorDiv(ball_x - gDC.ball_Speed -gDC.ball_radius ,32);
					int row1= Math.floorDiv(ball_y - gDC.ball_radius,32);
					int row2= Math.floorDiv((ball_y + gDC.ball_radius),32);
					if(gDC.tileMap[row1][col] == 0 && gDC.tileMap[row2][col] ==0){
						b.setX(ball_x - gDC.ball_Speed);
					}else{
						gDC.balls.remove(index);
					}
				}else{
					gDC.balls.remove(index);
				}
				break;
			case 1:
				if(ball_y - gDC.ball_radius - gDC.ball_Speed> 0){
					int row = Math.floorDiv(ball_y - gDC.tank_Speed - gDC.ball_radius,32);
					int col1= Math.floorDiv(ball_x - gDC.ball_radius ,32);
					int col2= Math.floorDiv(ball_x + gDC.ball_radius,32);
					if(gDC.tileMap[row][col1] == 0 && gDC.tileMap[row][col2] ==0){
						b.setY(ball_y - gDC.ball_Speed);
					}else{
						gDC.balls.remove(index);
					}
				}else{
					gDC.balls.remove(index);
				}
				break;
			case 2:
				if(ball_x + gDC.ball_radius + gDC.ball_Speed < gDC.mapCols * gDC.Seq_width){
					int col= Math.floorDiv(ball_x + gDC.ball_radius + gDC.ball_Speed,32);
					int row1= Math.floorDiv(ball_y - gDC.ball_radius,32);
					int row2= Math.floorDiv(ball_y + gDC.ball_radius,32);
					if(gDC.tileMap[row1][col] == 0 && gDC.tileMap[row2][col] ==0){
						b.setX(ball_x + gDC.ball_Speed);
					}else{
						gDC.balls.remove(index);
					}
				}else{
					gDC.balls.remove(index);
				}
				break;
			case 3:
				if(ball_y + gDC.ball_radius + gDC.ball_Speed< gDC.mapRows * gDC.Seq_width){
					int row = Math.floorDiv(ball_y+ gDC.ball_radius + gDC.ball_Speed,32);
					int col1= Math.floorDiv(ball_x - gDC.ball_radius ,32);
					int col2= Math.floorDiv(ball_x + gDC.ball_radius,32);
					if(gDC.tileMap[row][col1] == 0 && gDC.tileMap[row][col2] ==0){
						b.setY(ball_y + gDC.ball_Speed);
					}else{
						gDC.balls.remove(index);
					}
				}else{
					gDC.balls.remove(index);
				}
				break;
		}

	}
}
