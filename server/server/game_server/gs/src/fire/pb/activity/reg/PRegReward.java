
package fire.pb.activity.reg;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import fire.log.Logger;
import fire.log.beans.ItemBaseBean;
import fire.log.beans.RegBean;
import fire.log.enums.YYLoggerTuJingEnum;
import fire.pb.PropRole;
import fire.pb.fushi.PAddFuShi;
import fire.pb.fushi.FushiConst;
import fire.pb.game.Sregreward;
import fire.pb.item.Pack;
import fire.pb.talk.MessageMgr;
import fire.pb.util.MessageUtil;
import fire.pb.util.singlefactory.SingleContainer;
import mkdb.Procedure;
import xbean.RegDay;


//领取签到奖励
public class PRegReward extends RoleProcedure {
	private RegAssistant regAssistant;
	private static Logger logger = Logger.getLogger("AWARD");

	public PRegReward(long roleId, RegAssistant assistant) {
		super(roleId);
		this.regAssistant = assistant;
	}

	public PRegReward(long roleId) {
		this(roleId, (RegAssistant) SingleContainer.getInstance(RegAssistant.class));
	}

	@Override
	protected boolean process() throws Exception {
		Calendar calen = Calendar.getInstance();
		int monthOfYear = regAssistant.getMonthOfYear(calen);
		int dayOfMonth = regAssistant.getDayOfMonth(calen);
		
		if(roleId < 0){
			logger.error("PRegReward error:roleid:" + roleId );
			return false;
		}

		xbean.RegRec regRec = regAssistant.getRecord(roleId);

		Pack itemBag = new Pack(roleId, false);

		PropRole propRole = new PropRole(roleId, true);
		
		int vipLv = regAssistant.getVipLv(roleId);

		// 没有该月的记录
		if (!regRec.getMonthmap().containsKey(monthOfYear)) {
			logger.error("PRegReward error:没有该月的记录  roleid:" + roleId + " month:" + monthOfYear);
			return false;
		}

		Map<Integer, RegDay> dayMap = regRec.getMonthmap().get(monthOfYear).getDaymap();
		
		if (!dayMap.containsKey(dayOfMonth)) {
			logger.error("PRegReward error:没有该日的记录  roleid:" + roleId + " day:" + dayOfMonth);
			return false;
		}
		
		//签到次数
		int regTimesOfMonth = regAssistant.getRegTimesOfMonth(dayMap);

		//当天已经领取过奖励
		if (dayMap.get(dayOfMonth).getRewardflag() > 0) {
			logger.error("PRegReward error:今天已经领取过奖励  roleid:" + roleId + " day:" + dayOfMonth);
			return false;
		}

		//奖励id
		int rewardIdx = getRewardIdx(dayMap, monthOfYear);
		if (rewardIdx < 1) {
			logger.error("PRegReward error:奖励id错误  roleid:" + roleId + " rewardId:" + rewardIdx);
			return false;
		}

		Sregreward sRegRewardConf = configManager.getConfigById(Sregreward.class, rewardIdx);
		// 奖励未配置
		if (sRegRewardConf == null) {
			logger.error("PRegReward error:奖励未配置  roleid:" + roleId + " rewardId:" + rewardIdx);
			return false;
		}

		// vip经验比率
		int vipRatio = vipRatio(sRegRewardConf, vipLv);
		
		// 设置领奖标记
		dayMap.get(dayOfMonth).setRewardflag(1);
		
		// 设置上次注册时间
		regRec.setLastregtime(calen.getTimeInMillis());
		
		logger.info("PRegReward 签到奖励成功领取  roleid:" + roleId + " day:" + dayOfMonth + " rewardId:" + rewardIdx);
		
		return sendReward(propRole.getUserid(), sRegRewardConf, itemBag, vipRatio, regTimesOfMonth);
	}


	//获得可以领取的奖励的表中的位置
	private int getRewardIdx(Map<Integer, RegDay> dayMap, int month) {
		int allRegTimesOfMonth = regAssistant.getRegTimesOfMonth(dayMap);
		if (allRegTimesOfMonth == 0) {
			return 0;
		}
		
		return allRegTimesOfMonth + month * 100;
	}


	//发奖励
	protected boolean sendReward(int userId, Sregreward sRegRewardData, Pack itemBag,
			int vipRatio, int regTimes) {
		
		if (sRegRewardData.getItemid()>0&&sRegRewardData.getItemnum()>0) {
			// 添加物品
			if (sRegRewardData.getItemnum() * vipRatio != itemBag.doAddItem(
					sRegRewardData.getItemid(), sRegRewardData.getItemnum()
							* vipRatio, 0, sRegRewardData.getIsbind(), "签到奖励",
					fire.log.enums.YYLoggerTuJingEnum.tujing_Value_jianglizhongxin,
					sRegRewardData.getItemid())) {
				MessageMgr.sendMsgNotify(roleId, 144965 ,null);
				return false;
			}
			MessageUtil.psendAddItemWhileCommit(roleId, sRegRewardData.getItemid(), sRegRewardData.getItemnum());

		}
		int mType= sRegRewardData.getMtype();
		switch(mType){
			case fire.pb.game.MoneyType.MoneyType_SilverCoin:{
				// 添加银两
				if (sRegRewardData.getMoney() * vipRatio != itemBag.addSysMoney(
						sRegRewardData.getMoney() * vipRatio, "签到奖励",
						fire.log.enums.YYLoggerTuJingEnum.tujing_Value_jianglizhongxin, 0)) {
					return false;
				}
				MessageUtil.pSendAddOrRemoveCurrency(roleId, sRegRewardData.getMoney(), fire.pb.game.MoneyType.MoneyType_SilverCoin);
				break;
			}
			case fire.pb.game.MoneyType.MoneyType_GoldCoin:{
				// 添加金币
				if (sRegRewardData.getMoney() * vipRatio != itemBag.addSysGold(
						sRegRewardData.getMoney() * vipRatio, "签到奖励",
						fire.log.enums.YYLoggerTuJingEnum.tujing_Value_jianglizhongxin, 0)) {
					return false;
				}
				MessageUtil.pSendAddOrRemoveCurrency(roleId, sRegRewardData.getMoney(), fire.pb.game.MoneyType.MoneyType_GoldCoin);
				break;
			}
			case fire.pb.game.MoneyType.MoneyType_HearthStone:{
				// 添加符石
				Procedure.pexecuteWhileCommit(new PAddFuShi(userId, roleId,
						sRegRewardData.getMoney() * vipRatio,
						FushiConst.SYS_FUSHI, YYLoggerTuJingEnum.tujing_Value_xitongzengsong));
				MessageUtil.pSendAddOrRemoveCurrency(roleId, sRegRewardData.getMoney(), fire.pb.game.MoneyType.MoneyType_HearthStone);
				break;
			}
		}

		
		//运营日志
		ItemBaseBean itemBaseBeanData = new ItemBaseBean(sRegRewardData.getItemid(),sRegRewardData.getItemnum());
		List<ItemBaseBean> items=new ArrayList<ItemBaseBean>();
		items.add(itemBaseBeanData);
		RegBean regBeanData=new RegBean(fire.log.beans.RegBean.REG_ONE,regTimes,items);
		fire.log.YYLogger.signingLog(roleId,regBeanData);
		//刷新
		Procedure.pexecuteWhileCommit(new PQueryRegRec(roleId));
		return true;
	}

	private int vipRatio(Sregreward sRegRewardData, int vipLv) {
		if (vipLv >= sRegRewardData.getRatio()) {
			return 2;
		}
		
		return 1;
	}
	
}
