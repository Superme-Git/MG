
package fire.pb.move;

import com.locojoy.base.Marshal.Marshal;
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public class SpriteComponents implements Marshal , Comparable<SpriteComponents>{
	public final static int SPRITE_WEAPON = 1; // 武器
	public final static int SPRITE_HEADDRESS = 2; // 头饰
	public final static int SPRITE_BACKDRESS = 3; // 背饰
	public final static int SPRITE_FACEDRESS1 = 4; // 面饰1
	public final static int SPRITE_FACEDRESS2 = 5; // 面饰2
	public final static int SPRITE_HORSEDRESS = 6; // 坐骑
	public final static int SPRITE_WEAPONCOLOR = 7; // 武器颜色	武器颜色	byte 1为白色，2为绿色。。4为紫色 5为橙色
	public final static int SPRITE_FASHION = 8; // 时装
	public final static int ROLE_COLOR1 = 50; // 染色部位1
	public final static int ROLE_COLOR2 = 51; // 染色部位2
	public final static int SPRITE_EQUIP_EFFECT = 60; // 装备特效


	public SpriteComponents() {
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
		if (_o1_ instanceof SpriteComponents) {
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

	public int compareTo(SpriteComponents _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		return _c_;
	}

}

