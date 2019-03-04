import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.io.File;

import javax.imageio.ImageIO;

public class Explosion {
	int x, y;// 爆炸坐标
	private boolean live = true;// 爆炸的生命状态，默认为活着
	private static Toolkit tk = Toolkit.getDefaultToolkit();/// 拿到默认工具包，通过工具包的方法把硬盘上的图片加载进内存
	// 每次爆炸时没必要重新创建Toolkit对象并加载图片，所以设为静态
	private static Image[] images = { // 获取指定路径的图像
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
	int step = 0;// 爆炸阶段，对应不同图片
	private TankClient tc;// 持有引用
	private static boolean init = false;//判断是否初始化
	/*不进行初始化判断就直接画会存在一个问题：第一个坦克爆炸时不显示图片
	 * 原因：Toolkit.getImage()是延迟加载。也就是当执行draw方法时可能来不及画出图片
	 * 解决方法1：在第一次真正加载前手动初始化一个爆炸，代替那个不显示的爆炸
	 * */

	public Explosion(int x, int y, TankClient tc) {
		this.x = x;
		this.y = y;
		this.tc = tc;
	}

	public void draw(Graphics g) {// 画出爆炸
		if(!init) {//如果没有初始化，就提前画出一个爆炸，将其作为第一个爆炸（不显示）
			for (int i = 0; i < images.length; i++) {
				g.drawImage(images[i], -50, -50, null);//以防万一，画在窗口外
			}
			init = true;//表示已初始化，之后的爆炸可以跳过这个步骤
		}
		if (!live) {// 如果爆炸死亡就不画出来
			tc.explosions.remove(this);// 将死亡的爆炸移出容器
			return;
		}
		if (step == images.length) {// 如果爆炸结束
			live = false;// 爆炸死亡
			step = 0;// 阶段重置
			return;// 不画出来
		}
		g.drawImage(images[step], x, y, null);// 画出爆炸某阶段的图片，目前不需要图像观察者所以设为空
		step++;
	}
}
