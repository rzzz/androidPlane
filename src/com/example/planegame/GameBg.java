package com.example.planegame;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * @author Himi  //ժ��himi
 *
 */
public class GameBg {
	//��Ϸ������ͼƬ��Դ
	//Ϊ��ѭ�����ţ����ﶨ������λͼ����
	//����Դ���õ���ͬһ��ͼƬ
	private Bitmap bmpBackGround1;
	private Bitmap bmpBackGround2;
	//��Ϸ��������
	private int bg1x=0, bg1y=0, bg2x=0, bg2y=0;
	//���������ٶ�
	private int speed = 3;

	//��Ϸ�������캯��
	public GameBg(Bitmap bmpBackGround) {
		this.bmpBackGround1 = bmpBackGround;
		this.bmpBackGround2 = bmpBackGround;
		//�����õ�һ�ű����ײ���������������Ļ
		bg1y = -Math.abs(bmpBackGround1.getHeight() - MySurfaceView.screenH);	//��������ͼƬ����Ļ˭��˭С��������ȷ��
		//�ڶ��ű���ͼ�����ڵ�һ�ű������Ϸ�
		//+111��ԭ����Ȼ���ű���ͼ�޷�϶���ӵ�����ΪͼƬ��Դͷβ
		//ֱ�����Ӳ���г��Ϊ�����Ӿ�������������ͼ���Ӷ�������λ��
		bg2y = bg1y - bmpBackGround1.getHeight() + 1;
	}
	//��Ϸ�����Ļ�ͼ����
	public void draw(Canvas canvas, Paint paint) {
		canvas.save();
		//�������ű���
		canvas.drawBitmap(bmpBackGround1, bg1x, bg1y, paint);
		canvas.drawBitmap(bmpBackGround2, bg2x, bg2y, paint);
		canvas.restore();
	}
	//��Ϸ�������߼�����
	public void logic() {
		bg1y += speed;
		bg2y += speed;
		//����һ��ͼƬ��Y���곬����Ļ��
		//���������������õ��ڶ���ͼ���Ϸ�
		if (bg1y > MySurfaceView.screenH) {
			bg1y = bg2y - bmpBackGround1.getHeight() + 1;
		}
		//���ڶ���ͼƬ��Y���곬����Ļ��
		//���������������õ���һ��ͼ���Ϸ�
		if (bg2y > MySurfaceView.screenH) {
			bg2y = bg1y - bmpBackGround1.getHeight() + 1;
		}
	}
}
