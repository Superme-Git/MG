
package fire.msp;

import gnet.link.Onlines;
import fire.pb.scene.Scene;
import fire.pb.scene.SceneManager;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __GSendAllByScene__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class GSendAllByScene extends __GSendAllByScene__ {
	@Override
	protected void process() {
		// protocol handle
		Scene scene = SceneManager.getInstance().getSceneByID(sceneid);
		if(scene == null)
			return;
		Onlines.getInstance().sendOctets(scene.getSceneRoles().keySet(), protype, protocol);
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 720902;

	public int getType() {
		return 720902;
	}

	public int protype; // 协议的类型
	public com.locojoy.base.Octets protocol; // 协议marshal完的数据
	public long sceneid; // 场景ID

	public GSendAllByScene() {
		protocol = new com.locojoy.base.Octets();
	}

	public GSendAllByScene(int _protype_, com.locojoy.base.Octets _protocol_, long _sceneid_) {
		this.protype = _protype_;
		this.protocol = _protocol_;
		this.sceneid = _sceneid_;
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
		_os_.marshal(sceneid);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		protype = _os_.unmarshal_int();
		protocol = _os_.unmarshal_Octets();
		sceneid = _os_.unmarshal_long();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof GSendAllByScene) {
			GSendAllByScene _o_ = (GSendAllByScene)_o1_;
			if (protype != _o_.protype) return false;
			if (!protocol.equals(_o_.protocol)) return false;
			if (sceneid != _o_.sceneid) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += protype;
		_h_ += protocol.hashCode();
		_h_ += (int)sceneid;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(protype).append(",");
		_sb_.append("B").append(protocol.size()).append(",");
		_sb_.append(sceneid).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

