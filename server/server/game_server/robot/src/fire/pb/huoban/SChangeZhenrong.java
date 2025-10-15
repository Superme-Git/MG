
package fire.pb.huoban;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __SChangeZhenrong__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class SChangeZhenrong extends __SChangeZhenrong__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 818839;

	public int getType() {
		return 818839;
	}

	public int zhenrong; // 阵容编号
	public int zhenfa; // 光环编号
	public java.util.ArrayList<Integer> huobanlist; // 参战伙伴
	public int reason; // 更新原因 1-系统第一次自动更新 2-光环更新 3-参战伙伴更新 4-伙伴阵容切换

	public SChangeZhenrong() {
		huobanlist = new java.util.ArrayList<Integer>();
	}

	public SChangeZhenrong(int _zhenrong_, int _zhenfa_, java.util.ArrayList<Integer> _huobanlist_, int _reason_) {
		this.zhenrong = _zhenrong_;
		this.zhenfa = _zhenfa_;
		this.huobanlist = _huobanlist_;
		this.reason = _reason_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(zhenrong);
		_os_.marshal(zhenfa);
		_os_.compact_uint32(huobanlist.size());
		for (Integer _v_ : huobanlist) {
			_os_.marshal(_v_);
		}
		_os_.marshal(reason);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		zhenrong = _os_.unmarshal_int();
		zhenfa = _os_.unmarshal_int();
		for (int _size_ = _os_.uncompact_uint32(); _size_ > 0; --_size_) {
			int _v_;
			_v_ = _os_.unmarshal_int();
			huobanlist.add(_v_);
		}
		reason = _os_.unmarshal_int();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof SChangeZhenrong) {
			SChangeZhenrong _o_ = (SChangeZhenrong)_o1_;
			if (zhenrong != _o_.zhenrong) return false;
			if (zhenfa != _o_.zhenfa) return false;
			if (!huobanlist.equals(_o_.huobanlist)) return false;
			if (reason != _o_.reason) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += zhenrong;
		_h_ += zhenfa;
		_h_ += huobanlist.hashCode();
		_h_ += reason;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(zhenrong).append(",");
		_sb_.append(zhenfa).append(",");
		_sb_.append(huobanlist).append(",");
		_sb_.append(reason).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

