
package fire.pb.master;

import com.locojoy.base.Marshal.Marshal;
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public class PrenticeData implements Marshal {
	public long roleid; // 徒弟的roleid
	public java.lang.String rolename; // 徒弟的名字
	public int flag; // 这个徒弟是否免费罢免 1=免费 0=收费

	public PrenticeData() {
		rolename = "";
	}

	public PrenticeData(long _roleid_, java.lang.String _rolename_, int _flag_) {
		this.roleid = _roleid_;
		this.rolename = _rolename_;
		this.flag = _flag_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		_os_.marshal(roleid);
		_os_.marshal(rolename, "UTF-16LE");
		_os_.marshal(flag);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		roleid = _os_.unmarshal_long();
		rolename = _os_.unmarshal_String("UTF-16LE");
		flag = _os_.unmarshal_int();
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof PrenticeData) {
			PrenticeData _o_ = (PrenticeData)_o1_;
			if (roleid != _o_.roleid) return false;
			if (!rolename.equals(_o_.rolename)) return false;
			if (flag != _o_.flag) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += (int)roleid;
		_h_ += rolename.hashCode();
		_h_ += flag;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(roleid).append(",");
		_sb_.append("T").append(rolename.length()).append(",");
		_sb_.append(flag).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

}

