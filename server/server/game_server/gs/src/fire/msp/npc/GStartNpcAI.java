
package fire.msp.npc;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __GStartNpcAI__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class GStartNpcAI extends __GStartNpcAI__ {
	@Override
	protected void process() {
		//fire.pb.scene.manager.NpcMoveAIManager.getInstance().npcMoveByAiID(npckey, aiid);
		fire.pb.scene.npcai.AIManager.getInstance().npcMoveByAiID(npckey, aiid, roleids);
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 730433;

	public int getType() {
		return 730433;
	}

	public long npckey;
	public int aiid;
	public java.util.HashSet<Long> roleids;

	public GStartNpcAI() {
		roleids = new java.util.HashSet<Long>();
	}

	public GStartNpcAI(long _npckey_, int _aiid_, java.util.HashSet<Long> _roleids_) {
		this.npckey = _npckey_;
		this.aiid = _aiid_;
		this.roleids = _roleids_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(npckey);
		_os_.marshal(aiid);
		_os_.compact_uint32(roleids.size());
		for (Long _v_ : roleids) {
			_os_.marshal(_v_);
		}
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		npckey = _os_.unmarshal_long();
		aiid = _os_.unmarshal_int();
		for (int _size_ = _os_.uncompact_uint32(); _size_ > 0; --_size_) {
			long _v_;
			_v_ = _os_.unmarshal_long();
			roleids.add(_v_);
		}
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof GStartNpcAI) {
			GStartNpcAI _o_ = (GStartNpcAI)_o1_;
			if (npckey != _o_.npckey) return false;
			if (aiid != _o_.aiid) return false;
			if (!roleids.equals(_o_.roleids)) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += (int)npckey;
		_h_ += aiid;
		_h_ += roleids.hashCode();
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(npckey).append(",");
		_sb_.append(aiid).append(",");
		_sb_.append(roleids).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

