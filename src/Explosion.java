import java.awt.Color;
import java.awt.Graphics;

public class Explosion {
	int x, y;// ��ը����
	private boolean live = true;// ��ը������״̬��Ĭ��Ϊ����

	int[] diameter = { 4, 7, 12, 18, 27, 36, 49, 30, 14, 6 };// �ò�ֱͬ����Բģ�ⱬը����
	int step = 0;// ��ը�׶Σ���Ӧ��ֱͬ��

	private TankClient tc;// ��������

	public Explosion(int x, int y, TankClient tc) {
		this.x = x;
		this.y = y;
		this.tc = tc;
	}

	public void draw(Graphics g) {// ������ը
		if (!live) {// �����ըû���žͲ�������
			tc.explosions.remove(this);// �������ı�ը�Ƴ�����
			return;
		}
		if (step == diameter.length) {// �����ը����
			live = false;// ��ը����
			step = 0;// �׶�����
			return;// ��������
		}
		Color c = g.getColor();
		g.setColor(Color.ORANGE);
		g.fillOval(x, y, diameter[step], diameter[step]);// ����ĳһ�׶εı�ը
		step++;// ��ը������һ�׶�
		g.setColor(c);

	}
}
