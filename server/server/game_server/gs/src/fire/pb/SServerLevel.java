
package fire.pb;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __SServerLevel__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class SServerLevel extends __SServerLevel__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 786521;

	public int getType() {
		return 786521;
	}

	public int slevel; // 服务器等级
	public int newleveldays; // 开启新等级的剩余天数

	public SServerLevel() {
	}

	public SServerLevel(int _slevel_, int _newleveldays_) {
		this.slevel = _slevel_;
		this.newleveldays = _newleveldays_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(slevel);
		_os_.marshal(newleveldays);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		slevel = _os_.unmarshal_int();
		newleveldays = _os_.unmarshal_int();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof SServerLevel) {
			SServerLevel _o_ = (SServerLevel)_o1_;
			if (slevel != _o_.slevel) return false;
			if (newleveldays != _o_.newleveldays) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += slevel;
		_h_ += newleveldays;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(slevel).append(",");
		_sb_.append(newleveldays).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(SServerLevel _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = slevel - _o_.slevel;
		if (0 != _c_) return _c_;
		_c_ = newleveldays - _o_.newleveldays;
		if (0 != _c_) return _c_;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

