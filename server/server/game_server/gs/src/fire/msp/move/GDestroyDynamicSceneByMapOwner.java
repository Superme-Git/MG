
package fire.msp.move;

import fire.pb.scene.DuplicateHelper;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __GDestroyDynamicSceneByMapOwner__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class GDestroyDynamicSceneByMapOwner extends __GDestroyDynamicSceneByMapOwner__ {
	@Override
	protected void process() {
		// protocol handle
		DuplicateHelper.destroyDynamicScene(basemapid, ownerid);
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 724908;

	public int getType() {
		return 724908;
	}

	public int basemapid; // 副本的地图ID
	public long ownerid; // 副本的所有者ID(可能是角色ID,也可能是团体ID)

	public GDestroyDynamicSceneByMapOwner() {
	}

	public GDestroyDynamicSceneByMapOwner(int _basemapid_, long _ownerid_) {
		this.basemapid = _basemapid_;
		this.ownerid = _ownerid_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(basemapid);
		_os_.marshal(ownerid);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		basemapid = _os_.unmarshal_int();
		ownerid = _os_.unmarshal_long();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof GDestroyDynamicSceneByMapOwner) {
			GDestroyDynamicSceneByMapOwner _o_ = (GDestroyDynamicSceneByMapOwner)_o1_;
			if (basemapid != _o_.basemapid) return false;
			if (ownerid != _o_.ownerid) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += basemapid;
		_h_ += (int)ownerid;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(basemapid).append(",");
		_sb_.append(ownerid).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(GDestroyDynamicSceneByMapOwner _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = basemapid - _o_.basemapid;
		if (0 != _c_) return _c_;
		_c_ = Long.signum(ownerid - _o_.ownerid);
		if (0 != _c_) return _c_;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

