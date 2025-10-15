
package gnet;

import com.locojoy.base.Marshal.Marshal;
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public class SetMaxOnlineNumRes implements Marshal , Comparable<SetMaxOnlineNumRes>{
	public int retcode;

	public SetMaxOnlineNumRes() {
	}

	public SetMaxOnlineNumRes(int _retcode_) {
		this.retcode = _retcode_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		_os_.marshal(retcode);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		retcode = _os_.unmarshal_int();
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof SetMaxOnlineNumRes) {
			SetMaxOnlineNumRes _o_ = (SetMaxOnlineNumRes)_o1_;
			if (retcode != _o_.retcode) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += retcode;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(retcode).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(SetMaxOnlineNumRes _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = retcode - _o_.retcode;
		if (0 != _c_) return _c_;
		return _c_;
	}

}

