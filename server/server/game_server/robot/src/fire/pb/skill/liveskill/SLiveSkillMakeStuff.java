
package fire.pb.skill.liveskill;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __SLiveSkillMakeStuff__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class SLiveSkillMakeStuff extends __SLiveSkillMakeStuff__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 800518;

	public int getType() {
		return 800518;
	}

	public int ret; // 返回结果0是制作成功1是制作失败了 by changhao

	public SLiveSkillMakeStuff() {
	}

	public SLiveSkillMakeStuff(int _ret_) {
		this.ret = _ret_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(ret);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		ret = _os_.unmarshal_int();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof SLiveSkillMakeStuff) {
			SLiveSkillMakeStuff _o_ = (SLiveSkillMakeStuff)_o1_;
			if (ret != _o_.ret) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += ret;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(ret).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(SLiveSkillMakeStuff _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = ret - _o_.ret;
		if (0 != _c_) return _c_;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

