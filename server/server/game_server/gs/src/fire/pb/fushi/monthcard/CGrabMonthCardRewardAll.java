
package fire.pb.fushi.monthcard;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __CGrabMonthCardRewardAll__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

/***
 * 领取月卡奖励 by changhao
 *
 */
public class CGrabMonthCardRewardAll extends __CGrabMonthCardRewardAll__ {
	@Override
	protected void process() {
		// protocol handle
		
		final long roleid = gnet.link.Onlines.getInstance().findRoleid(this);
		if (roleid < 0)
		{
			return;
		}
		
		mkdb.Procedure grabmonthcardreward = new mkdb.Procedure()
		{
			@Override
			protected boolean process()
			{
				xbean.EMonthCard monthcard = xtable.Roleid2monthcard.get(roleid);
				if (monthcard != null)
				{
					boolean grab = fire.pb.fushi.MonthCardManager.getInstance().IsGrabReward(roleid, monthcard.getGrabtime());
					if (grab == true) //已经领取过奖励了 by changhao
					{
						return false;
					}
					
					//可以领取奖励 by changhao
					{
						xbean.Properties prop = xtable.Properties.select(roleid);
						if (prop == null)
						{
							return false;
						}
						
						fire.pb.item.Pack bag = (fire.pb.item.Pack)fire.pb.item.Module.getInstance().getItemMaps(roleid, fire.pb.item.BagTypes.BAG, false);

						if (fire.pb.fushi.Module.GetPayServiceType() == 1) //点卡服务器 by changhao
						{
							java.util.Map<Integer, fire.pb.fushi.SMonthCardConfigDayPay> config = fire.pb.main.ConfigManager.getInstance().getConf(fire.pb.fushi.SMonthCardConfigDayPay.class);
							for (fire.pb.fushi.SMonthCardConfigDayPay e : config.values())
							{
								if (e.getType() == 1)
								{
									java.util.Map<String, Object> paras = new java.util.HashMap<String, Object>(10);
									paras.put(fire.pb.activity.award.RewardMgr.ROLE_LEVEL, prop.getLevel());
									fire.pb.activity.award.RewardMgr.getInstance().distributeAllAward(roleid, e.getRewardid(), paras, fire.log.enums.YYLoggerTuJingEnum.tujing_Value_monthcard_reward, 0,fire.pb.PAddExpProc.OTHER, "月卡奖励");										
								}
								else if (e.getType() == 0)
								{
									if (bag.isFull())
									{
										fire.pb.item.ItemMaps tempBag = fire.pb.item.Module.getInstance().getItemMaps(roleid, fire.pb.item.BagTypes.TEMP, false);
										
										if (tempBag.doAddItem(e.getItemid(), e.getItemnum(), "月卡奖励", fire.log.enums.YYLoggerTuJingEnum.tujing_Value_monthcard_reward, 2) != e.getItemnum())
										{
											return false;
										}
									}
									else
									{
										if (bag.doAddItem(e.getItemid(), e.getItemnum(), "月卡奖励", fire.log.enums.YYLoggerTuJingEnum.tujing_Value_monthcard_reward, 2) != e.getItemnum())
										{
											return false;
										}
									}				
								}
							}							
						}
						else
						{
							java.util.Map<Integer, fire.pb.fushi.SMonthCardConfig> config = fire.pb.main.ConfigManager.getInstance().getConf(fire.pb.fushi.SMonthCardConfig.class);
							for (fire.pb.fushi.SMonthCardConfig e : config.values())
							{
								if (e.getType() == 1)
								{
									java.util.Map<String, Object> paras = new java.util.HashMap<String, Object>(10);
									paras.put(fire.pb.activity.award.RewardMgr.ROLE_LEVEL, prop.getLevel());
									fire.pb.activity.award.RewardMgr.getInstance().distributeAllAward(roleid, e.getRewardid(), paras, fire.log.enums.YYLoggerTuJingEnum.tujing_Value_monthcard_reward, 0,fire.pb.PAddExpProc.OTHER, "月卡奖励");										
								}
								else if (e.getType() == 0)
								{
									if (bag.isFull())
									{
										fire.pb.item.ItemMaps tempBag = fire.pb.item.Module.getInstance().getItemMaps(roleid, fire.pb.item.BagTypes.TEMP, false);
										
										if (tempBag.doAddItem(e.getItemid(), e.getItemnum(), "月卡奖励", fire.log.enums.YYLoggerTuJingEnum.tujing_Value_monthcard_reward, 2) != e.getItemnum())
										{
											return false;
										}
									}
									else
									{
										if (bag.doAddItem(e.getItemid(), e.getItemnum(), "月卡奖励", fire.log.enums.YYLoggerTuJingEnum.tujing_Value_monthcard_reward, 2) != e.getItemnum())
										{
											return false;
										}
									}				
								}
							}								
						}
					}
					
					monthcard.setGrabtime(System.currentTimeMillis());
					
					fire.pb.fushi.MonthCardManager.getInstance().SendMonthCardInfo(roleid);
				}
				
				return true;
			}
		};
		grabmonthcardreward.submit();
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 812690;

	public int getType() {
		return 812690;
	}


	public CGrabMonthCardRewardAll() {
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof CGrabMonthCardRewardAll) {
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(CGrabMonthCardRewardAll _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

