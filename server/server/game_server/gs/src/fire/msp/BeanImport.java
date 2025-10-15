
package fire.msp;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __BeanImport__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class BeanImport extends __BeanImport__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 720896;

	public int getType() {
		return 720896;
	}

	public fire.msp.move.DynamicSceneParams b1;
	public fire.msp.team.TeamChangeType b2;

	public BeanImport() {
		b1 = new fire.msp.move.DynamicSceneParams();
		b2 = new fire.msp.team.TeamChangeType();
	}

	public BeanImport(fire.msp.move.DynamicSceneParams _b1_, fire.msp.team.TeamChangeType _b2_) {
		this.b1 = _b1_;
		this.b2 = _b2_;
	}

	public final boolean _validator_() {
		if (!b1._validator_()) return false;
		if (!b2._validator_()) return false;
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(b1);
		_os_.marshal(b2);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		b1.unmarshal(_os_);
		b2.unmarshal(_os_);
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof BeanImport) {
			BeanImport _o_ = (BeanImport)_o1_;
			if (!b1.equals(_o_.b1)) return false;
			if (!b2.equals(_o_.b2)) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += b1.hashCode();
		_h_ += b2.hashCode();
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(b1).append(",");
		_sb_.append(b2).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

