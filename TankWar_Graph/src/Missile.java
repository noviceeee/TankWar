import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.List;

public class Missile {// �ӵ���

	public static final int WIDTH = 10;// �ӵ����
	public static final int HEIGHT = 10;// �ӵ��߶�
	public static final int SPEED = 20;// �ӵ�ÿ������������ƶ��ľ��룬�ɵ����ٶȣ��ӵ��ٶ�Ӧ����̹���ٶ�

	int x, y;// �ӵ���ʼ����
	Direction dir;// �ӵ��˶�����ֻ����̹���������оٳ����ľŸ�����֮һ

	private boolean live = true;// ��ʾ�ӵ�������Ĭ�ϻ���
	private boolean good = true;// �ӵ����ʣ��������ֵ��ң������ӵ�ֻ�ܴ�з�̹��
	private TankClient tc;// ���ڷ��ʴ����Ա����

	public Missile(int x, int y, Direction dir) {// ���ڴ���ָ������ͷ�����ӵ�����
		this.x = x;
		this.y = y;
		this.dir = dir;
	}

	public Missile(int x, int y, Direction dir, boolean good, TankClient tc) {// ���ع��췽��������TankClient����ͨ�������������������ʳ�Ա����
		this(x, y, dir);// ������һ�����췽��
		this.tc = tc;// ��ʼ��tc
		this.good = good;// �����ӵ��ǵ�����
	}

	public void draw(Graphics g) {// ���ӵ�
		if (!live) {// ����ӵ�������
			tc.missiles.remove(this);// ���ӵ��Ƴ�����
			return;// ���ٻ����ÿ��ӵ�
		}
		Color c = g.getColor();// ȡ����ǰ��ɫ
		if (!good)// �з��ӵ�Ϊ��ɫ
			g.setColor(Color.BLUE);
		else// �ҷ�Ϊ��ɫ
			g.setColor(Color.RED);
		g.fillOval(x, y, WIDTH, HEIGHT);// ��һ��Բ��Ϊ�ӵ����������꣬��ȣ��߶�
		g.setColor(c);// ��ԭ��ǰ��ɫ

		move();// ÿ���ػ���Ҫȷ���ӵ�����λ��
	}

	private void move() {// ���ݷ����ƶ��ӵ���STOP���״̬û��Ҫ
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
		if (x < 0 || y < 0 || x > TankClient.GAME_WIDTH || y > TankClient.GAME_HEIGHT) {// ����ӵ��������ڱ߽�
			live = false;// �ӵ�����
		}
	}

	public boolean isLive() {// �����ӵ����״̬
		return live;
	}

	public Rectangle getRect() {// ��ȡ���ӵ����ꡢ�����ͬ�ķ��飬���ڼ򵥼���ӵ���̹���Ƿ���ײ
		return new Rectangle(x, y, WIDTH, HEIGHT);
	}

	public boolean hitTank(Tank t) {// �ж��ӵ��Ƿ����̹��
		if (this.live && this.getRect().intersects(t.getRect()) && t.isLive() && this.good != t.isGood()) {
			// ����ӵ������̹�˷����ཻ���ӵ�̹�˶����ţ�����Ӫ��ͬ
			if (t.isGood()) {// ������ҷ�̹��
				t.setLife(t.getLife() - 1);// ������һ��������1
				if (t.getLife() == 0)// ����ֵΪ0ʱ�ҷ�̹������
					t.setLive(false);
			} else {// �з�̹�˱�����ֱ������
				t.setLive(false);
			}
			if (this.good)// ����ҷ��ӵ����ез����ҷ��÷�
				tc.score++;
			this.live = false;// �ӵ�����
			Explosion e = new Explosion(x, y, tc);// ����̹��ʱ������ը
			tc.explosions.add(e);
			return true;// �ж��ӵ�����̹��
		}
		return false;// δ����
	}

	public boolean hitTanks(List<Tank> tanks) {// �ж��ӵ��Ƿ����������ĳ��̹��
		for (int i = 0; i < tanks.size(); i++) {// ��������������̹�˽����ж�
			if (hitTank(tanks.get(i)))
				return true;
		}
		return false;
	}

	public boolean hitWall(Wall w) {// �ӵ��ٶȲ���̫���ǽ����̫�����ᷢ�����������ж��Ƿ�ײ�Ͼʹ�ǽ���������
		if (this.live && this.getRect().intersects(w.getRect())) {
			this.live = false;
			return true;
		}
		return false;
	}
}
