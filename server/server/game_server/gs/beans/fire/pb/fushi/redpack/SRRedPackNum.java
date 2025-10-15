
package fire.pb.fushi.redpack;

import com.locojoy.base.Marshal.Marshal;
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public class SRRedPackNum implements Marshal , Comparable<SRRedPackNum>{
	public int modeltype; // 红包类型
	public int redpacksendnum; // 发红包数量
	public int redpackreceivenum; // 收红包数量
	public int redpackreceivefushinum; // 发符石数量

	public SRRedPackNum() {
	}

	public SRRedPackNum(int _modeltype_, int _redpacksendnum_, int _redpackreceivenum_, int _redpackreceivefushinum_) {
		this.modeltype = _modeltype_;
		this.redpacksendnum = _redpacksendnum_;
		this.redpackreceivenum = _redpackreceivenum_;
		this.redpackreceivefushinum = _redpackreceivefushinum_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		_os_.marshal(modeltype);
		_os_.marshal(redpacksendnum);
		_os_.marshal(redpackreceivenum);
		_os_.marshal(redpackreceivefushinum);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		modeltype = _os_.unmarshal_int();
		redpacksendnum = _os_.unmarshal_int();
		redpackreceivenum = _os_.unmarshal_int();
		redpackreceivefushinum = _os_.unmarshal_int();
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof SRRedPackNum) {
			SRRedPackNum _o_ = (SRRedPackNum)_o1_;
			if (modeltype != _o_.modeltype) return false;
			if (redpacksendnum != _o_.redpacksendnum) return false;
			if (redpackreceivenum != _o_.redpackreceivenum) return false;
			if (redpackreceivefushinum != _o_.redpackreceivefushinum) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += modeltype;
		_h_ += redpacksendnum;
		_h_ += redpackreceivenum;
		_h_ += redpackreceivefushinum;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(modeltype).append(",");
		_sb_.append(redpacksendnum).append(",");
		_sb_.append(redpackreceivenum).append(",");
		_sb_.append(redpackreceivefushinum).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(SRRedPackNum _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = modeltype - _o_.modeltype;
		if (0 != _c_) return _c_;
		_c_ = redpacksendnum - _o_.redpacksendnum;
		if (0 != _c_) return _c_;
		_c_ = redpackreceivenum - _o_.redpackreceivenum;
		if (0 != _c_) return _c_;
		_c_ = redpackreceivefushinum - _o_.redpackreceivefushinum;
		if (0 != _c_) return _c_;
		return _c_;
	}

}

