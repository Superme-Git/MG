
package fire.pb.clan;

import com.locojoy.base.Marshal.Marshal;
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

/** 角色申请公会状态
*/
public class ApplyClan implements Marshal , Comparable<ApplyClan>{
	public long clankey; // 公会key
	public int applystate; // 申请状态 0取消  1申请中

	public ApplyClan() {
	}

	public ApplyClan(long _clankey_, int _applystate_) {
		this.clankey = _clankey_;
		this.applystate = _applystate_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		_os_.marshal(clankey);
		_os_.marshal(applystate);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		clankey = _os_.unmarshal_long();
		applystate = _os_.unmarshal_int();
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof ApplyClan) {
			ApplyClan _o_ = (ApplyClan)_o1_;
			if (clankey != _o_.clankey) return false;
			if (applystate != _o_.applystate) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += (int)clankey;
		_h_ += applystate;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(clankey).append(",");
		_sb_.append(applystate).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(ApplyClan _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = Long.signum(clankey - _o_.clankey);
		if (0 != _c_) return _c_;
		_c_ = applystate - _o_.applystate;
		if (0 != _c_) return _c_;
		return _c_;
	}

}

