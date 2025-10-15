
package fire.msp.move;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __MRoleGotoNotify__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class MRoleGotoNotify extends __MRoleGotoNotify__ {
	@Override
	protected void process() {
		new PRoleGotoNotify(roleid,gototype,oldsceneid,newsceneid).submit();
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 724918;

	public int getType() {
		return 724918;
	}

	public long roleid;
	public int gototype;
	public long oldsceneid;
	public long newsceneid;

	public MRoleGotoNotify() {
	}

	public MRoleGotoNotify(long _roleid_, int _gototype_, long _oldsceneid_, long _newsceneid_) {
		this.roleid = _roleid_;
		this.gototype = _gototype_;
		this.oldsceneid = _oldsceneid_;
		this.newsceneid = _newsceneid_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(roleid);
		_os_.marshal(gototype);
		_os_.marshal(oldsceneid);
		_os_.marshal(newsceneid);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		roleid = _os_.unmarshal_long();
		gototype = _os_.unmarshal_int();
		oldsceneid = _os_.unmarshal_long();
		newsceneid = _os_.unmarshal_long();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof MRoleGotoNotify) {
			MRoleGotoNotify _o_ = (MRoleGotoNotify)_o1_;
			if (roleid != _o_.roleid) return false;
			if (gototype != _o_.gototype) return false;
			if (oldsceneid != _o_.oldsceneid) return false;
			if (newsceneid != _o_.newsceneid) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += (int)roleid;
		_h_ += gototype;
		_h_ += (int)oldsceneid;
		_h_ += (int)newsceneid;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(roleid).append(",");
		_sb_.append(gototype).append(",");
		_sb_.append(oldsceneid).append(",");
		_sb_.append(newsceneid).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(MRoleGotoNotify _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = Long.signum(roleid - _o_.roleid);
		if (0 != _c_) return _c_;
		_c_ = gototype - _o_.gototype;
		if (0 != _c_) return _c_;
		_c_ = Long.signum(oldsceneid - _o_.oldsceneid);
		if (0 != _c_) return _c_;
		_c_ = Long.signum(newsceneid - _o_.newsceneid);
		if (0 != _c_) return _c_;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

