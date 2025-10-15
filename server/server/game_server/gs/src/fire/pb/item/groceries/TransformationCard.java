package fire.pb.item.groceries;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import xbean.Item;
import fire.pb.PropRole;
import fire.pb.WorldTime;
import fire.pb.item.GroceryItem;
import fire.pb.item.ItemMgr;
import fire.pb.item.Commontext.UseResult;
import fire.pb.skill.SkillRole;

public class TransformationCard extends GroceryItem {

	public TransformationCard(ItemMgr im, int itemid) {
		super(im, itemid);
	}
	
	public TransformationCard(ItemMgr im, Item item) {

		super(im, item);
	}

	@Override
	protected UseResult appendToRole(final long roleId, final int usenum) {
		if (usenum != 1)
			return UseResult.FAIL;
		Map<Integer, fire.pb.item.STransformationConfig> sTransConfigs = fire.pb.main.ConfigManager.getInstance().getConf(fire.pb.item.STransformationConfig.class);
		Map<Integer, fire.pb.item.STransformationEffectConfig> sTransEffectConfigs = fire.pb.main.ConfigManager.getInstance().getConf(fire.pb.item.STransformationEffectConfig.class);
		
		fire.pb.item.STransformationConfig sTransConfig = sTransConfigs.get(getItemId());
		if (sTransConfig == null)
			return UseResult.FAIL;
		fire.pb.item.STransformationEffectConfig sTransEffectConfig = sTransEffectConfigs.get(sTransConfig.effectid);
		if (sTransEffectConfig == null)
			return UseResult.FAIL;

		final xbean.TransfromInfo ti = xtable.Transfromroles.select(roleId);
		if (ti != null) {
			WorldTime now = WorldTime.getInstance();
			if (now.getTimeInMillis() < ti.getValiddate() && ti.getTransformid() > 0) {
				return UseResult.FAIL;
			}
		}
		
		fire.pb.mission.UtilHelper.itemTransform(roleId, sTransEffectConfig.shapeid, sTransConfig.time);
		xbean.TransfromByItemData transdata = xtable.Transformbyitem.get(roleId);
		if (transdata == null) {
			return UseResult.FAIL;
		}
		transdata.setTransformid(sTransEffectConfig.shapeid);
		transdata.setUseitemid(getItemId());
		
		List<String> paramLst = new ArrayList<String>();
		paramLst.add(sTransConfig.name);
		paramLst.add(String.valueOf(sTransConfig.time));
		fire.pb.talk.MessageMgr.psendMsgNotifyWhileCommit(roleId, 145324, paramLst);
		
		if(sTransEffectConfig.getSkills() != null && sTransEffectConfig.getSkills().size() > 0){
			SkillRole skillRole = new SkillRole(roleId);  
			Map<Integer, Integer> SExtSkill = new HashMap<Integer, Integer>();
			int index = 0;
			PropRole pRole = new PropRole(roleId, false);
			skillRole.removeExtSkill();
			for(int skillid: sTransEffectConfig.getSkills()){
				if(sTransEffectConfig.getSkillfactors().size() > index 
						&& sTransEffectConfig.getSkillconstants().size() > index){
					int nlv = sTransEffectConfig.getSkillfactors().get(index)*pRole.getLevel() + sTransEffectConfig.getSkillconstants().get(index);
					SExtSkill.put(skillid, nlv);
				}
				index++;
			}
			skillRole.addExtSkillWithSP(SExtSkill);
		}
		return UseResult.SUCC;
	}
}
