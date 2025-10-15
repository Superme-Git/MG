
package fire.pb.item;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __SMailState__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class SMailState extends __SMailState__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 787706;

	public int getType() {
		return 787706;
	}

	public byte kind; // 类型 0=定时邮件 1=GM邮件
	public long id; // id
	public byte statetype; // 状态类型 0=读取状态 1=领取状态
	public byte statevalue; // 状态值 0=否 1=是

	public SMailState() {
	}

	public SMailState(byte _kind_, long _id_, byte _statetype_, byte _statevalue_) {
		this.kind = _kind_;
		this.id = _id_;
		this.statetype = _statetype_;
		this.statevalue = _statevalue_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(kind);
		_os_.marshal(id);
		_os_.marshal(statetype);
		_os_.marshal(statevalue);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		kind = _os_.unmarshal_byte();
		id = _os_.unmarshal_long();
		statetype = _os_.unmarshal_byte();
		statevalue = _os_.unmarshal_byte();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof SMailState) {
			SMailState _o_ = (SMailState)_o1_;
			if (kind != _o_.kind) return false;
			if (id != _o_.id) return false;
			if (statetype != _o_.statetype) return false;
			if (statevalue != _o_.statevalue) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += (int)kind;
		_h_ += (int)id;
		_h_ += (int)statetype;
		_h_ += (int)statevalue;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(kind).append(",");
		_sb_.append(id).append(",");
		_sb_.append(statetype).append(",");
		_sb_.append(statevalue).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(SMailState _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = kind - _o_.kind;
		if (0 != _c_) return _c_;
		_c_ = Long.signum(id - _o_.id);
		if (0 != _c_) return _c_;
		_c_ = statetype - _o_.statetype;
		if (0 != _c_) return _c_;
		_c_ = statevalue - _o_.statevalue;
		if (0 != _c_) return _c_;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

