import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.util.List;
import java.util.Random;

public class Tank {// 坦克类

	public static final int WIDTH = 40;// 坦克宽度
	public static final int HEIGHT = 40;// 坦克高度
	public static final int SPEED = 5;// 坦克每次向横纵坐标方向移动的距离，可当作速度
	public static final int LIFE = 5;// 我方坦克总血量
	public static final int SKILL = 10;// 我方坦克总技能值

	TankClient tc;// 用于访问此类成员变量

	private boolean live = true;// 坦克生命状态，默认活着
	private int life = LIFE;// （我方）坦克生命值
	private BloodBar bb = new BloodBar();// 创建血条

	private int skill = SKILL;// 我方坦克技能值
	private SkillBar sb = new SkillBar();// 创建技能条

	private boolean good = true;// 区分敌方我方坦克

	private int x, y;// 坦克初始坐标
	int oldX, oldY;// 记录坦克旧的坐标，用于敌方撞墙时返回原先坐标
	private boolean bU = false, bD = false, bL = false, bR = false;// 依次代表上、下、左、右四个方向键的状态，当按下某一方向键时对应变量值为true

	private static Random r = new Random();// 用于随机指定敌方坦克方向，多个坦克可以共享一个随机数生成器，所以用static声明

	private Direction dir = Direction.STOP;// 坦克方向，默认为静止状态
	private Direction ptDir = Direction.D;// 炮筒方向默认向下

	private int step = r.nextInt(12) + 1;// 定义坦克朝某一方向移动的步数，用于使敌方随机运动更加不规则

	public Tank(int x, int y, boolean good) {// 构造方法，用于创建指定坐标的坦克对象
		this.x = x;
		this.y = y;
		this.good = good;
		this.oldX = x;
		this.oldY = y;
	}

	public Tank(int x, int y, boolean good, Direction dir, TankClient tc) {// 构造方法重载，传入TankClient对象，通过持有它的引用来访问成员变量
		this(x, y, good);// 调用另一个构造方法

		this.tc = tc;// 初始化tc
		this.dir = dir;// 指定坦克对象的方向
	}

	public void draw(Graphics g) {// 画坦克
		if (!live) {// 如果坦克死亡就不画
			if (!good)
				tc.tanks.remove(this);// 如果是敌方坦克，就将其从容器中移出
			return;
		}
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
		if (this.good) {// 画出我方坦克血条和技能条
			bb.draw(g);
			sb.draw(g);
		}
	}

	void move() {// 根据当前方向改变坦克坐标，控制移动
		this.oldX = x;
		this.oldY = y;// 每次移动前都记录旧的坐标值
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
		if (y < 40)// 窗口的标题栏高度
			y = 40;
		if (x > TankClient.GAME_WIDTH - WIDTH)
			x = TankClient.GAME_WIDTH - WIDTH;
		if (y > TankClient.GAME_HEIGHT - HEIGHT)
			y = TankClient.GAME_HEIGHT - HEIGHT;

		if (!good) {// 如果是敌方坦克
			if (step == 0) {// 步数为零时随机指定新的方向
				Direction[] dirs = Direction.values();// 不能直接根据索引找到枚举中的某一个值，要先将枚举转换成数组
				int num = r.nextInt(dirs.length);// 随机生成一个在[0,length)区间上的整数（含0不含length）作为数组索引
				dir = dirs[num];
				step = r.nextInt(12) + 1;// 随机生成新的步数，由于下一步要执行“step--”所以step必须大于0
			} // 否则当step为负数时方向永不改变
			step--;

			if (r.nextInt(10) > 7)// 随机开炮，这样设置可以控制开炮频率
				this.fire();
		}
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
		switch (key) {// 将方向键的状态重置为false
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
		case KeyEvent.VK_F:// 释放F键时，坦克开炮（如果设为键被按时开炮，会导致按住F不动时发射的子弹过于密集）
			fire();
			break;
		case KeyEvent.VK_S:// 释放S键时发射超级炮弹
			superFire();
			break;
		case KeyEvent.VK_F2:// 我方死亡时按F2键复活，血条技能条重置
			resurrect();
			break;
		}
		locateDirection();
	}

	public Missile fire() {// 坦克发射子弹
		if (!live)// 死了就不能发射
			return null;
		int x = this.x + WIDTH / 2 - Missile.WIDTH / 2;// 子弹左上角横坐标
		int y = this.y + HEIGHT / 2 - Missile.HEIGHT / 2;// 子弹左上角纵坐标
		Missile m = new Missile(x, y, ptDir, good, this.tc);// 创建一个和炮筒相同方向，位于坦克中央的己方子弹对象，并传入TankClient对象
		tc.missiles.add(m);// 将子弹放入容器
		return m;// 返回该子弹对象
	}

	public Missile fire(Direction dir) {// 重载fire方法，用于发射超级炮弹
		if (!live)// 死了就不能发射
			return null;
		int x = this.x + WIDTH / 2 - Missile.WIDTH / 2;// 子弹左上角横坐标
		int y = this.y + HEIGHT / 2 - Missile.HEIGHT / 2;// 子弹左上角纵坐标
		Missile m = new Missile(x, y, dir, good, this.tc);// 创建一个某方向、位于坦克中央的己方子弹对象，并传入TankClient对象
		tc.missiles.add(m);// 将子弹放入容器
		return m;// 返回该子弹对象
	}

	public Rectangle getRect() {// 获取和坦克坐标、宽高相同的方块，用于简单检测子弹和坦克是否碰撞
		return new Rectangle(x, y, WIDTH, HEIGHT);
	}

	public boolean isLive() {// 判断坦克是否活着，由于live属性是私有的，外部无法直接使用，所以向外提供一个访问方法
		if (!live && !this.isGood() && r.nextInt(5) > 1) {// 如果敌方坦克死亡就有概率掉落血块
			int x = this.x + WIDTH / 2 - Blood.WIDTH / 2;// 血块左上角横坐标
			int y = this.y + HEIGHT / 2 - Blood.HEIGHT / 2;// 血块左上角纵坐标
			Blood b = new Blood(x, y, tc);
			tc.bloods.add(b);
		}
		return live;
	}

	public void setLive(boolean live) {// 设置坦克是否活着
		this.live = live;
	}

	public boolean isGood() {// 判断坦克好坏
		return good;
	}

	private void stay() {// 返回上一步坐标
		x = oldX;
		y = oldY;
	}

	public boolean collidesWithWall(Wall w) {// 当撞墙时坦克返回上一步坐标（重新选择方向，避免停在墙前不动）
		if (this.live && this.getRect().intersects(w.getRect())) {// 简单检测坦克活着时和墙是否碰撞
			this.stay();
			return true;
		}
		return false;
	}

	public boolean collidesWithTanks(List<Tank> tanks) {// 解决坦克之间的重叠问题
		for (int i = 0; i < tanks.size(); i++) {// 遍历容器中的坦克
			Tank t = tanks.get(i);
			if (this != t && this.live && t.live && this.getRect().intersects(t.getRect())) {// 如果两辆不相同的坦克都活着且发生碰撞
				this.stay();// 均返回上一步坐标，避免互相重叠
				t.stay();
				return true;
			}

		}
		return false;
	}

	private void superFire() {// 有技能值时发射超级炮弹（同时朝八个方向发射）
		if (skill > 0) {
			Direction[] dirs = Direction.values();// 枚举类型不能直接根据索引取值，应先获取对应数组
			for (int i = 0; i < dirs.length - 1; i++) {// 向除STOP外的每个方向开炮
				fire(dirs[i]);
			}
			skill--;// 技能值减少
		}
	}

	public int getLife() {// 供外部访问私有变量life
		return life;
	}

	public void setLife(int life) {
		this.life = life;
	}

	public boolean eat(List<Blood> bloods) {// 坦克吃血块回血回技能
		for (int i = 0; i < bloods.size(); i++) {
			Blood b = bloods.get(i);

			if (b.isLive() && this.live && this.getRect().intersects(b.getRect())) {// 当血块和我方坦克都活着且碰撞时
				b.setLive(false);
				if (this.life < LIFE)// 血条不满时回血，满血时无效
					this.life += 1;
				if (this.skill < SKILL)// 技能条不满时回复技能
					this.skill += 2;
				return true;
			}
		}
		return false;
	}

	private void resurrect() {//满血复活，复活地点随机
		if (this.live == false) {
			this.live = true;
			this.life = LIFE;
			if(tc.score> 0)//复活一次扣1分，到零为止
			tc.score -= 1;
			randomLocate();
			while(collidesWithTanks(tc.tanks)) {//如果复活点存在其他坦克，就换个地点，避免两辆坦克卡住
			randomLocate();
			}
		}
	}
	
	private void randomLocate() {//在窗口范围内随机生成坐标位置
		x = r.nextInt(tc.GAME_WIDTH+1);//横坐标是[0, tc.GAME_WIDTH+1)区间上的整数，不包含tc.GAME_WIDTH+1
		y = 40+r.nextInt(tc.GAME_HEIGHT-40+1);//纵坐标同理，减去窗口的标题栏高度40
	}

	private class SkillBar {
		public void draw(Graphics g) {
			int w = WIDTH * skill / SKILL;// 当前技能条宽度
			Color c = g.getColor();
			g.setColor(Color.GREEN);
			g.drawRect(x, y - 10, WIDTH, 3);// 技能条总宽度
			g.setColor(Color.GREEN);
			g.fillRect(x, y - 10, w, 3);// 当前技能值
			g.setColor(c);

		}
	}

	private class BloodBar {
		public void draw(Graphics g) {
			int w = WIDTH * life / LIFE;// 当前血条宽度
			Color c = g.getColor();
			g.setColor(Color.RED);
			g.drawRect(x, y - 15, WIDTH, 3);// 血条总宽度
			g.setColor(Color.RED);
			g.fillRect(x, y - 15, w, 3);// 当前血条
			g.setColor(c);

		}
	}
}
