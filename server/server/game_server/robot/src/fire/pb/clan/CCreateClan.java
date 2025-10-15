
package fire.pb.clan;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __CCreateClan__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class CCreateClan extends __CCreateClan__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 808450;

	public int getType() {
		return 808450;
	}

	public java.lang.String clanname; // 公会名字
	public java.lang.String clanaim; // 公会宗旨（公告）

	public CCreateClan() {
		clanname = "";
		clanaim = "";
	}

	public CCreateClan(java.lang.String _clanname_, java.lang.String _clanaim_) {
		this.clanname = _clanname_;
		this.clanaim = _clanaim_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(clanname, "UTF-16LE");
		_os_.marshal(clanaim, "UTF-16LE");
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		clanname = _os_.unmarshal_String("UTF-16LE");
		clanaim = _os_.unmarshal_String("UTF-16LE");
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof CCreateClan) {
			CCreateClan _o_ = (CCreateClan)_o1_;
			if (!clanname.equals(_o_.clanname)) return false;
			if (!clanaim.equals(_o_.clanaim)) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += clanname.hashCode();
		_h_ += clanaim.hashCode();
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append("T").append(clanname.length()).append(",");
		_sb_.append("T").append(clanaim.length()).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

