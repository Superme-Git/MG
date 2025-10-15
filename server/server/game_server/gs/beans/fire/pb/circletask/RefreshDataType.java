
package fire.pb.circletask;

import com.locojoy.base.Marshal.Marshal;
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

/** 刷新任务的数据字段枚举值
*/
public class RefreshDataType implements Marshal , Comparable<RefreshDataType>{
	public final static int STATE = 1; // 任务状态
	public final static int DEST_NPD_KEY = 2; // _目的npckey
	public final static int DEST_NPD_ID = 3; // 目的npcid
	public final static int DEST_MAP_ID = 4; // 目的地图id
	public final static int DEST_XPOS = 5; // 目的x坐标
	public final static int DEST_YPOS = 6; // 目的y坐标
	public final static int DEST_ITEM_ID = 7; // 目的道具id
	public final static int SUMNUM = 8; // 累计次数
	public final static int DEST_ITEM1_NUM = 9; // 目的道具1的数量
	public final static int DEST_ITEM2_ID = 10; // 目的道具2id
	public final static int DEST_ITEM2_NUM = 11; // 目的道具2的数量
	public final static int QUEST_TYPE = 12; // 任务类型


	public RefreshDataType() {
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
		if (_o1_ instanceof RefreshDataType) {
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

	public int compareTo(RefreshDataType _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		return _c_;
	}

}

