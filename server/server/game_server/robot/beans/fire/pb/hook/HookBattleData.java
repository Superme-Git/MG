
package fire.pb.hook;

import com.locojoy.base.Marshal.Marshal;
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public class HookBattleData implements Marshal , Comparable<HookBattleData>{
	public byte isautobattle; // 是否自动战斗
	public short charoptype; // 人物操作类型
	public int charopid; // 人物操作id
	public short petoptype; // 宠物操作类型
	public int petopid; // 宠物操作类型

	public HookBattleData() {
	}

	public HookBattleData(byte _isautobattle_, short _charoptype_, int _charopid_, short _petoptype_, int _petopid_) {
		this.isautobattle = _isautobattle_;
		this.charoptype = _charoptype_;
		this.charopid = _charopid_;
		this.petoptype = _petoptype_;
		this.petopid = _petopid_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		_os_.marshal(isautobattle);
		_os_.marshal(charoptype);
		_os_.marshal(charopid);
		_os_.marshal(petoptype);
		_os_.marshal(petopid);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		isautobattle = _os_.unmarshal_byte();
		charoptype = _os_.unmarshal_short();
		charopid = _os_.unmarshal_int();
		petoptype = _os_.unmarshal_short();
		petopid = _os_.unmarshal_int();
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof HookBattleData) {
			HookBattleData _o_ = (HookBattleData)_o1_;
			if (isautobattle != _o_.isautobattle) return false;
			if (charoptype != _o_.charoptype) return false;
			if (charopid != _o_.charopid) return false;
			if (petoptype != _o_.petoptype) return false;
			if (petopid != _o_.petopid) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += (int)isautobattle;
		_h_ += charoptype;
		_h_ += charopid;
		_h_ += petoptype;
		_h_ += petopid;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(isautobattle).append(",");
		_sb_.append(charoptype).append(",");
		_sb_.append(charopid).append(",");
		_sb_.append(petoptype).append(",");
		_sb_.append(petopid).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(HookBattleData _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
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
		return _c_;
	}

}

