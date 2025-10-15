
package fire.pb.triggers;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __CNpcToTrigger__ extends mkio.Protocol { }

/** 客户端请求npc触发
*/
// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class CNpcToTrigger extends __CNpcToTrigger__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 817932;

	public int getType() {
		return 817932;
	}

	public int triggerid;
	public long npckey;

	public CNpcToTrigger() {
	}

	public CNpcToTrigger(int _triggerid_, long _npckey_) {
		this.triggerid = _triggerid_;
		this.npckey = _npckey_;
	}

	public final boolean _validator_() {
		if (triggerid <= 0) return false;
		if (npckey <= 0) return false;
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(triggerid);
		_os_.marshal(npckey);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		triggerid = _os_.unmarshal_int();
		npckey = _os_.unmarshal_long();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof CNpcToTrigger) {
			CNpcToTrigger _o_ = (CNpcToTrigger)_o1_;
			if (triggerid != _o_.triggerid) return false;
			if (npckey != _o_.npckey) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += triggerid;
		_h_ += (int)npckey;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(triggerid).append(",");
		_sb_.append(npckey).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(CNpcToTrigger _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = triggerid - _o_.triggerid;
		if (0 != _c_) return _c_;
		_c_ = Long.signum(npckey - _o_.npckey);
		if (0 != _c_) return _c_;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

