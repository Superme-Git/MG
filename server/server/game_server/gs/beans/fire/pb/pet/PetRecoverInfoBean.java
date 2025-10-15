
package fire.pb.pet;

import com.locojoy.base.Marshal.Marshal;
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

/** 宠物找回信息
*/
public class PetRecoverInfoBean implements Marshal , Comparable<PetRecoverInfoBean>{
	public int petid; // 宠物id
	public long uniqid; // 唯一id
	public int remaintime; // 剩余时间(单位:秒数)
	public int cost; // 找回消耗金币

	public PetRecoverInfoBean() {
	}

	public PetRecoverInfoBean(int _petid_, long _uniqid_, int _remaintime_, int _cost_) {
		this.petid = _petid_;
		this.uniqid = _uniqid_;
		this.remaintime = _remaintime_;
		this.cost = _cost_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		_os_.marshal(petid);
		_os_.marshal(uniqid);
		_os_.marshal(remaintime);
		_os_.marshal(cost);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		petid = _os_.unmarshal_int();
		uniqid = _os_.unmarshal_long();
		remaintime = _os_.unmarshal_int();
		cost = _os_.unmarshal_int();
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof PetRecoverInfoBean) {
			PetRecoverInfoBean _o_ = (PetRecoverInfoBean)_o1_;
			if (petid != _o_.petid) return false;
			if (uniqid != _o_.uniqid) return false;
			if (remaintime != _o_.remaintime) return false;
			if (cost != _o_.cost) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += petid;
		_h_ += (int)uniqid;
		_h_ += remaintime;
		_h_ += cost;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(petid).append(",");
		_sb_.append(uniqid).append(",");
		_sb_.append(remaintime).append(",");
		_sb_.append(cost).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(PetRecoverInfoBean _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = petid - _o_.petid;
		if (0 != _c_) return _c_;
		_c_ = Long.signum(uniqid - _o_.uniqid);
		if (0 != _c_) return _c_;
		_c_ = remaintime - _o_.remaintime;
		if (0 != _c_) return _c_;
		_c_ = cost - _o_.cost;
		if (0 != _c_) return _c_;
		return _c_;
	}

}

