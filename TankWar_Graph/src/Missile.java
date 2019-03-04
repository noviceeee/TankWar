import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.List;

public class Missile {// 子弹类

	public static final int WIDTH = 10;// 子弹宽度
	public static final int HEIGHT = 10;// 子弹高度
	public static final int SPEED = 20;// 子弹每次向横纵坐标移动的距离，可当作速度，子弹速度应大于坦克速度

	int x, y;// 子弹初始坐标
	Direction dir;// 子弹运动方向只能是坦克类中已列举出来的九个方向之一

	private boolean live = true;// 表示子弹生命，默认活着
	private boolean good = true;// 子弹性质，用于区分敌我，己方子弹只能打敌方坦克
	private TankClient tc;// 用于访问此类成员变量

	public Missile(int x, int y, Direction dir) {// 用于创建指定坐标和方向的子弹对象
		this.x = x;
		this.y = y;
		this.dir = dir;
	}

	public Missile(int x, int y, Direction dir, boolean good, TankClient tc) {// 重载构造方法，传入TankClient对象，通过持有它的引用来访问成员变量
		this(x, y, dir);// 调用另一个构造方法
		this.tc = tc;// 初始化tc
		this.good = good;// 定义子弹是敌是友
	}

	public void draw(Graphics g) {// 画子弹
		if (!live) {// 如果子弹已死亡
			tc.missiles.remove(this);// 将子弹移出容器
			return;// 不再画出该颗子弹
		}
		Color c = g.getColor();// 取出当前颜色
		if (!good)// 敌方子弹为蓝色
			g.setColor(Color.BLUE);
		else// 我方为红色
			g.setColor(Color.RED);
		g.fillOval(x, y, WIDTH, HEIGHT);// 画一个圆作为子弹，定义坐标，宽度，高度
		g.setColor(c);// 还原当前颜色

		move();// 每次重画都要确定子弹的新位置
	}

	private void move() {// 根据方向移动子弹，STOP这个状态没必要
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
		}
		if (x < 0 || y < 0 || x > TankClient.GAME_WIDTH || y > TankClient.GAME_HEIGHT) {// 如果子弹超出窗口边界
			live = false;// 子弹死亡
		}
	}

	public boolean isLive() {// 返回子弹存活状态
		return live;
	}

	public Rectangle getRect() {// 获取和子弹坐标、宽高相同的方块，用于简单检测子弹和坦克是否碰撞
		return new Rectangle(x, y, WIDTH, HEIGHT);
	}

	public boolean hitTank(Tank t) {// 判断子弹是否击中坦克
		if (this.live && this.getRect().intersects(t.getRect()) && t.isLive() && this.good != t.isGood()) {
			// 如果子弹方块和坦克方块相交，子弹坦克都活着，且阵营不同
			if (t.isGood()) {// 如果是我方坦克
				t.setLife(t.getLife() - 1);// 被击中一次生命减1
				if (t.getLife() == 0)// 生命值为0时我方坦克死亡
					t.setLive(false);
			} else {// 敌方坦克被击中直接死亡
				t.setLive(false);
			}
			if (this.good)// 如果我方子弹击中敌方，我方得分
				tc.score++;
			this.live = false;// 子弹死亡
			Explosion e = new Explosion(x, y, tc);// 击中坦克时产生爆炸
			tc.explosions.add(e);
			return true;// 判定子弹击中坦克
		}
		return false;// 未击中
	}

	public boolean hitTanks(List<Tank> tanks) {// 判断子弹是否击中容器内某个坦克
		for (int i = 0; i < tanks.size(); i++) {// 遍历容器内所有坦克进行判断
			if (hitTank(tanks.get(i)))
				return true;
		}
		return false;
	}

	public boolean hitWall(Wall w) {// 子弹速度不能太快或墙不能太薄，会发生还来不及判断是否撞上就穿墙而过的情况
		if (this.live && this.getRect().intersects(w.getRect())) {
			this.live = false;
			return true;
		}
		return false;
	}
}
