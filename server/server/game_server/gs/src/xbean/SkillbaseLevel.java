
package xbean;

import com.locojoy.base.Marshal.Marshal;
import com.locojoy.base.Marshal.MarshalException;
import com.locojoy.base.Marshal.OctetsStream;

public class SkillbaseLevel implements Marshal, Comparable<SkillbaseLevel> {

	private int level; // 

	public SkillbaseLevel() {
	}

	public SkillbaseLevel(int level) {
		this.level = level;
	}

	public int getLevel() { // 
		return level;
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_os_.marshal(level);
		return _os_;
	}

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		level = _os_.unmarshal_int();
		return _os_;
	}

	@Override
	public int compareTo(SkillbaseLevel _o_) {
		if (_o_ == this)
			return 0;
		int _c_ = 0;
		_c_ = Integer.signum(level - _o_.level);
		if (0 != _c_) return _c_;
		return _c_;
	}

	@Override
	public boolean equals(Object _o_) {
		if (_o_ instanceof SkillbaseLevel)
			return 0 == this.compareTo((SkillbaseLevel)_o_);
		return false;
	}

	@Override
	public int hashCode() {
		int _h_ = 0;
		_h_ += level;
		return _h_;
	}

}
