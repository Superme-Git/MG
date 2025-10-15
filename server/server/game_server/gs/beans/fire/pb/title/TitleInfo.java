
package fire.pb.title;

import com.locojoy.base.Marshal.Marshal;
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

/** 称谓 详细信息
*/
public class TitleInfo implements Marshal {
	public int titleid;
	public java.lang.String name;
	public long availtime;

	public TitleInfo() {
		name = "";
	}

	public TitleInfo(int _titleid_, java.lang.String _name_, long _availtime_) {
		this.titleid = _titleid_;
		this.name = _name_;
		this.availtime = _availtime_;
	}

	public final boolean _validator_() {
		if (titleid < 0) return false;
		if (availtime < -1) return false;
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		_os_.marshal(titleid);
		_os_.marshal(name, "UTF-16LE");
		_os_.marshal(availtime);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		titleid = _os_.unmarshal_int();
		name = _os_.unmarshal_String("UTF-16LE");
		availtime = _os_.unmarshal_long();
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof TitleInfo) {
			TitleInfo _o_ = (TitleInfo)_o1_;
			if (titleid != _o_.titleid) return false;
			if (!name.equals(_o_.name)) return false;
			if (availtime != _o_.availtime) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += titleid;
		_h_ += name.hashCode();
		_h_ += (int)availtime;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(titleid).append(",");
		_sb_.append("T").append(name.length()).append(",");
		_sb_.append(availtime).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

}

