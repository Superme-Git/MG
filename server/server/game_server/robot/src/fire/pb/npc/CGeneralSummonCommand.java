
package fire.pb.npc;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __CGeneralSummonCommand__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class CGeneralSummonCommand extends __CGeneralSummonCommand__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 795506;

	public int getType() {
		return 795506;
	}

	public int summontype; // 拉人类型 见TransmitTypes
	public long npckey; // npckey
	public int agree; // 1为同意，0为拒绝

	public CGeneralSummonCommand() {
	}

	public CGeneralSummonCommand(int _summontype_, long _npckey_, int _agree_) {
		this.summontype = _summontype_;
		this.npckey = _npckey_;
		this.agree = _agree_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(summontype);
		_os_.marshal(npckey);
		_os_.marshal(agree);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		summontype = _os_.unmarshal_int();
		npckey = _os_.unmarshal_long();
		agree = _os_.unmarshal_int();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof CGeneralSummonCommand) {
			CGeneralSummonCommand _o_ = (CGeneralSummonCommand)_o1_;
			if (summontype != _o_.summontype) return false;
			if (npckey != _o_.npckey) return false;
			if (agree != _o_.agree) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += summontype;
		_h_ += (int)npckey;
		_h_ += agree;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(summontype).append(",");
		_sb_.append(npckey).append(",");
		_sb_.append(agree).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(CGeneralSummonCommand _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = summontype - _o_.summontype;
		if (0 != _c_) return _c_;
		_c_ = Long.signum(npckey - _o_.npckey);
		if (0 != _c_) return _c_;
		_c_ = agree - _o_.agree;
		if (0 != _c_) return _c_;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

