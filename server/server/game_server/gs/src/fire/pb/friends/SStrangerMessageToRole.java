
package fire.pb.friends;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __SStrangerMessageToRole__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class SStrangerMessageToRole extends __SStrangerMessageToRole__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 806443;

	public int getType() {
		return 806443;
	}

	public fire.pb.friends.StrangerMessageBean strangermessage;

	public SStrangerMessageToRole() {
		strangermessage = new fire.pb.friends.StrangerMessageBean();
	}

	public SStrangerMessageToRole(fire.pb.friends.StrangerMessageBean _strangermessage_) {
		this.strangermessage = _strangermessage_;
	}

	public final boolean _validator_() {
		if (!strangermessage._validator_()) return false;
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(strangermessage);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		strangermessage.unmarshal(_os_);
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof SStrangerMessageToRole) {
			SStrangerMessageToRole _o_ = (SStrangerMessageToRole)_o1_;
			if (!strangermessage.equals(_o_.strangermessage)) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += strangermessage.hashCode();
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(strangermessage).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

