import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class Tank {// ̹����

	public static final int WIDTH = 35;// ̹��ͼƬ���
	public static final int HEIGHT = 35;// ̹��ͼƬ�߶�
	public static final int SPEED = ConfigManager.getProperties("speed");// ̹��ÿ����������귽���ƶ��ľ��룬�ɵ����ٶ�
	public static final int LIFE = ConfigManager.getProperties("myLife");// �ҷ�̹����Ѫ��
	public static final int SKILL = ConfigManager.getProperties("mySkill");// �ҷ�̹���ܼ���ֵ
	
	private static TankClient tc;// ���ڷ��ʴ����Ա����
	private static Toolkit tk = Toolkit.getDefaultToolkit();/// �õ�Ĭ�Ϲ��߰���ͨ�����߰��ķ�����Ӳ���ϵ�ͼƬ���ؽ��ڴ�
	private static Map<String, Image> imgs = new HashMap<String, Image>();//��з�̹��ͼƬ�����������ü�ֵ�ԣ�������ݴ�������ַ�����ȡ��ӦͼƬ
	private static Map<String, Image> gImgs = new HashMap<String, Image>();//���ҷ�̹��ͼƬ
	private static Image[] images= null;
	static {//��̬����飬������ļ��ض�ִ��
		images = new Image[]{
				tk.getImage(Tank.class.getClassLoader().getResource("images/tankU.gif")),
				tk.getImage(Tank.class.getClassLoader().getResource("images/tankLU.gif")),
				tk.getImage(Tank.class.getClassLoader().getResource("images/tankL.gif")),
				tk.getImage(Tank.class.getClassLoader().getResource("images/tankLD.gif")),
				tk.getImage(Tank.class.getClassLoader().getResource("images/tankD.gif")),
				tk.getImage(Tank.class.getClassLoader().getResource("images/tankRD.gif")),
				tk.getImage(Tank.class.getClassLoader().getResource("images/tankR.gif")),
				tk.getImage(Tank.class.getClassLoader().getResource("images/tankRU.gif")),
				
				tk.getImage(Tank.class.getClassLoader().getResource("images/goodTankU.gif")),
				tk.getImage(Tank.class.getClassLoader().getResource("images/goodTankLU.gif")),
				tk.getImage(Tank.class.getClassLoader().getResource("images/goodTankL.gif")),
				tk.getImage(Tank.class.getClassLoader().getResource("images/goodTankLD.gif")),
				tk.getImage(Tank.class.getClassLoader().getResource("images/goodTankD.gif")),
				tk.getImage(Tank.class.getClassLoader().getResource("images/goodTankRD.gif")),
				tk.getImage(Tank.class.getClassLoader().getResource("images/goodTankR.gif")),
				tk.getImage(Tank.class.getClassLoader().getResource("images/goodTankRU.gif"))
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


	private boolean live = true;// ̹������״̬��Ĭ�ϻ���
	private int life = LIFE;// ���ҷ���̹������ֵ
	private BloodBar bb = new BloodBar();// ����Ѫ��

	private int skill = SKILL;// �ҷ�̹�˼���ֵ
	private SkillBar sb = new SkillBar();// ����������

	private boolean good = true;// ���ֵз��ҷ�̹��

	private int x, y;// ̹�˳�ʼ����
	int oldX, oldY;// ��¼̹�˾ɵ����꣬���ڵз�ײǽʱ����ԭ������
	private boolean bU = false, bD = false, bL = false, bR = false;// ���δ����ϡ��¡������ĸ��������״̬��������ĳһ�����ʱ��Ӧ����ֵΪtrue

	private static Random r = new Random();// �������ָ���з�̹�˷��򣬶��̹�˿��Թ���һ���������������������static����

	private Direction dir = Direction.STOP;// ̹�˷���Ĭ��Ϊ��ֹ״̬
	private Direction ptDir = Direction.D;// ��Ͳ����Ĭ������

	private int step = r.nextInt(12) + 1;// ����̹�˳�ĳһ�����ƶ��Ĳ���������ʹ�з�����˶����Ӳ�����

	public Tank(int x, int y, boolean good) {// ���췽�������ڴ���ָ�������̹�˶���
		this.x = x;
		this.y = y;
		this.good = good;
		this.oldX = x;
		this.oldY = y;
	}

	public Tank(int x, int y, boolean good, Direction dir, TankClient tc) {// ���췽�����أ�����TankClient����ͨ�������������������ʳ�Ա����
		this(x, y, good);// ������һ�����췽��

		this.tc = tc;// ��ʼ��tc
		this.dir = dir;// ָ��̹�˶���ķ���
	}

	public void draw(Graphics g) {// ��̹��
		if (!live) {// ���̹�������Ͳ���
			if (!good)
				tc.tanks.remove(this);// ����ǵз�̹�ˣ��ͽ�����������Ƴ�
			return;
		}
		if(!good)//�з�ͼƬ
		switch (ptDir) {// ������Ͳ������ʾ��̹ͬ��ͼƬ
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
		else
			switch (ptDir) {// ������Ͳ������ʾ��̹ͬ��ͼƬ
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

		move();// ÿ�λ�̹�˶����ô˷�����ͨ���ı�̹������ʵ���ƶ�Ч��
		if (this.good) {// �����ҷ�̹��Ѫ���ͼ�����
			bb.draw(g);
			sb.draw(g);
		}
	}

	void move() {// ���ݵ�ǰ����ı�̹�����꣬�����ƶ�
		this.oldX = x;
		this.oldY = y;// ÿ���ƶ�ǰ����¼�ɵ�����ֵ
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

		if (this.dir != Direction.STOP) {// ���̹�˴��ڷǾ�ֹ״̬
			this.ptDir = this.dir;// ����Ͳ�������Ϊ��̹���˶�����һ��
		}

		if (x < 0)// ���̹�˳������⣬�˶������ڱ�Եʱ�޷�ǰ��
			x = 0;
		if (y < 40)// ���ڵı������߶�
			y = 40;
		if (x > TankClient.GAME_WIDTH - WIDTH)
			x = TankClient.GAME_WIDTH - WIDTH;
		if (y > TankClient.GAME_HEIGHT - HEIGHT)
			y = TankClient.GAME_HEIGHT - HEIGHT;

		if (!good) {// ����ǵз�̹��
			if (step == 0) {// ����Ϊ��ʱ���ָ���µķ���
				Direction[] dirs = Direction.values();// ����ֱ�Ӹ��������ҵ�ö���е�ĳһ��ֵ��Ҫ�Ƚ�ö��ת��������
				int num = r.nextInt(dirs.length);// �������һ����[0,length)�����ϵ���������0����length����Ϊ��������
				dir = dirs[num];
				step = r.nextInt(12) + 1;// ��������µĲ�����������һ��Ҫִ�С�step--������step�������0
			} // ����stepΪ����ʱ���������ı�
			step--;

			if (r.nextInt(10) > 7)// ������ڣ��������ÿ��Կ��ƿ���Ƶ��
				this.fire();
		}
	}

	public void keyPressed(KeyEvent e) {// �����°���ʱ
		int key = e.getKeyCode();// ��ȡ������
		switch (key) {// �������Ϊ�ĸ������֮һ������Ӧ�Ĳ���ֵ��Ϊtrue
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
		locateDirection();// ȷ��̹���ƶ�����
	}

	void locateDirection() {// �����ĸ��������״̬ȷ��̹�˷���
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

	public void keyReleased(KeyEvent e) {// �ͷŰ���ʱ
		int key = e.getKeyCode();// ��ȡ����
		switch (key) {// ���������״̬����Ϊfalse
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
		case KeyEvent.VK_F:// �ͷ�F��ʱ��̹�˿��ڣ������Ϊ������ʱ���ڣ��ᵼ�°�סF����ʱ������ӵ������ܼ���
			fire();
			break;
		case KeyEvent.VK_S:// �ͷ�S��ʱ���䳬���ڵ�
			superFire();
			break;
		case KeyEvent.VK_F2:// �ҷ�����ʱ��F2�����Ѫ������������
			if (this.live == false) {
				this.live = true;
				this.life = LIFE;
				this.skill = SKILL;
			}
			break;
		}
		locateDirection();
	}

	public Missile fire() {// ̹�˷����ӵ�
		if (!live)// ���˾Ͳ��ܷ���
			return null;
		int x = this.x + WIDTH / 2 - Missile.WIDTH / 2;// �ӵ����ϽǺ�����
		int y = this.y + HEIGHT / 2 - Missile.HEIGHT / 2;// �ӵ����Ͻ�������
		Missile m = new Missile(x, y, ptDir, good, this.tc);// ����һ������Ͳ��ͬ����λ��̹������ļ����ӵ����󣬲�����TankClient����
		tc.missiles.add(m);// ���ӵ���������
		return m;// ���ظ��ӵ�����
	}

	public Missile fire(Direction dir) {// ����fire���������ڷ��䳬���ڵ�
		if (!live)// ���˾Ͳ��ܷ���
			return null;
		int x = this.x + WIDTH / 2 - Missile.WIDTH / 2;// �ӵ����ϽǺ�����
		int y = this.y + HEIGHT / 2 - Missile.HEIGHT / 2;// �ӵ����Ͻ�������
		Missile m = new Missile(x, y, dir, good, this.tc);// ����һ��ĳ����λ��̹������ļ����ӵ����󣬲�����TankClient����
		tc.missiles.add(m);// ���ӵ���������
		return m;// ���ظ��ӵ�����
	}

	public Rectangle getRect() {// ��ȡ��̹�����ꡢ�����ͬ�ķ��飬���ڼ򵥼���ӵ���̹���Ƿ���ײ
		return new Rectangle(x, y, WIDTH, HEIGHT);
	}

	public boolean isLive() {// �ж�̹���Ƿ���ţ�����live������˽�еģ��ⲿ�޷�ֱ��ʹ�ã����������ṩһ�����ʷ���
		if (!live && !this.isGood() && r.nextInt(5) > 1) {// ����з�̹���������и��ʵ���Ѫ��
			int x = this.x + WIDTH / 2 - Blood.WIDTH / 2;// Ѫ�����ϽǺ�����
			int y = this.y + HEIGHT / 2 - Blood.HEIGHT / 2;// Ѫ�����Ͻ�������
			Blood b = new Blood(x, y, tc);
			tc.bloods.add(b);
		}
		return live;
	}

	public void setLive(boolean live) {// ����̹���Ƿ����
		this.live = live;
	}

	public boolean isGood() {// �ж�̹�˺û�
		return good;
	}

	private void stay() {// ������һ������
		x = oldX;
		y = oldY;
	}

	public boolean collidesWithWall(Wall w) {// ��ײǽʱ̹�˷�����һ�����꣨����ѡ���򣬱���ͣ��ǽǰ������
		if (this.live && this.getRect().intersects(w.getRect())) {// �򵥼��̹�˻���ʱ��ǽ�Ƿ���ײ
			this.stay();
			return true;
		}
		return false;
	}

	public boolean collidesWithTanks(List<Tank> tanks) {// ���̹��֮����ص�����
		for (int i = 0; i < tanks.size(); i++) {// ���������е�̹��
			Tank t = tanks.get(i);
			if (this != t && this.live && t.live && this.getRect().intersects(t.getRect())) {// �����������ͬ��̹�˶������ҷ�����ײ
				this.stay();// ��������һ�����꣬���⻥���ص�
				t.stay();
				return true;
			}

		}
		return false;
	}

	private void superFire() {// �м���ֵʱ���䳬���ڵ���ͬʱ���˸������䣩
		if (skill > 0) {
			Direction[] dirs = Direction.values();// ö�����Ͳ���ֱ�Ӹ�������ȡֵ��Ӧ�Ȼ�ȡ��Ӧ����
			for (int i = 0; i < dirs.length - 1; i++) {// ���STOP���ÿ��������
				fire(dirs[i]);
			}
			skill--;// ����ֵ����
		}
	}

	public int getLife() {// ���ⲿ����˽�б���life
		return life;
	}

	public void setLife(int life) {
		this.life = life;
	}

	private class BloodBar {
		public void draw(Graphics g) {
			int w = WIDTH * life / LIFE;// ��ǰѪ�����
			Color c = g.getColor();
			g.setColor(Color.RED);
			g.drawRect(x, y - 18, WIDTH, 3);// Ѫ���ܿ��
			g.setColor(Color.RED);
			g.fillRect(x, y - 18, w, 3);// ��ǰѪ��
			g.setColor(c);

		}
	}

	public boolean eat(List<Blood> bloods) {// ̹�˳�Ѫ���Ѫ�ؼ���
		for (int i = 0; i < bloods.size(); i++) {
			Blood b = bloods.get(i);

			if (b.isLive() && this.live && this.getRect().intersects(b.getRect())) {// ��Ѫ����ҷ�̹�˶���������ײʱ
				b.setLive(false);
				if (this.life < LIFE)// Ѫ������ʱ��Ѫ����Ѫʱ��Ч
					this.life += 1;
				if (this.skill < SKILL)// ����������ʱ�ظ�����
					this.skill += 1;
				return true;
			}
		}
		return false;
	}

	private class SkillBar {
		public void draw(Graphics g) {
			int w = WIDTH * skill / SKILL;// ��ǰ���������
			Color c = g.getColor();
			g.setColor(Color.GREEN);
			g.drawRect(x, y - 13, WIDTH, 3);// �������ܿ��
			g.setColor(Color.GREEN);
			g.fillRect(x, y - 13, w, 3);// ��ǰ����ֵ
			g.setColor(c);

		}
	}
}
