
package fire.pb.team;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __CSwapMember__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class CSwapMember extends __CSwapMember__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 794452;

	public int getType() {
		return 794452;
	}

	public int index1; // index是队员的序号，5人队伍的话，就是0~4
	public int index2;

	public CSwapMember() {
	}

	public CSwapMember(int _index1_, int _index2_) {
		this.index1 = _index1_;
		this.index2 = _index2_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(index1);
		_os_.marshal(index2);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		index1 = _os_.unmarshal_int();
		index2 = _os_.unmarshal_int();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof CSwapMember) {
			CSwapMember _o_ = (CSwapMember)_o1_;
			if (index1 != _o_.index1) return false;
			if (index2 != _o_.index2) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += index1;
		_h_ += index2;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(index1).append(",");
		_sb_.append(index2).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(CSwapMember _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = index1 - _o_.index1;
		if (0 != _c_) return _c_;
		_c_ = index2 - _o_.index2;
		if (0 != _c_) return _c_;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

