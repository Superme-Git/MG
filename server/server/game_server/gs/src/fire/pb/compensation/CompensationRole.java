package fire.pb.compensation;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import fire.log.enums.YYLoggerTuJingEnum;
import fire.pb.PAddExpProc;
import fire.pb.fushi.FushiConst;
import fire.pb.fushi.PAddVipExp;
import fire.pb.item.Pack;
import fire.pb.talk.MessageMgr;

/**
 * 发放个人补偿
 */
public class CompensationRole
{
	final private long roleId;
	final private boolean readonly;
	private xbean.CompensationRole xcompens;

	public static CompensationRole getSingleCompensation(long roleId, boolean readonly)
	{
		if(roleId <= 0)
			return null;
		
		return new CompensationRole(roleId, readonly);
	}
	
	private CompensationRole(long roleId, boolean readonly)
	{
		this.roleId = roleId;
		if(readonly)
			xcompens = xtable.Compensationroles.select(roleId);
		else
			xcompens = xtable.Compensationroles.get(roleId);
		
		if(xcompens == null)
		{
			if (readonly)
				xcompens = xbean.Pod.newCompensationRoleData();
			else
			    xcompens = xbean.Pod.newCompensationRole();
			if(!readonly)
				xtable.Compensationroles.insert(roleId, xcompens);
		}
		this.readonly = readonly;
	}
	
	public long getRoleId()
	{
		return roleId;
	}
	
	public void giveCompensition(long compenskey)
	{
		xcompens.getSinglecompensations().put(compenskey, (long)0);
	}

	/**
	 * 设置读取标记
	 * @param compenskey
	 */
	public boolean setReadflag(long compenskey)
	{
		if (xcompens.getSinglecompensations().containsKey(compenskey)) {
			xcompens.getSinglecompensations().replace(compenskey, (long)1);
			return true;
		}
		return false;
	}

	/**
	 * 是否有这个奖励
	 * @param compenskey
	 * @return
	 */
	public boolean isHaveKey(long compenskey)
	{
		return xcompens.getSinglecompensations().containsKey(compenskey);
	}

	/**
	 * 获取奖励
	 * @param compenskey
	 * @return
	 */
	public boolean receiveAward(long compenskey)
	{
		if (readonly) {
			return false;
		}

		if (xcompens.getSinglecompensations().isEmpty()) {
			return false;
		}

		for(long key : xcompens.getSinglecompensations().keySet()) {

			if (key == compenskey) {

				xbean.SingleCompensation xsingle = xtable.Singlecompensations.select(key);
				if(xsingle == null)
					continue;

				Pack bag = new Pack(roleId, false);
				for (xbean.SingleCompensationAward award : xsingle.getAwards()) {
					// 根据类型发奖励
					switch (award.getType()) {
					case Helper.AWARDTYPE_ITEM: // 道具
						{
							int itemId = (int) award.getId();
							int itemNum = (int) award.getNum();
							if (itemId <= 0 || itemNum <= 0) {
								continue;
							}
							boolean isBind = (award.getFlag() & Helper.AWARDFLAG_BIND) != 0;
							int itemFlag = (isBind ? fire.pb.Item.BIND : 0);
							int addNum = bag.doAddItem(itemId, itemNum, 0, itemFlag, "gm compensation", YYLoggerTuJingEnum.tujing_Value_mailget, itemId);
							if (addNum != itemNum) {
								// 提示背包满了
								MessageMgr.psendMsgNotify(roleId, 144875, null);
								return false; // 需要外面回滚
							}
						}
						break;
					case Helper.AWARDTYPE_MONEY: // 银币
						{
							long value = award.getNum();
							if (value > 0) {
								long addValue = bag.addSysMoney(value, "邮件加银币", YYLoggerTuJingEnum.tujing_Value_mailget, 0);
								if (addValue != value) {
									return false; // 需要外面回滚
								}
							}
						}
						break;
					case Helper.AWARDTYPE_GOLD: // 金币
						{
							long value = award.getNum();
							if (value > 0) {
								long addValue = bag.addSysGold(value, "邮件加金币", YYLoggerTuJingEnum.tujing_Value_mailget, 0);
								if (addValue != value) {
									return false; // 需要外面回滚
								}
							}
						}
						break;
					case Helper.AWARDTYPE_FUSHI: // 符石
						{
							long value = award.getNum();
							if (value > 0) {
								Integer userId = xtable.Roleid2userid.select(roleId);
								if (userId == null) {
									userId = xtable.Properties.selectUserid(roleId);
								}
								boolean ret = new fire.pb.fushi.PAddFuShi(userId, roleId, (int) value, FushiConst.CASH_FUSHI, YYLoggerTuJingEnum.tujing_Value_mailget).call();
								if (ret == false) {
									return false; // 需要外面回滚
								}
							}
						}
						break;
					case Helper.AWARDTYPE_EXP: // 经验
						{
							long value = award.getNum();
							if (value > 0) {
								boolean ret = new fire.pb.PAddExpProc(roleId, value, PAddExpProc.OTHER, "邮件添加").call();
								if (ret == false) {
									return false; // 需要外面回滚
								}
							}
						}
						break;
					case Helper.AWARDTYPE_VIPEXP: // VIP经验
						{
							long value = award.getNum();
							if (value > 0) {
								boolean ret = new fire.pb.fushi.PAddVipExp(roleId,(int) value, PAddVipExp.REASON_ADD_VIP_EXP_MAIL_AWARD).call();
								if (ret == false) {
									return false; // 需要外面回滚
								}
							}
						}
						break;
					default: // 错误的奖励类型
						return false;
					}
				}

				xcompens.getSinglecompensations().remove(compenskey);
				if(xcompens.getSinglecompensations().isEmpty()) {
					xtable.Compensationroles.remove(roleId);//删除
				}

				long curTime = System.currentTimeMillis();
				SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				String strTime = dateFormat.format(curTime);

				// 记录日志
				if (Module.logger.isInfoEnabled()) {
					String itemDetails = "";
					String otherDetails = "";
					for (xbean.SingleCompensationAward award : xsingle.getAwards()) {
						switch (award.getType()) {
						case Helper.AWARDTYPE_ITEM:
							itemDetails += "[" + award.getId() + "," + award.getNum() + "," + award.getFlag() + "],";
							break;
						case Helper.AWARDTYPE_MONEY:
							otherDetails += " MONEY=" + award.getNum();
							break;
						case Helper.AWARDTYPE_GOLD:
							otherDetails += " GOLD=" + award.getNum();
							break;
						case Helper.AWARDTYPE_FUSHI:
							otherDetails += " FUSHI=" + award.getNum();
							break;
						case Helper.AWARDTYPE_EXP:
							otherDetails += " EXP=" + award.getNum();
							break;
						case Helper.AWARDTYPE_VIPEXP:
							otherDetails += " VIPEXP=" + award.getNum();
							break;
						default:
							break;
						}
					}

					Module.logger.info("[Mail.ReceiveAward] roleId:" + roleId
							+ " kind:" + 1
							+ " id:" + compenskey
							+ " time:" + strTime
							+ " itemDetails:" + itemDetails
							+ " otherDetails:" + otherDetails
							+ " title:" + xsingle.getTitle());
				}
				// 记录运营日志
				List<fire.log.beans.ItemBaseBean> items = new ArrayList<fire.log.beans.ItemBaseBean>();
				for (xbean.SingleCompensationAward award : xsingle.getAwards()) {
					if (award.getType() == Helper.AWARDTYPE_ITEM) {
						items.add(new fire.log.beans.ItemBaseBean((int) award.getId(), (int) award.getNum()));
					} else {
						items.add(new fire.log.beans.ItemBaseBean(award.getType(), (int) award.getNum()));
					}
				}
				fire.log.beans.MailBean mailLog = new fire.log.beans.MailBean((byte) 1, compenskey,
						xsingle.getTitle(), xsingle.getContent(), strTime, items);
				fire.log.YYLogger.mailLog(roleId, mailLog);
				return true;
			}
		}

		return false;
	}
	
	/**
	 * 获取所有补偿
	 * 注意，如果return false，外面Procedure需要回滚
	 * @return
	 */
	@Deprecated
	public boolean fetchCompensitions()
	{
		if(readonly)
			return false;
		
		if(xcompens.getSinglecompensations().isEmpty())
		{
			MessageMgr.psendMsgNotify(roleId, 143054, null);
			return true;
		}
		List<Long> keys = new LinkedList<Long>();
		keys.addAll(xcompens.getSinglecompensations().keySet());
//		Pack bag = new Pack(roleId, false);
//		for(long compenskey : keys)
//		{
//			xbean.SingleCompensation xsingle = xtable.Singlecompensations.select(compenskey);
//			if(xsingle == null)
//				continue;
//
//			// 奖励
//			for (java.util.Map.Entry<Integer, Integer> e : xsingle.getItems().entrySet()) {
//
//				int itemId = e.getKey();
//				int itemNum = e.getValue();
//				if (itemNum <= 0) {
//					continue;
//				}
//
//				int addNum = bag.doAddItem(itemId, itemNum, "gm compensation", fire.log.enums.YYLoggerTuJingEnum.tujing_Value_buchangget, 1);
//				if (addNum < itemNum) {
//					MessageMgr.psendMsgNotify(roleId, 143059, null);
//					return false; // 需要外面回滚
//				}
//			}
//
//			xcompens.getSinglecompensations().remove(compenskey);
//		}

		if(xcompens.getSinglecompensations().isEmpty()) {
			xtable.Compensationroles.remove(roleId);//删除
		}

		MessageMgr.psendMsgNotify(roleId, 143055, null);

		return true;
	}
	
	public java.util.Map<Long, Long> getSinglecompensations()
	{
		return xcompens.getSinglecompensations();
	}
}
