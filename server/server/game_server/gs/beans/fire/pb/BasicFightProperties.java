
package fire.pb;

import com.locojoy.base.Marshal.Marshal;
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

/** 基础战斗属性
*/
public class BasicFightProperties implements Marshal , Comparable<BasicFightProperties>{
	public short cons; // 体质
	public short iq; // 智力
	public short str; // 力量
	public short endu; // 耐力
	public short agi; // 敏捷

	public BasicFightProperties() {
	}

	public BasicFightProperties(short _cons_, short _iq_, short _str_, short _endu_, short _agi_) {
		this.cons = _cons_;
		this.iq = _iq_;
		this.str = _str_;
		this.endu = _endu_;
		this.agi = _agi_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		_os_.marshal(cons);
		_os_.marshal(iq);
		_os_.marshal(str);
		_os_.marshal(endu);
		_os_.marshal(agi);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		cons = _os_.unmarshal_short();
		iq = _os_.unmarshal_short();
		str = _os_.unmarshal_short();
		endu = _os_.unmarshal_short();
		agi = _os_.unmarshal_short();
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof BasicFightProperties) {
			BasicFightProperties _o_ = (BasicFightProperties)_o1_;
			if (cons != _o_.cons) return false;
			if (iq != _o_.iq) return false;
			if (str != _o_.str) return false;
			if (endu != _o_.endu) return false;
			if (agi != _o_.agi) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += cons;
		_h_ += iq;
		_h_ += str;
		_h_ += endu;
		_h_ += agi;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(cons).append(",");
		_sb_.append(iq).append(",");
		_sb_.append(str).append(",");
		_sb_.append(endu).append(",");
		_sb_.append(agi).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(BasicFightProperties _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = cons - _o_.cons;
		if (0 != _c_) return _c_;
		_c_ = iq - _o_.iq;
		if (0 != _c_) return _c_;
		_c_ = str - _o_.str;
		if (0 != _c_) return _c_;
		_c_ = endu - _o_.endu;
		if (0 != _c_) return _c_;
		_c_ = agi - _o_.agi;
		if (0 != _c_) return _c_;
		return _c_;
	}

}

