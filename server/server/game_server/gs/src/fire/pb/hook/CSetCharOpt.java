
package fire.pb.hook;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __CSetCharOpt__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class CSetCharOpt extends __CSetCharOpt__ {
	@Override
	protected void process() {
		// protocol handle
		final long roleid = gnet.link.Onlines.getInstance().findRoleid(this);
		if(roleid <= 0) 
			return;
		
		if (charoptype > 0)
			new PReqSetHookDataProc(roleid, (short)HookDataUpdateEnum.HOOKDATA_CHAROPTYPE, (long)charoptype).submit();
		if(charopid > 0)
			new PReqSetHookDataProc(roleid, (short)HookDataUpdateEnum.HOOKDATA_CHAROPID, (long)charopid).submit();
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 810335;

	public int getType() {
		return 810335;
	}

	public short charoptype; // 设置人物操作类型
	public int charopid; // 设置人物操作id

	public CSetCharOpt() {
	}

	public CSetCharOpt(short _charoptype_, int _charopid_) {
		this.charoptype = _charoptype_;
		this.charopid = _charopid_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(charoptype);
		_os_.marshal(charopid);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		charoptype = _os_.unmarshal_short();
		charopid = _os_.unmarshal_int();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof CSetCharOpt) {
			CSetCharOpt _o_ = (CSetCharOpt)_o1_;
			if (charoptype != _o_.charoptype) return false;
			if (charopid != _o_.charopid) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += charoptype;
		_h_ += charopid;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(charoptype).append(",");
		_sb_.append(charopid).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(CSetCharOpt _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = charoptype - _o_.charoptype;
		if (0 != _c_) return _c_;
		_c_ = charopid - _o_.charopid;
		if (0 != _c_) return _c_;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

