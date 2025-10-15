
package gnet;

import com.locojoy.base.Marshal.Marshal;
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public class SetMaxOnlineNumArg implements Marshal , Comparable<SetMaxOnlineNumArg>{
	public int maxnum;
	public int fake_maxnum;

	public SetMaxOnlineNumArg() {
	}

	public SetMaxOnlineNumArg(int _maxnum_, int _fake_maxnum_) {
		this.maxnum = _maxnum_;
		this.fake_maxnum = _fake_maxnum_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		_os_.marshal(maxnum);
		_os_.marshal(fake_maxnum);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		maxnum = _os_.unmarshal_int();
		fake_maxnum = _os_.unmarshal_int();
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof SetMaxOnlineNumArg) {
			SetMaxOnlineNumArg _o_ = (SetMaxOnlineNumArg)_o1_;
			if (maxnum != _o_.maxnum) return false;
			if (fake_maxnum != _o_.fake_maxnum) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += maxnum;
		_h_ += fake_maxnum;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(maxnum).append(",");
		_sb_.append(fake_maxnum).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(SetMaxOnlineNumArg _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = maxnum - _o_.maxnum;
		if (0 != _c_) return _c_;
		_c_ = fake_maxnum - _o_.fake_maxnum;
		if (0 != _c_) return _c_;
		return _c_;
	}

}

