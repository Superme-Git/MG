
package fire.msp;

import com.locojoy.base.Marshal.Marshal;
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public class showpetinfo implements Marshal {
	public int petid;
	public long petkey;
	public java.lang.String petname;
	public int color;
	public int bodysize;
	public int showeffect; // 展示的特效

	public showpetinfo() {
		petname = "";
	}

	public showpetinfo(int _petid_, long _petkey_, java.lang.String _petname_, int _color_, int _bodysize_, int _showeffect_) {
		this.petid = _petid_;
		this.petkey = _petkey_;
		this.petname = _petname_;
		this.color = _color_;
		this.bodysize = _bodysize_;
		this.showeffect = _showeffect_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		_os_.marshal(petid);
		_os_.marshal(petkey);
		_os_.marshal(petname, "UTF-16LE");
		_os_.marshal(color);
		_os_.marshal(bodysize);
		_os_.marshal(showeffect);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		petid = _os_.unmarshal_int();
		petkey = _os_.unmarshal_long();
		petname = _os_.unmarshal_String("UTF-16LE");
		color = _os_.unmarshal_int();
		bodysize = _os_.unmarshal_int();
		showeffect = _os_.unmarshal_int();
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof showpetinfo) {
			showpetinfo _o_ = (showpetinfo)_o1_;
			if (petid != _o_.petid) return false;
			if (petkey != _o_.petkey) return false;
			if (!petname.equals(_o_.petname)) return false;
			if (color != _o_.color) return false;
			if (bodysize != _o_.bodysize) return false;
			if (showeffect != _o_.showeffect) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += petid;
		_h_ += (int)petkey;
		_h_ += petname.hashCode();
		_h_ += color;
		_h_ += bodysize;
		_h_ += showeffect;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(petid).append(",");
		_sb_.append(petkey).append(",");
		_sb_.append("T").append(petname.length()).append(",");
		_sb_.append(color).append(",");
		_sb_.append(bodysize).append(",");
		_sb_.append(showeffect).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

}

