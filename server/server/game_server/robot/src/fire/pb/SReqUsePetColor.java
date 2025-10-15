
package fire.pb;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __SReqUsePetColor__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class SReqUsePetColor extends __SReqUsePetColor__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 786544;

	public int getType() {
		return 786544;
	}

	public int petkey; // 宠物ID
	public int colorpos1; // 部位1
	public int colorpos2; // 部位2

	public SReqUsePetColor() {
	}

	public SReqUsePetColor(int _petkey_, int _colorpos1_, int _colorpos2_) {
		this.petkey = _petkey_;
		this.colorpos1 = _colorpos1_;
		this.colorpos2 = _colorpos2_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(petkey);
		_os_.marshal(colorpos1);
		_os_.marshal(colorpos2);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		petkey = _os_.unmarshal_int();
		colorpos1 = _os_.unmarshal_int();
		colorpos2 = _os_.unmarshal_int();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof SReqUsePetColor) {
			SReqUsePetColor _o_ = (SReqUsePetColor)_o1_;
			if (petkey != _o_.petkey) return false;
			if (colorpos1 != _o_.colorpos1) return false;
			if (colorpos2 != _o_.colorpos2) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += petkey;
		_h_ += colorpos1;
		_h_ += colorpos2;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(petkey).append(",");
		_sb_.append(colorpos1).append(",");
		_sb_.append(colorpos2).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(SReqUsePetColor _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = petkey - _o_.petkey;
		if (0 != _c_) return _c_;
		_c_ = colorpos1 - _o_.colorpos1;
		if (0 != _c_) return _c_;
		_c_ = colorpos2 - _o_.colorpos2;
		if (0 != _c_) return _c_;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

