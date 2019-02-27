import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;

public class Tank {// 坦克类

	public static final int WIDTH = 40;// 坦克宽度
	public static final int HEIGHT = 40;// 坦克高度
	public static final int SPEED = 5;// 坦克每次向横纵坐标方向移动的距离，可当作速度

	TankClient tc;// 用于访问此类成员变量

	private boolean live = true;//坦克生命状态，默认活着

	private boolean good = true;// 区分敌方我方坦克
	private int x, y;// 坦克初始坐标
	private boolean bU = false, bD = false, bL = false, bR = false;// 依次代表上、下、左、右四个方向键的状态，当按下某一方向键时对应变量值为true

	enum Direction {
		U, LU, L, LD, D, RD, R, RU, STOP
	};// 枚举，定义九种运动方向：上，左上，左， 左下，下， 右下， 右，右上，无（ 静止）

	private Direction dir = Direction.STOP;// 坦克方向，默认为静止状态
	private Direction ptDir = Direction.D;// 炮筒方向默认向下

	public Tank(int x, int y, boolean good) {// 构造方法，用于创建指定坐标的坦克对象
		this.x = x;
		this.y = y;
		this.good = good;
	}

	public Tank(int x, int y, boolean good, TankClient tc) {// 构造方法重载，传入TankClient对象，通过持有它的引用来访问成员变量
		this(x, y, good);// 调用另一个构造方法
		this.tc = tc;// 初始化tc
	}

	public void draw(Graphics g) {// 画坦克
		if(!live)//如果坦克死亡就不画
			return;
	
		Color c = g.getColor();// 取出当前颜色
		if (good)
			g.setColor(Color.RED);// 我方坦克为红色
		else
			g.setColor(Color.BLUE);// 敌方坦克为蓝色
		g.fillOval(x, y, WIDTH, HEIGHT);// 画一个圆作为坦克，设置其坐标，宽度和高度（左上角为原点， x轴向右，y轴向下）
		g.setColor(Color.WHITE);// 设置画笔为白色
		switch (ptDir) {// 根据炮筒方向画出炮筒（懒得计算了，所以对角线方向炮筒略长）
		case U:
			g.drawLine(x + WIDTH / 2, y, x + WIDTH / 2, y + HEIGHT / 2);
			break;
		case LU:
			g.drawLine(x, y, x + WIDTH / 2, y + HEIGHT / 2);
			break;
		case L:
			g.drawLine(x, y + HEIGHT / 2, x + WIDTH / 2, y + HEIGHT / 2);
			break;
		case LD:
			g.drawLine(x, y + HEIGHT, x + WIDTH / 2, y + HEIGHT / 2);
			break;
		case D:
			g.drawLine(x + WIDTH / 2, y + HEIGHT, x + WIDTH / 2, y + HEIGHT / 2);
			break;
		case RD:
			g.drawLine(x + WIDTH, y + HEIGHT, x + WIDTH / 2, y + HEIGHT / 2);
			break;
		case R:
			g.drawLine(x + WIDTH, y + HEIGHT / 2, x + WIDTH / 2, y + HEIGHT / 2);
			break;
		case RU:
			g.drawLine(x + WIDTH, y, x + WIDTH / 2, y + HEIGHT / 2);
			break;
		}
		g.setColor(c);// 恢复当前颜色

		move();// 每次画坦克都调用此方法，通过改变坦克坐标实现移动效果
	}

	void move() {// 根据当前方向改变坦克坐标，控制移动
		switch (dir) {
		case U:
			y -= SPEED;
			break;
		case LU:
			x -= SPEED;
			y -= SPEED;
			break;
		case L:
			x -= SPEED;
			break;
		case LD:
			x -= SPEED;
			y += SPEED;
			break;
		case D:
			y += SPEED;
			break;
		case RD:
			x += SPEED;
			y += SPEED;
			break;
		case R:
			x += SPEED;
			break;
		case RU:
			x += SPEED;
			y -= SPEED;
			break;
		case STOP:
			break;
		}

		if (this.dir != Direction.STOP) {// 如果坦克处于非静止状态
			this.ptDir = this.dir;// 将炮筒方向调整为与坦克运动方向一致
		}

		if (x < 0)// 解决坦克出界问题，运动到窗口边缘时无法前进
			x = 0;
		if (y < 0)
			y = 0;
		if (x > TankClient.GAME_WIDTH - WIDTH)
			x = TankClient.GAME_WIDTH - WIDTH;
		if (y > TankClient.GAME_HEIGHT - HEIGHT)
			y = TankClient.GAME_HEIGHT - HEIGHT;
	}

	public void keyPressed(KeyEvent e) {// 当按下按键时
		int key = e.getKeyCode();// 获取按键码
		switch (key) {// 如果按键为四个方向键之一，将对应的布尔值设为true
		case KeyEvent.VK_UP:
			bU = true;
			break;
		case KeyEvent.VK_DOWN:
			bD = true;
			break;
		case KeyEvent.VK_LEFT:
			bL = true;
			break;
		case KeyEvent.VK_RIGHT:
			bR = true;
			break;
		}
		locateDirection();// 确定坦克移动方向
	}

	void locateDirection() {// 根据四个方向键的状态确定坦克方向
		if (bU && !bD && !bL && !bR)
			dir = Direction.U;
		else if (bU && !bD && bL && !bR)
			dir = Direction.LU;
		else if (!bU && !bD && bL && !bR)
			dir = Direction.L;
		else if (!bU && bD && bL && !bR)
			dir = Direction.LD;
		else if (!bU && bD && !bL && !bR)
			dir = Direction.D;
		else if (!bU && bD && !bL && bR)
			dir = Direction.RD;
		else if (!bU && !bD && !bL && bR)
			dir = Direction.R;
		else if (bU && !bD && !bL && bR)
			dir = Direction.RU;
		else if (!bU && !bD && !bL && !bR)
			dir = Direction.STOP;
	}

	public void keyReleased(KeyEvent e) {// 释放按键时
		int key = e.getKeyCode();// 获取按键
		switch (key) {// 将该方向键的状态重置为false
		case KeyEvent.VK_CONTROL:// 释放ctrl键时，坦克开炮（如果设为键被按时开炮，会导致按住ctrl不动时发射的子弹过于密集）
			fire();
			break;
		case KeyEvent.VK_UP:
			bU = false;
			break;
		case KeyEvent.VK_DOWN:
			bD = false;
			break;
		case KeyEvent.VK_LEFT:
			bL = false;
			break;
		case KeyEvent.VK_RIGHT:
			bR = false;
			break;
		}
		locateDirection();
	}

	public Missile fire() {// 坦克发射子弹
		int x = this.x + WIDTH / 2 - Missile.WIDTH / 2;// 子弹左上角横坐标
		int y = this.y + HEIGHT / 2 - Missile.HEIGHT / 2;// 子弹左上角纵坐标
		Missile m = new Missile(x, y, ptDir, this.tc);// 创建一个和炮筒相同方向，位于坦克中央的子弹对象，并传入TankClient对象
		tc.missiles.add(m);// 将子弹放入容器
		return m;// 返回该子弹对象
	}
	
	public Rectangle getRect() {//获取和坦克坐标、宽高相同的方块，用于简单检测子弹和坦克是否碰撞
		return new Rectangle(x, y, WIDTH, HEIGHT);
	}
	public boolean isLive() {//判断坦克是否活着
		return live;
	}
	
	public void setLive(boolean live) {//设置坦克是否活着
		this.live = live;
	}
}
