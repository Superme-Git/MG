
package fire.pb.master;

import com.locojoy.base.Marshal.Marshal;
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public class Achive implements Marshal , Comparable<Achive>{
	public int currnumber; // 当前次数或者进度
	public int totalnum; // 总量 超过这个就表示玩家已经完成
	public int flag; // 0=未完成 1=完成 2=已经领奖

	public Achive() {
	}

	public Achive(int _currnumber_, int _totalnum_, int _flag_) {
		this.currnumber = _currnumber_;
		this.totalnum = _totalnum_;
		this.flag = _flag_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		_os_.marshal(currnumber);
		_os_.marshal(totalnum);
		_os_.marshal(flag);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		currnumber = _os_.unmarshal_int();
		totalnum = _os_.unmarshal_int();
		flag = _os_.unmarshal_int();
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof Achive) {
			Achive _o_ = (Achive)_o1_;
			if (currnumber != _o_.currnumber) return false;
			if (totalnum != _o_.totalnum) return false;
			if (flag != _o_.flag) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += currnumber;
		_h_ += totalnum;
		_h_ += flag;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(currnumber).append(",");
		_sb_.append(totalnum).append(",");
		_sb_.append(flag).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(Achive _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = currnumber - _o_.currnumber;
		if (0 != _c_) return _c_;
		_c_ = totalnum - _o_.totalnum;
		if (0 != _c_) return _c_;
		_c_ = flag - _o_.flag;
		if (0 != _c_) return _c_;
		return _c_;
	}

}

