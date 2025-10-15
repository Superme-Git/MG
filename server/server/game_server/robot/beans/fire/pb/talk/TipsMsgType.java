
package fire.pb.talk;

import com.locojoy.base.Marshal.Marshal;
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public class TipsMsgType implements Marshal , Comparable<TipsMsgType>{
	public final static int TIPS_POPMSG = 1; // 透明框提示
	public final static int TIPS_NPCTALK = 2; // npc对话框提示
	public final static int TIPS_MSG_CHANNEL = 3; // 消息频道提示
	public final static int TIPS_SYSBOARD = 4; // 系统公告提示
	public final static int TIPS_CONFIRM = 5; // 确认框提示
	public final static int TIPS_CLAN = 7; // 公会频道提示
	public final static int TIPS_CUR_CHANNEL = 8; // 当前频道提示
	public final static int TIPS_WORLD = 9; // 世界频道提示
	public final static int TIPS_TEAM_CHANNEL = 13; // 队伍频道提示
	public final static int TIPS_PRO_CHANNEL = 14; // 职业频道提示
	public final static int TIPS_SYS_CHANNEL = 15; // 系统频道提示
	public final static int TIPS_ROLE_CHANNEL = 18; // 跑马灯提示


	public TipsMsgType() {
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
		if (_o1_ instanceof TipsMsgType) {
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

	public int compareTo(TipsMsgType _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		return _c_;
	}

}

