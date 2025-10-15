
package fire.msp.move;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __GRemoveNpc__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class GRemoveNpc extends __GRemoveNpc__ {
	@Override
	protected void process() {
		// protocol handle
		for ( long npc : npcs ) {
			fire.pb.scene.manager.SceneNpcManager.getInstance().removeNpcFromScene( npc,trace );
		}
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 724904;

	public int getType() {
		return 724904;
	}

	public java.util.LinkedList<Long> npcs;
	public java.lang.String trace;

	public GRemoveNpc() {
		npcs = new java.util.LinkedList<Long>();
		trace = "";
	}

	public GRemoveNpc(java.util.LinkedList<Long> _npcs_, java.lang.String _trace_) {
		this.npcs = _npcs_;
		this.trace = _trace_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.compact_uint32(npcs.size());
		for (Long _v_ : npcs) {
			_os_.marshal(_v_);
		}
		_os_.marshal(trace, "UTF-16LE");
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		for (int _size_ = _os_.uncompact_uint32(); _size_ > 0; --_size_) {
			long _v_;
			_v_ = _os_.unmarshal_long();
			npcs.add(_v_);
		}
		trace = _os_.unmarshal_String("UTF-16LE");
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof GRemoveNpc) {
			GRemoveNpc _o_ = (GRemoveNpc)_o1_;
			if (!npcs.equals(_o_.npcs)) return false;
			if (!trace.equals(_o_.trace)) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += npcs.hashCode();
		_h_ += trace.hashCode();
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(npcs).append(",");
		_sb_.append("T").append(trace.length()).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

