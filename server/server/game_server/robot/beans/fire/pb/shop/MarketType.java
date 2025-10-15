
package fire.pb.shop;

import com.locojoy.base.Marshal.Marshal;
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

/** 摆摊类型
*/
public class MarketType implements Marshal , Comparable<MarketType>{
	public final static int CARE_FOR = 1; // 关注
	public final static int RARITY_PET = 2; // 珍品宠物(珍品)
	public final static int RARITY_EQUIP = 3; // 珍品装备(珍品)
	public final static int RARITY_PET_BOOK = 4; // 高级兽决(珍品)
	public final static int NORMAL_PET_BOOK = 5; // 低级兽决
	public final static int NORMAL_PHARMACEUTICAL = 6; // 药品
	public final static int COOKING = 7; // 烹饪
	public final static int ZHI_ZAO_FU = 8; // 制造符
	public final static int LIN_SHI_FU = 9; // 临时符
	public final static int TASK_ITEM = 10; // 任务道具
	public final static int PET_SHOP = 11; // 奇珍异宝
	public final static int RARITY_EQUIP_FORGING = 12; // 装备打造(珍品)


	public MarketType() {
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
		if (_o1_ instanceof MarketType) {
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

	public int compareTo(MarketType _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		return _c_;
	}

}

