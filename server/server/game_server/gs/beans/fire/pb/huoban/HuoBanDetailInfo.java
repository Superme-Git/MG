
package fire.pb.huoban;

import com.locojoy.base.Marshal.Marshal;
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public class HuoBanDetailInfo implements Marshal {
	public int huobanid; // 伙伴id
	public int infight; // 是否参战,1为参战
	public long state; // 是否解锁, 0为未解锁; 1为永久使用; 大于10为有多少分钟的剩余时间,如 134 表示为免费134 - 10 = 124分钟
	public int weekfree; // 本周是否免费 0不免费; 1本周免费
	public java.util.ArrayList<Integer> datas; // 变量值ID，变量值,气血,攻击,防御等等都在这里面

	public HuoBanDetailInfo() {
		datas = new java.util.ArrayList<Integer>();
	}

	public HuoBanDetailInfo(int _huobanid_, int _infight_, long _state_, int _weekfree_, java.util.ArrayList<Integer> _datas_) {
		this.huobanid = _huobanid_;
		this.infight = _infight_;
		this.state = _state_;
		this.weekfree = _weekfree_;
		this.datas = _datas_;
	}

	public final boolean _validator_() {
		if (weekfree < 0 || weekfree > 1) return false;
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		_os_.marshal(huobanid);
		_os_.marshal(infight);
		_os_.marshal(state);
		_os_.marshal(weekfree);
		_os_.compact_uint32(datas.size());
		for (Integer _v_ : datas) {
			_os_.marshal(_v_);
		}
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		huobanid = _os_.unmarshal_int();
		infight = _os_.unmarshal_int();
		state = _os_.unmarshal_long();
		weekfree = _os_.unmarshal_int();
		for (int _size_ = _os_.uncompact_uint32(); _size_ > 0; --_size_) {
			int _v_;
			_v_ = _os_.unmarshal_int();
			datas.add(_v_);
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof HuoBanDetailInfo) {
			HuoBanDetailInfo _o_ = (HuoBanDetailInfo)_o1_;
			if (huobanid != _o_.huobanid) return false;
			if (infight != _o_.infight) return false;
			if (state != _o_.state) return false;
			if (weekfree != _o_.weekfree) return false;
			if (!datas.equals(_o_.datas)) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += huobanid;
		_h_ += infight;
		_h_ += (int)state;
		_h_ += weekfree;
		_h_ += datas.hashCode();
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(huobanid).append(",");
		_sb_.append(infight).append(",");
		_sb_.append(state).append(",");
		_sb_.append(weekfree).append(",");
		_sb_.append(datas).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

}

