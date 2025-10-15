
package fire.msp.npc;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __MRemoveNpcsFromGS__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class MRemoveNpcsFromGS extends __MRemoveNpcsFromGS__ {
	@Override
	protected void process() {
		fire.pb.npc.PRemoveNpcFromGS procedure = new fire.pb.npc.PRemoveNpcFromGS(npckeys,false);
		procedure.trace = "MRemoveNpcsFromGS";
		
		if (mkdb.Transaction.current() != null) {
			procedure.call();
		}
		else {
			procedure.submit();
		}
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 730429;

	public int getType() {
		return 730429;
	}

	public java.util.LinkedList<Long> npckeys;

	public MRemoveNpcsFromGS() {
		npckeys = new java.util.LinkedList<Long>();
	}

	public MRemoveNpcsFromGS(java.util.LinkedList<Long> _npckeys_) {
		this.npckeys = _npckeys_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.compact_uint32(npckeys.size());
		for (Long _v_ : npckeys) {
			_os_.marshal(_v_);
		}
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		for (int _size_ = _os_.uncompact_uint32(); _size_ > 0; --_size_) {
			long _v_;
			_v_ = _os_.unmarshal_long();
			npckeys.add(_v_);
		}
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof MRemoveNpcsFromGS) {
			MRemoveNpcsFromGS _o_ = (MRemoveNpcsFromGS)_o1_;
			if (!npckeys.equals(_o_.npckeys)) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += npckeys.hashCode();
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(npckeys).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

