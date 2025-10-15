
package fire.pb.school;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __SShouxiShape__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class SShouxiShape extends __SShouxiShape__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 810438;

	public int getType() {
		return 810438;
	}

	public long shouxikey; // 首席-弟子的baseid
	public java.lang.String name; // 首席名字
	public int shape; // 外形,如果为0的话表示用npc默认的造型就可以了
	public java.util.HashMap<Byte,Integer> components; // 角色换装信息key值参考SpriteComponents的枚举，value为0代表脱下某件换装
	public int titleid; // 称号id

	public SShouxiShape() {
		name = "";
		components = new java.util.HashMap<Byte,Integer>();
	}

	public SShouxiShape(long _shouxikey_, java.lang.String _name_, int _shape_, java.util.HashMap<Byte,Integer> _components_, int _titleid_) {
		this.shouxikey = _shouxikey_;
		this.name = _name_;
		this.shape = _shape_;
		this.components = _components_;
		this.titleid = _titleid_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(shouxikey);
		_os_.marshal(name, "UTF-16LE");
		_os_.marshal(shape);
		_os_.compact_uint32(components.size());
		for (java.util.Map.Entry<Byte, Integer> _e_ : components.entrySet()) {
			_os_.marshal(_e_.getKey());
			_os_.marshal(_e_.getValue());
		}
		_os_.marshal(titleid);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		shouxikey = _os_.unmarshal_long();
		name = _os_.unmarshal_String("UTF-16LE");
		shape = _os_.unmarshal_int();
		for (int size = _os_.uncompact_uint32(); size > 0; --size) {
			byte _k_;
			_k_ = _os_.unmarshal_byte();
			int _v_;
			_v_ = _os_.unmarshal_int();
			components.put(_k_, _v_);
		}
		titleid = _os_.unmarshal_int();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof SShouxiShape) {
			SShouxiShape _o_ = (SShouxiShape)_o1_;
			if (shouxikey != _o_.shouxikey) return false;
			if (!name.equals(_o_.name)) return false;
			if (shape != _o_.shape) return false;
			if (!components.equals(_o_.components)) return false;
			if (titleid != _o_.titleid) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += (int)shouxikey;
		_h_ += name.hashCode();
		_h_ += shape;
		_h_ += components.hashCode();
		_h_ += titleid;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(shouxikey).append(",");
		_sb_.append("T").append(name.length()).append(",");
		_sb_.append(shape).append(",");
		_sb_.append(components).append(",");
		_sb_.append(titleid).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

