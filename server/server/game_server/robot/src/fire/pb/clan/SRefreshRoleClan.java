
package fire.pb.clan;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __SRefreshRoleClan__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class SRefreshRoleClan extends __SRefreshRoleClan__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 808519;

	public int getType() {
		return 808519;
	}

	public long clankey; // 公会key 大于0表示有公会
	public java.lang.String clanname; // 公会名称

	public SRefreshRoleClan() {
		clanname = "";
	}

	public SRefreshRoleClan(long _clankey_, java.lang.String _clanname_) {
		this.clankey = _clankey_;
		this.clanname = _clanname_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(clankey);
		_os_.marshal(clanname, "UTF-16LE");
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		clankey = _os_.unmarshal_long();
		clanname = _os_.unmarshal_String("UTF-16LE");
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof SRefreshRoleClan) {
			SRefreshRoleClan _o_ = (SRefreshRoleClan)_o1_;
			if (clankey != _o_.clankey) return false;
			if (!clanname.equals(_o_.clanname)) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += (int)clankey;
		_h_ += clanname.hashCode();
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(clankey).append(",");
		_sb_.append("T").append(clanname.length()).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

