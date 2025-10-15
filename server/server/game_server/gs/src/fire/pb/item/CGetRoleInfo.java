
package fire.pb.item;

import java.util.Properties;

import fire.pb.Item;
import fire.pb.util.FireProp;


// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __CGetRoleInfo__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class CGetRoleInfo extends __CGetRoleInfo__ {
	@Override
	protected void process() {
		// protocol handle
		final long thisroleId = gnet.link.Onlines.getInstance().findRoleid(this);
		if (thisroleId < 0)
			return;
		Properties prop = fire.pb.item.Module.getInstance().getProp();
		final fire.pb.map.Role thisrole = fire.pb.map.RoleManager.getInstance().getRoleByID(thisroleId);
		if (null == thisrole)
			return;
		final fire.pb.map.Role role = fire.pb.map.RoleManager.getInstance().getRoleByID(roleid);
		if (role == null){
			final int failed = FireProp.getIntValue(prop, "check.equip.failed");	
			fire.pb.talk.MessageMgr.sendMsgNotify(thisroleId, failed, null);
			return;
		}
		
		final ItemMaps bag=Module.getInstance().getItemMaps(roleid,BagTypes.EQUIP,true);
		final SGetRoleInfo res=new SGetRoleInfo();
		res.roleid = roleid;
		
		//目标对象
		xbean.Properties targetRoleProp=xtable.Properties.select(roleid);
		
		res.rolename = targetRoleProp.getRolename();
		res.shape = targetRoleProp.getShape();
		res.level =targetRoleProp.getLevel();
		res.school =targetRoleProp.getSchool();
		
		res.packinfo=bag.getPackInfo();
		res.equipscore = Module.getInstance().getEquipTotalScore(roleid);
		for (Item item : res.packinfo.items){
			ItemBase bitem = bag.getItem(item.key);
			if (bitem == null)
				continue;
			res.tips.put( item.key, bitem.getTips() );
		
		}
		gnet.link.Onlines.getInstance().send(thisroleId, res);
		
		SNoticeRoleGetInfo sSendGetOtherRoloeInfoMsg = new SNoticeRoleGetInfo();
		sSendGetOtherRoloeInfoMsg.roleid = thisroleId;
		sSendGetOtherRoloeInfoMsg.rolename = xtable.Properties.selectRolename(thisroleId);
		gnet.link.Onlines.getInstance().send(roleid, sSendGetOtherRoloeInfoMsg);
		
		
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 787709;

	public int getType() {
		return 787709;
	}

	public long roleid;

	public CGetRoleInfo() {
	}

	public CGetRoleInfo(long _roleid_) {
		this.roleid = _roleid_;
	}

	public final boolean _validator_() {
		if (roleid < 1) return false;
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(roleid);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		roleid = _os_.unmarshal_long();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof CGetRoleInfo) {
			CGetRoleInfo _o_ = (CGetRoleInfo)_o1_;
			if (roleid != _o_.roleid) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += (int)roleid;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(roleid).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(CGetRoleInfo _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = Long.signum(roleid - _o_.roleid);
		if (0 != _c_) return _c_;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

