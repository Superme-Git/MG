
package fire.pb.skill.liveskill;
import java.util.ArrayList;
import java.util.List;

import fire.log.enums.YYLoggerTuJingEnum;
import fire.pb.item.AddItemResult;
import fire.pb.item.BagTypes;
import fire.pb.item.Pack;
import fire.pb.talk.MessageMgr;



// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __CLiveSkillMakeEnhancement__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

/***
 * 附魔制作
 * @author changhao
 *
 */
public class CLiveSkillMakeEnhancement extends __CLiveSkillMakeEnhancement__ {
	@Override
	protected void process() {
		// protocol handle

		final long roleid = gnet.link.Onlines.getInstance().findRoleid(this);
		if (roleid < 0)
			return;
		mkdb.Procedure liveskillmakeenhancement = new mkdb.Procedure()
		{
			@Override
			protected boolean process()
			{
				xbean.Properties prop = xtable.Properties.select(roleid);
				int school = prop.getSchool();
				
				int skilllevel = LiveSkillManager.getInstance().GetEnhanceLevelBySchool(roleid, school);//取得职业技能等级  by changhao
				if (skilllevel == 0)
				{
					LiveSkillManager.logger.debug("CLiveSkillMakeEnhancement skilllevel error:" + roleid);
					return true;
				}
				
				int liveskillid = LiveSkillManager.getInstance().GetEnhanceLiveSkillBySchool(school);
				int itemid = LiveSkillManager.getInstance().GetEnhanceItemIdBySchool(school);
							
				boolean energyenough = LiveSkillManager.getInstance().ConsumeMakeEnergy(roleid, 
						liveskillid, skilllevel,"附魔", YYLoggerTuJingEnum.tujing_Value_fumo, false);
				
				if (energyenough == false) //消耗活力 by changhao
				{
					java.util.List<String> parameters = new java.util.ArrayList<String>();
					String s = Integer.toString(LiveSkillManager.getInstance().NeedEnergy(liveskillid, skilllevel));
					parameters.add(s);
					MessageMgr.psendMsgNotify(roleid, 143432, parameters);
					return true;						
				}							
				
				Pack bag = (Pack)fire.pb.item.Module.getInstance().getItemMaps(roleid, fire.pb.item.BagTypes.BAG, false);
								
				SLiveSkillMakeEnhancement msg = new SLiveSkillMakeEnhancement();
				
				fire.pb.item.groceries.AttrUpItem item = (fire.pb.item.groceries.AttrUpItem)fire.pb.item.Module.getInstance().getItemManager().genItemBase(itemid, 1);
				item.GenAttr(skilllevel);
				
				if (bag.isFull())
				{
					fire.pb.item.ItemMaps tempBag = fire.pb.item.Module.getInstance().getItemMaps(roleid, BagTypes.TEMP, false);
					if (tempBag.doAddItem(item, -1, LiveSkillManager.Enhancement, fire.log.enums.YYLoggerTuJingEnum.tujing_Value_fumoget, 2) != AddItemResult.SUCC)
					{
						return false;
					}
				}
				else
				{
					if(bag.doAddItem(item, -1, LiveSkillManager.Enhancement, fire.log.enums.YYLoggerTuJingEnum.tujing_Value_fumoget, 2) != AddItemResult.SUCC)
					{
						return false;
					}						
				}
				
				List<String> param = new ArrayList<String>();
				param.add(item.getName());
				param.add("ffffff00");
				MessageMgr.psendMsgNotify(roleid, 150107, param);				
				mkdb.Procedure.psendWhileCommit(roleid, msg);
				
				return true;
			}
		};
		
		liveskillmakeenhancement.submit();			
		
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 800525;

	public int getType() {
		return 800525;
	}


	public CLiveSkillMakeEnhancement() {
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
		if (_o1_ instanceof CLiveSkillMakeEnhancement) {
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

	public int compareTo(CLiveSkillMakeEnhancement _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

