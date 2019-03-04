import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.net.URL;

public class Wall {
	private static URL wallURL = Wall.class.getClassLoader().getResource("images/wall.gif");
	
 //ImageIcon ii = new ImageIcon(wallURL);//��ʽһ
// Image wall = ii.getImage();
	 
	 /*
	static Image wall;//��ʽ��
	static {
		
		try {
			 wall = ImageIO.read(wallURL);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}*/

	private static Toolkit tk = Toolkit.getDefaultToolkit();//��ʽ�����ӳټ��أ����ܵ���ͼƬ���ܼ�ʱ���س���
	private static Image wall = tk.getImage(wallURL);

	int x, y;// ǽ������
	TankClient tc;

	public Wall(int x, int y, TankClient tc) {// ����ǽʱָ������
		this.x = x;
		this.y = y;
		this.tc = tc;
	}

	public void draw(Graphics g) {
		g.drawImage(wall, x, y, null);// ��ǽ
	}

	public Rectangle getRect() {// ��ȡ��ǽλ�ô�С��ͬ�ķ���������ײ���
		return new Rectangle(x, y, wall.getWidth(null), wall.getHeight(null));
	}
}
