
package fire.pb.pet;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __SSetFightPetRest__ extends mkio.Protocol { }

/** 收回参战宠物, 将当前的参战宠物设置为休息状态。
*/
// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class SSetFightPetRest extends __SSetFightPetRest__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 788443;

	public int getType() {
		return 788443;
	}

	public byte isinbattle; // 0=战斗外设置休息 1=战斗中召回

	public SSetFightPetRest() {
	}

	public SSetFightPetRest(byte _isinbattle_) {
		this.isinbattle = _isinbattle_;
	}

	public final boolean _validator_() {
		if (isinbattle < 0 || isinbattle > 1) return false;
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(isinbattle);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		isinbattle = _os_.unmarshal_byte();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof SSetFightPetRest) {
			SSetFightPetRest _o_ = (SSetFightPetRest)_o1_;
			if (isinbattle != _o_.isinbattle) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += (int)isinbattle;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(isinbattle).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(SSetFightPetRest _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = isinbattle - _o_.isinbattle;
		if (0 != _c_) return _c_;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

