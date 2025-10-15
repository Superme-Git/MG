
package fire.pb;

import com.locojoy.base.Marshal.Marshal;
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public class AU2GameType implements Marshal , Comparable<AU2GameType>{
	public final static int INVITE_FRIEND = 1; // 游戏内发邮件邀请好友回归
	public final static int RECOMMEND_FRIEND = 2; // 推荐好友
	public final static int PRESEND_CARD = 3; // 礼品卡
	public final static int QUICK_PAY = 4; // 快捷支付
	public final static int VIP_INFO = 5; // VIP信息获取
	public final static int QUICK_PAY_REQUEST = 6; // 快捷支付请求


	public AU2GameType() {
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
		if (_o1_ instanceof AU2GameType) {
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

	public int compareTo(AU2GameType _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		return _c_;
	}

}

