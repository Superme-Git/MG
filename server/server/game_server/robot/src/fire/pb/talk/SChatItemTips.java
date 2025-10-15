
package fire.pb.talk;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __SChatItemTips__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class SChatItemTips extends __SChatItemTips__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 792446;

	public int getType() {
		return 792446;
	}

	public fire.pb.talk.DisplayInfo displayinfo;
	public com.locojoy.base.Octets tips;

	public SChatItemTips() {
		displayinfo = new fire.pb.talk.DisplayInfo();
		tips = new com.locojoy.base.Octets();
	}

	public SChatItemTips(fire.pb.talk.DisplayInfo _displayinfo_, com.locojoy.base.Octets _tips_) {
		this.displayinfo = _displayinfo_;
		this.tips = _tips_;
	}

	public final boolean _validator_() {
		if (!displayinfo._validator_()) return false;
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(displayinfo);
		_os_.marshal(tips);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		displayinfo.unmarshal(_os_);
		tips = _os_.unmarshal_Octets();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof SChatItemTips) {
			SChatItemTips _o_ = (SChatItemTips)_o1_;
			if (!displayinfo.equals(_o_.displayinfo)) return false;
			if (!tips.equals(_o_.tips)) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += displayinfo.hashCode();
		_h_ += tips.hashCode();
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(displayinfo).append(",");
		_sb_.append("B").append(tips.size()).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

