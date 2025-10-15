
package fire.pb.item;
import java.util.Properties;

import fire.pb.Item;
import fire.pb.util.FireProp;


// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __CGetRoleEquip__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class CGetRoleEquip extends __CGetRoleEquip__ {
	@Override
	protected void process() {
		// protocol handle
		final long thisroleId = gnet.link.Onlines.getInstance()
				.findRoleid(this);
		if (thisroleId < 0)
			return;
		Properties prop = fire.pb.item.Module.getInstance().getProp();
		final fire.pb.map.Role thisrole = fire.pb.map.RoleManager
				.getInstance().getRoleByID(thisroleId);
		if (null == thisrole)
			return;
		final fire.pb.map.Role role = fire.pb.map.RoleManager.getInstance()
				.getRoleByID(roleid);
		if (role == null) {
			final int failed = FireProp.getIntValue(prop, "check.equip.failed");
			fire.pb.talk.MessageMgr.sendMsgNotify(thisroleId, failed, null);
			return;
		}
		
		xbean.Properties otherprop = xtable.Properties.select(roleid);
		Integer v = otherprop.getSysconfigmap().get(fire.pb.SysConfigType.refuseotherseeequip);
		if (v != null && v > 0)
		{
			fire.pb.talk.MessageMgr.sendMsgNotify(thisroleId, 166063, null);			
			return;
		}
		
		final ItemMaps bag = Module.getInstance().getItemMaps(roleid,
				BagTypes.EQUIP, true);
		final SGetRoleEquip res = new SGetRoleEquip();
		res.roleid = roleid;
		res.rolename = xtable.Properties.selectRolename(roleid);
		res.shape = xtable.Properties.selectShape(roleid);
		res.equipinfo = bag.getPackInfo();
		res.profession = xtable.Properties.selectSchool(roleid);
		fire.pb.map.Role.getPlayerComponents(roleid, res.components);
		res.totalscore = xtable.Properties.selectRolescore(roleid);
		res.rolelevel = xtable.Properties.selectLevel(roleid);
		for (Item item : res.equipinfo.items) {
			ItemBase bitem = bag.getItem(item.key);
			if (bitem == null)
				continue;
			res.tips.put(item.key, bitem.getTips());
		}
		gnet.link.Onlines.getInstance().send(thisroleId, res);

		if (sendmsg == 1) {
			final int succ = FireProp.getIntValue(prop, "check.equip.success");
			java.util.ArrayList<String> args = new java.util.ArrayList<String>();
			args.add(xtable.Properties.selectRolename(thisroleId));
			args.add(String.valueOf(thisroleId));
			fire.pb.talk.MessageMgr.sendMsgNotify(roleid, succ, args);
		}
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 787478;

	public int getType() {
		return 787478;
	}

	public long roleid;
	public byte sendmsg;

	public CGetRoleEquip() {
	}

	public CGetRoleEquip(long _roleid_, byte _sendmsg_) {
		this.roleid = _roleid_;
		this.sendmsg = _sendmsg_;
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
		_os_.marshal(sendmsg);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		roleid = _os_.unmarshal_long();
		sendmsg = _os_.unmarshal_byte();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof CGetRoleEquip) {
			CGetRoleEquip _o_ = (CGetRoleEquip)_o1_;
			if (roleid != _o_.roleid) return false;
			if (sendmsg != _o_.sendmsg) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += (int)roleid;
		_h_ += (int)sendmsg;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(roleid).append(",");
		_sb_.append(sendmsg).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(CGetRoleEquip _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = Long.signum(roleid - _o_.roleid);
		if (0 != _c_) return _c_;
		_c_ = sendmsg - _o_.sendmsg;
		if (0 != _c_) return _c_;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

