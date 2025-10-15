
package fire.pb.skill;

import com.locojoy.base.Marshal.Marshal;
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

/** 装备上的技能
*/
public class EquipSkill implements Marshal , Comparable<EquipSkill>{
	public int skill; // 特技id
	public int effect; // 特效id

	public EquipSkill() {
	}

	public EquipSkill(int _skill_, int _effect_) {
		this.skill = _skill_;
		this.effect = _effect_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		_os_.marshal(skill);
		_os_.marshal(effect);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		skill = _os_.unmarshal_int();
		effect = _os_.unmarshal_int();
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof EquipSkill) {
			EquipSkill _o_ = (EquipSkill)_o1_;
			if (skill != _o_.skill) return false;
			if (effect != _o_.effect) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += skill;
		_h_ += effect;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(skill).append(",");
		_sb_.append(effect).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(EquipSkill _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = skill - _o_.skill;
		if (0 != _c_) return _c_;
		_c_ = effect - _o_.effect;
		if (0 != _c_) return _c_;
		return _c_;
	}

}

