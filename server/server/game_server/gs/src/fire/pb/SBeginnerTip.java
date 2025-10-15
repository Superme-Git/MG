
package fire.pb;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __SBeginnerTip__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class SBeginnerTip extends __SBeginnerTip__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 786458;

	public int getType() {
		return 786458;
	}

	public int tipid; // id为BeginnerTipType
	public int tipvalue; // 0为未显示,1为已显示

	public SBeginnerTip() {
	}

	public SBeginnerTip(int _tipid_, int _tipvalue_) {
		this.tipid = _tipid_;
		this.tipvalue = _tipvalue_;
	}

	public final boolean _validator_() {
		if (tipvalue < 0 || tipvalue > 1) return false;
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(tipid);
		_os_.marshal(tipvalue);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		tipid = _os_.unmarshal_int();
		tipvalue = _os_.unmarshal_int();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof SBeginnerTip) {
			SBeginnerTip _o_ = (SBeginnerTip)_o1_;
			if (tipid != _o_.tipid) return false;
			if (tipvalue != _o_.tipvalue) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += tipid;
		_h_ += tipvalue;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(tipid).append(",");
		_sb_.append(tipvalue).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(SBeginnerTip _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = tipid - _o_.tipid;
		if (0 != _c_) return _c_;
		_c_ = tipvalue - _o_.tipvalue;
		if (0 != _c_) return _c_;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

