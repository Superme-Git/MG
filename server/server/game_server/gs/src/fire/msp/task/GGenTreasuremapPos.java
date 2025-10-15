
package fire.msp.task;

import fire.pb.scene.MapUtil;
import fire.pb.scene.SceneClient;
import fire.pb.scene.sPos.GridPos;
// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __GGenTreasuremapPos__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class GGenTreasuremapPos extends __GGenTreasuremapPos__ {
	@Override
	protected void process() {
		// protocol handle
		MGenTreasuremapPos mgtp = new MGenTreasuremapPos();
		mgtp.mapid = mapid;
		mgtp.roleid = roleid;
		mgtp.bagid = bagid;
		mgtp.key = key;
		
		GridPos pos = MapUtil.genPosInMap(mapid);
		mgtp.posx = pos.getX();
		mgtp.posy = pos.getY();
		
		SceneClient.pSend(mgtp);
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 739901;

	public int getType() {
		return 739901;
	}

	public long roleid; // 玩家id
	public int bagid; // bagid
	public int key; // key
	public int mapid;

	public GGenTreasuremapPos() {
	}

	public GGenTreasuremapPos(long _roleid_, int _bagid_, int _key_, int _mapid_) {
		this.roleid = _roleid_;
		this.bagid = _bagid_;
		this.key = _key_;
		this.mapid = _mapid_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(roleid);
		_os_.marshal(bagid);
		_os_.marshal(key);
		_os_.marshal(mapid);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		roleid = _os_.unmarshal_long();
		bagid = _os_.unmarshal_int();
		key = _os_.unmarshal_int();
		mapid = _os_.unmarshal_int();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof GGenTreasuremapPos) {
			GGenTreasuremapPos _o_ = (GGenTreasuremapPos)_o1_;
			if (roleid != _o_.roleid) return false;
			if (bagid != _o_.bagid) return false;
			if (key != _o_.key) return false;
			if (mapid != _o_.mapid) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += (int)roleid;
		_h_ += bagid;
		_h_ += key;
		_h_ += mapid;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(roleid).append(",");
		_sb_.append(bagid).append(",");
		_sb_.append(key).append(",");
		_sb_.append(mapid).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(GGenTreasuremapPos _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = Long.signum(roleid - _o_.roleid);
		if (0 != _c_) return _c_;
		_c_ = bagid - _o_.bagid;
		if (0 != _c_) return _c_;
		_c_ = key - _o_.key;
		if (0 != _c_) return _c_;
		_c_ = mapid - _o_.mapid;
		if (0 != _c_) return _c_;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

