
package fire.pb.skill.liveskill;





import fire.log.enums.YYLoggerTuJingEnum;
import fire.pb.item.AddItemResult;
import fire.pb.item.BagTypes;
import fire.pb.item.Pack;
import fire.pb.main.ConfigManager;
import fire.pb.skill.SSkillError;
import fire.pb.skill.SkillConstant;
import fire.pb.skill.SkillRole;
import fire.pb.util.Misc;



// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __CLiveSkillMakeFood__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

/***
 * 制作食物
 * @author changhao
 *
 */
public class CLiveSkillMakeFood extends __CLiveSkillMakeFood__ {
	@Override
	protected void process() {
		// protocol handle
		
		final long roleid = gnet.link.Onlines.getInstance().findRoleid(this);
		if (roleid < 0)
			return;
		mkdb.Procedure liveskillmakefood = new mkdb.Procedure()
		{
			@Override
			protected boolean process()
			{
				SkillRole skillrole = new SkillRole(roleid);
				
				xbean.LiveSkill liveskill = skillrole.getLiveSkills().get(LiveSkillManager.LIVE_SKILL_TYPE_COOKING);	//得到生活技能等级 by changhao			
				if (liveskill == null)
				{
					psend(roleid, new SSkillError(SkillConstant.SkillError.SkillLevelNotEnough));
					return true;					
				}
				
				int skilllevel = liveskill.getLevel();
						
				boolean energyenough = LiveSkillManager.getInstance().ConsumeMakeEnergy(roleid, 
						LiveSkillManager.LIVE_SKILL_TYPE_COOKING, skilllevel,"制作食物",
						YYLoggerTuJingEnum.tujing_Value_liveskillcost, false);
				
				if (energyenough == false)
				{
					psend(roleid, new SSkillError(SkillConstant.SkillError.EnergyNotEnough));
					return true;						
				}
				
				int totalweight = LiveSkillManager.getInstance().GetCookingWeightByLevel(skilllevel);
				int curweight = 0;
				int randomValue = Misc.getRandomBetween(0, totalweight - 1);
				int fooditem = 0;
				for (fire.pb.item.SFoodAndDrugEffect e : LiveSkillManager.getInstance().cookingfoodlist)
				{
					curweight += e.getPengrenWeight();
					if (randomValue < curweight)
					{
						fooditem = e.getId();
						break;
					}
				}	
				
				if (fooditem == 0)
				{
					psend(roleid, new SSkillError(SkillConstant.SkillError.NoCookingFood));
					return true;					
				}
				
				Pack bag = (Pack)fire.pb.item.Module.getInstance().getItemMaps(roleid, fire.pb.item.BagTypes.BAG, false);
				
				fire.pb.item.food.GeneralFood food = (fire.pb.item.food.GeneralFood)fire.pb.item.Module.getInstance().getItemManager().genItemBase(fooditem, 1);
				//food.setFlag( fire.pb.Item.BIND);
					
				fire.pb.common.SCommon common = ConfigManager.getInstance().getConf(fire.pb.common.SCommon.class).get(158);
	
				int delta = Integer.parseInt(common.value);
		
				int random = fire.pb.util.Misc.getRandomBetween(Math.max(liveskill.getLevel() - delta, 1), liveskill.getLevel());
				food.setQuality(random);
				
				//if(bag.addItem(food, -1, LiveSkillManager.CookingString, fire.log.YYLogger.COUNTER_TYPE, 2) != AddItemResult.SUCC)
				//{
				//	return false;
				//}
				
				if (bag.isFull())
				{
					fire.pb.item.ItemMaps tempBag = fire.pb.item.Module.getInstance().getItemMaps(roleid, BagTypes.TEMP, false);
					if (tempBag.doAddItem(food, -1, LiveSkillManager.CookingString, fire.log.enums.YYLoggerTuJingEnum.tujing_Value_gonghuixiulianget, 2) != AddItemResult.SUCC)
					{
						return false;
					}
				}
				else
				{
					if(bag.doAddItem(food, -1, LiveSkillManager.CookingString, fire.log.enums.YYLoggerTuJingEnum.tujing_Value_gonghuixiulianget, 2) != AddItemResult.SUCC)
					{
						return false;
					}						
				}				
					
				SLiveSkillMakeFood msg = new SLiveSkillMakeFood();
				msg.itemid = fooditem;
				msg.ret = 0;
				
				mkdb.Procedure.psendWhileCommit(roleid, msg);
							
				return true;
			}
		};
		
		liveskillmakefood.submit();
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 800521;

	public int getType() {
		return 800521;
	}


	public CLiveSkillMakeFood() {
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
		if (_o1_ instanceof CLiveSkillMakeFood) {
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

	public int compareTo(CLiveSkillMakeFood _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

