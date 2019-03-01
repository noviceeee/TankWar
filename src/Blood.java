import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

public class Blood {// �з�̹�˱��ݻٺ����Ѫ�飬�ҷ��Ե���ɻָ�һ��Ѫ��
	int WIDTH = 10;// ���
	int HEIGHT = 10;// �߶�
	int x, y;//����
	TankClient tc;
	
	private boolean live = true;// Ĭ�ϻ���
	
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

	public void draw(Graphics g) {// ��Ѫ��
		if(!live) {
			tc.explosions.remove(this);// ��������Ѫ���Ƴ�������������
			return;
			}
		Color c = g.getColor();
		g.setColor(Color.PINK);
			g.fillRect(x, y, WIDTH, HEIGHT);
		g.setColor(c);
	}

	public Rectangle getRect() {//���ɺ�Ѫ��λ�ô�С��ͬ�ķ��飬���ں�̹�˵���ײ��⣨Ѫ�鱻̹�˳Ե���
		return new Rectangle(x, y, WIDTH, HEIGHT);
	}
}
