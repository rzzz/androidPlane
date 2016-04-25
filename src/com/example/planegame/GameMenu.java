package com.example.planegame;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Region;
import android.view.MotionEvent;

/**
 * @author Himi	 //ժ��himi
 * ��ʼ��Ϸ�˵�
 */
public class GameMenu {
	//�˵�����ͼ
	private Bitmap bmpMenu;
	//��ťͼƬ��Դ(���º�δ����ͼ)
	private Bitmap bmpButton, bmpButtonPress;
	//��ť������
	private int btnX, btnY;
	//��ť�Ƿ��±�ʶλ
	private Boolean isPress;
	//�˵���ʼ��
	public GameMenu(Bitmap bmpMenu, Bitmap bmpButton, Bitmap bmpButtonPress) {
		this.bmpMenu = bmpMenu;
		this.bmpButton = bmpButton;
		this.bmpButtonPress = bmpButtonPress;
		//X���У�Y������Ļ�ײ�
		btnX = MySurfaceView.screenW / 2 - bmpButton.getWidth() / 2;
		btnY = MySurfaceView.screenH - bmpButton.getHeight() * 2;
		isPress = false;
	}
	//�˵���ͼ����
	public void draw(Canvas canvas, Paint paint) {
		canvas.save();
		//���Ʋ˵�����ͼ
		canvas.scale(MySurfaceView.screenW / (float)bmpMenu.getWidth(), MySurfaceView.screenH / (float)bmpMenu.getHeight(), 0, 0);
		canvas.drawBitmap(bmpMenu, 0, 0, paint);
		canvas.restore();
		
		canvas.save();
		//����δ���°�ťͼ
		if (isPress) {//�����Ƿ��»��Ʋ�ͬ״̬�İ�ťͼ
			canvas.drawBitmap(bmpButtonPress, btnX, btnY, paint);
		} else {
			canvas.drawBitmap(bmpButton, btnX, btnY, paint);
		}
		canvas.restore();
	}
	//�˵������¼���������Ҫ���ڴ���ť�¼�
	public void onTouchEvent(MotionEvent event) {
		//��ȡ�û���ǰ����λ��
		int pointX = (int) event.getX();
		int pointY = (int) event.getY();
		//���û��ǰ��¶������ƶ�����
		if (event.getAction() == MotionEvent.ACTION_DOWN || event.getAction() == MotionEvent.ACTION_MOVE) {
			//�ж��û��Ƿ����˰�ť
			Rect rect = new Rect(btnX, btnY, btnX + bmpButton.getWidth(), btnY + bmpButton.getHeight());
			Region region = new Region(rect);
			if (region.contains(pointX, pointY)) {
				isPress = true;
			} else {
				isPress = false;
			}
			//���û���̧����
		} else if (event.getAction() == MotionEvent.ACTION_UP) {
			//̧���ж��Ƿ�����ť����ֹ�û��ƶ�����
			Rect rect = new Rect(btnX, btnY, btnX + bmpButton.getWidth(), btnY + bmpButton.getHeight());
			Region region = new Region(rect);
			if (region.contains(pointX, pointY)) {
				//��ԭButton״̬Ϊδ����״̬
				isPress = false;
				//�ı䵱ǰ��Ϸ״̬Ϊ��ʼ��Ϸ
				MySurfaceView.gameState = MySurfaceView.GAMEING;
			}
		}
	}
}
