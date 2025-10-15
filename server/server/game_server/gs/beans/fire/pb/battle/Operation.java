
package fire.pb.battle;

import com.locojoy.base.Marshal.Marshal;
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public class Operation implements Marshal , Comparable<Operation>{
	public int operationtype; // 操作类型	 参考OperationType中的值
	public int aim; // 作用目标
	public int operationid; // 操作的值（使用物品时表示物品ID，招唤宠物时为宠物）

	public Operation() {
	}

	public Operation(int _operationtype_, int _aim_, int _operationid_) {
		this.operationtype = _operationtype_;
		this.aim = _aim_;
		this.operationid = _operationid_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		_os_.marshal(operationtype);
		_os_.marshal(aim);
		_os_.marshal(operationid);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		operationtype = _os_.unmarshal_int();
		aim = _os_.unmarshal_int();
		operationid = _os_.unmarshal_int();
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof Operation) {
			Operation _o_ = (Operation)_o1_;
			if (operationtype != _o_.operationtype) return false;
			if (aim != _o_.aim) return false;
			if (operationid != _o_.operationid) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += operationtype;
		_h_ += aim;
		_h_ += operationid;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(operationtype).append(",");
		_sb_.append(aim).append(",");
		_sb_.append(operationid).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(Operation _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = operationtype - _o_.operationtype;
		if (0 != _c_) return _c_;
		_c_ = aim - _o_.aim;
		if (0 != _c_) return _c_;
		_c_ = operationid - _o_.operationid;
		if (0 != _c_) return _c_;
		return _c_;
	}

}

