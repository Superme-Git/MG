
package fire.pb.clan;

import com.locojoy.base.Marshal.Marshal;
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public class ClanPositionType implements Marshal , Comparable<ClanPositionType>{
	public final static int ClanMember = 11; // 成员
	public final static int ClanArmyGroupElite4 = 10; // 第四军团精英
	public final static int ClanArmyGroupElite3 = 9; // 第三军团精英
	public final static int ClanArmyGroupElite2 = 8; // 第二军团精英
	public final static int ClanArmyGroupElite1 = 7; // 第一军团精英
	public final static int ClanArmyGroup4 = 6; // 第四军团长
	public final static int ClanArmyGroup3 = 5; // 第三军团长
	public final static int ClanArmyGroup2 = 4; // 第二军团长
	public final static int ClanArmyGroup1 = 3; // 第一军团长
	public final static int ClanViceMaster = 2; // 副会长
	public final static int ClanMaster = 1; // 会长


	public ClanPositionType() {
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
		if (_o1_ instanceof ClanPositionType) {
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

	public int compareTo(ClanPositionType _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		return _c_;
	}

}

