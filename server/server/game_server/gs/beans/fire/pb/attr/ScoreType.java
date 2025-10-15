
package fire.pb.attr;

import com.locojoy.base.Marshal.Marshal;
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public class ScoreType implements Marshal , Comparable<ScoreType>{
	public final static int ROLE_LEVEL_SCORE = 1; // 角色等级能力
	public final static int EQUIP_LEVEL_SCORE = 2; // 装备一级属性
	public final static int EQUIP_EFFCT = 3; // 装备特效
	public final static int EQUIP_SKILL = 4; // 装备特技
	public final static int EQUIP_GEM = 5; // 每个宝石
	public final static int SCHOOL_SKILL = 6; // 职业技能
	public final static int GUILD_SKILL = 7; // 公会技能
	public final static int GUILD_SHAVE = 8; // 公会修炼
	public final static int PET_LEVEL_SCORE = 9; // 宠物一级属性
	public final static int PET_LOW_SKILL = 10; // 宠物低级技能
	public final static int PET_HIGH_SKILL = 11; // 宠物高级技能
	public final static int PET_GROWING = 12; // 宠物成长


	public ScoreType() {
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof ScoreType) {
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(ScoreType _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		return _c_;
	}

}

