
package fire.pb.activity.reg;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __SQueryRegData__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class SQueryRegData extends __SQueryRegData__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 810533;

	public int getType() {
		return 810533;
	}

	public int month; // 月
	public int times; // 签到次数
	public int suppregtimes; // 补签次数
	public int cansuppregtimes; // 可补签次数
	public java.util.ArrayList<Integer> suppregdays; // 补签日子
	public int rewardflag; // 1-领取过 2-未领取

	public SQueryRegData() {
		suppregdays = new java.util.ArrayList<Integer>();
	}

	public SQueryRegData(int _month_, int _times_, int _suppregtimes_, int _cansuppregtimes_, java.util.ArrayList<Integer> _suppregdays_, int _rewardflag_) {
		this.month = _month_;
		this.times = _times_;
		this.suppregtimes = _suppregtimes_;
		this.cansuppregtimes = _cansuppregtimes_;
		this.suppregdays = _suppregdays_;
		this.rewardflag = _rewardflag_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(month);
		_os_.marshal(times);
		_os_.marshal(suppregtimes);
		_os_.marshal(cansuppregtimes);
		_os_.compact_uint32(suppregdays.size());
		for (Integer _v_ : suppregdays) {
			_os_.marshal(_v_);
		}
		_os_.marshal(rewardflag);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		month = _os_.unmarshal_int();
		times = _os_.unmarshal_int();
		suppregtimes = _os_.unmarshal_int();
		cansuppregtimes = _os_.unmarshal_int();
		for (int _size_ = _os_.uncompact_uint32(); _size_ > 0; --_size_) {
			int _v_;
			_v_ = _os_.unmarshal_int();
			suppregdays.add(_v_);
		}
		rewardflag = _os_.unmarshal_int();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof SQueryRegData) {
			SQueryRegData _o_ = (SQueryRegData)_o1_;
			if (month != _o_.month) return false;
			if (times != _o_.times) return false;
			if (suppregtimes != _o_.suppregtimes) return false;
			if (cansuppregtimes != _o_.cansuppregtimes) return false;
			if (!suppregdays.equals(_o_.suppregdays)) return false;
			if (rewardflag != _o_.rewardflag) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += month;
		_h_ += times;
		_h_ += suppregtimes;
		_h_ += cansuppregtimes;
		_h_ += suppregdays.hashCode();
		_h_ += rewardflag;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(month).append(",");
		_sb_.append(times).append(",");
		_sb_.append(suppregtimes).append(",");
		_sb_.append(cansuppregtimes).append(",");
		_sb_.append(suppregdays).append(",");
		_sb_.append(rewardflag).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

