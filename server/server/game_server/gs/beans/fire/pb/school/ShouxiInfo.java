
package fire.pb.school;

import com.locojoy.base.Marshal.Marshal;
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public class ShouxiInfo implements Marshal , Comparable<ShouxiInfo>{
	public int maxhp;
	public int maxmp;
	public int hitrate;
	public int attack; // 攻击
	public int defend;
	public int magicattack; // 法术攻击
	public int magicdef; // 法术防御
	public int speed;
	public int dodge; // 躲闪

	public ShouxiInfo() {
	}

	public ShouxiInfo(int _maxhp_, int _maxmp_, int _hitrate_, int _attack_, int _defend_, int _magicattack_, int _magicdef_, int _speed_, int _dodge_) {
		this.maxhp = _maxhp_;
		this.maxmp = _maxmp_;
		this.hitrate = _hitrate_;
		this.attack = _attack_;
		this.defend = _defend_;
		this.magicattack = _magicattack_;
		this.magicdef = _magicdef_;
		this.speed = _speed_;
		this.dodge = _dodge_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		_os_.marshal(maxhp);
		_os_.marshal(maxmp);
		_os_.marshal(hitrate);
		_os_.marshal(attack);
		_os_.marshal(defend);
		_os_.marshal(magicattack);
		_os_.marshal(magicdef);
		_os_.marshal(speed);
		_os_.marshal(dodge);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		maxhp = _os_.unmarshal_int();
		maxmp = _os_.unmarshal_int();
		hitrate = _os_.unmarshal_int();
		attack = _os_.unmarshal_int();
		defend = _os_.unmarshal_int();
		magicattack = _os_.unmarshal_int();
		magicdef = _os_.unmarshal_int();
		speed = _os_.unmarshal_int();
		dodge = _os_.unmarshal_int();
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof ShouxiInfo) {
			ShouxiInfo _o_ = (ShouxiInfo)_o1_;
			if (maxhp != _o_.maxhp) return false;
			if (maxmp != _o_.maxmp) return false;
			if (hitrate != _o_.hitrate) return false;
			if (attack != _o_.attack) return false;
			if (defend != _o_.defend) return false;
			if (magicattack != _o_.magicattack) return false;
			if (magicdef != _o_.magicdef) return false;
			if (speed != _o_.speed) return false;
			if (dodge != _o_.dodge) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += maxhp;
		_h_ += maxmp;
		_h_ += hitrate;
		_h_ += attack;
		_h_ += defend;
		_h_ += magicattack;
		_h_ += magicdef;
		_h_ += speed;
		_h_ += dodge;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(maxhp).append(",");
		_sb_.append(maxmp).append(",");
		_sb_.append(hitrate).append(",");
		_sb_.append(attack).append(",");
		_sb_.append(defend).append(",");
		_sb_.append(magicattack).append(",");
		_sb_.append(magicdef).append(",");
		_sb_.append(speed).append(",");
		_sb_.append(dodge).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(ShouxiInfo _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = maxhp - _o_.maxhp;
		if (0 != _c_) return _c_;
		_c_ = maxmp - _o_.maxmp;
		if (0 != _c_) return _c_;
		_c_ = hitrate - _o_.hitrate;
		if (0 != _c_) return _c_;
		_c_ = attack - _o_.attack;
		if (0 != _c_) return _c_;
		_c_ = defend - _o_.defend;
		if (0 != _c_) return _c_;
		_c_ = magicattack - _o_.magicattack;
		if (0 != _c_) return _c_;
		_c_ = magicdef - _o_.magicdef;
		if (0 != _c_) return _c_;
		_c_ = speed - _o_.speed;
		if (0 != _c_) return _c_;
		_c_ = dodge - _o_.dodge;
		if (0 != _c_) return _c_;
		return _c_;
	}

}

