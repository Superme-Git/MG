
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

/**
 * 领取签到奖励
 */
public class PSuppRegReward extends RoleProcedure {
	private RegAssistant regAssistant;
	private int sRegDay;
	private static Logger logger = Logger.getLogger("AWARD");

	public PSuppRegReward(long roleId, int suppregday, RegAssistant assistant) {
		super(roleId);
		this.sRegDay = suppregday;
		this.regAssistant = assistant;
	}

	public PSuppRegReward(long roleId, int suppsignday) {
		this(roleId, suppsignday, (RegAssistant) SingleContainer
			.getInstance(RegAssistant.class));
	}

	@Override
	protected boolean process() throws Exception {
		Calendar calendar = Calendar.getInstance();
		int month = regAssistant.getMonthOfYear(calendar);
		int day = sRegDay;

		xbean.RegRec record = regAssistant.getRecord(roleId);

		Pack bag = new Pack(roleId, false);

		PropRole prole = new PropRole(roleId, true);
		
		int vipLevel = regAssistant.getVipLv(roleId);

		// 没有该月的记录
		if (!record.getMonthmap().containsKey(month)) {
			logger.error("PSuppRegReward error:没有该月的记录  roleid:" + roleId + " month:" + month);
			return false;
		}
		
		int suppSignNum = record.getMonthmap().get(month).getSuppregnum();
		if(suppSignNum < 1){
			logger.error("PSuppRegReward error:补签次数异常  roleid:" + roleId + " suppSignNum:" + suppSignNum);
			return false;
		}

		Map<Integer, RegDay> days = record.getMonthmap().get(month).getDaymap();
		
		if (!days.containsKey(day)) {
			logger.error("PSuppRegReward error:没有该日的记录  roleid:" + roleId + " day:" + day);
			return false;
		}

		// 今天已经领取过奖励
		if (days.get(day).getRewardflag() > 0) {
			logger.error("PSuppRegReward error:今天已经领取过奖励  roleid:" + roleId + " day:" + day);
			return false;
		}

		// 奖励id
		int rewardId = getRewardId(days, month);
		if (rewardId < 1) {
			logger.error("PSuppRegReward error:奖励id错误  roleid:" + roleId + " rewardId:" + rewardId);
			return false;
		}
		
		//签到次数
		int signNums = regAssistant.getRegTimesOfMonth(days);

		Sregreward ssignreward = configManager.getConfigById(
				Sregreward.class, rewardId);
		// 奖励未配置
		if (ssignreward == null) {
			logger.error("PSuppRegReward error:奖励未配置  roleid:" + roleId + " rewardId:" + rewardId);
			return false;
		}

		// vip增益
		int vipPercent = vipPercent(ssignreward, vipLevel);
		// 设置领奖标记
		days.get(day).setRewardflag(1);
		
		record.getMonthmap().get(month).setSuppregnum(suppSignNum - 1);
		// 设置上次操作时间
		record.setLastregtime(calendar.getTimeInMillis());

		logger.info("PSuppRegReward 补签奖励成功领取  roleid:" + roleId + " day:" + day + " rewardId:" + rewardId);
		
		return sendReward(prole.getUserid(), ssignreward, bag, vipPercent, signNums);
	}

	/**
	 * 取得可以领取的奖励的位置，签到次数
	 * 
	 * @author cn 奖励id为0101，一月份的第一个 1201,12月份的第一个
	 */
	private int getRewardId(Map<Integer, RegDay> days, int month) {
		int total = regAssistant.getRegTimesOfMonth(days);
		if (total == 0) {
			return 0;
		}
		return total + month * 100;
	}

	/**
	 * 发奖励
	 * 
	 * @author cn
	 */
	protected boolean sendReward(int userId, Sregreward ssignreward,
			Pack bag, int vipPercent, int signNums) {
		
		if (ssignreward.getItemid()>0&&ssignreward.getItemnum()>0) {
			// 添加物品
			if (ssignreward.getItemnum() * vipPercent != bag.doAddItem(
					ssignreward.getItemid(), ssignreward.getItemnum()
							* vipPercent, 0, ssignreward.getIsbind(), "签到奖励",
					fire.log.enums.YYLoggerTuJingEnum.tujing_Value_qiandao,
					ssignreward.getItemid())) {
				MessageMgr.sendMsgNotify(roleId, 144965 ,null);
				return false;
			}
			MessageUtil.psendAddItemWhileCommit(roleId, ssignreward.getItemid(), ssignreward.getItemnum());
		}
		int mType= ssignreward.getMtype();
		switch(mType){
			case fire.pb.game.MoneyType.MoneyType_SilverCoin:{
				// 添加银两
				if (ssignreward.getMoney() * vipPercent != bag.addSysMoney(
						ssignreward.getMoney() * vipPercent, "签到奖励",
						fire.log.enums.YYLoggerTuJingEnum.tujing_Value_jianglizhongxin, 0)) {
					return false;
				}
				MessageUtil.pSendAddOrRemoveCurrency(roleId, ssignreward.getMoney(), fire.pb.game.MoneyType.MoneyType_SilverCoin);
				break;
			}
			case fire.pb.game.MoneyType.MoneyType_GoldCoin:{
				// 添加金币
				if (ssignreward.getMoney() * vipPercent != bag.addSysGold(
						ssignreward.getMoney() * vipPercent, "签到奖励",
						fire.log.enums.YYLoggerTuJingEnum.tujing_Value_jianglizhongxin, 0)) {
					return false;
				}
				MessageUtil.pSendAddOrRemoveCurrency(roleId, ssignreward.getMoney(), fire.pb.game.MoneyType.MoneyType_GoldCoin);
				break;
			}
			case fire.pb.game.MoneyType.MoneyType_HearthStone:{
				// 添加符石
				Procedure.pexecuteWhileCommit(new PAddFuShi(userId, roleId,
						ssignreward.getMoney() * vipPercent,
						FushiConst.SYS_FUSHI, YYLoggerTuJingEnum.tujing_Value_qiandao));
				MessageUtil.pSendAddOrRemoveCurrency(roleId, ssignreward.getMoney(), fire.pb.game.MoneyType.MoneyType_HearthStone);
				break;
			}
		}

		
		//运营日志
		ItemBaseBean itembbean = new ItemBaseBean(ssignreward.getItemid(),ssignreward.getItemnum());
		List<ItemBaseBean> items=new ArrayList<ItemBaseBean>();
		items.add(itembbean);
		RegBean sibean=new RegBean(fire.log.beans.RegBean.REG_ONE,signNums,items);
		fire.log.YYLogger.signingLog(roleId,sibean);
		
		//刷新
		Procedure.pexecuteWhileCommit(new PQueryRegRec(roleId));
		return true;
	}

	private int vipPercent(Sregreward ssignreward, int vipLevel) {
		if (vipLevel >= ssignreward.getRatio()) {
			return 2;
		}
		return 1;

	}

}
