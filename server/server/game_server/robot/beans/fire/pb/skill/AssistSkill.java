
package fire.pb.skill;

import com.locojoy.base.Marshal.Marshal;
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

/** 辅助技能
*/
public class AssistSkill implements Marshal , Comparable<AssistSkill>{
	public int id; // 辅助技能ID
	public int level; // 级别
	public int exp; // 熟练度

	public AssistSkill() {
	}

	public AssistSkill(int _id_, int _level_, int _exp_) {
		this.id = _id_;
		this.level = _level_;
		this.exp = _exp_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		_os_.marshal(id);
		_os_.marshal(level);
		_os_.marshal(exp);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		id = _os_.unmarshal_int();
		level = _os_.unmarshal_int();
		exp = _os_.unmarshal_int();
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof AssistSkill) {
			AssistSkill _o_ = (AssistSkill)_o1_;
			if (id != _o_.id) return false;
			if (level != _o_.level) return false;
			if (exp != _o_.exp) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += id;
		_h_ += level;
		_h_ += exp;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(id).append(",");
		_sb_.append(level).append(",");
		_sb_.append(exp).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(AssistSkill _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = id - _o_.id;
		if (0 != _c_) return _c_;
		_c_ = level - _o_.level;
		if (0 != _c_) return _c_;
		_c_ = exp - _o_.exp;
		if (0 != _c_) return _c_;
		return _c_;
	}

}

