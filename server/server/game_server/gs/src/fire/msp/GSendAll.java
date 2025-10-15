
package fire.msp;

import fire.pb.scene.SceneManager;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __GSendAll__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class GSendAll extends __GSendAll__ {
	@Override
	protected void process() {
		
		SceneManager.getInstance().sendOctetsAll(protype, protocol);
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 720900;

	public int getType() {
		return 720900;
	}

	public int protype; // 协议的类型
	public com.locojoy.base.Octets protocol; // 协议marshal完的数据

	public GSendAll() {
		protocol = new com.locojoy.base.Octets();
	}

	public GSendAll(int _protype_, com.locojoy.base.Octets _protocol_) {
		this.protype = _protype_;
		this.protocol = _protocol_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(protype);
		_os_.marshal(protocol);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		protype = _os_.unmarshal_int();
		protocol = _os_.unmarshal_Octets();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof GSendAll) {
			GSendAll _o_ = (GSendAll)_o1_;
			if (protype != _o_.protype) return false;
			if (!protocol.equals(_o_.protocol)) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += protype;
		_h_ += protocol.hashCode();
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(protype).append(",");
		_sb_.append("B").append(protocol.size()).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

