import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.io.File;

import javax.imageio.ImageIO;

public class Explosion {
	int x, y;// ��ը����
	private boolean live = true;// ��ը������״̬��Ĭ��Ϊ����
	private static Toolkit tk = Toolkit.getDefaultToolkit();/// �õ�Ĭ�Ϲ��߰���ͨ�����߰��ķ�����Ӳ���ϵ�ͼƬ���ؽ��ڴ�
	// ÿ�α�ըʱû��Ҫ���´���Toolkit���󲢼���ͼƬ��������Ϊ��̬
	private static Image[] images = { // ��ȡָ��·����ͼ��
			tk.getImage(Explosion.class.getClassLoader().getResource("images/0.gif")),
			tk.getImage(Explosion.class.getClassLoader().getResource("images/1.gif")),
			tk.getImage(Explosion.class.getClassLoader().getResource("images/2.gif")),
			tk.getImage(Explosion.class.getClassLoader().getResource("images/3.gif")),
			tk.getImage(Explosion.class.getClassLoader().getResource("images/4.gif")),
			tk.getImage(Explosion.class.getClassLoader().getResource("images/5.gif")),
			tk.getImage(Explosion.class.getClassLoader().getResource("images/6.gif")),
			tk.getImage(Explosion.class.getClassLoader().getResource("images/7.gif")),
			tk.getImage(Explosion.class.getClassLoader().getResource("images/8.gif")),
			tk.getImage(Explosion.class.getClassLoader().getResource("images/9.gif")),
			tk.getImage(Explosion.class.getClassLoader().getResource("images/10.gif")) };
	int step = 0;// ��ը�׶Σ���Ӧ��ͬͼƬ
	private TankClient tc;// ��������
	private static boolean init = false;//�ж��Ƿ��ʼ��
	/*�����г�ʼ���жϾ�ֱ�ӻ������һ�����⣺��һ��̹�˱�ըʱ����ʾͼƬ
	 * ԭ��Toolkit.getImage()���ӳټ��ء�Ҳ���ǵ�ִ��draw����ʱ��������������ͼƬ
	 * �������1���ڵ�һ����������ǰ�ֶ���ʼ��һ����ը�������Ǹ�����ʾ�ı�ը
	 * */

	public Explosion(int x, int y, TankClient tc) {
		this.x = x;
		this.y = y;
		this.tc = tc;
	}

	public void draw(Graphics g) {// ������ը
		if(!init) {//���û�г�ʼ��������ǰ����һ����ը��������Ϊ��һ����ը������ʾ��
			for (int i = 0; i < images.length; i++) {
				g.drawImage(images[i], -50, -50, null);//�Է���һ�����ڴ�����
			}
			init = true;//��ʾ�ѳ�ʼ����֮��ı�ը���������������
		}
		if (!live) {// �����ը�����Ͳ�������
			tc.explosions.remove(this);// �������ı�ը�Ƴ�����
			return;
		}
		if (step == images.length) {// �����ը����
			live = false;// ��ը����
			step = 0;// �׶�����
			return;// ��������
		}
		g.drawImage(images[step], x, y, null);// ������ըĳ�׶ε�ͼƬ��Ŀǰ����Ҫͼ��۲���������Ϊ��
		step++;
	}
}
