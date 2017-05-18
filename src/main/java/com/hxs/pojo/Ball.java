package com.hxs.pojo;

import org.json.simple.JSONObject;

/**
 * 子弹类
 * @author xiaoshan
 *
 */
public class Ball {
	/**
	 * 子弹当前位置的x轴坐标
	 */
	private int x;
	/**
	 * 子弹当前位置的y轴坐标
	 */
	private int y;
	/**
	 * 子弹运动方向，0123表示上下左右
	 */
	private int r;
	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}
	public int getR() {
		return r;
	}
	public void setR(int r) {
		this.r = r;
	}
	@Override
	public String toString() {
		return "Ball [x=" + x + ", y=" + y + ", r=" + r + "]";
	}
	public Object getJsonObject() {
		JSONObject Json_ball = new JSONObject();
		Json_ball.put("X", this.getX());
		Json_ball.put("Y", this.getY());
		Json_ball.put("R", this.getR());
		return Json_ball;
	}
	
	
}
