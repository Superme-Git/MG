
package fire.pb.skill.liveskill;




import fire.log.enums.YYLoggerTuJingEnum;
import fire.pb.item.GroceryItemShuXing;
import fire.pb.main.ConfigManager;
import fire.pb.skill.SSkillError;
import fire.pb.skill.SkillConstant;
import fire.pb.skill.SkillRole;
import fire.pb.util.BagUtil;




// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __CLiveSkillMakeStuff__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

/***
 * 制作符
 * @author changhao
 *
 */
public class CLiveSkillMakeStuff extends __CLiveSkillMakeStuff__ {
	@Override
	protected void process() {
		// protocol handle
		
		final long roleid = gnet.link.Onlines.getInstance().findRoleid(this);
		if (roleid < 0)
			return;
		mkdb.Procedure livemakstuff = new mkdb.Procedure()
		{
			@Override
			/*加锁顺序 team->roleid->match by changhao*/
			protected boolean process()
			{	
				fire.pb.item.GroceryItemShuXing config = (GroceryItemShuXing) fire.pb.item.Module.getInstance().getItemManager().getAttr(itemid);
				if (config == null)
				{
					psend(roleid, new SSkillError(SkillConstant.SkillError.LiveSkillIdError));
					return true;						
				}
				
				int skillid = LiveSkillManager.getInstance().GetMakeStuffSkillIdByItemId(config);
				if (skillid == 0)
				{
					psend(roleid, new SSkillError(SkillConstant.SkillError.LiveSkillIdError));
					return true;						
				}
				
				SkillRole skillrole = new SkillRole(roleid);
				
				xbean.LiveSkill liveskill = skillrole.getLiveSkills().get(skillid);	//得到生活技能等级 by changhao			
				if (liveskill == null)
				{
					psend(roleid, new SSkillError(SkillConstant.SkillError.SkillLevelNotEnough));
					return true;					
				}
				
				int skilllevel = liveskill.getLevel();
				
				int needlevel = LiveSkillManager.getInstance().GetMakeStuffNeedLevelByItemId(config);
				
				if (skilllevel < needlevel)
				{
					psend(roleid, new SSkillError(SkillConstant.SkillError.SkillLevelNotEnough));
					return true;						
				}
							
				boolean energyenough = LiveSkillManager.getInstance().ConsumeMakeEnergy(roleid, skillid, 
						needlevel,"打符", YYLoggerTuJingEnum.tujing_Value_liveskillcost, false);
				
				if (energyenough == false) //消耗活力 by changhao
				{
					psend(roleid, new SSkillError(SkillConstant.SkillError.EnergyNotEnough));
					return true;						
				}
				
				int added = BagUtil.addItem(roleid, itemid, itemnum, LiveSkillManager.MakeStuff, fire.log.enums.YYLoggerTuJingEnum.tujing_Value_zhizuofu, 1, true);
				
				if (itemnum != added)
				{
					psend(roleid, new SSkillError(SkillConstant.SkillError.UnkownError));
					return false;						
				}
				
				SLiveSkillMakeStuff msg = new SLiveSkillMakeStuff();
				msg.ret = 0;
				
				mkdb.Procedure.psendWhileCommit(roleid, msg);
				
				return true;
			}
			
		};
		livemakstuff.submit();	
		
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 800517;

	public int getType() {
		return 800517;
	}

	public int itemid; // 物品的ID by changhao
	public int itemnum; // 物品数量 by changhao

	public CLiveSkillMakeStuff() {
	}

	public CLiveSkillMakeStuff(int _itemid_, int _itemnum_) {
		this.itemid = _itemid_;
		this.itemnum = _itemnum_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(itemid);
		_os_.marshal(itemnum);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		itemid = _os_.unmarshal_int();
		itemnum = _os_.unmarshal_int();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof CLiveSkillMakeStuff) {
			CLiveSkillMakeStuff _o_ = (CLiveSkillMakeStuff)_o1_;
			if (itemid != _o_.itemid) return false;
			if (itemnum != _o_.itemnum) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += itemid;
		_h_ += itemnum;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(itemid).append(",");
		_sb_.append(itemnum).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(CLiveSkillMakeStuff _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = itemid - _o_.itemid;
		if (0 != _c_) return _c_;
		_c_ = itemnum - _o_.itemnum;
		if (0 != _c_) return _c_;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

