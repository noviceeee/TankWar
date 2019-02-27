import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

public class Missile {//�ӵ���
	
	public static final int WIDTH = 10;//�ӵ����
	public static final int HEIGHT = 10;//�ӵ��߶�
	public static final int SPEED = 10;//�ӵ�ÿ������������ƶ��ľ��룬�ɵ����ٶȣ��ӵ��ٶ�Ӧ����̹���ٶ�
	
	int x, y;//�ӵ���ʼ����
	Tank.Direction dir;//�ӵ��˶�����ֻ����̹���������оٳ����ľŸ�����֮һ
	
	private boolean live = true;//��ʾ�ӵ�������Ĭ�ϻ���
	private TankClient tc;//���ڷ��ʴ����Ա����

	public Missile(int x, int y, Tank.Direction dir) {//���ڴ���ָ������ͷ�����ӵ�����
		this.x = x;
		this.y = y;
		this.dir = dir;
	}
	
	public Missile(int x, int y, Tank.Direction dir, TankClient tc) {//���ع��췽��������TankClient����ͨ�������������������ʳ�Ա����
		this(x, y, dir);//������һ�����췽��
		this.tc = tc;//��ʼ��tc
	}
	
	public void draw(Graphics g) {//���ӵ�
		if(!live) {//����ӵ�������
			tc.missiles.remove(this);//���ӵ��Ƴ�����
			return;//���ٻ����ÿ��ӵ�
		}
		Color c = g.getColor();//ȡ����ǰ��ɫ
		g.setColor(Color.WHITE);//������Ϊ��ɫ
		g.fillOval(x, y, WIDTH, HEIGHT);//��һ��Բ��Ϊ�ӵ����������꣬��ȣ��߶�
		g.setColor(c);//��ԭ��ǰ��ɫ
		
		move();//ÿ���ػ���Ҫȷ���ӵ�����λ��
	}

	private void move() {//���ݷ����ƶ��ӵ���STOP���״̬û��Ҫ
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
		if(x < 0 || y < 0 || x > TankClient.GAME_WIDTH || y > TankClient.GAME_HEIGHT) {//����ӵ��������ڱ߽�
			live = false;//�ӵ�����
		}
	}
	
	public boolean isLive() {//�����ӵ����״̬
		return live;
	}
	
	public Rectangle getRect() {//��ȡ���ӵ����ꡢ�����ͬ�ķ��飬���ڼ򵥼���ӵ���̹���Ƿ���ײ
		return new Rectangle(x, y, WIDTH, HEIGHT);
	}
	
	public boolean hitTank(Tank t) {//�ӵ�����̹��
		if(this.getRect().intersects(t.getRect()) && t.isLive()) {//����ӵ������̹�˷����ཻ������̹�˻���
			t.setLive(false);//̹������
			this.live = false;//�ӵ�����
			Explosion e = new Explosion(x, y, tc);//����̹��ʱ������ը
			tc.explosions.add(e);
			return true;//�ж��ӵ�����̹��
		}
		return false;//δ����
	}
}
