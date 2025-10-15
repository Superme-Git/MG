
package fire.pb;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __CSetMaxScreenShowNum__ extends mkio.Protocol { }

/** 客户端自动更改同屏显示人数
*/
// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class CSetMaxScreenShowNum extends __CSetMaxScreenShowNum__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 786495;

	public int getType() {
		return 786495;
	}

	public short maxscreenshownum;

	public CSetMaxScreenShowNum() {
	}

	public CSetMaxScreenShowNum(short _maxscreenshownum_) {
		this.maxscreenshownum = _maxscreenshownum_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(maxscreenshownum);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		maxscreenshownum = _os_.unmarshal_short();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof CSetMaxScreenShowNum) {
			CSetMaxScreenShowNum _o_ = (CSetMaxScreenShowNum)_o1_;
			if (maxscreenshownum != _o_.maxscreenshownum) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += maxscreenshownum;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(maxscreenshownum).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(CSetMaxScreenShowNum _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = maxscreenshownum - _o_.maxscreenshownum;
		if (0 != _c_) return _c_;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

