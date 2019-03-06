import java.awt.Color;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

public class TankClient extends Frame {// ̹�˿ͻ��ˣ�Ϊ�������׼��
	public static final int GAME_WIDTH = 800;// ���ڿ��
	public static final int GAME_HEIGHT = 600;// ���ڸ߶�

	public int score = 0;// ��ʼ�÷�
	private int time = 2 * 60 * 1000;// ��Ϸʣ��ʱ�䣨���룩
	private boolean isPlay = true;// ������Ϸ��ͣ/��ʼ

	Tank myTank = new Tank(50, 50, true, Direction.STOP, this);
	// �����ҷ�̹�˶���ָ����ʼ���꣬��ֹ״̬�����뵱ǰTankClient����

	Wall w1 = new Wall(50, 120, this);// ��������ǽ
	Wall w2 = new Wall(300, 200, this);

	List<Tank> tanks = new ArrayList<Tank>();// װ̹�˵�����
	List<Missile> missiles = new ArrayList<Missile>();// ����һ��װ�ӵ�����������������ΪMissile
	List<Explosion> explosions = new ArrayList<Explosion>(); // װ��ը������
	List<Blood> bloods = new ArrayList<Blood>();

	Image offScreenImage = null;// ����һ������ͼ������ʵ��˫���壬�����˸���⣬��:�����ж����������ͼ���ϣ���һ������ʾ����

	public void paint(Graphics g) {// ��дpaint�������൱�ڽ���һ���������û��� g��ͼ
		Color c = g.getColor();// ȡ����ǰ��ɫ
		g.setColor(Color.WHITE);// ������Ϊ��ɫ
		g.drawString("YOUR SCORE: " + score, 10, 50);// �ں�������д����ǰ�÷�
		g.drawString("YOUR TIME: " + new Time(time).timeToString(), 10, 70);// �ں�������д����ʣʱ��
		g.setColor(c);// ��ԭ��ɫ

		for (int i = 0; i < missiles.size(); i++) {// ѭ��������ȡ�������е��ӵ���������
			Missile m = missiles.get(i);
			m.hitTanks(tanks);// �ж��ӵ��Ƿ���������ڵ�ĳ��̹��
			m.hitTank(myTank);// �ж��ӵ��Ƿ�����Լ���̹��
			m.hitWall(w1);
			m.hitWall(w2);
			m.draw(g);
		}

		for (int i = 0; i < explosions.size(); i++) {// �������б�ը������
			Explosion e = explosions.get(i);
			e.draw(g);
		}
		for (int i = 0; i < bloods.size(); i++) {// ��������Ѫ�鲢����
			Blood b = bloods.get(i);
			b.draw(g);
		}

		if (tanks.size() == 0)
			for (int i = 0; i < ConfigManager.getProperties("enemy"); i++) {// ÿ���з�̹������Ϊ0ʱ������һ������
				tanks.add(new Tank(50 + 60 * (i + 1), 500, false, Direction.D, this));
			}
		for (int i = 0; i < tanks.size(); i++) {// �������ез�̹�˲�����
			Tank t = tanks.get(i);
			t.collidesWithWall(w1);// ̹��ײǽ����
			t.collidesWithWall(w2);// ̹���ص�����
			t.collidesWithTanks(tanks);
			t.draw(g);
		}

		myTank.draw(g);// ���ö������draw���������뻭��g����̹��
		myTank.collidesWithTanks(tanks);// ̹���ص�����
		myTank.eat(bloods);
		w1.draw(g);// ��ǽ
		w2.draw(g);
	}

	public void update(Graphics g) {// ����������������������»���ʱ�䳤�����ײ�����˸����������һ���ǲ�����д update ����������˫����ͼƬ�������˸������
		if (offScreenImage == null) {
			offScreenImage = this.createImage(GAME_WIDTH, GAME_HEIGHT);// ������С�봰����ͬ�Ļ���ͼ��
		}
		Graphics gOffScreen = offScreenImage.getGraphics();// ��������ͼ���ͼ�������ģ��൱�ڻ�ȡ�� ����ͼ��Ļ���
		Color c = gOffScreen.getColor();// ��ȡ��ǰ��ɫ
		gOffScreen.setColor(Color.BLACK);// ��������Ϊ��ɫ
		gOffScreen.fillRect(0, 0, GAME_WIDTH, GAME_HEIGHT);// �����ڴ��������꣨0��0�����봰�ڴ�С��ͬ�ľ��Σ���ÿ�ζ��ػ汳��������̹�˵��˶��켣�ᱻ����
		gOffScreen.setColor(c);// �ָ���ǰ��ɫ
		paint(gOffScreen);// ����paint�������ڻ���ͼ���ϻ���̹��
		g.drawImage(offScreenImage, 0, 0, null);// �ڴ��ڻ��ƻ���ͼ�����꣨0��0����û��ʹ��ͼ��۲��ߣ�������Ϊnull
	}

	public void launchFrame() {// ����һ�����������ڳ�ʼ������
		this.setLocation(600, 200);// ����λ��
		this.setSize(GAME_WIDTH, GAME_HEIGHT);// ���ڴ�С
		this.setTitle("TankWar");// ���ڱ���
		this.addWindowListener(new WindowAdapter() {// ��Ӵ��ڼ����¼�
			public void windowClosing(WindowEvent e) {// ��д�˷�����������������ڵĹرհ�ťʱ�������¼���
				System.exit(0);// �����������򣨷��������ʾ�������˳���
			}
		});
		this.setResizable(false);// ���ڴ�С���ɸı�
		this.setBackground(Color.BLACK);// ����Ϊ��ɫ

		this.addKeyListener(new KeyMonitor());// ��Ӽ��̼����¼�

		this.setVisible(true);// ���ô��ڿɼ�

		new Thread(new PaintThread()).start();// �����̣߳���̹���ƶ�
	}

	public static void main(String[] args) {
		TankClient tc = new TankClient();
		tc.launchFrame();

	}

	// �߳�ʹ�ػ�Ƶ�ʸ����ȶ���Ҳ��������ػ����ٶȡ������ػ����ܽ���ӵ��Զ����е�����
	private class PaintThread implements Runnable {// �����̣߳������ػ��ı�λ�õ�̹�ˣ�ʵ���ƶ�
		public void run() {// �˷�������Ҫִ�е��߳�����
			while (true) {// ����ѭ��
				if (isPlay) {// ��Ϸ״̬
					if (time == 0)// ���ʱ�䵽�ˣ���Ϸ����
						return;
					repaint();// �ػ������������������������������˷����ᾡ����ô������ paint ����������˷����ᾡ����ô������ update ������
					try {
						Thread.sleep(100);// ÿ��100�������һ��repaint����
						time -= 100;
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				} else// ��ͣ״̬
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						e.printStackTrace();
					} // ÿ��100�������һ��repaint����
			}
		}
	}

	// ʵ��KeyListener�ӿ���ʵ���������з�������KeyAdapter���Ѿ�ʵ����KeyListener�ķ�������Ϊ�գ����̳�KeyAdapter��ֻ����д�Լ���Ҫ�ķ���
	private class KeyMonitor extends KeyAdapter {// ���ü��̼����¼�

		public void keyReleased(KeyEvent e) {
			myTank.keyReleased(e);// �ͷŰ���ʱ�����¼�
			int key = e.getKeyCode();
			if (key == KeyEvent.VK_SPACE) {// ������¿ո������Ϸ��ͣ/��ʼ
				if (isPlay)
					isPlay = false;
				else
					isPlay = true;
			}
		}

		public void keyPressed(KeyEvent e) {// �����°���ʱ�����¼�
			myTank.keyPressed(e);// ���ö��󷽷���ͨ����������̹���ƶ�����
		}

	}
}
