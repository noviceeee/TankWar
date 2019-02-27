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

public class TankClient extends Frame {//̹�˿ͻ��ˣ�Ϊ�������׼��

	public static final int GAME_WIDTH = 800;// ���ڿ���
	public static final int GAME_HEIGHT = 600;// ���ڸ߶�

	Tank myTank = new Tank(100,100, true, this);//�����ҷ�̹�˶��󣬳�ʼ���꣨100��100�������뵱ǰTankClient����
	Tank enemyTank = new Tank(200,200, false, this);//�����з�̹�˶��󣬳�ʼ���꣨200��200�������뵱ǰTankClient����

	List<Missile> missiles = new ArrayList<Missile>();//����һ��װ�ӵ�����������������ΪMissile
	List<Explosion> explosions = new ArrayList<Explosion>(); //װ��ը������
	
	Image offScreenImage = null;// ����һ������ͼ������ʵ��˫���壬�����˸���⣬��:�����ж����������ͼ���ϣ���һ������ʾ����

	public void paint(Graphics g) {// ��дpaint�������൱�ڽ���һ���������û��� g��ͼ
		Color c = g.getColor();//ȡ����ǰ��ɫ
		g.setColor(Color.WHITE);//������Ϊ��ɫ
		g.drawString("missiles count: "+missiles.size(), 10, 50);//�ں�������д����ǰ�ӵ�����
		g.drawString("explosions count: "+explosions.size(), 10, 70);//��ǰ��ը����
		g.setColor(c);//��ԭ��ɫ
		
		for(int i = 0; i < missiles.size(); i++) {//ѭ��������ȡ�������е��ӵ���������
			Missile m = missiles.get(i);
			m.hitTank(enemyTank);//�ж��Ƿ����̹��
				m.draw(g);
		}	
		
		for(int i = 0; i < explosions.size(); i++) {//������ը���н׶β�����
			Explosion e = explosions.get(i);
			e.draw(g);
			
		}
		
		myTank.draw(g);//���ö������draw���������뻭��g����̹��
		enemyTank.draw(g);//�з�̹��
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
		this.addWindowListener(new WindowAdapter() {// ���Ӵ��ڼ����¼�
			public void windowClosing(WindowEvent e) {// ��д�˷�������������������ڵĹرհ�ťʱ�������¼���
				System.exit(0);// �����������򣨷��������ʾ�������˳���
			}
		});
		this.setResizable(false);// ���ڴ�С���ɸı�
		this.setBackground(Color.BLACK);// ����Ϊ��ɫ
		
		this.addKeyListener(new KeyMonitor());//���Ӽ��̼����¼�
		
		this.setVisible(true);// ���ô��ڿɼ�

		new Thread(new PaintThread()).start();// �����̣߳���̹���ƶ�
	}

	public static void main(String[] args) {
		TankClient tc = new TankClient();
		tc.launchFrame();

	}

	//�߳�ʹ�ػ�Ƶ�ʸ����ȶ���Ҳ��������ػ����ٶȡ������ػ����ܽ���ӵ��Զ����е�����
	private class PaintThread implements Runnable {// �����̣߳������ػ��ı�λ�õ�̹�ˣ�ʵ���ƶ�
		public void run() {// �˷�������Ҫִ�е��߳�����
			while (true) {// ����ѭ��
				repaint();// �ػ������������������������������˷����ᾡ����ô������ paint ����������˷����ᾡ����ô������ update ������
				try {
					Thread.sleep(100);// ÿ��100�������һ��repaint����
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

	// ʵ��KeyListener�ӿ���ʵ���������з�������KeyAdapter���Ѿ�ʵ����KeyListener�ķ�������Ϊ�գ����̳�KeyAdapter��ֻ����д�Լ���Ҫ�ķ���
	private class KeyMonitor extends KeyAdapter {//���ü��̼����¼�

		public void keyReleased(KeyEvent e) {
			myTank.keyReleased(e);//�ͷŰ���ʱ�����¼�
		}

		public void keyPressed(KeyEvent e) {//�����°���ʱ�����¼�
			myTank.keyPressed(e);//���ö��󷽷���ͨ����������̹���ƶ�����
		}
		
	}
}