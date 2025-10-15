
package fire.pb.school;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __SSendShouxiInfo__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class SSendShouxiInfo extends __SSendShouxiInfo__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 810433;

	public int getType() {
		return 810433;
	}

	public fire.pb.school.ShouxiInfo shouxi;
	public long shouxikey;

	public SSendShouxiInfo() {
		shouxi = new fire.pb.school.ShouxiInfo();
	}

	public SSendShouxiInfo(fire.pb.school.ShouxiInfo _shouxi_, long _shouxikey_) {
		this.shouxi = _shouxi_;
		this.shouxikey = _shouxikey_;
	}

	public final boolean _validator_() {
		if (!shouxi._validator_()) return false;
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(shouxi);
		_os_.marshal(shouxikey);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		shouxi.unmarshal(_os_);
		shouxikey = _os_.unmarshal_long();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof SSendShouxiInfo) {
			SSendShouxiInfo _o_ = (SSendShouxiInfo)_o1_;
			if (!shouxi.equals(_o_.shouxi)) return false;
			if (shouxikey != _o_.shouxikey) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += shouxi.hashCode();
		_h_ += (int)shouxikey;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(shouxi).append(",");
		_sb_.append(shouxikey).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

