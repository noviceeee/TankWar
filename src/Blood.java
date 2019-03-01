import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

public class Blood {// 敌方坦克被摧毁后掉落血块，我方吃掉后可恢复一定血量
	int WIDTH = 10;// 宽度
	int HEIGHT = 10;// 高度
	int x, y;//坐标
	TankClient tc;
	
	private boolean live = true;// 默认活着
	
	public Blood(int x, int y, TankClient tc) {
		this.x = x;
		this.y = y;
		this.tc = tc;
	}
	
	public boolean isLive() {
		return live;
	}

	public void setLive(boolean live) {
		this.live = live;
	}

	public void draw(Graphics g) {// 画血块
		if(!live) {
			tc.explosions.remove(this);// 将死亡的血块移出容器，不画出
			return;
			}
		Color c = g.getColor();
		g.setColor(Color.PINK);
			g.fillRect(x, y, WIDTH, HEIGHT);
		g.setColor(c);
	}

	public Rectangle getRect() {//生成和血块位置大小相同的方块，用于和坦克的碰撞检测（血块被坦克吃掉）
		return new Rectangle(x, y, WIDTH, HEIGHT);
	}
}
