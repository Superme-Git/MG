
package fire.pb.master;

import fire.pb.master.proc.PEvaluateProc;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __CEvaluate__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class CEvaluate extends __CEvaluate__ {
	@Override
	protected void process() {
		 final long rid = gnet.link.Onlines.getInstance().findRoleid(this);
		 if(rid > 0){
			new PEvaluateProc(rid, roleid, flag).submit();
		 }
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 816475;

	public int getType() {
		return 816475;
	}

	public int flag; // 1=对师傅评价 2=对徒弟的评价
	public long roleid; // 要评价的人
	public int result; // 评价

	public CEvaluate() {
	}

	public CEvaluate(int _flag_, long _roleid_, int _result_) {
		this.flag = _flag_;
		this.roleid = _roleid_;
		this.result = _result_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(flag);
		_os_.marshal(roleid);
		_os_.marshal(result);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		flag = _os_.unmarshal_int();
		roleid = _os_.unmarshal_long();
		result = _os_.unmarshal_int();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof CEvaluate) {
			CEvaluate _o_ = (CEvaluate)_o1_;
			if (flag != _o_.flag) return false;
			if (roleid != _o_.roleid) return false;
			if (result != _o_.result) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += flag;
		_h_ += (int)roleid;
		_h_ += result;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(flag).append(",");
		_sb_.append(roleid).append(",");
		_sb_.append(result).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(CEvaluate _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = flag - _o_.flag;
		if (0 != _c_) return _c_;
		_c_ = Long.signum(roleid - _o_.roleid);
		if (0 != _c_) return _c_;
		_c_ = result - _o_.result;
		if (0 != _c_) return _c_;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

