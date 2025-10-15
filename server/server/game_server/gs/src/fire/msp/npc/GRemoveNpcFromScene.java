
package fire.msp.npc;

import fire.pb.scene.manager.SceneNpcManager;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __GRemoveNpcFromScene__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class GRemoveNpcFromScene extends __GRemoveNpcFromScene__ {
	@Override
	protected void process() {
		// protocol handle
		SceneNpcManager.getInstance().removeNpcFromScene(npckey,trace);
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 730401;

	public int getType() {
		return 730401;
	}

	public long npckey;
	public java.lang.String trace;

	public GRemoveNpcFromScene() {
		trace = "";
	}

	public GRemoveNpcFromScene(long _npckey_, java.lang.String _trace_) {
		this.npckey = _npckey_;
		this.trace = _trace_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(npckey);
		_os_.marshal(trace, "UTF-16LE");
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		npckey = _os_.unmarshal_long();
		trace = _os_.unmarshal_String("UTF-16LE");
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof GRemoveNpcFromScene) {
			GRemoveNpcFromScene _o_ = (GRemoveNpcFromScene)_o1_;
			if (npckey != _o_.npckey) return false;
			if (!trace.equals(_o_.trace)) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += (int)npckey;
		_h_ += trace.hashCode();
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(npckey).append(",");
		_sb_.append("T").append(trace.length()).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

