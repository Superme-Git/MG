
package fire.pb.game;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __SBeginXueYueWheel__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class SBeginXueYueWheel extends __SBeginXueYueWheel__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 810367;

	public int getType() {
		return 810367;
	}

	public int wheelindex;

	public SBeginXueYueWheel() {
	}

	public SBeginXueYueWheel(int _wheelindex_) {
		this.wheelindex = _wheelindex_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(wheelindex);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		wheelindex = _os_.unmarshal_int();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof SBeginXueYueWheel) {
			SBeginXueYueWheel _o_ = (SBeginXueYueWheel)_o1_;
			if (wheelindex != _o_.wheelindex) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += wheelindex;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(wheelindex).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(SBeginXueYueWheel _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = wheelindex - _o_.wheelindex;
		if (0 != _c_) return _c_;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

