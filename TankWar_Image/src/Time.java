
public class Time {// ��ʾ��Ϸ����ʱ
	int time;// ʣ��ʱ�䣨���룩

	public Time(int time) {
		this.time = time;
	}

	int minute;// ʣ�����
	int second;// ��ȥʣ����Ӻ��ʣ����

	public String timeToString() {//����ʣ��ʱ����ַ�����ʾ
		minute = time / 1000 / 60;
		second = time / 1000 % 60;
		if (second > 9)//ʣ�������Ϊ��λ��ʱʮλ����
			return minute + ": " + second;
		return minute + ": 0" + second;
	}
}
