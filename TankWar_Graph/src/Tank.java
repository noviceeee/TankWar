import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.util.List;
import java.util.Random;

public class Tank {// ̹����

	public static final int WIDTH = 40;// ̹�˿���
	public static final int HEIGHT = 40;// ̹�˸߶�
	public static final int SPEED = 5;// ̹��ÿ����������귽���ƶ��ľ��룬�ɵ����ٶ�
	public static final int LIFE = 5;// �ҷ�̹����Ѫ��
	public static final int SKILL = 10;// �ҷ�̹���ܼ���ֵ

	TankClient tc;// ���ڷ��ʴ����Ա����

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
		Color c = g.getColor();// ȡ����ǰ��ɫ
		if (good)
			g.setColor(Color.RED);// �ҷ�̹��Ϊ��ɫ
		else
			g.setColor(Color.BLUE);// �з�̹��Ϊ��ɫ
		g.fillOval(x, y, WIDTH, HEIGHT);// ��һ��Բ��Ϊ̹�ˣ����������꣬���Ⱥ͸߶ȣ����Ͻ�Ϊԭ�㣬 x�����ң�y�����£�
		g.setColor(Color.WHITE);// ���û���Ϊ��ɫ
		switch (ptDir) {// ������Ͳ���򻭳���Ͳ�����ü����ˣ����ԶԽ��߷�����Ͳ�Գ���
		case U:
			g.drawLine(x + WIDTH / 2, y, x + WIDTH / 2, y + HEIGHT / 2);
			break;
		case LU:
			g.drawLine(x, y, x + WIDTH / 2, y + HEIGHT / 2);
			break;
		case L:
			g.drawLine(x, y + HEIGHT / 2, x + WIDTH / 2, y + HEIGHT / 2);
			break;
		case LD:
			g.drawLine(x, y + HEIGHT, x + WIDTH / 2, y + HEIGHT / 2);
			break;
		case D:
			g.drawLine(x + WIDTH / 2, y + HEIGHT, x + WIDTH / 2, y + HEIGHT / 2);
			break;
		case RD:
			g.drawLine(x + WIDTH, y + HEIGHT, x + WIDTH / 2, y + HEIGHT / 2);
			break;
		case R:
			g.drawLine(x + WIDTH, y + HEIGHT / 2, x + WIDTH / 2, y + HEIGHT / 2);
			break;
		case RU:
			g.drawLine(x + WIDTH, y, x + WIDTH / 2, y + HEIGHT / 2);
			break;
		}
		g.setColor(c);// �ָ���ǰ��ɫ

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
			resurrect();
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

	public Rectangle getRect() {// ��ȡ��̹�����ꡢ������ͬ�ķ��飬���ڼ򵥼���ӵ���̹���Ƿ���ײ
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

	public boolean eat(List<Blood> bloods) {// ̹�˳�Ѫ���Ѫ�ؼ���
		for (int i = 0; i < bloods.size(); i++) {
			Blood b = bloods.get(i);

			if (b.isLive() && this.live && this.getRect().intersects(b.getRect())) {// ��Ѫ����ҷ�̹�˶���������ײʱ
				b.setLive(false);
				if (this.life < LIFE)// Ѫ������ʱ��Ѫ����Ѫʱ��Ч
					this.life += 1;
				if (this.skill < SKILL)// ����������ʱ�ظ�����
					this.skill += 2;
				return true;
			}
		}
		return false;
	}

	private void resurrect() {//��Ѫ�������ص����
		if (this.live == false) {
			this.live = true;
			this.life = LIFE;
			if(tc.score> 0)//����һ�ο�1�֣�����Ϊֹ
			tc.score -= 1;
			randomLocate();
			while(collidesWithTanks(tc.tanks)) {//���������������̹�ˣ��ͻ����ص㣬��������̹�˿�ס
			randomLocate();
			}
		}
	}
	
	private void randomLocate() {//�ڴ��ڷ�Χ�������������λ��
		x = r.nextInt(tc.GAME_WIDTH+1);//��������[0, tc.GAME_WIDTH+1)�����ϵ�������������tc.GAME_WIDTH+1
		y = 40+r.nextInt(tc.GAME_HEIGHT-40+1);//������ͬ������ȥ���ڵı������߶�40
	}

	private class SkillBar {
		public void draw(Graphics g) {
			int w = WIDTH * skill / SKILL;// ��ǰ����������
			Color c = g.getColor();
			g.setColor(Color.GREEN);
			g.drawRect(x, y - 10, WIDTH, 3);// �������ܿ���
			g.setColor(Color.GREEN);
			g.fillRect(x, y - 10, w, 3);// ��ǰ����ֵ
			g.setColor(c);

		}
	}

	private class BloodBar {
		public void draw(Graphics g) {
			int w = WIDTH * life / LIFE;// ��ǰѪ������
			Color c = g.getColor();
			g.setColor(Color.RED);
			g.drawRect(x, y - 15, WIDTH, 3);// Ѫ���ܿ���
			g.setColor(Color.RED);
			g.fillRect(x, y - 15, w, 3);// ��ǰѪ��
			g.setColor(c);

		}
	}
}