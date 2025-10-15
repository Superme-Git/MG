
package fire.pb.skill.liveskill;

import fire.log.enums.YYLoggerTuJingEnum;
import fire.pb.DataInit;
import fire.pb.RoleConfigManager;
import fire.pb.common.SCommon;
import fire.pb.item.Pack;
import fire.pb.main.ConfigManager;
import fire.pb.skill.SSkillError;
import fire.pb.skill.SkillConstant;




// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __CLiveSkillMakeFarm__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

/***
 * 伐木
 * @author changhao
 *
 */
public class CLiveSkillMakeFarm extends __CLiveSkillMakeFarm__ {
	@Override
	protected void process() {
		
		final long roleid = gnet.link.Onlines.getInstance().findRoleid(this);	
		if (roleid < 0)
			return;
		mkdb.Procedure liveskillmakefarm = new mkdb.Procedure()
		{			
			@Override
			protected boolean process()
			{
				int skilllevel = 1;
				int money = 100;
				boolean energyenough = LiveSkillManager.getInstance().ConsumeMakeEnergy(roleid, 
						LiveSkillManager.LIVE_SKILL_TYPE_MAKE_FARM, skilllevel,"打工",
						YYLoggerTuJingEnum.tujing_Value_dagong, false);
				
				if (energyenough == false) //消耗活力 by changhao
				{
					psend(roleid, new SSkillError(SkillConstant.SkillError.EnergyNotEnough));
					return true;						
				}
				
				Pack bag = (Pack)fire.pb.item.Module.getInstance().getItemMaps(roleid, fire.pb.item.BagTypes.BAG, false);
				
				if (fire.pb.fushi.Module.GetPayServiceType() == 1) //点卡服务器 by changhao
				{
					fire.pb.fushi.SCommonDayPay cost = ConfigManager.getInstance().getConf(fire.pb.fushi.SCommonDayPay.class).get(7);
					money = cost.getServerdata();
					
					//增加银币 by changhao
					if(bag.addSysMoney(money, LiveSkillManager.MakeFarm, fire.log.enums.YYLoggerTuJingEnum.tujing_Value_gonghuiyaofang, 0) != money)
					{
						psend(roleid, new SSkillError(SkillConstant.SkillError.MoneyNotEnough));
						return false;					
					}		
				}
				else
				{
					final fire.pb.common.SCommon cc = fire.pb.RoleConfigManager.getRoleCommonConfig(109);
					money = Integer.parseInt(cc.getValue());
					
					//增加金币 by changhao
					if(bag.subGold(money, LiveSkillManager.MakeFarm, fire.log.enums.YYLoggerTuJingEnum.tujing_Value_gonghuiyaofang, 0) != money)
					{
						psend(roleid, new SSkillError(SkillConstant.SkillError.MoneyNotEnough));
						return false;					
					}					
				}
				
				SLiveSkillMakeFarm msg = new SLiveSkillMakeFarm();
				msg.addgold = money;
				mkdb.Procedure.psendWhileCommit(roleid, msg);					
		
				return true;
			}
		};
		
		liveskillmakefarm.submit();
		
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 800527;

	public int getType() {
		return 800527;
	}


	public CLiveSkillMakeFarm() {
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
		if (_o1_ instanceof CLiveSkillMakeFarm) {
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

	public int compareTo(CLiveSkillMakeFarm _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

