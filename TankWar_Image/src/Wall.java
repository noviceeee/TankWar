import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.net.URL;

public class Wall {
	private static URL wallURL = Wall.class.getClassLoader().getResource("images/wall.gif");
	
 //ImageIcon ii = new ImageIcon(wallURL);//方式一
// Image wall = ii.getImage();
	 
	 /*
	static Image wall;//方式二
	static {
		
		try {
			 wall = ImageIO.read(wallURL);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}*/

	private static Toolkit tk = Toolkit.getDefaultToolkit();//方式三，延迟加载，可能导致图片不能及时加载出来
	private static Image wall = tk.getImage(wallURL);

	int x, y;// 墙的坐标
	TankClient tc;

	public Wall(int x, int y, TankClient tc) {// 生成墙时指定坐标
		this.x = x;
		this.y = y;
		this.tc = tc;
	}

	public void draw(Graphics g) {
		g.drawImage(wall, x, y, null);// 画墙
	}

	public Rectangle getRect() {// 获取与墙位置大小相同的方块用于碰撞检测
		return new Rectangle(x, y, wall.getWidth(null), wall.getHeight(null));
	}
}
