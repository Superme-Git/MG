
package fire.pb.npc;

import fire.pb.redirect.PExitCopyProc;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __CExitCopy__ extends mkio.Protocol { }

/** 离开副本
*/
// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class CExitCopy extends __CExitCopy__ {
	@Override
	protected void process() {
		
		final long roleid = gnet.link.Onlines.getInstance().findRoleid(this);
		if(roleid >0){
			new PExitCopyProc(roleid, gototype).submit();
		}
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 795668;

	public int getType() {
		return 795668;
	}

	public byte gototype; // 类型  0 正常退出; 1 跳转地图

	public CExitCopy() {
	}

	public CExitCopy(byte _gototype_) {
		this.gototype = _gototype_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(gototype);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		gototype = _os_.unmarshal_byte();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof CExitCopy) {
			CExitCopy _o_ = (CExitCopy)_o1_;
			if (gototype != _o_.gototype) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += (int)gototype;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(gototype).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(CExitCopy _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = gototype - _o_.gototype;
		if (0 != _c_) return _c_;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

