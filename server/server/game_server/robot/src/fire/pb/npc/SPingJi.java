
package fire.pb.npc;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __SPingJi__ extends mkio.Protocol { }

/** 通知客户端评级 以及获得的奖励
*/
// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class SPingJi extends __SPingJi__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 795667;

	public int getType() {
		return 795667;
	}

	public byte grade; // 等级  1=s 2=a 3=b 4=c
	public int exp; // 经验

	public SPingJi() {
	}

	public SPingJi(byte _grade_, int _exp_) {
		this.grade = _grade_;
		this.exp = _exp_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(grade);
		_os_.marshal(exp);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		grade = _os_.unmarshal_byte();
		exp = _os_.unmarshal_int();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof SPingJi) {
			SPingJi _o_ = (SPingJi)_o1_;
			if (grade != _o_.grade) return false;
			if (exp != _o_.exp) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += (int)grade;
		_h_ += exp;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(grade).append(",");
		_sb_.append(exp).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(SPingJi _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = grade - _o_.grade;
		if (0 != _c_) return _c_;
		_c_ = exp - _o_.exp;
		if (0 != _c_) return _c_;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

