package com.hxs.pojo;

import org.json.simple.JSONObject;

/**
 * 坦克类
 * @author xiaoshan
 *
 */
public class Tank {
	/**
	 * 坦克的ID
	 */
	private String id;
	/**
	 * 坦克方块的左上角X坐标值
	 */
	private int X;
	/**
	 * 坦克方块的左上角Y坐标值
	 */
	private int Y;
	/**
	 * 坦克当前的运动方向，0向左，1向上，2向右，3向下
	 */
	private int R;
	/**
	 * 坦克当前的帧显示状态0-9
	 */
	private int F;
	
	public int getX() {
		return X;
	}
	public void setX(int x) {
		X = x;
	}
	public int getY() {
		return Y;
	}
	public void setY(int y) {
		Y = y;
	}
	public int getR() {
		return R;
	}
	public void setR(int r) {
		R = r;
	}
	public int getF() {
		return F;
	}
	public void setF(int f) {
		F = f;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	@Override
	public String toString() {
		return "Tank [id=" + id + ", X=" + X + ", Y=" + Y + ", R=" + R + ", F=" + F + "]";
	}
	
	@SuppressWarnings("unchecked")
	public JSONObject getJsonObject(){
		JSONObject Json_tank = new JSONObject();
		Json_tank.put("id", this.getId());
		Json_tank.put("X", this.getX());
		Json_tank.put("Y", this.getY());
		Json_tank.put("F", this.getF());
		Json_tank.put("R", this.getR());
		
		return Json_tank;
	}
	
}
