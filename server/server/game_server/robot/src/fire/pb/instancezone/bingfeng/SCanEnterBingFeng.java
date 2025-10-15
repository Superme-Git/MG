
package fire.pb.instancezone.bingfeng;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __SCanEnterBingFeng__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class SCanEnterBingFeng extends __SCanEnterBingFeng__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 804562;

	public int getType() {
		return 804562;
	}

	public int finish; // 是否已经完成

	public SCanEnterBingFeng() {
	}

	public SCanEnterBingFeng(int _finish_) {
		this.finish = _finish_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(finish);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		finish = _os_.unmarshal_int();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof SCanEnterBingFeng) {
			SCanEnterBingFeng _o_ = (SCanEnterBingFeng)_o1_;
			if (finish != _o_.finish) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += finish;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(finish).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(SCanEnterBingFeng _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = finish - _o_.finish;
		if (0 != _c_) return _c_;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

