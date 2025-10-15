
package fire.pb.item.onlinegift;

import java.util.Map;

import fire.log.Logger;
import fire.pb.item.SGetTimeAward;
import fire.pb.item.STimingReward;
import fire.pb.main.ConfigManager;
import mkdb.Procedure;


public class TimingRewardAssistant {

	static Map<Integer, STimingReward> timingRewardConf = ConfigManager.getInstance().getConf(fire.pb.item.STimingReward.class);
	
	static Logger logger = Logger.getLogger("AWARD");

	//获得下一次定时奖励需要等待多少时间,单位是毫秒
	public static long getTimeWait(int rewardId) {

		STimingReward sTimingRewardConf = timingRewardConf.get(rewardId);
		if (sTimingRewardConf != null)
			return sTimingRewardConf.getTimewait() * 1000;
		else
			return -1;
	}
	
	public static void sendTimingRewardMsg(final long roleId) {
		Procedure.pexecuteWhileCommit(new mkdb.Procedure(){
			@Override
			protected boolean process() throws Exception {
				xbean.RewardData rewardData = xtable.Timingreward.get(roleId);
				if (rewardData==null){
					rewardData = xbean.Pod.newRewardData();
					xtable.Timingreward.insert(roleId, rewardData);
				}
				int rewardId = rewardData.getRewardid();
				
				//下次奖励等待时间
				long timeWait = getTimeWait(rewardId + 1);
				if(timeWait <= 0){
					return false;
				}

				//第一次登录
				if(rewardData.getLastrewardtime() <=0){
					rewardData.setTimewait(timeWait);
					rewardData.setLastrewardtime(System.currentTimeMillis());
				}
				else if(System.currentTimeMillis() - rewardData.getLastrewardtime() > timeWait){
					rewardData.setTimewait(0);
				}
				else{
					rewardData.setTimewait(timeWait - System.currentTimeMillis() + rewardData.getLastrewardtime());
				}

				int newRewardId = rewardId + 1;
				long newTimeWait = rewardData.getTimewait();
				psendWhileCommit(roleId, new SGetTimeAward(newRewardId, newTimeWait));
				
				logger.info("TimingRewardAssistant sendTimingRewardMsg newRewardId:" + newRewardId + " newTimeWait:" + newTimeWait);
				return true;
			}
		});
	}
	
	public static void offline(final long roleId){
		new mkdb.Procedure(){
			@Override
			protected boolean process() throws Exception {
				xbean.RewardData rewardData = xtable.Timingreward.get(roleId);
				if (rewardData==null){
					return false;
				}
				
				int rewardId = rewardData.getRewardid();
				
				//下次奖励等待时间
				long newTimeWait = getTimeWait(rewardId + 1);
				if(newTimeWait <= 0){
					return false;
				}
				
				long remainTime = rewardData.getTimewait();
				long lastRewardTime = rewardData.getLastrewardtime();
				long currentTime = System.currentTimeMillis();
				
				long timeDiff = currentTime - lastRewardTime;
				rewardData.setTimewait(Math.max(0, remainTime - timeDiff));
				rewardData.setLastrewardtime(currentTime);
				
				return true;
			}
		}.call();
	}

	
}
