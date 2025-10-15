
package fire.pb.team.teammelon;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __SOneTeamRollMelonInfo__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class SOneTeamRollMelonInfo extends __SOneTeamRollMelonInfo__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 794526;

	public int getType() {
		return 794526;
	}

	public long melonid;
	public int itemid;
	public fire.pb.team.teammelon.RoleRollInfo rollinfo; // 某人的ROLL点信息 by changhao

	public SOneTeamRollMelonInfo() {
		rollinfo = new fire.pb.team.teammelon.RoleRollInfo();
	}

	public SOneTeamRollMelonInfo(long _melonid_, int _itemid_, fire.pb.team.teammelon.RoleRollInfo _rollinfo_) {
		this.melonid = _melonid_;
		this.itemid = _itemid_;
		this.rollinfo = _rollinfo_;
	}

	public final boolean _validator_() {
		if (!rollinfo._validator_()) return false;
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(melonid);
		_os_.marshal(itemid);
		_os_.marshal(rollinfo);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		melonid = _os_.unmarshal_long();
		itemid = _os_.unmarshal_int();
		rollinfo.unmarshal(_os_);
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof SOneTeamRollMelonInfo) {
			SOneTeamRollMelonInfo _o_ = (SOneTeamRollMelonInfo)_o1_;
			if (melonid != _o_.melonid) return false;
			if (itemid != _o_.itemid) return false;
			if (!rollinfo.equals(_o_.rollinfo)) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += (int)melonid;
		_h_ += itemid;
		_h_ += rollinfo.hashCode();
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(melonid).append(",");
		_sb_.append(itemid).append(",");
		_sb_.append(rollinfo).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

