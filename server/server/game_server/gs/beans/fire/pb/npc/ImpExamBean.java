
package fire.pb.npc;

import com.locojoy.base.Marshal.Marshal;
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public class ImpExamBean implements Marshal , Comparable<ImpExamBean>{
	public int questionid; // 题目的id
	public int questionnum; // 这道题是第几题,从1开始计数
	public int righttimes; // 已答对题目数
	public long remaintime; // 距离活动结束时间（只有第一道题目给时间  以后不给时间）
	public byte lastright; // 上一题答对了还是答错了,1表示对,0表示错 -1=第一次发出来题目
	public int accuexp; // 本次累积获得多少经验
	public int accumoney; // 本次累积获得多少钱
	public int delwrongval; // 地王令
	public int chorightval; // 天王令
	public int helpcnt; // 求助次数

	public ImpExamBean() {
	}

	public ImpExamBean(int _questionid_, int _questionnum_, int _righttimes_, long _remaintime_, byte _lastright_, int _accuexp_, int _accumoney_, int _delwrongval_, int _chorightval_, int _helpcnt_) {
		this.questionid = _questionid_;
		this.questionnum = _questionnum_;
		this.righttimes = _righttimes_;
		this.remaintime = _remaintime_;
		this.lastright = _lastright_;
		this.accuexp = _accuexp_;
		this.accumoney = _accumoney_;
		this.delwrongval = _delwrongval_;
		this.chorightval = _chorightval_;
		this.helpcnt = _helpcnt_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		_os_.marshal(questionid);
		_os_.marshal(questionnum);
		_os_.marshal(righttimes);
		_os_.marshal(remaintime);
		_os_.marshal(lastright);
		_os_.marshal(accuexp);
		_os_.marshal(accumoney);
		_os_.marshal(delwrongval);
		_os_.marshal(chorightval);
		_os_.marshal(helpcnt);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		questionid = _os_.unmarshal_int();
		questionnum = _os_.unmarshal_int();
		righttimes = _os_.unmarshal_int();
		remaintime = _os_.unmarshal_long();
		lastright = _os_.unmarshal_byte();
		accuexp = _os_.unmarshal_int();
		accumoney = _os_.unmarshal_int();
		delwrongval = _os_.unmarshal_int();
		chorightval = _os_.unmarshal_int();
		helpcnt = _os_.unmarshal_int();
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof ImpExamBean) {
			ImpExamBean _o_ = (ImpExamBean)_o1_;
			if (questionid != _o_.questionid) return false;
			if (questionnum != _o_.questionnum) return false;
			if (righttimes != _o_.righttimes) return false;
			if (remaintime != _o_.remaintime) return false;
			if (lastright != _o_.lastright) return false;
			if (accuexp != _o_.accuexp) return false;
			if (accumoney != _o_.accumoney) return false;
			if (delwrongval != _o_.delwrongval) return false;
			if (chorightval != _o_.chorightval) return false;
			if (helpcnt != _o_.helpcnt) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += questionid;
		_h_ += questionnum;
		_h_ += righttimes;
		_h_ += (int)remaintime;
		_h_ += (int)lastright;
		_h_ += accuexp;
		_h_ += accumoney;
		_h_ += delwrongval;
		_h_ += chorightval;
		_h_ += helpcnt;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(questionid).append(",");
		_sb_.append(questionnum).append(",");
		_sb_.append(righttimes).append(",");
		_sb_.append(remaintime).append(",");
		_sb_.append(lastright).append(",");
		_sb_.append(accuexp).append(",");
		_sb_.append(accumoney).append(",");
		_sb_.append(delwrongval).append(",");
		_sb_.append(chorightval).append(",");
		_sb_.append(helpcnt).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(ImpExamBean _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = questionid - _o_.questionid;
		if (0 != _c_) return _c_;
		_c_ = questionnum - _o_.questionnum;
		if (0 != _c_) return _c_;
		_c_ = righttimes - _o_.righttimes;
		if (0 != _c_) return _c_;
		_c_ = Long.signum(remaintime - _o_.remaintime);
		if (0 != _c_) return _c_;
		_c_ = lastright - _o_.lastright;
		if (0 != _c_) return _c_;
		_c_ = accuexp - _o_.accuexp;
		if (0 != _c_) return _c_;
		_c_ = accumoney - _o_.accumoney;
		if (0 != _c_) return _c_;
		_c_ = delwrongval - _o_.delwrongval;
		if (0 != _c_) return _c_;
		_c_ = chorightval - _o_.chorightval;
		if (0 != _c_) return _c_;
		_c_ = helpcnt - _o_.helpcnt;
		if (0 != _c_) return _c_;
		return _c_;
	}

}

