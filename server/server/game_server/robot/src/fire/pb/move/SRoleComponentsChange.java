
package fire.pb.move;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __SRoleComponentsChange__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class SRoleComponentsChange extends __SRoleComponentsChange__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 790476;

	public int getType() {
		return 790476;
	}

	public long roleid; // 精灵ID（包括人物和npc）
	public byte spritetype; // 0代表角色，1代表npc
	public java.util.HashMap<Byte,Integer> components; // 角色换装信息key值参考SpriteComponents的枚举，value为0代表脱下某件换装

	public SRoleComponentsChange() {
		components = new java.util.HashMap<Byte,Integer>();
	}

	public SRoleComponentsChange(long _roleid_, byte _spritetype_, java.util.HashMap<Byte,Integer> _components_) {
		this.roleid = _roleid_;
		this.spritetype = _spritetype_;
		this.components = _components_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(roleid);
		_os_.marshal(spritetype);
		_os_.compact_uint32(components.size());
		for (java.util.Map.Entry<Byte, Integer> _e_ : components.entrySet()) {
			_os_.marshal(_e_.getKey());
			_os_.marshal(_e_.getValue());
		}
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		roleid = _os_.unmarshal_long();
		spritetype = _os_.unmarshal_byte();
		for (int size = _os_.uncompact_uint32(); size > 0; --size) {
			byte _k_;
			_k_ = _os_.unmarshal_byte();
			int _v_;
			_v_ = _os_.unmarshal_int();
			components.put(_k_, _v_);
		}
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof SRoleComponentsChange) {
			SRoleComponentsChange _o_ = (SRoleComponentsChange)_o1_;
			if (roleid != _o_.roleid) return false;
			if (spritetype != _o_.spritetype) return false;
			if (!components.equals(_o_.components)) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += (int)roleid;
		_h_ += (int)spritetype;
		_h_ += components.hashCode();
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(roleid).append(",");
		_sb_.append(spritetype).append(",");
		_sb_.append(components).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

