import java.awt.Color;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class TankClient extends Frame {
	public static final int GAME_WIDTH = 800;// ���ڿ��
	public static final int GAME_HEIGHT = 600;// ���ڸ߶�

	public int score = 0;// ��ʼ�÷�
	private int time = 2* 60*1000;// ��Ϸʣ��ʱ�䣨���룩
	private boolean play = true;// ������Ϸ��ͣ/��ʼ
	private static boolean restart = false;// ������Ϸ���¿�ʼ

	Tank myTank = new Tank(true, Direction.STOP, this);
	// �����ҷ�̹�˶���ָ����ʼ���꣬��ֹ״̬�����뵱ǰTankClient����

	Wall w1 = new Wall(50, 120, this);// ��������ǽ
	Wall w2 = new Wall(300, 200, this);

	List<Tank> tanks = new ArrayList<Tank>();// װ̹�˵�����
	List<Missile> missiles = new ArrayList<Missile>();// ����һ��װ�ӵ�����������������ΪMissile
	List<Explosion> explosions = new ArrayList<Explosion>(); // װ��ը������
	List<Blood> bloods = new ArrayList<Blood>();

	Image offScreenImage = null;// ����һ������ͼ������ʵ��˫���壬�����˸���⣬��:�����ж����������ͼ���ϣ���һ������ʾ����

	private void init() {// �������¿�ʼʱ��ʼ������
		score = 0;
		time = 2 * 60 * 1000;
		play = true;
		restart = false;
		myTank = new Tank(true, Direction.STOP, this);
		tanks.clear();
		missiles.clear();
		explosions.clear();
		bloods.clear();
	}

	public void paint(Graphics g) {// ��дpaint�������൱�ڽ���һ���������û��� g��ͼ
		if (time > 0)
			play(g);// ��Ϸ���н���
		else
			gameOver(g);// ��Ϸ��������
	}

	private void play(Graphics g) {
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
				tanks.add(new Tank(false, Direction.D, this));
			}
		for (int i = 0; i < tanks.size(); i++) {// �������ез�̹�˲�����
			Tank t = tanks.get(i);
			t.collidesWithTanks(tanks);// ̹����ײ����
			t.draw(g);
		}

		myTank.draw(g);// ���ö������draw���������뻭��g����̹��
		myTank.collidesWithTanks(tanks);// ̹���ص�����
		myTank.eat(bloods);
		w1.draw(g);// ��ǽ
		w2.draw(g);
	}

	private void gameOver(Graphics g) {// ��Ϸ��������
		Color c = g.getColor();
		g.setColor(Color.WHITE);
		g.setFont(new Font("����", Font.BOLD, 120));
		g.drawString("GAME OVER", 90, 280);
		g.setFont(new Font("����", Font.BOLD, 50));
		g.drawString("your score: " + score, 200, 450);
		g.setFont(new Font("����", Font.CENTER_BASELINE, 20));
		g.drawString("press R to play again", 270, 500);
		g.setColor(c);
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
		do {
			TankClient tc = new TankClient();
			tc.launchFrame();
			if (!restart)
				break;

		} while (true);

	}

	// �߳�ʹ�ػ�Ƶ�ʸ����ȶ���Ҳ��������ػ����ٶȡ������ػ����ܽ���ӵ��Զ����е�����
	private class PaintThread implements Runnable {// �����̣߳������ػ��ı�λ�õ�̹�ˣ�ʵ���ƶ�
		public void run() {// �˷�������Ҫִ�е��߳�����
			while (true) {// ����ѭ��
				if (play) {// ��Ϸ״̬

					if (time < 0 && restart)// ���ʱ�䵽�ˣ���ѡ�����¿�ʼ���������ݳ�ʼ��
						init();
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
					}

			}
		}
	}

	// ʵ��KeyListener�ӿ���ʵ���������з�������KeyAdapter���Ѿ�ʵ����KeyListener�ķ�������Ϊ�գ����̳�KeyAdapter��ֻ����д�Լ���Ҫ�ķ���
	private class KeyMonitor extends KeyAdapter {// ���ü��̼����¼�

		public void keyReleased(KeyEvent e) {
			myTank.keyReleased(e);// �ͷŰ���ʱ�����¼�
			int key = e.getKeyCode();
			if (key == KeyEvent.VK_SPACE) {// ��Ϸʱ���ڣ�������¿ո������Ϸ��ͣ/��ʼ
				if (play && time > 0)
					play = false;
				else
					play = true;
			}
			if (key == KeyEvent.VK_R) // �����Ϸ��������R�����¿�ʼ
				restart = true;
		}

		public void keyPressed(KeyEvent e) {// �����°���ʱ�����¼�
			myTank.keyPressed(e);// ���ö��󷽷���ͨ����������̹���ƶ�����
		}
	}
}
