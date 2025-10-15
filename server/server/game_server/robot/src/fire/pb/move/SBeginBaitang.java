
package fire.pb.move;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __SBeginBaitang__ extends mkio.Protocol { }

/** 开始婚礼，里边带着新郎新娘的id
*/
// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class SBeginBaitang extends __SBeginBaitang__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 790483;

	public int getType() {
		return 790483;
	}

	public long roleid1;
	public long roleid2;

	public SBeginBaitang() {
	}

	public SBeginBaitang(long _roleid1_, long _roleid2_) {
		this.roleid1 = _roleid1_;
		this.roleid2 = _roleid2_;
	}

	public final boolean _validator_() {
		if (roleid1 < 1) return false;
		if (roleid2 < 1) return false;
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(roleid1);
		_os_.marshal(roleid2);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		roleid1 = _os_.unmarshal_long();
		roleid2 = _os_.unmarshal_long();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof SBeginBaitang) {
			SBeginBaitang _o_ = (SBeginBaitang)_o1_;
			if (roleid1 != _o_.roleid1) return false;
			if (roleid2 != _o_.roleid2) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += (int)roleid1;
		_h_ += (int)roleid2;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(roleid1).append(",");
		_sb_.append(roleid2).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(SBeginBaitang _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = Long.signum(roleid1 - _o_.roleid1);
		if (0 != _c_) return _c_;
		_c_ = Long.signum(roleid2 - _o_.roleid2);
		if (0 != _c_) return _c_;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

