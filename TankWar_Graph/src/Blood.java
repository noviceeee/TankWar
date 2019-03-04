import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

public class Blood {// �з�̹�˱��ݻٺ����Ѫ�飬�ҷ��Ե���ɻָ�һ��Ѫ��
	public static final int WIDTH = 10;// ���
	public static final int HEIGHT = 10;// �߶�
	int x, y;// ����
	TankClient tc;

	private boolean live = true;// Ĭ�ϻ���
	private int time = 200;// ���ʱ�䣬һ��ʱ���Ѫ���Զ���ʧ

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
		if (time == 0)// ���ʱ��Ϊ0ʱѪ������
			live = false;
		if (!live) {
			tc.bloods.remove(this);// ��������Ѫ���Ƴ�������������
			return;
		}
		Color c = g.getColor();
		g.setColor(Color.PINK);
		g.fillRect(x, y, WIDTH, HEIGHT);// ����Ѫ��
		g.setColor(c);
		time--;// ÿ��һ�Σ�Ѫ����ʱ�����
	}

	public Rectangle getRect() {// ���ɺ�Ѫ��λ�ô�С��ͬ�ķ��飬���ں�̹�˵���ײ��⣨Ѫ�鱻̹�˳Ե���
		return new Rectangle(x, y, WIDTH, HEIGHT);
	}
}
