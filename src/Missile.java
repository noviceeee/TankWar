import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

public class Missile {//子弹类
	
	public static final int WIDTH = 10;//子弹宽度
	public static final int HEIGHT = 10;//子弹高度
	public static final int SPEED = 10;//子弹每次向横纵坐标移动的距离，可当作速度，子弹速度应大于坦克速度
	
	int x, y;//子弹初始坐标
	Tank.Direction dir;//子弹运动方向只能是坦克类中已列举出来的九个方向之一
	
	private boolean live = true;//表示子弹生命，默认活着
	private TankClient tc;//用于访问此类成员变量

	public Missile(int x, int y, Tank.Direction dir) {//用于创建指定坐标和方向的子弹对象
		this.x = x;
		this.y = y;
		this.dir = dir;
	}
	
	public Missile(int x, int y, Tank.Direction dir, TankClient tc) {//重载构造方法，传入TankClient对象，通过持有它的引用来访问成员变量
		this(x, y, dir);//调用另一个构造方法
		this.tc = tc;//初始化tc
	}
	
	public void draw(Graphics g) {//画子弹
		if(!live) {//如果子弹已死亡
			tc.missiles.remove(this);//将子弹移出容器
			return;//不再画出该颗子弹
		}
		Color c = g.getColor();//取出当前颜色
		g.setColor(Color.WHITE);//画笔设为白色
		g.fillOval(x, y, WIDTH, HEIGHT);//画一个圆作为子弹，定义坐标，宽度，高度
		g.setColor(c);//还原当前颜色
		
		move();//每次重画都要确定子弹的新位置
	}

	private void move() {//根据方向移动子弹，STOP这个状态没必要
		switch(dir) {
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
		if(x < 0 || y < 0 || x > TankClient.GAME_WIDTH || y > TankClient.GAME_HEIGHT) {//如果子弹超出窗口边界
			live = false;//子弹死亡
		}
	}
	
	public boolean isLive() {//返回子弹存活状态
		return live;
	}
	
	public Rectangle getRect() {//获取和子弹坐标、宽高相同的方块，用于简单检测子弹和坦克是否碰撞
		return new Rectangle(x, y, WIDTH, HEIGHT);
	}
	
	public boolean hitTank(Tank t) {//子弹攻击坦克
		if(this.getRect().intersects(t.getRect()) && t.isLive()) {//如果子弹方块和坦克方块相交，并且坦克活着
			t.setLive(false);//坦克死亡
			this.live = false;//子弹死亡
			Explosion e = new Explosion(x, y, tc);//击中坦克时产生爆炸
			tc.explosions.add(e);
			return true;//判定子弹击中坦克
		}
		return false;//未击中
	}
}
