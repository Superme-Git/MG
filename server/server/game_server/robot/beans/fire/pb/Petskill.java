
package fire.pb;

import com.locojoy.base.Marshal.Marshal;
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public class Petskill implements Marshal , Comparable<Petskill>{
	public int skillid;
	public int skillexp;
	public byte skilltype; // 0=先天技能  1=通过打书学习的技能
	public byte certification; // 0=未认证 1=认证

	public Petskill() {
	}

	public Petskill(int _skillid_, int _skillexp_, byte _skilltype_, byte _certification_) {
		this.skillid = _skillid_;
		this.skillexp = _skillexp_;
		this.skilltype = _skilltype_;
		this.certification = _certification_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		_os_.marshal(skillid);
		_os_.marshal(skillexp);
		_os_.marshal(skilltype);
		_os_.marshal(certification);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		skillid = _os_.unmarshal_int();
		skillexp = _os_.unmarshal_int();
		skilltype = _os_.unmarshal_byte();
		certification = _os_.unmarshal_byte();
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof Petskill) {
			Petskill _o_ = (Petskill)_o1_;
			if (skillid != _o_.skillid) return false;
			if (skillexp != _o_.skillexp) return false;
			if (skilltype != _o_.skilltype) return false;
			if (certification != _o_.certification) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += skillid;
		_h_ += skillexp;
		_h_ += (int)skilltype;
		_h_ += (int)certification;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(skillid).append(",");
		_sb_.append(skillexp).append(",");
		_sb_.append(skilltype).append(",");
		_sb_.append(certification).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(Petskill _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = skillid - _o_.skillid;
		if (0 != _c_) return _c_;
		_c_ = skillexp - _o_.skillexp;
		if (0 != _c_) return _c_;
		_c_ = skilltype - _o_.skilltype;
		if (0 != _c_) return _c_;
		_c_ = certification - _o_.certification;
		if (0 != _c_) return _c_;
		return _c_;
	}

}

