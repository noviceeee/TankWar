import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

public class Wall {
	int x, y, width, height;// ǽ�����ꡢ����
	TankClient tc;

	public Wall(int x, int y, int width, int height, TankClient tc) {
		this.x = x;
		this.y = y;
		this.height = height;
		this.width = width;
		this.tc = tc;
	}
	
	public void draw(Graphics g) {
		Color c = g.getColor();
		g.setColor(Color.WHITE);
		g.fillRect(x, y, width, height);//����ǽ
		g.setColor(c);
	}
	
	public Rectangle getRect() {
		return new Rectangle(x, y, width, height);
	}
}
