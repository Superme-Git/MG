
package fire.pb.team;

import com.locojoy.base.Marshal.Marshal;
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public class SingleCharacterBasic implements Marshal {
	public long roleid; // 玩家ID
	public int level; // 等级
	public java.lang.String name; // 名字
	public int school; // 职业
	public fire.pb.team.Pos1 position; // 坐标
	public byte camp; // 0中立  1部落 2联盟

	public SingleCharacterBasic() {
		name = "";
		position = new fire.pb.team.Pos1();
	}

	public SingleCharacterBasic(long _roleid_, int _level_, java.lang.String _name_, int _school_, fire.pb.team.Pos1 _position_, byte _camp_) {
		this.roleid = _roleid_;
		this.level = _level_;
		this.name = _name_;
		this.school = _school_;
		this.position = _position_;
		this.camp = _camp_;
	}

	public final boolean _validator_() {
		if (!position._validator_()) return false;
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		_os_.marshal(roleid);
		_os_.marshal(level);
		_os_.marshal(name, "UTF-16LE");
		_os_.marshal(school);
		_os_.marshal(position);
		_os_.marshal(camp);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		roleid = _os_.unmarshal_long();
		level = _os_.unmarshal_int();
		name = _os_.unmarshal_String("UTF-16LE");
		school = _os_.unmarshal_int();
		position.unmarshal(_os_);
		camp = _os_.unmarshal_byte();
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof SingleCharacterBasic) {
			SingleCharacterBasic _o_ = (SingleCharacterBasic)_o1_;
			if (roleid != _o_.roleid) return false;
			if (level != _o_.level) return false;
			if (!name.equals(_o_.name)) return false;
			if (school != _o_.school) return false;
			if (!position.equals(_o_.position)) return false;
			if (camp != _o_.camp) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += (int)roleid;
		_h_ += level;
		_h_ += name.hashCode();
		_h_ += school;
		_h_ += position.hashCode();
		_h_ += (int)camp;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(roleid).append(",");
		_sb_.append(level).append(",");
		_sb_.append("T").append(name.length()).append(",");
		_sb_.append(school).append(",");
		_sb_.append(position).append(",");
		_sb_.append(camp).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

}

