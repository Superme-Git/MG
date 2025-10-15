
package fire.pb.pet;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __SSetFightPet__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class SSetFightPet extends __SSetFightPet__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 788441;

	public int getType() {
		return 788441;
	}

	public int petkey; // 参战宠物，如果当前有参战宠物，则替换。
	public byte isinbattle; // 0=战斗外设置参战 1=战斗中召唤

	public SSetFightPet() {
	}

	public SSetFightPet(int _petkey_, byte _isinbattle_) {
		this.petkey = _petkey_;
		this.isinbattle = _isinbattle_;
	}

	public final boolean _validator_() {
		if (petkey < 1) return false;
		if (isinbattle < 0 || isinbattle > 1) return false;
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(petkey);
		_os_.marshal(isinbattle);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		petkey = _os_.unmarshal_int();
		isinbattle = _os_.unmarshal_byte();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof SSetFightPet) {
			SSetFightPet _o_ = (SSetFightPet)_o1_;
			if (petkey != _o_.petkey) return false;
			if (isinbattle != _o_.isinbattle) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += petkey;
		_h_ += (int)isinbattle;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(petkey).append(",");
		_sb_.append(isinbattle).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(SSetFightPet _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = petkey - _o_.petkey;
		if (0 != _c_) return _c_;
		_c_ = isinbattle - _o_.isinbattle;
		if (0 != _c_) return _c_;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

