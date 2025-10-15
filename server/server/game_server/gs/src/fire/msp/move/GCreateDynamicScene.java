
package fire.msp.move;

import fire.pb.scene.Scene;
import fire.pb.scene.SceneFactory;
import fire.pb.scene.SceneManager;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __GCreateDynamicScene__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class GCreateDynamicScene extends __GCreateDynamicScene__ {
	@Override
	protected void process() {
		Scene myScene = SceneManager.getInstance().getSceneByMapAndOwner(mapid, ownerid);

		if (null == myScene){
			myScene = SceneFactory.createDynamicScene(mapid, ownerid, ownername,livetime,scenetype,parameters);
		}
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 724924;

	public int getType() {
		return 724924;
	}

	public int mapid; // 地图id
	public long ownerid; // 副本的所有者ID(可能是角色ID,也可能是团体ID)
	public java.lang.String ownername;
	public byte reusable; // 副本对象是否可重用(角色都离开后是否可重进入,还是重新创建新的对象)
	public int livetime; // 空场景存活时间，单位秒。可重用副本时，等于0则一直存活不销毁；不可重用的副本时，此字段无效
	public int scenetype; // 场景类型
	public com.locojoy.base.Octets parameters; // 创建副本用到的参数，默认为DynamicSceneParams类型，不同的副本用到的参数可能不同，所以用octets

	public GCreateDynamicScene() {
		ownername = "";
		parameters = new com.locojoy.base.Octets();
	}

	public GCreateDynamicScene(int _mapid_, long _ownerid_, java.lang.String _ownername_, byte _reusable_, int _livetime_, int _scenetype_, com.locojoy.base.Octets _parameters_) {
		this.mapid = _mapid_;
		this.ownerid = _ownerid_;
		this.ownername = _ownername_;
		this.reusable = _reusable_;
		this.livetime = _livetime_;
		this.scenetype = _scenetype_;
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
		_os_.marshal(ownername, "UTF-16LE");
		_os_.marshal(reusable);
		_os_.marshal(livetime);
		_os_.marshal(scenetype);
		_os_.marshal(parameters);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		mapid = _os_.unmarshal_int();
		ownerid = _os_.unmarshal_long();
		ownername = _os_.unmarshal_String("UTF-16LE");
		reusable = _os_.unmarshal_byte();
		livetime = _os_.unmarshal_int();
		scenetype = _os_.unmarshal_int();
		parameters = _os_.unmarshal_Octets();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof GCreateDynamicScene) {
			GCreateDynamicScene _o_ = (GCreateDynamicScene)_o1_;
			if (mapid != _o_.mapid) return false;
			if (ownerid != _o_.ownerid) return false;
			if (!ownername.equals(_o_.ownername)) return false;
			if (reusable != _o_.reusable) return false;
			if (livetime != _o_.livetime) return false;
			if (scenetype != _o_.scenetype) return false;
			if (!parameters.equals(_o_.parameters)) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += mapid;
		_h_ += (int)ownerid;
		_h_ += ownername.hashCode();
		_h_ += (int)reusable;
		_h_ += livetime;
		_h_ += scenetype;
		_h_ += parameters.hashCode();
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(mapid).append(",");
		_sb_.append(ownerid).append(",");
		_sb_.append("T").append(ownername.length()).append(",");
		_sb_.append(reusable).append(",");
		_sb_.append(livetime).append(",");
		_sb_.append(scenetype).append(",");
		_sb_.append("B").append(parameters.size()).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

