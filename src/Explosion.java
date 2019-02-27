import java.awt.Color;
import java.awt.Graphics;

public class Explosion {
	int x, y;// 爆炸坐标
	private boolean live = true;// 爆炸的生命状态，默认为活着

	int[] diameter = { 4, 7, 12, 18, 27, 36, 49, 30, 14, 6 };// 用不同直径的圆模拟爆炸过程
	int step = 0;// 爆炸阶段，对应不同直径

	private TankClient tc;// 持有引用

	public Explosion(int x, int y, TankClient tc) {
		this.x = x;
		this.y = y;
		this.tc = tc;
	}

	public void draw(Graphics g) {// 画出爆炸
		if (!live) {// 如果爆炸没活着就不画出来
			tc.explosions.remove(this);// 将死亡的爆炸移出容器
			return;
		}
		if (step == diameter.length) {// 如果爆炸结束
			live = false;// 爆炸死亡
			step = 0;// 阶段重置
			return;// 不画出来
		}
		Color c = g.getColor();
		g.setColor(Color.ORANGE);
		g.fillOval(x, y, diameter[step], diameter[step]);// 画出某一阶段的爆炸
		step++;// 爆炸进入下一阶段
		g.setColor(c);

	}
}
