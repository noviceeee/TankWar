
public class Time {// 显示游戏倒计时
	int time;// 剩余时间（毫秒）

	public Time(int time) {
		this.time = time;
	}

	int minute;// 剩余分钟
	int second;// 减去剩余分钟后的剩余秒

	public String timeToString() {//返回剩余时间的字符串表示
		minute = time / 1000 / 60;
		second = time / 1000 % 60;
		if (second > 9)//剩余的秒数为个位数时十位补零
			return minute + ": " + second;
		return minute + ": 0" + second;
	}
}
