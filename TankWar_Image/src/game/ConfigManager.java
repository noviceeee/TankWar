import java.io.IOException;
import java.util.Properties;

public class ConfigManager {//���õ���ģʽд�����ļ������࣬�����ʱ�����ڴ��д���Ψһһ�������ļ��������������
	static Properties p = new Properties();
	static {
		try {
			p.load(ConfigManager.class.getClassLoader().getResourceAsStream("config/game.properties"));//���ļ����ؽ��ڴ�
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private ConfigManager() {//�����췽����Ϊ˽�У��ⲿ���ܴ����������
	}

	static int getProperties(String key) {//ͨ������ȡ�ļ��ж�Ӧ��ֵ
		return Integer.parseInt(p.getProperty(key));//�ַ���ת����ֵ
	}
}
