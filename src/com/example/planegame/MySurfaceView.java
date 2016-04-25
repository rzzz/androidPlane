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
	// 定义游戏状态常量
	public static final int GAME_MENU = 0;// 游戏开始菜单
	public static final int GAMEING = 1;// 游戏中
	public static final int GAME_WIN = 2;// 游戏胜利
	public static final int GAME_LOST = 3;// 游戏失败
	public static final int GAME_PAUSE = 4;// 游戏暂停
	// 当前游戏状态(默认初始在游戏菜单界面)
	public static int gameState = GAME_MENU;
	// 声明一个Resources实例便于加载图片
	private Resources res;
	// 声明游戏需要用到的图片资源(图片声明)
	private Bitmap bmpBackGround;// 游戏背景
	private Bitmap bmpBoom;// 爆炸效果
	private Bitmap bmpBoosBoom;// Boos爆炸效果
	private Bitmap bmpButton;// 游戏开始按钮
	private Bitmap bmpButtonPress;// 游戏开始按钮被点击
	private Bitmap bmpEnemyDuck;// 怪物鸭子
	private Bitmap bmpEnemyFly;// 怪物苍蝇
	private Bitmap bmpEnemyBoos;// 怪物猪头Boos
	private Bitmap bmpGameWin;// 游戏胜利背景
	private Bitmap bmpGameLost;// 游戏失败背景
	private Bitmap bmpPlayer;// 游戏主角飞机
	private Bitmap bmpPlayerHp;// 主角飞机血量
	private Bitmap bmpMenu;// 菜单背景
	public static Bitmap bmpBullet;// 子弹
	public static Bitmap bmpEnemyBullet;// 敌机子弹
	public static Bitmap bmpBossBullet;// Boss子弹

	// 声明一个菜单对象
	private GameMenu gameMenu;
	// 声明一个滚动游戏背景对象
	private GameBg backGround;

	// 声明主角对象
	private Player player;

	// 声明一个敌机容器
	private Vector<Enemy> vcEnemy;
	// 每次生成敌机的时间(毫秒)
	private int createEnemyTime = 50;
	private int count;// 计数器
	// 敌人数组：1和2表示敌机的种类，-1表示Boss
	// 二维数组的每一维都是一组怪物
	private int enemyArray[][] = { { 1, 2 }, { 1, 1 }, { 1, 3, 1, 2 },
			{ 1, 2 }, { 2, 3 }, { 3, 1, 3 }, { 2, 2 }, { 1, 2 }, { 2, 2 },
			{ 1, 3, 1, 1 }, { 2, 1 }, { 1, 3 }, { 2, 1 }, { -1 } };
	// 当前取出一维数组的下标
	private int enemyArrayIndex; // 从0开时走，最后一个见boss是结束游戏，boss一出，小怪全无
	// 是否出现Boss标识位
	private boolean isBoss;
	// 随机库，为创建的敌机赋予随即坐标
	private Random random;
	
	//敌机子弹容器
	private Vector<Bullet> vcBullet;
	//添加子弹的计数器
	private int countEnemyBullet;
	
	//主角子弹容器
	private Vector<Bullet> vcBulletPlayer;
	//添加子弹的计数器
	private int countPlayerBullet;
	
	//爆炸效果容器
	private Vector<Boom> vcBoom;
	
	//声明Boss
	private Boss boss;
	//Boss的子弹容器
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
	 * 自定义的游戏初始化函数
	 */
	private void initGame() {
		// 放置游戏切入后台重新进入游戏时，游戏被重置!
		// 当游戏状态处于菜单时，才会重置游戏
		if (gameState == GAME_MENU) {
			// 加载游戏资源
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

			// 菜单类实例
			gameMenu = new GameMenu(bmpMenu, bmpButton, bmpButtonPress);
			// 实例游戏背景
			backGround = new GameBg(bmpBackGround);

			// 实例主角
			player = new Player(bmpPlayer, bmpPlayerHp);
			//主角子弹容器实例
			vcBulletPlayer = new Vector<Bullet>();
			
			// 实例敌机容器
			vcEnemy = new Vector<Enemy>();
			//敌机子弹容器实例
			vcBullet = new Vector<Bullet>();
			
			// 实例随机库
			random = new Random();
			
			//爆炸效果容器实例
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

		// 逻辑处理根据游戏状态不同进行不同处理
		switch (gameState) {
		case GAME_MENU:
			//展示开始游戏界面
			break;
		case GAMEING:
			// 背景逻辑
			backGround.logic();
			player.logic();
			if (!isBoss) {
				// 敌机逻辑，计算一次子弹碰撞，先移除死亡的，然后计算一次机体碰撞，然后更新剩余的敌机逻辑，最后增加新的敌机
				//处理主角子弹与敌机碰撞
				for (int i = 0; i < vcBulletPlayer.size(); i++) {
					//取出主角子弹容器的每个元素
					Bullet blPlayer = vcBulletPlayer.elementAt(i);
					if(!blPlayer.isDead){
						for (int j = 0; j < vcEnemy.size(); j++) {
							//添加爆炸效果
							//取出敌机容器的每个元与主角子弹遍历判断
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
						// 当主角血量小于0，判定游戏失败
						if (player.getPlayerHp() <= -1) {
							gameState = GAME_LOST;
						}
					}else{
						en.logic();
					}
				}				
								
				//计算是否需要更新怪物
				count++;
				if (count % createEnemyTime == 0) {
					for (int i = 0; i < enemyArray[enemyArrayIndex].length; i++) {
						// 苍蝇
						if (enemyArray[enemyArrayIndex][i] == 1) {
							int x = random.nextInt(screenW - 100) + 50;
							vcEnemy.addElement(new Enemy(bmpEnemyFly, 1, x, -50));
							// 鸭子左
						} else if (enemyArray[enemyArrayIndex][i] == 2) {
							int y = random.nextInt(20);
							vcEnemy.addElement(new Enemy(bmpEnemyDuck, 2, -50,	y));
							// 鸭子右
						} else if (enemyArray[enemyArrayIndex][i] == 3) {
							int y = random.nextInt(20);
							vcEnemy.addElement(new Enemy(bmpEnemyDuck, 3, screenW + 50, y));
						}
					}
					// 这里判断下一组是否为最后一组(Boss)
					if (enemyArrayIndex == enemyArray.length - 1) {
						isBoss = true;
					} else {
						enemyArrayIndex++;
					}
				}
				
				//敌机子弹逻辑，先移除死亡的，然后计算一次碰撞，然后更新剩余的子弹逻辑，最后添加新的子弹
				Iterator<Bullet> bti = vcBullet.iterator();
				while (bti.hasNext()) {
					Bullet bt = bti.next();
					if(bt.isDead){
						bti.remove();
					}else if(player.isCollsionWith(bt)){
						bti.remove();
						player.setPlayerHp(player.getPlayerHp() - 1);
						//当主角血量小于0，判定游戏失败
						if (player.getPlayerHp() <= -1) {
							gameState = GAME_LOST;
						}
					}else{
						bt.logic();
					}
				}
				
				//添加一个敌机子弹
				countEnemyBullet++;
				if (countEnemyBullet % 40 == 0) {
					for (int i = 0; i < vcEnemy.size(); i++) {
						Enemy en = vcEnemy.elementAt(i);
						//不同类型敌机不同的子弹运行轨迹
						int bulletType = 0;
						switch (en.type) {
						//苍蝇
						case Enemy.TYPE_FLY:
							bulletType = Bullet.BULLET_FLY;
							break;
						//鸭子
						case Enemy.TYPE_DUCKL:
						case Enemy.TYPE_DUCKR:
							bulletType = Bullet.BULLET_DUCK;
							break;
						}
						vcBullet.add(new Bullet(bmpEnemyBullet, en.x + 10, en.y + 20, bulletType));
					}
				}				
			} else {
				// 小怪已出完，小怪出完的下一帧，出boss，boss一出，小怪即小怪子弹全无
				vcEnemy.clear();
				vcBullet.clear();
				
				gameState = GAME_WIN;
			}
			
			//主角子弹逻辑，删除，更新，添加，它的碰撞在前面已计算
			Iterator<Bullet> pbui = vcBulletPlayer.iterator();
			while(pbui.hasNext()){
				Bullet b = pbui.next();
				if(b.isDead){
					pbui.remove();
				}else{
					b.logic();
				}
			}
			
			//添加一个主角子弹
			countPlayerBullet++;
			if (countPlayerBullet % 20 == 0) {
				vcBulletPlayer.add(new Bullet(bmpBullet, player.x + 15, player.y - 20, Bullet.BULLET_PLAYER));
			}
			
			//爆炸效果逻辑
			Iterator<Boom> boi = vcBoom.iterator();
			while (boi.hasNext()) {
				Boom boom = boi.next();
				if (boom.playEnd) {
					//播放完毕的从容器中删除
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
				// 绘图函数根据游戏状态不同进行不同绘制
				switch (gameState) {
				case GAME_MENU:
					gameMenu.draw(canvas, paint);
					break;
				case GAMEING:
					backGround.draw(canvas, paint);
					player.draw(canvas, paint);
					if (!isBoss) {
						//敌机绘制
						for (int i = 0; i < vcEnemy.size(); i++) {
							vcEnemy.elementAt(i).draw(canvas, paint);
						}
						//敌机子弹绘制
						for (int i = 0; i < vcBullet.size(); i++) {
							vcBullet.elementAt(i).draw(canvas, paint);
						}
					} else {
						
					}
					
					//处理主角子弹绘制
					for (int i = 0; i < vcBulletPlayer.size(); i++) {
						vcBulletPlayer.elementAt(i).draw(canvas, paint);
					}
					//爆炸效果绘制
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
		// 触屏监听事件函数根据游戏状态不同进行不同监听
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
		//处理back返回按键
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			//游戏胜利、失败、进行时都默认返回菜单
			if (gameState == GAMEING || gameState == GAME_WIN || gameState == GAME_LOST) {
				gameState = GAME_MENU;
				//Boss状态设置为没出现
				isBoss = false;
				//重置游戏
				initGame();
				//重置怪物出场
				enemyArrayIndex = 0;
			} else if (gameState == GAME_MENU) {
				//当前游戏状态在菜单界面，默认返回按键退出游戏
				MainActivity.instance.finish();
				System.exit(0);
			}
			//表示此按键已处理，不再交给系统处理，
			//从而避免游戏被切入后台
			return true;
		}
		
		// 按键监听事件函数根据游戏状态不同进行不同监听
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
	 * 按键抬起事件监听
	 */
	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		//处理back返回按键
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			//游戏胜利、失败、进行时都默认返回菜单
			if (gameState == GAMEING || gameState == GAME_WIN || gameState == GAME_LOST) {
				gameState = GAME_MENU;
			}
			//表示此按键已处理，不再交给系统处理，
			//从而避免游戏被切入后台
			return true;
		}
		
		// 按键监听事件函数根据游戏状态不同进行不同监听
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
