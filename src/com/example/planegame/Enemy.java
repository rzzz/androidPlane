package com.example.planegame;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * @author Himi  //ժ��himi
 *
 */
public class Enemy {
	//�л��������ʶ
	public int type;	
	//��Ӭ
	public static final int TYPE_FLY = 1;
	//Ѽ��(���������˶�)
	public static final int TYPE_DUCKL = 2;
	//Ѽ��(���������˶�)
	public static final int TYPE_DUCKR = 3;
	
	//�л�ͼƬ��Դ
	public Bitmap bmpEnemy;
	//�л�����
	public int x, y;
	//�л�ÿ֡�Ŀ��
	public int frameW, frameH;
	//�л���ǰ֡�±�
	private int frameIndex;	//0��
	//�л����ƶ��ٶ�
	private int speed;;
	//�жϵл��Ƿ��Ѿ������򱻻�ɱ
	public boolean isDead;

	//�л��Ĺ��캯��
	public Enemy(Bitmap bmpEnemy, int enemyType, int x, int y) {
		this.bmpEnemy = bmpEnemy;
		frameW = bmpEnemy.getWidth() / 10;
		frameH = bmpEnemy.getHeight();
		this.type = enemyType;
		this.x = x;
		this.y = y;
		//��ͬ����ĵл�Ѫ����ͬ
		switch (type) {
		//��Ӭ
		case TYPE_FLY:
			speed = 25;
			break;
		//Ѽ��
		case TYPE_DUCKL:
			speed = 3;
			break;
		case TYPE_DUCKR:
			speed = 3;
			break;
		}
	}

	//�л���ͼ����
	public void draw(Canvas canvas, Paint paint) {
		if(isDead) return;
		
		canvas.save();
		canvas.clipRect(x, y, x + frameW, y + frameH);
		canvas.drawBitmap(bmpEnemy, x - frameIndex * frameW, y, paint);
		canvas.restore();
	}

	//�л��߼�AI
	public void logic() {
		if(isDead) return;
		
		//����ѭ������֡�γɶ���
		frameIndex++;
		if (frameIndex >= 10) {
			frameIndex = 0;
		}
		//��ͬ����ĵл�ӵ�в�ͬ��AI�߼�
		switch (type) {
		case TYPE_FLY:
				//���ٳ��֣����ٷ���
				speed -= 1;
				y += speed;
				
				if (y <= -200) {
					isDead = true;
				}
			break;
		case TYPE_DUCKL:
				//б���½��˶�
				x += speed / 2;
				y += speed;
				
				if (x > MySurfaceView.screenW) {
					isDead = true;
				}
			break;
		case TYPE_DUCKR:
				//б���½��˶�
				x -= speed / 2;
				y += speed;
				
				if (x <= -100) {
					isDead = true;
				}
			break;
		}
	}
	
	//�ж���ײ(�л��������ӵ���ײ)
	public boolean isCollsionWith(Bullet bullet) {
		if(!isDead){
			int x2 = bullet.bulletX;
			int y2 = bullet.bulletY;
			int w2 = bullet.bmpBullet.getWidth();
			int h2 = bullet.bmpBullet.getHeight();
			if (x >= x2 + w2) {
				return false;
			} else if (x + frameW <= x2) {
				return false;
			} else if (y >= y2 + h2) {
				return false;
			} else if (y + frameH <= y2) {
				return false;
			}
			//������ײ����������
			isDead = true;
			return true;
		}else{
			return false;
		}

	}

}
