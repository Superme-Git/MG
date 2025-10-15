package fire.msp.npc;


import java.util.Map.Entry;

import fire.pb.scene.manager.SceneNpcManager;
import fire.pb.scene.movable.NPC;
import fire.pb.scene.movable.Role;
import fire.pb.school.SShouxiShape;



// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __GChangeShouxiShape__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class GChangeShouxiShape extends __GChangeShouxiShape__ {
	@Override
	protected void process() {
		NPC npc = SceneNpcManager.getInstance().getNpcByKey(npckey);
		if (null == npc) return;
		if (null == npc.getScene())	return;
		
		npc.setName(name);
		npc.setShape(shape);
		for(Entry<Byte, Integer> entry:components.entrySet()){
			if(entry!=null){
				byte type=entry.getKey();
				int value=entry.getValue();
				npc.components.put(type, value);
			}
		}
		SShouxiShape shape = new SShouxiShape();
		shape.shouxikey = npckey;
		shape.shape = this.shape;
		shape.name = name;
		shape.components = components;
		npc.sendAround(shape);
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 730415;

	public int getType() {
		return 730415;
	}

	public long npckey;
	public long roleid; // 需要改变成玩家的ID by changhao
	public int shape;
	public java.util.HashMap<Byte,Integer> components; // 角色换装信息key值参考SpriteComponents的枚举，value为0代表脱下某件换装
	public java.lang.String name;

	public GChangeShouxiShape() {
		components = new java.util.HashMap<Byte,Integer>();
		name = "";
	}

	public GChangeShouxiShape(long _npckey_, long _roleid_, int _shape_, java.util.HashMap<Byte,Integer> _components_, java.lang.String _name_) {
		this.npckey = _npckey_;
		this.roleid = _roleid_;
		this.shape = _shape_;
		this.components = _components_;
		this.name = _name_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(npckey);
		_os_.marshal(roleid);
		_os_.marshal(shape);
		_os_.compact_uint32(components.size());
		for (java.util.Map.Entry<Byte, Integer> _e_ : components.entrySet()) {
			_os_.marshal(_e_.getKey());
			_os_.marshal(_e_.getValue());
		}
		_os_.marshal(name, "UTF-16LE");
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		npckey = _os_.unmarshal_long();
		roleid = _os_.unmarshal_long();
		shape = _os_.unmarshal_int();
		for (int size = _os_.uncompact_uint32(); size > 0; --size) {
			byte _k_;
			_k_ = _os_.unmarshal_byte();
			int _v_;
			_v_ = _os_.unmarshal_int();
			components.put(_k_, _v_);
		}
		name = _os_.unmarshal_String("UTF-16LE");
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof GChangeShouxiShape) {
			GChangeShouxiShape _o_ = (GChangeShouxiShape)_o1_;
			if (npckey != _o_.npckey) return false;
			if (roleid != _o_.roleid) return false;
			if (shape != _o_.shape) return false;
			if (!components.equals(_o_.components)) return false;
			if (!name.equals(_o_.name)) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += (int)npckey;
		_h_ += (int)roleid;
		_h_ += shape;
		_h_ += components.hashCode();
		_h_ += name.hashCode();
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(npckey).append(",");
		_sb_.append(roleid).append(",");
		_sb_.append(shape).append(",");
		_sb_.append(components).append(",");
		_sb_.append("T").append(name.length()).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}
