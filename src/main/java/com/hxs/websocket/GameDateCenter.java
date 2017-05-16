package com.hxs.websocket;

import java.util.ArrayList;
import java.util.HashMap;

import com.hxs.pojo.Ball;
import com.hxs.pojo.Tank;

public class GameDateCenter {
	/**
	 * 地图多少行
	 */
	int mapRows = 15;
	/**
	 * 地图多少列
	 */
	int mapCols = 30;
	/**
	 * 地图小方块的边长
	 */
	int Seq_width = 32;
	/**
	 * 定义地图矩阵
	 */
	int tileMap[][] = {
	               {26,26,26,0,0,0,0,0,0,0,0,0,50,0,0,0,0,0,0,0,0,0,100,100,100,100,0,26,26,26},
	               {26,0,0,0,0,0,71,0,71,0,0,0,50,0,0,0,71,71,71,71,0,0,0,0,0,0,0,0,0,0},
	               {26,0,34,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,71,0,0,0,0,0,0,0,0,0,0},
	               {0,0,34,0,0,0,0,0,0,34,34,0,0,0,0,0,0,0,0,71,0,0,34,34,34,34,34,0,0,0},
	               {0,0,34,0,0,0,0,0,0,0,0,34,0,0,0,0,35,0,0,71,0,0,0,0,0,0,0,0,0,100},
	               {0,0,34,0,0,0,0,0,0,35,0,34,0,0,0,0,35,0,0,0,0,0,35,35,35,35,0,0,0,100},
	               {0,0,34,0,0,0,0,0,0,35,0,34,0,34,0,0,35,0,0,0,0,0,0,0,0,35,0,0,0,100},
	               {0,0,0,0,35,35,35,0,0,35,0,0,0,34,0,0,35,0,0,0,0,0,0,0,0,35,0,0,0,100},
	               {0,0,0,0,0,0,35,0,0,0,0,0,0,34,0,0,35,35,35,0,0,34,0,0,0,35,0,72,0,0},
	               {0,100,100,100,0,0,35,0,0,0,0,0,0,0,0,0,0,0,35,0,0,34,0,0,0,0,0,72,0,0},
	               {0,100,100,100,0,0,35,0,0,0,0,0,0,0,0,0,0,0,35,0,0,34,0,0,0,0,0,72,0,0},
	               {0,0,0,100,0,0,0,0,0,0,72,72,72,72,72,0,0,0,35,0,0,34,34,0,100,0,0,72,0,0},
	               {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,35,0,0,0,0,0,0,0,0,72,0,0},
	               {50,0,34,34,34,34,34,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,100,0,0,0,0,0},
	               {50,0,0,0,0,0,0,0,0,50,50,50,50,50,0,0,0,0,0,50,50,50,50,0,0,0,0,0,0,26}
	};
	/**
	 * 坦克的数组,前为玩家session_ID;
	 */
	HashMap<String,Tank> tanks=new HashMap<String,Tank>();
	/**,
	 * 子弹数组
	 */
	ArrayList<Ball> balls=new ArrayList<Ball>();

	public static GameDateCenter getInstance(){
		return new GameDateCenter();
	}
	
}