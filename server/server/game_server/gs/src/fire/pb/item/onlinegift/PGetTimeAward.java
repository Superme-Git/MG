
package fire.pb.item.onlinegift;

import fire.log.Logger;
import fire.pb.item.SGetTimeAward;
import fire.pb.item.STimingReward;
import fire.pb.util.BagUtil;
import fire.pb.util.MessageUtil;
import mkdb.Procedure;


public class PGetTimeAward extends Procedure {

	private final long roleId;

	private final int rewardId;
	
	private static Logger logger = Logger.getLogger("AWARD");

	public PGetTimeAward(long roleId, int rewardId) {

		super();
		this.roleId = roleId;
		this.rewardId = rewardId;
	}

	@Override
	protected boolean process() throws Exception {
		
		xbean.RewardData rewardData = xtable.Timingreward.get(roleId);
		if (rewardData == null) {
			rewardData = xbean.Pod.newRewardData();
			xtable.Timingreward.insert(roleId, rewardData);
		}
		
		int newRewardId = rewardData.getRewardid() + 1;
		if (rewardId != newRewardId){
			logger.error("POpenOnlineGift error roleid:" + roleId + "rewardId:" + rewardId + " newRewardId:" + newRewardId);
			return false;
		}
		
		long timeWait = TimingRewardAssistant.getTimeWait(rewardId);
		if(timeWait <= 0){
			logger.error("POpenOnlineGift error roleid:" + roleId + "rewardId:" + rewardId + " timeWait:" + timeWait);
			return false;
		}
		
		long lastTimeWait = rewardData.getTimewait();
		long currentTime = System.currentTimeMillis();
		
		//时间不到
		if (currentTime - rewardData.getLastrewardtime()< lastTimeWait){
			logger.error("POpenOnlineGift error roleid:" + roleId + "rewardId:" + rewardId + " currentTime:" + currentTime + " lastTimeWait:" + lastTimeWait);
			return false;
		}
		
		//领奖
		STimingReward sTimingRewardConf = TimingRewardAssistant.timingRewardConf.get(rewardId);
		if (sTimingRewardConf != null) {
			int addItemNum1 = 0;
			int addItemNum2 = 0;
			int addItemNum3 = 0;
			if((sTimingRewardConf.getItemid_1() > 0) && (sTimingRewardConf.getItemnum_1() > 0)){
				addItemNum1 = addTimingRewardItem( sTimingRewardConf.getItemid_1(), sTimingRewardConf.getItemnum_1(),sTimingRewardConf.getItembind_1(), "timing reward");
			}
			if((sTimingRewardConf.getItemid_2() > 0) && (sTimingRewardConf.getItemnum_2() > 0)){
				addItemNum2 = addTimingRewardItem( sTimingRewardConf.getItemid_2(), sTimingRewardConf.getItemnum_2(),sTimingRewardConf.getItembind_2(), "timing reward");
			}
			if((sTimingRewardConf.getItemid_3() > 0) && (sTimingRewardConf.getItemnum_3() > 0)){
				addItemNum3 = addTimingRewardItem( sTimingRewardConf.getItemid_3(), sTimingRewardConf.getItemnum_3(),sTimingRewardConf.getItembind_3(), "timing reward");
			}
			if(addItemNum1 != sTimingRewardConf.getItemnum_1() || addItemNum2 !=sTimingRewardConf.getItemnum_2() ||  addItemNum3 !=sTimingRewardConf.getItemnum_3() ){
				fire.pb.talk.MessageMgr.psendMsgNotifyWhileRollback(roleId,140655, null);
				logger.error("POpenOnlineGift error roleid:" + roleId + "number1:" + addItemNum1 + " number2:" + addItemNum2 + " number3:" + addItemNum3);
				return false;
			}
		}
		
		rewardData.setRewardid(rewardId);
		rewardData.setLastrewardtime(currentTime);
		long newTimeWait = TimingRewardAssistant.getTimeWait(rewardId+1);
		rewardData.setTimewait(newTimeWait);
		if (rewardId > TimingRewardAssistant.timingRewardConf.size()) {
			return false;
		}
		int sendRewardId = rewardId + 1;
		if(sendRewardId > TimingRewardAssistant.timingRewardConf.size()){
			sendRewardId = -1;
		}
		SGetTimeAward msg = new SGetTimeAward(sendRewardId, rewardData.getTimewait());
		psendWhileCommit(roleId, msg);
		
		logger.info("POpenOnlineGift roleid:" + roleId + " 领取定时奖励成功！");
		return true;
	}
	
	private int addTimingRewardItem( int itemId, int itemNum, int isBind, String addItemStr ) {
		int itemNumber = 0;
		if (isBind==1){
			itemNumber =  BagUtil.addBindItem(roleId, itemId, itemNum, "timing reward",fire.log.enums.YYLoggerTuJingEnum.tujing_Value_jianglizhongxin, 1,false);
		} else{
			itemNumber = BagUtil.addItem(roleId, itemId, itemNum, "timing reward", fire.log.enums.YYLoggerTuJingEnum.tujing_Value_jianglizhongxin, 1, false);
		}

		MessageUtil.psendAddItemWhileCommit(roleId, itemId, itemNum);
		return itemNumber;
	}

}
