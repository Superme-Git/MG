
package fire.msp.npc;
import gnet.link.Onlines;

import java.util.HashSet;
import java.util.Set;


import fire.pb.move.SRemoveActivityNpc;
import fire.pb.scene.Scene;
import fire.pb.scene.SceneManager;
import fire.pb.scene.movable.Role;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __GRemoveNpcFromMiniMap__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class GRemoveNpcFromMiniMap extends __GRemoveNpcFromMiniMap__ {
	@Override
	protected void process() {
		Scene scene=SceneManager.getInstance().getSceneByID(sceneid);
		Set<Long> roleids = getAllSceneRoles(scene);
		SRemoveActivityNpc sRemoveActivityNpc = new SRemoveActivityNpc(baseids);
		Onlines.getInstance().send(roleids, sRemoveActivityNpc);
	}
	private Set<Long> getAllSceneRoles(Scene scene) {
	       Set<Long> result = new HashSet<Long>();
	       for (Role role :  scene.getSceneRoles().values()) {
			      result.add(role.getRoleID());	
			}
			return result;
		}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 730410;

	public int getType() {
		return 730410;
	}

	public java.util.ArrayList<Integer> baseids;
	public long sceneid;

	public GRemoveNpcFromMiniMap() {
		baseids = new java.util.ArrayList<Integer>();
	}

	public GRemoveNpcFromMiniMap(java.util.ArrayList<Integer> _baseids_, long _sceneid_) {
		this.baseids = _baseids_;
		this.sceneid = _sceneid_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.compact_uint32(baseids.size());
		for (Integer _v_ : baseids) {
			_os_.marshal(_v_);
		}
		_os_.marshal(sceneid);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		for (int _size_ = _os_.uncompact_uint32(); _size_ > 0; --_size_) {
			int _v_;
			_v_ = _os_.unmarshal_int();
			baseids.add(_v_);
		}
		sceneid = _os_.unmarshal_long();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof GRemoveNpcFromMiniMap) {
			GRemoveNpcFromMiniMap _o_ = (GRemoveNpcFromMiniMap)_o1_;
			if (!baseids.equals(_o_.baseids)) return false;
			if (sceneid != _o_.sceneid) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += baseids.hashCode();
		_h_ += (int)sceneid;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(baseids).append(",");
		_sb_.append(sceneid).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

