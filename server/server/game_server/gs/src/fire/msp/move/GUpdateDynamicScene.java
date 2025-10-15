
package fire.msp.move;

import fire.pb.scene.DynamicScene;
import fire.pb.scene.Scene;
import fire.pb.scene.SceneManager;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __GUpdateDynamicScene__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class GUpdateDynamicScene extends __GUpdateDynamicScene__ {
	@Override
	protected void process() {
		// protocol handle
		Scene scene = SceneManager.getInstance().getSceneByMapAndOwner(mapid, ownerid);
		if(scene == null)
			return;
		if(!(scene instanceof DynamicScene))
			return;
		((DynamicScene)scene).update(parameters);
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 724921;

	public int getType() {
		return 724921;
	}

	public int mapid; // 地图id
	public long ownerid; // 副本的所有者ID(可能是角色ID,也可能是团体ID)
	public com.locojoy.base.Octets parameters; // 更新副本用到的参数，因为不同的副本用到的参数可能不同，自己unmarshal

	public GUpdateDynamicScene() {
		parameters = new com.locojoy.base.Octets();
	}

	public GUpdateDynamicScene(int _mapid_, long _ownerid_, com.locojoy.base.Octets _parameters_) {
		this.mapid = _mapid_;
		this.ownerid = _ownerid_;
		this.parameters = _parameters_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(mapid);
		_os_.marshal(ownerid);
		_os_.marshal(parameters);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		mapid = _os_.unmarshal_int();
		ownerid = _os_.unmarshal_long();
		parameters = _os_.unmarshal_Octets();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof GUpdateDynamicScene) {
			GUpdateDynamicScene _o_ = (GUpdateDynamicScene)_o1_;
			if (mapid != _o_.mapid) return false;
			if (ownerid != _o_.ownerid) return false;
			if (!parameters.equals(_o_.parameters)) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += mapid;
		_h_ += (int)ownerid;
		_h_ += parameters.hashCode();
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(mapid).append(",");
		_sb_.append(ownerid).append(",");
		_sb_.append("B").append(parameters.size()).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

