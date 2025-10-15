
package fire.pb.item;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __SRecoverItemInfo__ extends mkio.Protocol { }

/** 服务器返回一个找回道具的信息
*/
// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class SRecoverItemInfo extends __SRecoverItemInfo__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 787798;

	public int getType() {
		return 787798;
	}

	public long uniqid; // 唯一id
	public com.locojoy.base.Octets tips;

	public SRecoverItemInfo() {
		tips = new com.locojoy.base.Octets();
	}

	public SRecoverItemInfo(long _uniqid_, com.locojoy.base.Octets _tips_) {
		this.uniqid = _uniqid_;
		this.tips = _tips_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(uniqid);
		_os_.marshal(tips);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		uniqid = _os_.unmarshal_long();
		tips = _os_.unmarshal_Octets();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof SRecoverItemInfo) {
			SRecoverItemInfo _o_ = (SRecoverItemInfo)_o1_;
			if (uniqid != _o_.uniqid) return false;
			if (!tips.equals(_o_.tips)) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += (int)uniqid;
		_h_ += tips.hashCode();
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(uniqid).append(",");
		_sb_.append("B").append(tips.size()).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

