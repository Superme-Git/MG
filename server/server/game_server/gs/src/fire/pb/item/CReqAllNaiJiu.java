
package fire.pb.item;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __CReqAllNaiJiu__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class CReqAllNaiJiu extends __CReqAllNaiJiu__ {
	@Override
	protected void process() {
		// protocol handle
		final long roleId = gnet.link.Onlines.getInstance().findRoleid(this);
		if(roleId < 0) {
			return;
		}
		
		new PRefreshNaiJiu(this.packid, roleId).submit();
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 787654;

	public int getType() {
		return 787654;
	}

	public int packid;

	public CReqAllNaiJiu() {
	}

	public CReqAllNaiJiu(int _packid_) {
		this.packid = _packid_;
	}

	public final boolean _validator_() {
		if (packid < 1) return false;
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(packid);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		packid = _os_.unmarshal_int();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof CReqAllNaiJiu) {
			CReqAllNaiJiu _o_ = (CReqAllNaiJiu)_o1_;
			if (packid != _o_.packid) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += packid;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(packid).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(CReqAllNaiJiu _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = packid - _o_.packid;
		if (0 != _c_) return _c_;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

