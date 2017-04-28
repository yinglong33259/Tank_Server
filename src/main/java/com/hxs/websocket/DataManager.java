package com.hxs.websocket;

import java.util.ArrayList;
import java.util.HashMap;

import javax.websocket.Session;

/**
 * Created by xiaoshan on 2017/4/27.
 */
public class DataManager {
	public static final DataManager dataManager=new DataManager();
	
	public int playerNum=0;
	public HashMap<String, Session> sessions;

	public int getPlayerNum() {
        return playerNum;
    }

    public void setPlayerNum(int playerNum) {
        this.playerNum = playerNum;
    }
    
    public HashMap<String, Session> getSessions() {
		return sessions;
	}

	public void setSessions(HashMap<String, Session> sessions) {
		this.sessions = sessions;
	}

	private DataManager() {
    	//其他类不可生成新的对象
	}
    public static DataManager  getInstance(){
    	return dataManager;
    }
}
