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

public class TankClient extends Frame {// 坦克客户端，为网络版做准备
	public static final int GAME_WIDTH = 800;// 窗口宽度
	public static final int GAME_HEIGHT = 600;// 窗口高度

	public int score = 0;// 初始得分
	public int time = 2 * 60 * 1000;// 游戏剩余时间（毫秒）
	private boolean play = true;// 控制游戏暂停/开始
	private static boolean restart = false;// 控制游戏重新开始

	Tank myTank = new Tank(true, Direction.STOP, this);
	// 创建我方坦克对象，指定初始坐标，静止状态，传入当前TankClient对象
	Wall w1 = new Wall(70, 120, 230, 40, this);// 生成两堵墙
	Wall w2 = new Wall(500, 200, 40, 150, this);

	List<Tank> tanks = new ArrayList<Tank>();// 装坦克的容器
	List<Missile> missiles = new ArrayList<Missile>();// 创建一个装子弹的容器，泛型类型为Missile
	List<Explosion> explosions = new ArrayList<Explosion>(); // 装爆炸的容器
	List<Blood> bloods = new ArrayList<Blood>();

	Image offScreenImage = null;// 创建一个虚拟图像，用于实现双缓冲，解决闪烁问题，即:将所有东西画在这个图像上，再一次性显示出来

	private void init() {// 用于重新开始时初始化数据
		score = 0;
		time = 2 * 60 * 1000;
		play = true;
		restart = false;
		myTank = new Tank( true, Direction.STOP, this);
		tanks.clear();
		missiles.clear();
		explosions.clear();
		bloods.clear();
	}

	public void paint(Graphics g) {// 重写paint方法，相当于建立一个画布，用画笔 g画图
		if (time > 0)
			play(g);// 游戏运行界面
		else
			gameOver(g);// 游戏结束界面
	}

	private void play(Graphics g) {
		Color c = g.getColor();// 取出当前颜色
		g.setColor(Color.WHITE);// 画笔设为白色
		g.drawString("YOUR SCORE: " + score, 10, 50);// 在合适坐标写出当前得分
		g.drawString("YOUR TIME: " + new Time(time).timeToString(), 10, 70);// 在合适坐标写出所剩时间
		g.setColor(c);// 还原颜色

		for (int i = 0; i < missiles.size(); i++) {// 循环操作，取出容器中的子弹并画出来
			Missile m = missiles.get(i);
			m.hitTanks(tanks);// 判断子弹是否击中容器内的某辆坦克
			m.hitTank(myTank);// 判断子弹是否击中自己的坦克
			m.hitWall(w1);
			m.hitWall(w2);
			m.draw(g);
		}

		for (int i = 0; i < explosions.size(); i++) {// 遍历所有爆炸并画出
			Explosion e = explosions.get(i);
			e.draw(g);
		}
		for (int i = 0; i < bloods.size(); i++) {// 遍历所有血块并画出
			Blood b = bloods.get(i);
			b.draw(g);
		}

		if (tanks.size() == 0)
			for (int i = 0; i < 10; i++) {// 每当敌方坦克数量为0时就生成十辆
				tanks.add(new Tank( false, Direction.D, this));
			}
		for (int i = 0; i < tanks.size(); i++) {// 遍历所有敌方坦克并画出
			Tank t = tanks.get(i);
			t.collidesWithTanks(tanks);
			t.draw(g);
		}

		myTank.draw(g);// 调用对象里的draw方法，传入画笔g画出坦克
		myTank.collidesWithTanks(tanks);// 坦克重叠处理
		myTank.eat(bloods);
		w1.draw(g);// 画墙
		w2.draw(g);
	}

	private void gameOver(Graphics g) {// 游戏结束界面
		Color c = g.getColor();
		g.setColor(Color.WHITE);
		g.setFont(new Font("宋体", Font.BOLD, 120));
		g.drawString("GAME OVER", 90, 280);
		g.setFont(new Font("宋体", Font.BOLD, 50));
		g.drawString("your score: " + score, 200, 450);
		g.setFont(new Font("宋体", Font.CENTER_BASELINE, 20));
		g.drawString("press R to play again", 270, 500);
		g.setColor(c);
	}

	public void update(Graphics g) {// 对于重量级组件，由于重新绘制时间长，容易产生闪烁的现象，所以一般是采用重写 update 方法，利用双缓冲图片来解决闪烁的问题
		if (offScreenImage == null) {
			offScreenImage = this.createImage(GAME_WIDTH, GAME_HEIGHT);// 创建大小与窗口相同的缓冲图像
		}
		Graphics gOffScreen = offScreenImage.getGraphics();// 创建缓冲图像的图形上下文，相当于获取画 缓冲图像的画笔
		Color c = gOffScreen.getColor();// 获取当前颜色
		gOffScreen.setColor(Color.BLACK);// 将画笔设为黑色
		gOffScreen.fillRect(0, 0, GAME_WIDTH, GAME_HEIGHT);// 画出在窗口中坐标（0，0）且与窗口大小相同的矩形，即每次都重绘背景，否则坦克的运动轨迹会被保留
		gOffScreen.setColor(c);// 恢复当前颜色
		paint(gOffScreen);// 调用paint方法，在缓冲图像上绘制坦克
		g.drawImage(offScreenImage, 0, 0, null);// 在窗口绘制缓冲图像，坐标（0，0），没有使用图像观察者，所以设为null
	}

	public void launchFrame() {// 定义一个方法，用于初始化窗口
		this.setLocation(600, 200);// 窗口位置
		this.setSize(GAME_WIDTH, GAME_HEIGHT);// 窗口大小
		this.setTitle("TankWar");// 窗口标题
		this.addWindowListener(new WindowAdapter() {// 添加窗口监听事件
			public void windowClosing(WindowEvent e) {// 重写此方法，处理当鼠标点击窗口的关闭按钮时触发的事件。
				System.exit(0);// 正常结束程序（非零参数表示非正常退出）
			}
		});
		this.setResizable(false);// 窗口大小不可改变
		this.setBackground(Color.BLACK);// 背景为黑色

		this.addKeyListener(new KeyMonitor());// 添加键盘监听事件

		this.setVisible(true);// 设置窗口可见

		new Thread(new PaintThread()).start();// 启动线程，让坦克移动
	}

	public static void main(String[] args) {
		TankClient tc = new TankClient();
		tc.launchFrame();

	}

	// 线程使重画频率更加稳定，也方便控制重画的速度。按键重画不能解决子弹自动飞行的问题
	private class PaintThread implements Runnable {// 创建线程，不断重画改变位置的坦克，实现移动
		public void run() {// 此方法包含要执行的线程内容
			while (true) {// 无限循环
				if (play) {// 游戏状态
					if (time < 0 && restart)// 如果时间到了，若选择重新开始则将所有数据初始化
						init();
					repaint();// 重绘此组件，如果此组件是轻量级组件，则此方法会尽快调用此组件的 paint 方法。否则此方法会尽快调用此组件的 update 方法。
					try {
						Thread.sleep(100);// 每隔100毫秒调用一次repaint方法
						time -= 100;
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				} else// 暂停状态
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}

			}
		}
	}

	// 实现KeyListener接口需实现里面所有方法，而KeyAdapter类已经实现了KeyListener的方法（均为空），继承KeyAdapter后只需重写自己需要的方法
	private class KeyMonitor extends KeyAdapter {// 设置键盘监听事件

		public void keyReleased(KeyEvent e) {
			myTank.keyReleased(e);// 释放按键时触发事件
			int key = e.getKeyCode();
			if (key == KeyEvent.VK_SPACE && time >0) {// 游戏时间内如果按下空格键，游戏暂停/开始
				if (play)
					play = false;
				else
					play = true;
			}
			if (key == KeyEvent.VK_R && time < 0) // 如果游戏结束，按R键重新开始
				restart = true;
		}

		public void keyPressed(KeyEvent e) {// 当按下按键时触发事件
			myTank.keyPressed(e);// 调用对象方法，通过按键控制坦克移动方向
		}

	}
}
