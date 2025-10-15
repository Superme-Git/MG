
package fire.pb.pingbi;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __SSearchBlackRoleInfo__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class SSearchBlackRoleInfo extends __SSearchBlackRoleInfo__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 819146;

	public int getType() {
		return 819146;
	}

	public fire.pb.pingbi.SearchBlackRoleInfo searchblackrole;

	public SSearchBlackRoleInfo() {
		searchblackrole = new fire.pb.pingbi.SearchBlackRoleInfo();
	}

	public SSearchBlackRoleInfo(fire.pb.pingbi.SearchBlackRoleInfo _searchblackrole_) {
		this.searchblackrole = _searchblackrole_;
	}

	public final boolean _validator_() {
		if (!searchblackrole._validator_()) return false;
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(searchblackrole);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		searchblackrole.unmarshal(_os_);
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof SSearchBlackRoleInfo) {
			SSearchBlackRoleInfo _o_ = (SSearchBlackRoleInfo)_o1_;
			if (!searchblackrole.equals(_o_.searchblackrole)) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += searchblackrole.hashCode();
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(searchblackrole).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

