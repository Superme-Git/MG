
package cross;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __BroadcastData__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class BroadcastData extends __BroadcastData__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 232;

	public int getType() {
		return 232;
	}

	public int dtype;
	public com.locojoy.base.Octets content;

	public BroadcastData() {
		content = new com.locojoy.base.Octets();
	}

	public BroadcastData(int _dtype_, com.locojoy.base.Octets _content_) {
		this.dtype = _dtype_;
		this.content = _content_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(dtype);
		_os_.marshal(content);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		dtype = _os_.unmarshal_int();
		content = _os_.unmarshal_Octets();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof BroadcastData) {
			BroadcastData _o_ = (BroadcastData)_o1_;
			if (dtype != _o_.dtype) return false;
			if (!content.equals(_o_.content)) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += dtype;
		_h_ += content.hashCode();
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(dtype).append(",");
		_sb_.append("B").append(content.size()).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

