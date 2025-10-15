
package fire.pb.skill.liveskill;





import fire.log.enums.YYLoggerTuJingEnum;
import fire.pb.item.AddItemResult;
import fire.pb.item.BagTypes;
import fire.pb.item.Pack;
import fire.pb.item.SFoodAndDrugEffect;
import fire.pb.main.ConfigManager;
import fire.pb.skill.SSkillError;
import fire.pb.skill.SkillConstant;
import fire.pb.skill.SkillRole;
import fire.pb.util.Misc;



// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __CLiveSkillMakeDrug__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

/***
 * 做药
 * @author changhao
 *
 */
public class CLiveSkillMakeDrug extends __CLiveSkillMakeDrug__ {
	@Override
	protected void process() {
		// protocol handle

		final long roleid = gnet.link.Onlines.getInstance().findRoleid(this);
		if (roleid < 0)
			return;
		mkdb.Procedure liveskillmakedrug = new mkdb.Procedure()
		{
			@Override
			protected boolean process()
			{
				SkillRole skillrole = new SkillRole(roleid);
				
				xbean.LiveSkill liveskill = skillrole.getLiveSkills().get(LiveSkillManager.LIVE_SKILL_TYPE_MAKE_DRUG);	//得到生活技能等级 by changhao			
				if (liveskill == null)
				{
					LiveSkillManager.logger.debug("CLiveSkillMakeDrug skilllevel error:" + roleid);
					psend(roleid, new SSkillError(SkillConstant.SkillError.SkillLevelNotEnough));
					return true;					
				}
				
				int skilllevel = liveskill.getLevel();
				
				boolean energyenough = LiveSkillManager.getInstance().ConsumeMakeEnergy(
						roleid, LiveSkillManager.LIVE_SKILL_TYPE_COOKING, skilllevel,"炼药 ", 
						YYLoggerTuJingEnum.tujing_Value_liveskillcost, false);
				
				if (energyenough == false) //消耗活力 by changhao
				{
					psend(roleid, new SSkillError(SkillConstant.SkillError.EnergyNotEnough));
					return true;						
				}							
				
				Pack bag = (Pack)fire.pb.item.Module.getInstance().getItemMaps(roleid, fire.pb.item.BagTypes.BAG, false);
				
				int count = 0;
				
				int makingsweight = 0;
				
				if (makingslist.size() == 0) //使用银币炼药 by changhao
				{
					fire.pb.item.SFoodAndDrugEffect stuff = (SFoodAndDrugEffect) fire.pb.item.Module.getInstance().getItemManager().getAttr(LiveSkillManager.ITEM_TYPE_DRUG_MAKINGS_LOW);
					if (stuff == null)
					{
						psend(roleid, new SSkillError(SkillConstant.SkillError.ItemNotExist));
						return true;						
					}
					
					makingsweight = stuff.lianyaoMaterialWeight * 4;
					count = 4;
					
					//扣钱 by changhao
					if(bag.subMoney(-6000, LiveSkillManager.MakeDrug, fire.log.enums.YYLoggerTuJingEnum.tujing_Value_gonghuilianjin, 0) != -6000)
					{
						psend(roleid, new SSkillError(SkillConstant.SkillError.MoneyNotEnough));
						return false;	
					}
				}
				else if (makingslist.size() < 2) //一个材料不能炼药 by changhao
				{
					psend(roleid, new SSkillError(SkillConstant.SkillError.OneStuffCannot));
					return true;
				}
				else
				{
					for (Integer makings : makingslist)
					{
						fire.pb.item.SFoodAndDrugEffect stuff = (SFoodAndDrugEffect) fire.pb.item.Module.getInstance().getItemManager().getAttr(makings);
						if (stuff != null)
						{
							fire.pb.item.ItemNameClassConfig classid = ConfigManager.getInstance().getConf(fire.pb.item.ItemNameClassConfig.class).get(stuff.getTypeid());
							if (classid != null)
							{
								boolean drugstufftype = classid.classname.equals(LiveSkillManager.DrugStuff);
								if (drugstufftype == true) //如果是炼药材料  by changhao
								{
									makingsweight += stuff.lianyaoMaterialWeight;
									
									//成功消耗道具 by changhao
									if(bag.removeItemById(makings, 1, fire.log.enums.YYLoggerTuJingEnum.tujing_Value_gonghuiyaofanguse, 0, LiveSkillManager.MakeDrug) == 1)
									{
										count ++;
									}						
								}								
							}
						}
					}		
				}
								
				if (count < 2) //一个材料不能炼药 by changhao
				{
					psend(roleid, new SSkillError(SkillConstant.SkillError.OneStuffCannot));
					return true;					
				}
				
				int prob = GetProbByNum(count);
				
				int index = Misc.getRandomBetween(1, 1000);
				
				SLiveSkillMakeDrug msg = new SLiveSkillMakeDrug();
				
				if (index <= prob) //制作开始 by changhao
				{
					fire.pb.item.SFoodAndDrugEffect jinchuang = (SFoodAndDrugEffect) fire.pb.item.Module.getInstance().getItemManager().getAttr(LiveSkillManager.ITEM_TYPE_DRUG_JIN_CHUANG);
					int jinchuangweight = jinchuang.getLianyaoWeight() - skilllevel * 2 - makingsweight;
					int drugtotalweight = LiveSkillManager.getInstance().drugtotalweight;
					
					int randomValue = Misc.getRandomBetween(0, jinchuangweight + drugtotalweight - 1);
					
					int curWeight = 0;
					
					int drug = LiveSkillManager.ITEM_TYPE_DRUG_JIN_CHUANG; //最终产出啥药 by changhao
					if (randomValue < jinchuangweight) //是否产生金疮药 by changhao
					{

					}
					else
					{
						//产生那个药 by changhao
						curWeight = jinchuangweight;
						for (fire.pb.item.SFoodAndDrugEffect e : LiveSkillManager.getInstance().druglist)
						{
							curWeight += e.getLianyaoWeight();
							
							if (randomValue < curWeight)
							{
								drug = e.getId();
								break;
							}
						}						
					}
					
					fire.pb.item.drug.GeneralDrug drugitem = (fire.pb.item.drug.GeneralDrug)fire.pb.item.Module.getInstance().getItemManager().genItemBase(drug, 1);
					//drugitem.setFlag( fire.pb.Item.BIND);
					//根据技能等级随机生成品质 by changhao
					
					fire.pb.common.SCommon common = ConfigManager.getInstance().getConf(fire.pb.common.SCommon.class).get(159);
					int delta = Integer.parseInt(common.value);					
					
					int quality = Misc.getRandomBetween(Math.max(skilllevel - delta, 1), skilllevel);
					drugitem.setQuality(quality);
					
					if (bag.isFull())
					{
						fire.pb.item.ItemMaps tempBag = fire.pb.item.Module.getInstance().getItemMaps(roleid, BagTypes.TEMP, false);
						if (tempBag.doAddItem(drugitem, -1, LiveSkillManager.MakeDrug, fire.log.enums.YYLoggerTuJingEnum.tujing_Value_gonghuiyaofang, 2) != AddItemResult.SUCC)
						{
							return false;
						}
					}
					else
					{
						if(bag.doAddItem(drugitem, -1, LiveSkillManager.MakeDrug, fire.log.enums.YYLoggerTuJingEnum.tujing_Value_gonghuiyaofang, 2) != AddItemResult.SUCC)
						{
							return false;
						}						
					}
									
					msg.itemid = drug;
					msg.ret = 0;
					
					mkdb.Procedure.psendWhileCommit(roleid, msg);
				}
				else //制作失败 by changhao
				{
					msg.itemid = 0;
					msg.ret = 1;
					
					mkdb.Procedure.psendWhileCommit(roleid, msg);				
				}
				
				return true;
			}
		};
		
		liveskillmakedrug.submit();	
		
	}
	
	//根据道具数量得到生成的概率 by changhao
	public int GetProbByNum(int num)
	{
		if (num == 2)
		{
			return (int)(1000F * (2F / 3F));
		}
		else if (num == 3)
		{
			return (int)(1000F * (7F / 8F));
		}
		else if (num == 4)
		{
			return 1000;
		}
		
		return 0;
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 800519;

	public int getType() {
		return 800519;
	}

	public java.util.LinkedList<Integer> makingslist; // 材料链 by changhao

	public CLiveSkillMakeDrug() {
		makingslist = new java.util.LinkedList<Integer>();
	}

	public CLiveSkillMakeDrug(java.util.LinkedList<Integer> _makingslist_) {
		this.makingslist = _makingslist_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.compact_uint32(makingslist.size());
		for (Integer _v_ : makingslist) {
			_os_.marshal(_v_);
		}
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		for (int _size_ = _os_.uncompact_uint32(); _size_ > 0; --_size_) {
			int _v_;
			_v_ = _os_.unmarshal_int();
			makingslist.add(_v_);
		}
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof CLiveSkillMakeDrug) {
			CLiveSkillMakeDrug _o_ = (CLiveSkillMakeDrug)_o1_;
			if (!makingslist.equals(_o_.makingslist)) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += makingslist.hashCode();
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(makingslist).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

