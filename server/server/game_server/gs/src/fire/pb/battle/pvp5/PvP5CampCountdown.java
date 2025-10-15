package fire.pb.battle.pvp5;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import fire.pb.talk.MessageMgr;

/**
 * 5v5一方阵营无人应战的倒计时
 * @author XGM
 */
public class PvP5CampCountdown {

	static class SingletonHolder {
		static PvP5CampCountdown singleton = new PvP5CampCountdown();
	}

	public static PvP5CampCountdown getInstance() {
		return SingletonHolder.singleton;
	}

	/**
	 * 阵营信息
	 */
	public static class CampInfo {
		public int winCamp = 0; // 即将胜利阵营
		public long startTime = 0; // 起始时间
		public int elapseMinute = -1; // 逝去时间(单位:分钟)
	}
	/**
	 * 每个赛场的倒计时
	 * key = grade, value = CampInfo
	 */
	private Map<Integer, CampInfo> countdown = new HashMap<Integer, CampInfo>();

	public PvP5CampCountdown() {
	}

	public void init() {
		synchronized (countdown) {
			countdown.clear();
		}
	}

	/**
	 * 进入倒计时
	 */
	public void enter(int grade, int camp) {
		synchronized (countdown) {
			CampInfo v = countdown.get(grade);
			// 如果之前没有记录或者战况逆转
			if (v == null || v.winCamp != camp) {
				v = new CampInfo();
				v.winCamp = camp;
				v.startTime = System.currentTimeMillis();
				v.elapseMinute = -1;
				countdown.put(grade, v);
			}
		}
	}

	/**
	 * 心跳
	 */
	public int tick(int grade, Function<Integer, Void> call) {
		long now = System.currentTimeMillis();
		int remainMinute = -1;
		synchronized (countdown) {
			CampInfo v = countdown.get(grade);
			if (v != null) {
				// 计算过去了几分钟
				long elapseSecond = (now - v.startTime) / 1000;
				int elapseMinute = (int) (elapseSecond / 60);
				if (elapseMinute > v.elapseMinute) {
					// 每分钟回调一次
					v.elapseMinute = elapseMinute;
					remainMinute = Math.max(5 - elapseMinute, 0);
				}
			}
		}
		if (remainMinute != -1) {
			call.apply(remainMinute);
		}
		return remainMinute;
	}

	/**
	 * 离开倒计时
	 */
	public void leave(int grade) {
		synchronized (countdown) {
			countdown.remove(grade);
		}
	}

	/**
	 * 广播阵营胜利倒计时
	 */
	public static void broadcast(int grade, int winCamp, int remainMinute) {
		// 本届奥山战场$精英组$$雷毛$阵营已无人应战，$5$分钟内将判负
		int loseCamp = (winCamp == 0 ? 1 : 0);
		List<String> strings = new LinkedList<String>();
		strings.add(PvP5Helper.getGradeName(grade));
		strings.add(PvP5Helper.getCampName(loseCamp));
		strings.add(String.valueOf(remainMinute));
		MessageMgr.broadcastMsgNotify(172019, -1, strings);
	}
}
