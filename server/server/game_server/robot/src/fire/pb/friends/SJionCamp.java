
package fire.pb.friends;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __SJionCamp__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class SJionCamp extends __SJionCamp__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 806559;

	public int getType() {
		return 806559;
	}

	public long roleid; // 注意roleID等于自己的 还是别人的
	public byte camptype; // 0=无阵营 1=部落 2=联盟
	public byte selecttype; // 0=选择阵营 1=更换阵营

	public SJionCamp() {
	}

	public SJionCamp(long _roleid_, byte _camptype_, byte _selecttype_) {
		this.roleid = _roleid_;
		this.camptype = _camptype_;
		this.selecttype = _selecttype_;
	}

	public final boolean _validator_() {
		if (camptype < 0 || camptype > 2) return false;
		if (selecttype < 0 || selecttype > 1) return false;
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(roleid);
		_os_.marshal(camptype);
		_os_.marshal(selecttype);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		roleid = _os_.unmarshal_long();
		camptype = _os_.unmarshal_byte();
		selecttype = _os_.unmarshal_byte();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof SJionCamp) {
			SJionCamp _o_ = (SJionCamp)_o1_;
			if (roleid != _o_.roleid) return false;
			if (camptype != _o_.camptype) return false;
			if (selecttype != _o_.selecttype) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += (int)roleid;
		_h_ += (int)camptype;
		_h_ += (int)selecttype;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(roleid).append(",");
		_sb_.append(camptype).append(",");
		_sb_.append(selecttype).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(SJionCamp _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = Long.signum(roleid - _o_.roleid);
		if (0 != _c_) return _c_;
		_c_ = camptype - _o_.camptype;
		if (0 != _c_) return _c_;
		_c_ = selecttype - _o_.selecttype;
		if (0 != _c_) return _c_;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

