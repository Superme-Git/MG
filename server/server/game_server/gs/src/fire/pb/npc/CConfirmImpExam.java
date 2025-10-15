
package fire.pb.npc;

import fire.pb.activity.impexam.PConfirmProc;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __CConfirmImpExam__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class CConfirmImpExam extends __CConfirmImpExam__ {
	@Override
	protected void process() {
		//这个protocal在全村统考,全省统考,全国统考都会用到.
		final long roleid = gnet.link.Onlines.getInstance().findRoleid(this);
		if(roleid >0){
			new PConfirmProc(roleid, impexamtype, operate).submit();
		}
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 795459;

	public int getType() {
		return 795459;
	}

	public int impexamtype; // 考试类型
	public byte operate; // 0=愿意  1=放弃参加

	public CConfirmImpExam() {
	}

	public CConfirmImpExam(int _impexamtype_, byte _operate_) {
		this.impexamtype = _impexamtype_;
		this.operate = _operate_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(impexamtype);
		_os_.marshal(operate);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		impexamtype = _os_.unmarshal_int();
		operate = _os_.unmarshal_byte();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof CConfirmImpExam) {
			CConfirmImpExam _o_ = (CConfirmImpExam)_o1_;
			if (impexamtype != _o_.impexamtype) return false;
			if (operate != _o_.operate) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += impexamtype;
		_h_ += (int)operate;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(impexamtype).append(",");
		_sb_.append(operate).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(CConfirmImpExam _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = impexamtype - _o_.impexamtype;
		if (0 != _c_) return _c_;
		_c_ = operate - _o_.operate;
		if (0 != _c_) return _c_;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

