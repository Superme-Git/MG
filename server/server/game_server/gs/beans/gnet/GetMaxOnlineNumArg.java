
package gnet;

import com.locojoy.base.Marshal.Marshal;
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public class GetMaxOnlineNumArg implements Marshal , Comparable<GetMaxOnlineNumArg>{
	public int padding;

	public GetMaxOnlineNumArg() {
	}

	public GetMaxOnlineNumArg(int _padding_) {
		this.padding = _padding_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		_os_.marshal(padding);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		padding = _os_.unmarshal_int();
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof GetMaxOnlineNumArg) {
			GetMaxOnlineNumArg _o_ = (GetMaxOnlineNumArg)_o1_;
			if (padding != _o_.padding) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += padding;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(padding).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(GetMaxOnlineNumArg _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = padding - _o_.padding;
		if (0 != _c_) return _c_;
		return _c_;
	}

}

