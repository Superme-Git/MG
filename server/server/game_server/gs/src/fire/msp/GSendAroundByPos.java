
package fire.msp;

import java.util.Set;

import fire.pb.scene.Scene;
import fire.pb.scene.SceneManager;
import fire.pb.scene.sPos.Position;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __GSendAroundByPos__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class GSendAroundByPos extends __GSendAroundByPos__ {
	@Override
	protected void process() {
		// protocol handle
		Scene s = SceneManager.getInstance().getSceneByID(sceneid);
		if(s == null)
			return;
		Position pos = new fire.pb.scene.sPos.GridPos(gposx,gposy).toPosition();
		Set<Integer> aroundIndexs = s.getScreenPos(pos).getAroundScreenIndexs();
		if(aroundIndexs.isEmpty())
			return;
		Set<Long> roleids = s.getRoleInScreensByNum(aroundIndexs,Integer.MAX_VALUE, null).keySet();
		gnet.link.Onlines.getInstance().sendOctets(roleids, protype, protocol) ;
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 720910;

	public int getType() {
		return 720910;
	}

	public int protype; // 协议的类型
	public com.locojoy.base.Octets protocol; // 协议marshal完的数据
	public long sceneid; // 场景ID
	public short gposx; // 格子坐标
	public short gposy; // 格子坐标

	public GSendAroundByPos() {
		protocol = new com.locojoy.base.Octets();
	}

	public GSendAroundByPos(int _protype_, com.locojoy.base.Octets _protocol_, long _sceneid_, short _gposx_, short _gposy_) {
		this.protype = _protype_;
		this.protocol = _protocol_;
		this.sceneid = _sceneid_;
		this.gposx = _gposx_;
		this.gposy = _gposy_;
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
		_os_.marshal(gposx);
		_os_.marshal(gposy);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		protype = _os_.unmarshal_int();
		protocol = _os_.unmarshal_Octets();
		sceneid = _os_.unmarshal_long();
		gposx = _os_.unmarshal_short();
		gposy = _os_.unmarshal_short();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof GSendAroundByPos) {
			GSendAroundByPos _o_ = (GSendAroundByPos)_o1_;
			if (protype != _o_.protype) return false;
			if (!protocol.equals(_o_.protocol)) return false;
			if (sceneid != _o_.sceneid) return false;
			if (gposx != _o_.gposx) return false;
			if (gposy != _o_.gposy) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += protype;
		_h_ += protocol.hashCode();
		_h_ += (int)sceneid;
		_h_ += gposx;
		_h_ += gposy;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(protype).append(",");
		_sb_.append("B").append(protocol.size()).append(",");
		_sb_.append(sceneid).append(",");
		_sb_.append(gposx).append(",");
		_sb_.append(gposy).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

