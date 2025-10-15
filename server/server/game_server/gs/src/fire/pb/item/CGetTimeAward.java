
package fire.pb.item;


import fire.pb.item.onlinegift.PGetTimeAward;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __CGetTimeAward__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class CGetTimeAward extends __CGetTimeAward__ {
	@Override
	protected void process() {
		final long roleid = gnet.link.Onlines.getInstance().findRoleid(this);
		if (roleid == -1)
			return;
		new PGetTimeAward(roleid,awardid).submit();
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 787506;

	public int getType() {
		return 787506;
	}

	public int awardid;

	public CGetTimeAward() {
	}

	public CGetTimeAward(int _awardid_) {
		this.awardid = _awardid_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(awardid);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		awardid = _os_.unmarshal_int();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof CGetTimeAward) {
			CGetTimeAward _o_ = (CGetTimeAward)_o1_;
			if (awardid != _o_.awardid) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += awardid;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(awardid).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(CGetTimeAward _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = awardid - _o_.awardid;
		if (0 != _c_) return _c_;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

