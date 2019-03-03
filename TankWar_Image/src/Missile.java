import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Missile {// �ӵ���

	public static final int WIDTH = 10;// �ӵ����.ͼƬ�治׼ȷ���պ��ð�
	public static final int HEIGHT = 10;// �ӵ��߶�
	public static final int SPEED = Tank.SPEED*2;// �ӵ�ÿ������������ƶ��ľ��룬�ɵ����ٶȣ��ӵ��ٶ�Ӧ����̹���ٶ�
	private static Toolkit tk = Toolkit.getDefaultToolkit();/// �õ�Ĭ�Ϲ��߰���ͨ�����߰��ķ�����Ӳ���ϵ�ͼƬ���ؽ��ڴ�
	private static Map<String, Image> imgs = new HashMap<String, Image>();//��з��ӵ�ͼƬ�����ü�ֵ�ԣ�������ݴ�������ַ�����ȡ��ӦͼƬ
	private static Map<String, Image> gImgs = new HashMap<String, Image>();//���ҷ��ӵ�ͼƬ
	private static Image[] images= null;
	static {//��̬����飬������ļ��ض�ִ��
		images = new Image[]{
				tk.getImage(Tank.class.getClassLoader().getResource("images/missileU.gif")),
				tk.getImage(Tank.class.getClassLoader().getResource("images/missileLU.gif")),
				tk.getImage(Tank.class.getClassLoader().getResource("images/missileL.gif")),
				tk.getImage(Tank.class.getClassLoader().getResource("images/missileLD.gif")),
				tk.getImage(Tank.class.getClassLoader().getResource("images/missileD.gif")),
				tk.getImage(Tank.class.getClassLoader().getResource("images/missileRD.gif")),
				tk.getImage(Tank.class.getClassLoader().getResource("images/missileR.gif")),
				tk.getImage(Tank.class.getClassLoader().getResource("images/missileRU.gif")),
				
				tk.getImage(Tank.class.getClassLoader().getResource("images/goodMissileU.gif")),
				tk.getImage(Tank.class.getClassLoader().getResource("images/goodMissileLU.gif")),
				tk.getImage(Tank.class.getClassLoader().getResource("images/goodMissileL.gif")),
				tk.getImage(Tank.class.getClassLoader().getResource("images/goodMissileLD.gif")),
				tk.getImage(Tank.class.getClassLoader().getResource("images/goodMissileD.gif")),
				tk.getImage(Tank.class.getClassLoader().getResource("images/goodMissileRD.gif")),
				tk.getImage(Tank.class.getClassLoader().getResource("images/goodMissileR.gif")),
				tk.getImage(Tank.class.getClassLoader().getResource("images/goodMissileRU.gif"))
		};
		imgs.put("U", images[0]);
		imgs.put("LU", images[1]);
		imgs.put("L", images[2]);
		imgs.put("LD", images[3]);
		imgs.put("D", images[4]);
		imgs.put("RD", images[5]);
		imgs.put("R", images[6]);
		imgs.put("RU", images[7]);
		
		gImgs.put("U", images[8]);
		gImgs.put("LU", images[9]);
		gImgs.put("L", images[10]);
		gImgs.put("LD", images[11]);
		gImgs.put("D", images[12]);
		gImgs.put("RD", images[13]);
		gImgs.put("R", images[14]);
		gImgs.put("RU", images[15]);
	}

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

		if (!good)// �з��ӵ�
			switch (dir) {// ���ݷ�����ʾ��ͬͼƬ
			case U:
				g.drawImage(imgs.get("U"), x,y, null);
				break;
			case LU:
				g.drawImage(imgs.get("LU"), x,y, null);
				break;
			case L:
				g.drawImage(imgs.get("L"), x,y, null);
				break;
			case LD:
				g.drawImage(imgs.get("LD"), x,y, null);
				break;
			case D:
				g.drawImage(imgs.get("D"), x,y, null);
				break;
			case RD:
				g.drawImage(imgs.get("RD"), x,y, null);
				break;
			case R:
				g.drawImage(imgs.get("R"), x,y, null);
				break;
			case RU:
				g.drawImage(imgs.get("RU"), x,y, null);
				break;
			}
		else// �ҷ��ӵ�
			switch (dir) {// ���ݷ�����ʾ��ͬͼƬ
			case U:
				g.drawImage(gImgs.get("U"), x,y, null);
				break;
			case LU:
				g.drawImage(gImgs.get("LU"), x,y, null);
				break;
			case L:
				g.drawImage(gImgs.get("L"), x,y, null);
				break;
			case LD:
				g.drawImage(gImgs.get("LD"), x,y, null);
				break;
			case D:
				g.drawImage(gImgs.get("D"), x,y, null);
				break;
			case RD:
				g.drawImage(gImgs.get("RD"), x,y, null);
				break;
			case R:
				g.drawImage(gImgs.get("R"), x,y, null);
				break;
			case RU:
				g.drawImage(gImgs.get("RU"), x,y, null);
				break;
			}

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
