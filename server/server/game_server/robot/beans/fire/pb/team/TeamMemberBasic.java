
package fire.pb.team;

import com.locojoy.base.Marshal.Marshal;
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public class TeamMemberBasic implements Marshal {
	public long roleid;
	public java.lang.String rolename;
	public int level;
	public long sceneid;
	public fire.pb.team.Pos1 pos;
	public int school;
	public int hp;
	public int maxhp;
	public int mp;
	public int maxmp;
	public java.lang.String title;
	public int state;
	public int shape;
	public byte hugindex; // [1,5]拥抱对象的index
	public java.util.HashMap<Byte,Integer> components; // 角色换装信息key值参考SpriteComponents的枚举，value为0代表脱下某件换装
	public byte camp; // 0中立  1部落 2联盟

	public TeamMemberBasic() {
		rolename = "";
		pos = new fire.pb.team.Pos1();
		title = "";
		components = new java.util.HashMap<Byte,Integer>();
	}

	public TeamMemberBasic(long _roleid_, java.lang.String _rolename_, int _level_, long _sceneid_, fire.pb.team.Pos1 _pos_, int _school_, int _hp_, int _maxhp_, int _mp_, int _maxmp_, java.lang.String _title_, int _state_, int _shape_, byte _hugindex_, java.util.HashMap<Byte,Integer> _components_, byte _camp_) {
		this.roleid = _roleid_;
		this.rolename = _rolename_;
		this.level = _level_;
		this.sceneid = _sceneid_;
		this.pos = _pos_;
		this.school = _school_;
		this.hp = _hp_;
		this.maxhp = _maxhp_;
		this.mp = _mp_;
		this.maxmp = _maxmp_;
		this.title = _title_;
		this.state = _state_;
		this.shape = _shape_;
		this.hugindex = _hugindex_;
		this.components = _components_;
		this.camp = _camp_;
	}

	public final boolean _validator_() {
		if (!pos._validator_()) return false;
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		_os_.marshal(roleid);
		_os_.marshal(rolename, "UTF-16LE");
		_os_.marshal(level);
		_os_.marshal(sceneid);
		_os_.marshal(pos);
		_os_.marshal(school);
		_os_.marshal(hp);
		_os_.marshal(maxhp);
		_os_.marshal(mp);
		_os_.marshal(maxmp);
		_os_.marshal(title, "UTF-16LE");
		_os_.marshal(state);
		_os_.marshal(shape);
		_os_.marshal(hugindex);
		_os_.compact_uint32(components.size());
		for (java.util.Map.Entry<Byte, Integer> _e_ : components.entrySet()) {
			_os_.marshal(_e_.getKey());
			_os_.marshal(_e_.getValue());
		}
		_os_.marshal(camp);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		roleid = _os_.unmarshal_long();
		rolename = _os_.unmarshal_String("UTF-16LE");
		level = _os_.unmarshal_int();
		sceneid = _os_.unmarshal_long();
		pos.unmarshal(_os_);
		school = _os_.unmarshal_int();
		hp = _os_.unmarshal_int();
		maxhp = _os_.unmarshal_int();
		mp = _os_.unmarshal_int();
		maxmp = _os_.unmarshal_int();
		title = _os_.unmarshal_String("UTF-16LE");
		state = _os_.unmarshal_int();
		shape = _os_.unmarshal_int();
		hugindex = _os_.unmarshal_byte();
		for (int size = _os_.uncompact_uint32(); size > 0; --size) {
			byte _k_;
			_k_ = _os_.unmarshal_byte();
			int _v_;
			_v_ = _os_.unmarshal_int();
			components.put(_k_, _v_);
		}
		camp = _os_.unmarshal_byte();
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof TeamMemberBasic) {
			TeamMemberBasic _o_ = (TeamMemberBasic)_o1_;
			if (roleid != _o_.roleid) return false;
			if (!rolename.equals(_o_.rolename)) return false;
			if (level != _o_.level) return false;
			if (sceneid != _o_.sceneid) return false;
			if (!pos.equals(_o_.pos)) return false;
			if (school != _o_.school) return false;
			if (hp != _o_.hp) return false;
			if (maxhp != _o_.maxhp) return false;
			if (mp != _o_.mp) return false;
			if (maxmp != _o_.maxmp) return false;
			if (!title.equals(_o_.title)) return false;
			if (state != _o_.state) return false;
			if (shape != _o_.shape) return false;
			if (hugindex != _o_.hugindex) return false;
			if (!components.equals(_o_.components)) return false;
			if (camp != _o_.camp) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += (int)roleid;
		_h_ += rolename.hashCode();
		_h_ += level;
		_h_ += (int)sceneid;
		_h_ += pos.hashCode();
		_h_ += school;
		_h_ += hp;
		_h_ += maxhp;
		_h_ += mp;
		_h_ += maxmp;
		_h_ += title.hashCode();
		_h_ += state;
		_h_ += shape;
		_h_ += (int)hugindex;
		_h_ += components.hashCode();
		_h_ += (int)camp;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(roleid).append(",");
		_sb_.append("T").append(rolename.length()).append(",");
		_sb_.append(level).append(",");
		_sb_.append(sceneid).append(",");
		_sb_.append(pos).append(",");
		_sb_.append(school).append(",");
		_sb_.append(hp).append(",");
		_sb_.append(maxhp).append(",");
		_sb_.append(mp).append(",");
		_sb_.append(maxmp).append(",");
		_sb_.append("T").append(title.length()).append(",");
		_sb_.append(state).append(",");
		_sb_.append(shape).append(",");
		_sb_.append(hugindex).append(",");
		_sb_.append(components).append(",");
		_sb_.append(camp).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

}

