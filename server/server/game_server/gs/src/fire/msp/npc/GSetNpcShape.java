
package fire.msp.npc;

import fire.pb.scene.manager.SceneNpcManager;
import fire.pb.scene.movable.NPC;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __GSetNpcShape__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class GSetNpcShape extends __GSetNpcShape__ {
	@Override
	protected void process() {
		NPC npc=SceneNpcManager.getInstance().getNpcByKey(npckey);
		npc.setShape(shape);
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 730411;

	public int getType() {
		return 730411;
	}

	public long npckey;
	public int shape;

	public GSetNpcShape() {
	}

	public GSetNpcShape(long _npckey_, int _shape_) {
		this.npckey = _npckey_;
		this.shape = _shape_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(npckey);
		_os_.marshal(shape);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		npckey = _os_.unmarshal_long();
		shape = _os_.unmarshal_int();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof GSetNpcShape) {
			GSetNpcShape _o_ = (GSetNpcShape)_o1_;
			if (npckey != _o_.npckey) return false;
			if (shape != _o_.shape) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += (int)npckey;
		_h_ += shape;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(npckey).append(",");
		_sb_.append(shape).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(GSetNpcShape _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = Long.signum(npckey - _o_.npckey);
		if (0 != _c_) return _c_;
		_c_ = shape - _o_.shape;
		if (0 != _c_) return _c_;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

