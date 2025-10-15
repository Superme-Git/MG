
package fire.pb.battle.pvp3;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __CPvP3OpenBox__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class CPvP3OpenBox extends __CPvP3OpenBox__ {
	@Override
	protected void process() {

		final long roleId = gnet.link.Onlines.getInstance().findRoleid(this);
		if (roleId <= 0)
			return;

		PvP3Control.getInstance().getAward(roleId, boxtype);
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 793653;

	public int getType() {
		return 793653;
	}

	public byte boxtype; // 1:首胜，2：十战，3：五胜

	public CPvP3OpenBox() {
	}

	public CPvP3OpenBox(byte _boxtype_) {
		this.boxtype = _boxtype_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(boxtype);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		boxtype = _os_.unmarshal_byte();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof CPvP3OpenBox) {
			CPvP3OpenBox _o_ = (CPvP3OpenBox)_o1_;
			if (boxtype != _o_.boxtype) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += (int)boxtype;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(boxtype).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(CPvP3OpenBox _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = boxtype - _o_.boxtype;
		if (0 != _c_) return _c_;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

