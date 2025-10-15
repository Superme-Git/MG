
package fire.pb.npc;

import com.locojoy.base.Marshal.Marshal;
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public class NpcServiceMappingTypes implements Marshal , Comparable<NpcServiceMappingTypes>{
	public final static int NONE = 0;
	public final static int ACCEPT_CIRCLE_TASK = 1; // 接任务
	public final static int SUBMIT_CIRCLE_TASK = 2; // 提交任务
	public final static int QUERY_CIRCLE_TASK = 3; // 查询任务
	public final static int QUERY_CIRCLE_TEAM = 4; // 便捷组队
	public final static int QUERY_CIRCLE_BATTLE = 5; // 进入战斗
	public final static int CIRCLE_PRODUCE = 6; // 打造物品
	public final static int RENXING_CIRCLE_TASK = 7; // 任性任务
	public final static int CHALLENGE_NPC = 9; // NPC挑战
	public final static int ENTER_INST = 10; // 进入副本
	public final static int POP_UI = 11; // 弹界面服务
	public final static int QUERY_CAMERA_URL = 12; // 请求录像路径
	public final static int ACCEPT_TUPO = 13; // 接突破任务


	public NpcServiceMappingTypes() {
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
		if (_o1_ instanceof NpcServiceMappingTypes) {
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

	public int compareTo(NpcServiceMappingTypes _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		return _c_;
	}

}

