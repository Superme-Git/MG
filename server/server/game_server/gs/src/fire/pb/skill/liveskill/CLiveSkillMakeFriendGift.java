
package fire.pb.skill.liveskill;




import fire.log.enums.YYLoggerTuJingEnum;
import fire.pb.main.ConfigManager;
import fire.pb.skill.SSkillError;
import fire.pb.skill.SkillConstant;
import fire.pb.util.BagUtil;



// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __CLiveSkillMakeFriendGift__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

/***
 * 同心圆
 * @author changhao
 *
 */
public class CLiveSkillMakeFriendGift extends __CLiveSkillMakeFriendGift__ {
	@Override
	protected void process() {
		// protocol handle
		
		final long roleid = gnet.link.Onlines.getInstance().findRoleid(this);
		if (roleid < 0)
			return;
		mkdb.Procedure liveskillmakefriendgift = new mkdb.Procedure()
		{
			@Override
			protected boolean process()
			{
				int skilllevel = 1;
				
				fire.pb.skill.SLifeSkill config = ConfigManager.getInstance().getConf(fire.pb.skill.SLifeSkill.class).get(LiveSkillManager.LIVE_SKILL_TYPE_MAKE_FRIEND_GIFT);
				
				if (config == null)
				{
					LiveSkillManager.logger.error("CLiveSkillMakeFriendGift config error:" + LiveSkillManager.LIVE_SKILL_TYPE_MAKE_FRIEND_GIFT);
					
					psend(roleid, new SSkillError(SkillConstant.SkillError.LiveSkillIdError));
					return true;						
				}
				
				fire.pb.skill.SLifeSkillCost cost = ConfigManager.getInstance().getConf(fire.pb.skill.SLifeSkillCost.class).get(skilllevel);
				if (cost == null)
				{
					LiveSkillManager.logger.error("CLiveSkillMakeFriendGift skill level error:" + skilllevel);
					
					psend(roleid, new SSkillError(SkillConstant.SkillError.LiveSkillIdError));
					return true;					
				}
				
				boolean energyenough = LiveSkillManager.getInstance().ConsumeMakeEnergy(roleid, LiveSkillManager.LIVE_SKILL_TYPE_MAKE_FRIEND_GIFT, 
						skilllevel,"好友礼物", YYLoggerTuJingEnum.tujing_Value_friendgive, false);
				
				if (energyenough == false) //消耗活力 by changhao
				{
					psend(roleid, new SSkillError(SkillConstant.SkillError.EnergyNotEnough));
					return true;						
				}				
				
				int added = BagUtil.addItem(roleid, LiveSkillManager.ITEM_TYPE_FRIEND_GIFT, 1, LiveSkillManager.MakeStuff, fire.log.enums.YYLoggerTuJingEnum.tujing_Value_tongxinjie, 1, true);
				
				if (1 != added)
				{
					psend(roleid, new SSkillError(SkillConstant.SkillError.UnkownError));
					return false;						
				}
				
				SLiveSkillMakeFriendGift msg = new SLiveSkillMakeFriendGift();
				msg.itemid = LiveSkillManager.ITEM_TYPE_FRIEND_GIFT;
				mkdb.Procedure.psendWhileCommit(roleid, msg);	
				
				return true;
			}
		};
		
		liveskillmakefriendgift.submit();
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 800523;

	public int getType() {
		return 800523;
	}


	public CLiveSkillMakeFriendGift() {
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
		if (_o1_ instanceof CLiveSkillMakeFriendGift) {
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

	public int compareTo(CLiveSkillMakeFriendGift _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

