
package fire.msp;

import com.locojoy.base.Marshal.Marshal;
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public class modeinfo implements Marshal , Comparable<modeinfo>{
	public int modeid;
	public int transformid;
	public int follownpc;

	public modeinfo() {
	}

	public modeinfo(int _modeid_, int _transformid_, int _follownpc_) {
		this.modeid = _modeid_;
		this.transformid = _transformid_;
		this.follownpc = _follownpc_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		_os_.marshal(modeid);
		_os_.marshal(transformid);
		_os_.marshal(follownpc);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		modeid = _os_.unmarshal_int();
		transformid = _os_.unmarshal_int();
		follownpc = _os_.unmarshal_int();
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof modeinfo) {
			modeinfo _o_ = (modeinfo)_o1_;
			if (modeid != _o_.modeid) return false;
			if (transformid != _o_.transformid) return false;
			if (follownpc != _o_.follownpc) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += modeid;
		_h_ += transformid;
		_h_ += follownpc;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(modeid).append(",");
		_sb_.append(transformid).append(",");
		_sb_.append(follownpc).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(modeinfo _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = modeid - _o_.modeid;
		if (0 != _c_) return _c_;
		_c_ = transformid - _o_.transformid;
		if (0 != _c_) return _c_;
		_c_ = follownpc - _o_.follownpc;
		if (0 != _c_) return _c_;
		return _c_;
	}

}

