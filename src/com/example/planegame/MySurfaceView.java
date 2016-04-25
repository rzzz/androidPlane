package com.example.planegame;

import java.util.Iterator;
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

public class MySurfaceView extends SurfaceView implements
		SurfaceHolder.Callback, Runnable {

	private SurfaceHolder sfh;
	private Paint paint;
	private Thread th;
	private boolean flag;
	private Canvas canvas;
	public static int screenW, screenH;
	// ������Ϸ״̬����
	public static final int GAME_MENU = 0;// ��Ϸ��ʼ�˵�
	public static final int GAMEING = 1;// ��Ϸ��
	public static final int GAME_WIN = 2;// ��Ϸʤ��
	public static final int GAME_LOST = 3;// ��Ϸʧ��
	public static final int GAME_PAUSE = 4;// ��Ϸ��ͣ
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
	
	//��ըЧ������
	private Vector<Boom> vcBoom;
	
	//����Boss
	private Boss boss;
	//Boss���ӵ�����
	public static Vector<Bullet> vcBulletBoss;
	
	public MySurfaceView(Context context) {
		super(context);

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
			//�����ӵ�����ʵ��
			vcBulletPlayer = new Vector<Bullet>();
			
			// ʵ���л�����
			vcEnemy = new Vector<Enemy>();
			//�л��ӵ�����ʵ��
			vcBullet = new Vector<Bullet>();
			
			// ʵ�������
			random = new Random();
			
			//��ըЧ������ʵ��
			vcBoom = new Vector<Boom>();
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
			//չʾ��ʼ��Ϸ����
			break;
		case GAMEING:
			// �����߼�
			backGround.logic();
			player.logic();
			if (!isBoss) {
				// �л��߼�������һ���ӵ���ײ�����Ƴ������ģ�Ȼ�����һ�λ�����ײ��Ȼ�����ʣ��ĵл��߼�����������µĵл�
				//���������ӵ���л���ײ
				for (int i = 0; i < vcBulletPlayer.size(); i++) {
					//ȡ�������ӵ�������ÿ��Ԫ��
					Bullet blPlayer = vcBulletPlayer.elementAt(i);
					if(!blPlayer.isDead){
						for (int j = 0; j < vcEnemy.size(); j++) {
							//��ӱ�ըЧ��
							//ȡ���л�������ÿ��Ԫ�������ӵ������ж�
							Enemy en = vcEnemy.elementAt(j);
							if(!en.isDead){
								if (en.isCollsionWith(blPlayer)) {
									en.isDead = true;
									blPlayer.isDead = true;
									vcBoom.add(new Boom(bmpBoom, vcEnemy.elementAt(j).x, vcEnemy.elementAt(j).y, 7));
								}
							}
						}
					}
				}
				
				Iterator<Enemy> eni = vcEnemy.iterator();
				while (eni.hasNext()) {
					Enemy en = eni.next();
					if (en.isDead) {
						eni.remove();
					}else if(player.isCollsionWith(en)){
						eni.remove();
						player.setPlayerHp(player.getPlayerHp() - 1);
						// ������Ѫ��С��0���ж���Ϸʧ��
						if (player.getPlayerHp() <= -1) {
							gameState = GAME_LOST;
						}
					}else{
						en.logic();
					}
				}				
								
				//�����Ƿ���Ҫ���¹���
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
							vcEnemy.addElement(new Enemy(bmpEnemyDuck, 2, -50,	y));
							// Ѽ����
						} else if (enemyArray[enemyArrayIndex][i] == 3) {
							int y = random.nextInt(20);
							vcEnemy.addElement(new Enemy(bmpEnemyDuck, 3, screenW + 50, y));
						}
					}
					// �����ж���һ���Ƿ�Ϊ���һ��(Boss)
					if (enemyArrayIndex == enemyArray.length - 1) {
						isBoss = true;
					} else {
						enemyArrayIndex++;
					}
				}
				
				//�л��ӵ��߼������Ƴ������ģ�Ȼ�����һ����ײ��Ȼ�����ʣ����ӵ��߼����������µ��ӵ�
				Iterator<Bullet> bti = vcBullet.iterator();
				while (bti.hasNext()) {
					Bullet bt = bti.next();
					if(bt.isDead){
						bti.remove();
					}else if(player.isCollsionWith(bt)){
						bti.remove();
						player.setPlayerHp(player.getPlayerHp() - 1);
						//������Ѫ��С��0���ж���Ϸʧ��
						if (player.getPlayerHp() <= -1) {
							gameState = GAME_LOST;
						}
					}else{
						bt.logic();
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
				vcEnemy.clear();
				vcBullet.clear();
				
				gameState = GAME_WIN;
			}
			
			//�����ӵ��߼���ɾ�������£���ӣ�������ײ��ǰ���Ѽ���
			Iterator<Bullet> pbui = vcBulletPlayer.iterator();
			while(pbui.hasNext()){
				Bullet b = pbui.next();
				if(b.isDead){
					pbui.remove();
				}else{
					b.logic();
				}
			}
			
			//���һ�������ӵ�
			countPlayerBullet++;
			if (countPlayerBullet % 20 == 0) {
				vcBulletPlayer.add(new Bullet(bmpBullet, player.x + 15, player.y - 20, Bullet.BULLET_PLAYER));
			}
			
			//��ըЧ���߼�
			Iterator<Boom> boi = vcBoom.iterator();
			while (boi.hasNext()) {
				Boom boom = boi.next();
				if (boom.playEnd) {
					//������ϵĴ�������ɾ��
					boi.remove();
				} else {
					boom.logic();
				}
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
					//��ըЧ������
					for (int i = 0; i < vcBoom.size(); i++) {
						vcBoom.elementAt(i).draw(canvas, paint);
					}
					break;
				case GAME_PAUSE:
					break;
				case GAME_WIN:
					canvas.drawBitmap(bmpGameWin, 0, 0, paint);
					break;
				case GAME_LOST:
					canvas.drawBitmap(bmpGameLost, 0, 0, paint);
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
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
		//����back���ذ���
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			//��Ϸʤ����ʧ�ܡ�����ʱ��Ĭ�Ϸ��ز˵�
			if (gameState == GAMEING || gameState == GAME_WIN || gameState == GAME_LOST) {
				gameState = GAME_MENU;
				//Boss״̬����Ϊû����
				isBoss = false;
				//������Ϸ
				initGame();
				//���ù������
				enemyArrayIndex = 0;
			} else if (gameState == GAME_MENU) {
				//��ǰ��Ϸ״̬�ڲ˵����棬Ĭ�Ϸ��ذ����˳���Ϸ
				MainActivity.instance.finish();
				System.exit(0);
			}
			//��ʾ�˰����Ѵ������ٽ���ϵͳ����
			//�Ӷ�������Ϸ�������̨
			return true;
		}
		
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
		//����back���ذ���
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			//��Ϸʤ����ʧ�ܡ�����ʱ��Ĭ�Ϸ��ز˵�
			if (gameState == GAMEING || gameState == GAME_WIN || gameState == GAME_LOST) {
				gameState = GAME_MENU;
			}
			//��ʾ�˰����Ѵ������ٽ���ϵͳ����
			//�Ӷ�������Ϸ�������̨
			return true;
		}
		
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
