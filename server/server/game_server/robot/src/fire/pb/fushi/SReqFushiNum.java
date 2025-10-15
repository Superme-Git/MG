
package fire.pb.fushi;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __SReqFushiNum__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class SReqFushiNum extends __SReqFushiNum__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 812442;

	public int getType() {
		return 812442;
	}

	public int num; // 普通符石的数量
	public int bindnum; // 绑定符石的数量
	public int totalnum; // 总的充值符石数量 不包含赠送的 by changhao

	public SReqFushiNum() {
	}

	public SReqFushiNum(int _num_, int _bindnum_, int _totalnum_) {
		this.num = _num_;
		this.bindnum = _bindnum_;
		this.totalnum = _totalnum_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(num);
		_os_.marshal(bindnum);
		_os_.marshal(totalnum);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		num = _os_.unmarshal_int();
		bindnum = _os_.unmarshal_int();
		totalnum = _os_.unmarshal_int();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof SReqFushiNum) {
			SReqFushiNum _o_ = (SReqFushiNum)_o1_;
			if (num != _o_.num) return false;
			if (bindnum != _o_.bindnum) return false;
			if (totalnum != _o_.totalnum) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += num;
		_h_ += bindnum;
		_h_ += totalnum;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(num).append(",");
		_sb_.append(bindnum).append(",");
		_sb_.append(totalnum).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(SReqFushiNum _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = num - _o_.num;
		if (0 != _c_) return _c_;
		_c_ = bindnum - _o_.bindnum;
		if (0 != _c_) return _c_;
		_c_ = totalnum - _o_.totalnum;
		if (0 != _c_) return _c_;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

