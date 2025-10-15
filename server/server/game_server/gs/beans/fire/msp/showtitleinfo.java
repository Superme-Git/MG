
package fire.msp;

import com.locojoy.base.Marshal.Marshal;
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public class showtitleinfo implements Marshal {
	public int titleid;
	public java.lang.String titlename;
	public long validtime;

	public showtitleinfo() {
		titlename = "";
	}

	public showtitleinfo(int _titleid_, java.lang.String _titlename_, long _validtime_) {
		this.titleid = _titleid_;
		this.titlename = _titlename_;
		this.validtime = _validtime_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		_os_.marshal(titleid);
		_os_.marshal(titlename, "UTF-16LE");
		_os_.marshal(validtime);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		titleid = _os_.unmarshal_int();
		titlename = _os_.unmarshal_String("UTF-16LE");
		validtime = _os_.unmarshal_long();
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof showtitleinfo) {
			showtitleinfo _o_ = (showtitleinfo)_o1_;
			if (titleid != _o_.titleid) return false;
			if (!titlename.equals(_o_.titlename)) return false;
			if (validtime != _o_.validtime) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += titleid;
		_h_ += titlename.hashCode();
		_h_ += (int)validtime;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(titleid).append(",");
		_sb_.append("T").append(titlename.length()).append(",");
		_sb_.append(validtime).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

}

