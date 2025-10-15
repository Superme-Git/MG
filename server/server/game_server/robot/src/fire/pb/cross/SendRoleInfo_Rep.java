
package fire.pb.cross;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __SendRoleInfo_Rep__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class SendRoleInfo_Rep extends __SendRoleInfo_Rep__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 819065;

	public int getType() {
		return 819065;
	}

	public long roleid;
	public int myzoneid;
	public byte flag; // 1到跨服，2到原服

	public SendRoleInfo_Rep() {
	}

	public SendRoleInfo_Rep(long _roleid_, int _myzoneid_, byte _flag_) {
		this.roleid = _roleid_;
		this.myzoneid = _myzoneid_;
		this.flag = _flag_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(roleid);
		_os_.marshal(myzoneid);
		_os_.marshal(flag);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		roleid = _os_.unmarshal_long();
		myzoneid = _os_.unmarshal_int();
		flag = _os_.unmarshal_byte();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof SendRoleInfo_Rep) {
			SendRoleInfo_Rep _o_ = (SendRoleInfo_Rep)_o1_;
			if (roleid != _o_.roleid) return false;
			if (myzoneid != _o_.myzoneid) return false;
			if (flag != _o_.flag) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += (int)roleid;
		_h_ += myzoneid;
		_h_ += (int)flag;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(roleid).append(",");
		_sb_.append(myzoneid).append(",");
		_sb_.append(flag).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(SendRoleInfo_Rep _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = Long.signum(roleid - _o_.roleid);
		if (0 != _c_) return _c_;
		_c_ = myzoneid - _o_.myzoneid;
		if (0 != _c_) return _c_;
		_c_ = flag - _o_.flag;
		if (0 != _c_) return _c_;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

