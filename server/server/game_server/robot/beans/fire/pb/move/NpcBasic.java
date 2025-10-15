
package fire.pb.move;

import com.locojoy.base.Marshal.Marshal;
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public class NpcBasic implements Marshal {
	public long npckey; // npckey :npc的唯一ID
	public int id; // npcid: npc的基本ID
	public java.lang.String name;
	public fire.pb.move.Pos pos;
	public byte posz;
	public fire.pb.move.Pos destpos; // 运动中的NPC的目的点
	public int speed; // 移动NPC速度
	public int dir;
	public int shape; // npc 造型
	public int scenestate; // npc 状态
	public java.util.HashMap<Byte,Integer> components; // 角色换装信息key值参考SpriteComponents的枚举

	public NpcBasic() {
		name = "";
		pos = new fire.pb.move.Pos();
		destpos = new fire.pb.move.Pos();
		components = new java.util.HashMap<Byte,Integer>();
	}

	public NpcBasic(long _npckey_, int _id_, java.lang.String _name_, fire.pb.move.Pos _pos_, byte _posz_, fire.pb.move.Pos _destpos_, int _speed_, int _dir_, int _shape_, int _scenestate_, java.util.HashMap<Byte,Integer> _components_) {
		this.npckey = _npckey_;
		this.id = _id_;
		this.name = _name_;
		this.pos = _pos_;
		this.posz = _posz_;
		this.destpos = _destpos_;
		this.speed = _speed_;
		this.dir = _dir_;
		this.shape = _shape_;
		this.scenestate = _scenestate_;
		this.components = _components_;
	}

	public final boolean _validator_() {
		if (!pos._validator_()) return false;
		if (!destpos._validator_()) return false;
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		_os_.marshal(npckey);
		_os_.marshal(id);
		_os_.marshal(name, "UTF-16LE");
		_os_.marshal(pos);
		_os_.marshal(posz);
		_os_.marshal(destpos);
		_os_.marshal(speed);
		_os_.marshal(dir);
		_os_.marshal(shape);
		_os_.marshal(scenestate);
		_os_.compact_uint32(components.size());
		for (java.util.Map.Entry<Byte, Integer> _e_ : components.entrySet()) {
			_os_.marshal(_e_.getKey());
			_os_.marshal(_e_.getValue());
		}
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		npckey = _os_.unmarshal_long();
		id = _os_.unmarshal_int();
		name = _os_.unmarshal_String("UTF-16LE");
		pos.unmarshal(_os_);
		posz = _os_.unmarshal_byte();
		destpos.unmarshal(_os_);
		speed = _os_.unmarshal_int();
		dir = _os_.unmarshal_int();
		shape = _os_.unmarshal_int();
		scenestate = _os_.unmarshal_int();
		for (int size = _os_.uncompact_uint32(); size > 0; --size) {
			byte _k_;
			_k_ = _os_.unmarshal_byte();
			int _v_;
			_v_ = _os_.unmarshal_int();
			components.put(_k_, _v_);
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof NpcBasic) {
			NpcBasic _o_ = (NpcBasic)_o1_;
			if (npckey != _o_.npckey) return false;
			if (id != _o_.id) return false;
			if (!name.equals(_o_.name)) return false;
			if (!pos.equals(_o_.pos)) return false;
			if (posz != _o_.posz) return false;
			if (!destpos.equals(_o_.destpos)) return false;
			if (speed != _o_.speed) return false;
			if (dir != _o_.dir) return false;
			if (shape != _o_.shape) return false;
			if (scenestate != _o_.scenestate) return false;
			if (!components.equals(_o_.components)) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += (int)npckey;
		_h_ += id;
		_h_ += name.hashCode();
		_h_ += pos.hashCode();
		_h_ += (int)posz;
		_h_ += destpos.hashCode();
		_h_ += speed;
		_h_ += dir;
		_h_ += shape;
		_h_ += scenestate;
		_h_ += components.hashCode();
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(npckey).append(",");
		_sb_.append(id).append(",");
		_sb_.append("T").append(name.length()).append(",");
		_sb_.append(pos).append(",");
		_sb_.append(posz).append(",");
		_sb_.append(destpos).append(",");
		_sb_.append(speed).append(",");
		_sb_.append(dir).append(",");
		_sb_.append(shape).append(",");
		_sb_.append(scenestate).append(",");
		_sb_.append(components).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

}

