package com.example.planegame;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Region;
import android.view.MotionEvent;

/**
 * @author Himi	 //摘自himi
 * 开始游戏菜单
 */
public class GameMenu {
	//菜单背景图
	private Bitmap bmpMenu;
	//按钮图片资源(按下和未按下图)
	private Bitmap bmpButton, bmpButtonPress;
	//按钮的坐标
	private int btnX, btnY;
	//按钮是否按下标识位
	private Boolean isPress;
	//菜单初始化
	public GameMenu(Bitmap bmpMenu, Bitmap bmpButton, Bitmap bmpButtonPress) {
		this.bmpMenu = bmpMenu;
		this.bmpButton = bmpButton;
		this.bmpButtonPress = bmpButtonPress;
		//X居中，Y紧接屏幕底部
		btnX = MySurfaceView.screenW / 2 - bmpButton.getWidth() / 2;
		btnY = MySurfaceView.screenH - bmpButton.getHeight() * 2;
		isPress = false;
	}
	//菜单绘图函数
	public void draw(Canvas canvas, Paint paint) {
		canvas.save();
		//绘制菜单背景图
		canvas.scale(MySurfaceView.screenW / (float)bmpMenu.getWidth(), MySurfaceView.screenH / (float)bmpMenu.getHeight(), 0, 0);
		canvas.drawBitmap(bmpMenu, 0, 0, paint);
		canvas.restore();
		
		canvas.save();
		//绘制未按下按钮图
		if (isPress) {//根据是否按下绘制不同状态的按钮图
			canvas.drawBitmap(bmpButtonPress, btnX, btnY, paint);
		} else {
			canvas.drawBitmap(bmpButton, btnX, btnY, paint);
		}
		canvas.restore();
	}
	//菜单触屏事件函数，主要用于处理按钮事件
	public void onTouchEvent(MotionEvent event) {
		//获取用户当前触屏位置
		int pointX = (int) event.getX();
		int pointY = (int) event.getY();
		//当用户是按下动作或移动动作
		if (event.getAction() == MotionEvent.ACTION_DOWN || event.getAction() == MotionEvent.ACTION_MOVE) {
			//判定用户是否点击了按钮
			Rect rect = new Rect(btnX, btnY, btnX + bmpButton.getWidth(), btnY + bmpButton.getHeight());
			Region region = new Region(rect);
			if (region.contains(pointX, pointY)) {
				isPress = true;
			} else {
				isPress = false;
			}
			//当用户是抬起动作
		} else if (event.getAction() == MotionEvent.ACTION_UP) {
			//抬起判断是否点击按钮，防止用户移动到别处
			Rect rect = new Rect(btnX, btnY, btnX + bmpButton.getWidth(), btnY + bmpButton.getHeight());
			Region region = new Region(rect);
			if (region.contains(pointX, pointY)) {
				//还原Button状态为未按下状态
				isPress = false;
				//改变当前游戏状态为开始游戏
				MySurfaceView.gameState = MySurfaceView.GAMEING;
			}
		}
	}
}
