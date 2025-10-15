
package fire.pb.clan;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __SClanAim__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class SClanAim extends __SClanAim__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 808480;

	public int getType() {
		return 808480;
	}

	public long clanid; // 公会id
	public java.lang.String clanaim; // 公会宗旨
	public java.lang.String oldclanname; // 公会曾用名

	public SClanAim() {
		clanaim = "";
		oldclanname = "";
	}

	public SClanAim(long _clanid_, java.lang.String _clanaim_, java.lang.String _oldclanname_) {
		this.clanid = _clanid_;
		this.clanaim = _clanaim_;
		this.oldclanname = _oldclanname_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(clanid);
		_os_.marshal(clanaim, "UTF-16LE");
		_os_.marshal(oldclanname, "UTF-16LE");
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		clanid = _os_.unmarshal_long();
		clanaim = _os_.unmarshal_String("UTF-16LE");
		oldclanname = _os_.unmarshal_String("UTF-16LE");
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof SClanAim) {
			SClanAim _o_ = (SClanAim)_o1_;
			if (clanid != _o_.clanid) return false;
			if (!clanaim.equals(_o_.clanaim)) return false;
			if (!oldclanname.equals(_o_.oldclanname)) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += (int)clanid;
		_h_ += clanaim.hashCode();
		_h_ += oldclanname.hashCode();
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(clanid).append(",");
		_sb_.append("T").append(clanaim.length()).append(",");
		_sb_.append("T").append(oldclanname.length()).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

