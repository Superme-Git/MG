
package fire.pb.hook;

import com.locojoy.base.Marshal.Marshal;
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public class HookData implements Marshal , Comparable<HookData>{
	public short cangetdpoint; // 可领取双倍点数
	public short getdpoint; // 已领取双倍点数
	public byte isautobattle; // 是否自动战斗
	public short charoptype; // 人物操作类型
	public int charopid; // 人物操作id
	public short petoptype; // 宠物操作类型
	public int petopid; // 宠物操作类型
	public long offlineexp; // 离线经验

	public HookData() {
	}

	public HookData(short _cangetdpoint_, short _getdpoint_, byte _isautobattle_, short _charoptype_, int _charopid_, short _petoptype_, int _petopid_, long _offlineexp_) {
		this.cangetdpoint = _cangetdpoint_;
		this.getdpoint = _getdpoint_;
		this.isautobattle = _isautobattle_;
		this.charoptype = _charoptype_;
		this.charopid = _charopid_;
		this.petoptype = _petoptype_;
		this.petopid = _petopid_;
		this.offlineexp = _offlineexp_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		_os_.marshal(cangetdpoint);
		_os_.marshal(getdpoint);
		_os_.marshal(isautobattle);
		_os_.marshal(charoptype);
		_os_.marshal(charopid);
		_os_.marshal(petoptype);
		_os_.marshal(petopid);
		_os_.marshal(offlineexp);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		cangetdpoint = _os_.unmarshal_short();
		getdpoint = _os_.unmarshal_short();
		isautobattle = _os_.unmarshal_byte();
		charoptype = _os_.unmarshal_short();
		charopid = _os_.unmarshal_int();
		petoptype = _os_.unmarshal_short();
		petopid = _os_.unmarshal_int();
		offlineexp = _os_.unmarshal_long();
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof HookData) {
			HookData _o_ = (HookData)_o1_;
			if (cangetdpoint != _o_.cangetdpoint) return false;
			if (getdpoint != _o_.getdpoint) return false;
			if (isautobattle != _o_.isautobattle) return false;
			if (charoptype != _o_.charoptype) return false;
			if (charopid != _o_.charopid) return false;
			if (petoptype != _o_.petoptype) return false;
			if (petopid != _o_.petopid) return false;
			if (offlineexp != _o_.offlineexp) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += cangetdpoint;
		_h_ += getdpoint;
		_h_ += (int)isautobattle;
		_h_ += charoptype;
		_h_ += charopid;
		_h_ += petoptype;
		_h_ += petopid;
		_h_ += (int)offlineexp;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(cangetdpoint).append(",");
		_sb_.append(getdpoint).append(",");
		_sb_.append(isautobattle).append(",");
		_sb_.append(charoptype).append(",");
		_sb_.append(charopid).append(",");
		_sb_.append(petoptype).append(",");
		_sb_.append(petopid).append(",");
		_sb_.append(offlineexp).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(HookData _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = cangetdpoint - _o_.cangetdpoint;
		if (0 != _c_) return _c_;
		_c_ = getdpoint - _o_.getdpoint;
		if (0 != _c_) return _c_;
		_c_ = isautobattle - _o_.isautobattle;
		if (0 != _c_) return _c_;
		_c_ = charoptype - _o_.charoptype;
		if (0 != _c_) return _c_;
		_c_ = charopid - _o_.charopid;
		if (0 != _c_) return _c_;
		_c_ = petoptype - _o_.petoptype;
		if (0 != _c_) return _c_;
		_c_ = petopid - _o_.petopid;
		if (0 != _c_) return _c_;
		_c_ = Long.signum(offlineexp - _o_.offlineexp);
		if (0 != _c_) return _c_;
		return _c_;
	}

}

