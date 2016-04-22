package com.example.planegame;

import java.util.Random;
import java.util.Vector;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;

public class MySurfaceView extends SurfaceView implements
		SurfaceHolder.Callback, Runnable {

	private SurfaceHolder sfh;
	private Paint paint;
	private Thread th;
	private boolean flag;
	private Canvas canvas;
	public static int screenW, screenH;
	// ������Ϸ״̬����
	public static final int GAME_MENU = 0;// ��Ϸ��ʼ����˵�
	public static final int GAMEING = 1;// ��Ϸ��
	public static final int GAME_WIN = 2;// ��Ϸʤ��
	public static final int GAME_LOST = 3;// ��Ϸʧ��
	public static final int GAME_PAUSE = -1;// ��Ϸ��ͣ����˵�
	// ��ǰ��Ϸ״̬(Ĭ�ϳ�ʼ����Ϸ�˵�����)
	public static int gameState = GAME_MENU;
	// ����һ��Resourcesʵ�����ڼ���ͼƬ
	private Resources res;
	// ������Ϸ��Ҫ�õ���ͼƬ��Դ(ͼƬ����)
	private Bitmap bmpBackGround;// ��Ϸ����
	private Bitmap bmpBoom;// ��ըЧ��
	private Bitmap bmpBoosBoom;// Boos��ըЧ��
	private Bitmap bmpButton;// ��Ϸ��ʼ��ť
	private Bitmap bmpButtonPress;// ��Ϸ��ʼ��ť�����
	private Bitmap bmpEnemyDuck;// ����Ѽ��
	private Bitmap bmpEnemyFly;// �����Ӭ
	private Bitmap bmpEnemyBoos;// ������ͷBoos
	private Bitmap bmpGameWin;// ��Ϸʤ������
	private Bitmap bmpGameLost;// ��Ϸʧ�ܱ���
	private Bitmap bmpPlayer;// ��Ϸ���Ƿɻ�
	private Bitmap bmpPlayerHp;// ���Ƿɻ�Ѫ��
	private Bitmap bmpMenu;// �˵�����
	public static Bitmap bmpBullet;// �ӵ�
	public static Bitmap bmpEnemyBullet;// �л��ӵ�
	public static Bitmap bmpBossBullet;// Boss�ӵ�

	// ����һ���˵�����
	private GameMenu gameMenu;
	// ����һ��������Ϸ��������
	private GameBg backGround;

	// �������Ƕ���
	private Player player;

	// ����һ���л�����
	private Vector<Enemy> vcEnemy;
	// ÿ�����ɵл���ʱ��(����)
	private int createEnemyTime = 50;
	private int count;// ������
	// �������飺1��2��ʾ�л������࣬-1��ʾBoss
	// ��ά�����ÿһά����һ�����
	private int enemyArray[][] = { { 1, 2 }, { 1, 1 }, { 1, 3, 1, 2 },
			{ 1, 2 }, { 2, 3 }, { 3, 1, 3 }, { 2, 2 }, { 1, 2 }, { 2, 2 },
			{ 1, 3, 1, 1 }, { 2, 1 }, { 1, 3 }, { 2, 1 }, { -1 } };
	// ��ǰȡ��һά������±�
	private int enemyArrayIndex; // ��0��ʱ�ߣ����һ����boss�ǽ�����Ϸ��bossһ����С��ȫ��
	// �Ƿ����Boss��ʶλ
	private boolean isBoss;
	// ����⣬Ϊ�����ĵл������漴����
	private Random random;
	
	//�л��ӵ�����
	private Vector<Bullet> vcBullet;
	//����ӵ��ļ�����
	private int countEnemyBullet;
	
	//�����ӵ�����
	private Vector<Bullet> vcBulletPlayer;
	//����ӵ��ļ�����
	private int countPlayerBullet;

	public MySurfaceView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub

		sfh = this.getHolder();
		sfh.addCallback(this);

		paint = new Paint();
		paint.setColor(Color.RED);
		paint.setAntiAlias(true);

		res = getResources();

		setFocusable(true);
		setFocusableInTouchMode(true);
		setKeepScreenOn(true);
	}

	@Override
	public void surfaceCreated(SurfaceHolder paramSurfaceHolder) {
		// TODO Auto-generated method stub
		screenW = this.getWidth();
		screenH = this.getHeight();

		initGame();
		flag = true;
		th = new Thread(this);
		th.start();
	}

	/*
	 * �Զ������Ϸ��ʼ������
	 */
	private void initGame() {
		// ������Ϸ�����̨���½�����Ϸʱ����Ϸ������!
		// ����Ϸ״̬���ڲ˵�ʱ���Ż�������Ϸ
		if (gameState == GAME_MENU) {
			// ������Ϸ��Դ
			bmpBackGround = BitmapFactory.decodeResource(res,
					R.drawable.background);
			bmpBoom = BitmapFactory.decodeResource(res, R.drawable.boom);
			bmpBoosBoom = BitmapFactory.decodeResource(res,
					R.drawable.boos_boom);
			bmpButton = BitmapFactory.decodeResource(res, R.drawable.button);
			bmpButtonPress = BitmapFactory.decodeResource(res,
					R.drawable.button_press);
			bmpEnemyDuck = BitmapFactory.decodeResource(res,
					R.drawable.enemy_duck);
			bmpEnemyFly = BitmapFactory.decodeResource(res,
					R.drawable.enemy_fly);
			bmpEnemyBoos = BitmapFactory.decodeResource(res,
					R.drawable.enemy_pig);
			bmpGameWin = BitmapFactory.decodeResource(res, R.drawable.gamewin);
			bmpGameLost = BitmapFactory
					.decodeResource(res, R.drawable.gamelost);
			bmpPlayer = BitmapFactory.decodeResource(res, R.drawable.player);
			bmpPlayerHp = BitmapFactory.decodeResource(res, R.drawable.hp);
			bmpMenu = BitmapFactory.decodeResource(res, R.drawable.menu);
			bmpBullet = BitmapFactory.decodeResource(res, R.drawable.bullet);
			bmpEnemyBullet = BitmapFactory.decodeResource(res,
					R.drawable.bullet_enemy);
			bmpBossBullet = BitmapFactory.decodeResource(res,
					R.drawable.boosbullet);

			// �˵���ʵ��
			gameMenu = new GameMenu(bmpMenu, bmpButton, bmpButtonPress);
			// ʵ����Ϸ����
			backGround = new GameBg(bmpBackGround);

			// ʵ������
			player = new Player(bmpPlayer, bmpPlayerHp);

			// ʵ���л�����
			vcEnemy = new Vector<Enemy>();
			// ʵ�������
			random = new Random();
		}
	}

	@Override
	public void run() {
		while (flag) {
			long start = System.currentTimeMillis();
			myDraw();
			logic();
			long end = System.currentTimeMillis();
			try {
				if (end - start < 50) {
					Thread.sleep(50 - (end - start));
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	private void logic() {

		// �߼����������Ϸ״̬��ͬ���в�ͬ����
		switch (gameState) {
		case GAME_MENU:
			break;
		case GAMEING:
			// �����߼�
			backGround.logic();
			player.logic();
			if (!isBoss) {
				// �л��߼������Ƴ������ģ�������µ�
				Vector toDel = new Vector<Enemy>();
				for (int i = 0; i < vcEnemy.size(); i++) {
					Enemy en = vcEnemy.elementAt(i);
					if (en.isDead) {
						toDel.addElement(en);
						// vcEnemy.removeElementAt(i);
					} else {
						en.logic();
					}
				}
				for (int i = 0; i < toDel.size(); i++) {
					vcEnemy.removeElement(toDel.elementAt(i));
				}

				count++;
				if (count % createEnemyTime == 0) {
					for (int i = 0; i < enemyArray[enemyArrayIndex].length; i++) {
						// ��Ӭ
						if (enemyArray[enemyArrayIndex][i] == 1) {
							int x = random.nextInt(screenW - 100) + 50;
							vcEnemy.addElement(new Enemy(bmpEnemyFly, 1, x, -50));
							// Ѽ����
						} else if (enemyArray[enemyArrayIndex][i] == 2) {
							int y = random.nextInt(20);
							vcEnemy.addElement(new Enemy(bmpEnemyDuck, 2, -50,
									y));
							// Ѽ����
						} else if (enemyArray[enemyArrayIndex][i] == 3) {
							int y = random.nextInt(20);
							vcEnemy.addElement(new Enemy(bmpEnemyDuck, 3,
									screenW + 50, y));
						}
					}
					// �����ж���һ���Ƿ�Ϊ���һ��(Boss)
					if (enemyArrayIndex == enemyArray.length - 1) {
						isBoss = true;
					} else {
						enemyArrayIndex++;
					}
				}

				// ����л������ǵ���ײ
				for (int i = 0; i < vcEnemy.size(); i++) {
					if (player.isCollsionWith(vcEnemy.elementAt(i))) {
						// ������ײ������Ѫ��-1
						player.setPlayerHp(player.getPlayerHp() - 1);
						// ������Ѫ��С��0���ж���Ϸʧ��
						if (player.getPlayerHp() <= -1) {
							gameState = GAME_LOST;
						}
					}
				}
				
				//�ӵ��߼������Ƴ������ģ�������µ�
				//����л��ӵ���������ײ
				for (int i = 0; i < vcBullet.size(); i++) {
					Bullet bt = vcBullet.elementAt(i);
					if (player.isCollsionWith(bt)) {
						bt.isDead = true;
						//������ײ������Ѫ��-1
						player.setPlayerHp(player.getPlayerHp() - 1);
						//������Ѫ��С��0���ж���Ϸʧ��
						if (player.getPlayerHp() <= -1) {
							gameState = GAME_LOST;
						}
					}
				}
				//���������ӵ���л���ײ
				for (int i = 0; i < vcBulletPlayer.size(); i++) {
					//ȡ�������ӵ�������ÿ��Ԫ��
					Bullet blPlayer = vcBulletPlayer.elementAt(i);
					for (int j = 0; j < vcEnemy.size(); j++) {
						//��ӱ�ըЧ��
						//ȡ���л�������ÿ��Ԫ�������ӵ������ж�
						Enemy en = vcEnemy.elementAt(i);
						if (en.isCollsionWith(blPlayer)) {
							//
						}
					}
				}
				//���һ���л��ӵ�
				countEnemyBullet++;
				if (countEnemyBullet % 40 == 0) {
					for (int i = 0; i < vcEnemy.size(); i++) {
						Enemy en = vcEnemy.elementAt(i);
						//��ͬ���͵л���ͬ���ӵ����й켣
						int bulletType = 0;
						switch (en.type) {
						//��Ӭ
						case Enemy.TYPE_FLY:
							bulletType = Bullet.BULLET_FLY;
							break;
						//Ѽ��
						case Enemy.TYPE_DUCKL:
						case Enemy.TYPE_DUCKR:
							bulletType = Bullet.BULLET_DUCK;
							break;
						}
						vcBullet.add(new Bullet(bmpEnemyBullet, en.x + 10, en.y + 20, bulletType));
					}
				}
				
			} else {
				// С���ѳ��꣬С�ֳ������һ֡����boss��bossһ����С�ּ�С���ӵ�ȫ��
			}
			break;
		case GAME_PAUSE:
			break;
		case GAME_WIN:
			break;
		case GAME_LOST:
			break;
		}
	}

	public void myDraw() {
		try {
			canvas = sfh.lockCanvas();
			if (canvas != null) {
				canvas.drawColor(Color.WHITE);
				// ��ͼ����������Ϸ״̬��ͬ���в�ͬ����
				switch (gameState) {
				case GAME_MENU:
					gameMenu.draw(canvas, paint);
					break;
				case GAMEING:
					backGround.draw(canvas, paint);
					player.draw(canvas, paint);
					if (!isBoss) {
						//�л�����
						for (int i = 0; i < vcEnemy.size(); i++) {
							vcEnemy.elementAt(i).draw(canvas, paint);
						}
						//�л��ӵ�����
						for (int i = 0; i < vcBullet.size(); i++) {
							vcBullet.elementAt(i).draw(canvas, paint);
						}
					} else {

					}
					
					//���������ӵ�����
					for (int i = 0; i < vcBulletPlayer.size(); i++) {
						vcBulletPlayer.elementAt(i).draw(canvas, paint);
					}
					break;
				case GAME_PAUSE:
					break;
				case GAME_WIN:
					break;
				case GAME_LOST:
					break;
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		} finally {
			if (canvas != null)
				sfh.unlockCanvasAndPost(canvas);
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// ���������¼�����������Ϸ״̬��ͬ���в�ͬ����
		switch (gameState) {
		case GAME_MENU:
			gameMenu.onTouchEvent(event);
			break;
		case GAMEING:
			break;
		case GAME_PAUSE:
			break;
		case GAME_WIN:
			break;
		case GAME_LOST:
			break;
		}
		return true;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// ���������¼�����������Ϸ״̬��ͬ���в�ͬ����
		switch (gameState) {
		case GAME_MENU:
			break;
		case GAMEING:
			player.onKeyDown(keyCode, event);
			break;
		case GAME_PAUSE:
			break;
		case GAME_WIN:
			break;
		case GAME_LOST:
			break;
		}
		return super.onKeyDown(keyCode, event);
	}

	/**
	 * ����̧���¼�����
	 */
	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		// ���������¼�����������Ϸ״̬��ͬ���в�ͬ����
		switch (gameState) {
		case GAME_MENU:
			break;
		case GAMEING:
			player.onKeyUp(keyCode, event);
			break;
		case GAME_PAUSE:
			break;
		case GAME_WIN:
			break;
		case GAME_LOST:
			break;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void surfaceChanged(SurfaceHolder paramSurfaceHolder, int paramInt1,
			int paramInt2, int paramInt3) {
		// TODO Auto-generated method stub

	}

	@Override
	public void surfaceDestroyed(SurfaceHolder paramSurfaceHolder) {
		// TODO Auto-generated method stub
		flag = false;
	}

}
