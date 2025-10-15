
package fire.pb;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __SAddPointAttrData__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class SAddPointAttrData extends __SAddPointAttrData__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 786531;

	public int getType() {
		return 786531;
	}

	public float max_hp; // 生命
	public float max_mp; // 魔法
	public float attack; // 物攻
	public float defend; // 物防
	public float magic_attack; // 法攻
	public float magic_def; // 法防
	public float speed; // 速度

	public SAddPointAttrData() {
	}

	public SAddPointAttrData(float _max_hp_, float _max_mp_, float _attack_, float _defend_, float _magic_attack_, float _magic_def_, float _speed_) {
		this.max_hp = _max_hp_;
		this.max_mp = _max_mp_;
		this.attack = _attack_;
		this.defend = _defend_;
		this.magic_attack = _magic_attack_;
		this.magic_def = _magic_def_;
		this.speed = _speed_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(max_hp);
		_os_.marshal(max_mp);
		_os_.marshal(attack);
		_os_.marshal(defend);
		_os_.marshal(magic_attack);
		_os_.marshal(magic_def);
		_os_.marshal(speed);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		max_hp = _os_.unmarshal_float();
		max_mp = _os_.unmarshal_float();
		attack = _os_.unmarshal_float();
		defend = _os_.unmarshal_float();
		magic_attack = _os_.unmarshal_float();
		magic_def = _os_.unmarshal_float();
		speed = _os_.unmarshal_float();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof SAddPointAttrData) {
			SAddPointAttrData _o_ = (SAddPointAttrData)_o1_;
			if (max_hp != _o_.max_hp) return false;
			if (max_mp != _o_.max_mp) return false;
			if (attack != _o_.attack) return false;
			if (defend != _o_.defend) return false;
			if (magic_attack != _o_.magic_attack) return false;
			if (magic_def != _o_.magic_def) return false;
			if (speed != _o_.speed) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += Float.floatToIntBits(max_hp);
		_h_ += Float.floatToIntBits(max_mp);
		_h_ += Float.floatToIntBits(attack);
		_h_ += Float.floatToIntBits(defend);
		_h_ += Float.floatToIntBits(magic_attack);
		_h_ += Float.floatToIntBits(magic_def);
		_h_ += Float.floatToIntBits(speed);
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(max_hp).append(",");
		_sb_.append(max_mp).append(",");
		_sb_.append(attack).append(",");
		_sb_.append(defend).append(",");
		_sb_.append(magic_attack).append(",");
		_sb_.append(magic_def).append(",");
		_sb_.append(speed).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

