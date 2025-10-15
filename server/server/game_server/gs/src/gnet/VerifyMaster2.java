
package gnet;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __VerifyMaster2__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class VerifyMaster2 extends __VerifyMaster2__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 604;

	public int getType() {
		return 604;
	}

	public int zoneid;
	public long roleid;
	public com.locojoy.base.Octets rolename;
	public com.locojoy.base.Octets faction;

	public VerifyMaster2() {
		roleid = -1;
		rolename = new com.locojoy.base.Octets();
		faction = new com.locojoy.base.Octets();
	}

	public VerifyMaster2(int _zoneid_, long _roleid_, com.locojoy.base.Octets _rolename_, com.locojoy.base.Octets _faction_) {
		this.zoneid = _zoneid_;
		this.roleid = _roleid_;
		this.rolename = _rolename_;
		this.faction = _faction_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(zoneid);
		_os_.marshal(roleid);
		_os_.marshal(rolename);
		_os_.marshal(faction);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		zoneid = _os_.unmarshal_int();
		roleid = _os_.unmarshal_long();
		rolename = _os_.unmarshal_Octets();
		faction = _os_.unmarshal_Octets();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof VerifyMaster2) {
			VerifyMaster2 _o_ = (VerifyMaster2)_o1_;
			if (zoneid != _o_.zoneid) return false;
			if (roleid != _o_.roleid) return false;
			if (!rolename.equals(_o_.rolename)) return false;
			if (!faction.equals(_o_.faction)) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += zoneid;
		_h_ += (int)roleid;
		_h_ += rolename.hashCode();
		_h_ += faction.hashCode();
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(zoneid).append(",");
		_sb_.append(roleid).append(",");
		_sb_.append("B").append(rolename.size()).append(",");
		_sb_.append("B").append(faction.size()).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

