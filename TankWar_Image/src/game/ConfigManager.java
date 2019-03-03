import java.io.IOException;
import java.util.Properties;

public class ConfigManager {//采用单例模式写配置文件管理类，类加载时就在内存中创建唯一一个配置文件对象供其他类调用
	static Properties p = new Properties();
	static {
		try {
			p.load(ConfigManager.class.getClassLoader().getResourceAsStream("config/game.properties"));//将文件加载进内存
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private ConfigManager() {//将构造方法设为私有，外部不能创建本类对象
	}

	static int getProperties(String key) {//通过键获取文件中对应的值
		return Integer.parseInt(p.getProperty(key));//字符串转成数值
	}
}
