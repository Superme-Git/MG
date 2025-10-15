
package fire.pb.huoban;

import com.locojoy.base.Marshal.Marshal;
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public class ZhenrongInfo implements Marshal {
	public int zhenfa; // 光环编号
	public java.util.ArrayList<Integer> huobanlist; // 参战伙伴

	public ZhenrongInfo() {
		huobanlist = new java.util.ArrayList<Integer>();
	}

	public ZhenrongInfo(int _zhenfa_, java.util.ArrayList<Integer> _huobanlist_) {
		this.zhenfa = _zhenfa_;
		this.huobanlist = _huobanlist_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		_os_.marshal(zhenfa);
		_os_.compact_uint32(huobanlist.size());
		for (Integer _v_ : huobanlist) {
			_os_.marshal(_v_);
		}
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		zhenfa = _os_.unmarshal_int();
		for (int _size_ = _os_.uncompact_uint32(); _size_ > 0; --_size_) {
			int _v_;
			_v_ = _os_.unmarshal_int();
			huobanlist.add(_v_);
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof ZhenrongInfo) {
			ZhenrongInfo _o_ = (ZhenrongInfo)_o1_;
			if (zhenfa != _o_.zhenfa) return false;
			if (!huobanlist.equals(_o_.huobanlist)) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += zhenfa;
		_h_ += huobanlist.hashCode();
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(zhenfa).append(",");
		_sb_.append(huobanlist).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

}

