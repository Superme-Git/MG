
package fire.pb.master;

import com.locojoy.base.Marshal.Marshal;
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public class Master implements Marshal {
	public long roleid; // 师傅的roleid
	public java.lang.String nickname; // 师傅的名字
	public int level; // 师傅等级
	public int school; // 职业id
	public int shape; // 师父造型
	public int rank; // 师父级别，根据声望得到
	public java.lang.String declaration; // 宣言

	public Master() {
		nickname = "";
		declaration = "";
	}

	public Master(long _roleid_, java.lang.String _nickname_, int _level_, int _school_, int _shape_, int _rank_, java.lang.String _declaration_) {
		this.roleid = _roleid_;
		this.nickname = _nickname_;
		this.level = _level_;
		this.school = _school_;
		this.shape = _shape_;
		this.rank = _rank_;
		this.declaration = _declaration_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		_os_.marshal(roleid);
		_os_.marshal(nickname, "UTF-16LE");
		_os_.marshal(level);
		_os_.marshal(school);
		_os_.marshal(shape);
		_os_.marshal(rank);
		_os_.marshal(declaration, "UTF-16LE");
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		roleid = _os_.unmarshal_long();
		nickname = _os_.unmarshal_String("UTF-16LE");
		level = _os_.unmarshal_int();
		school = _os_.unmarshal_int();
		shape = _os_.unmarshal_int();
		rank = _os_.unmarshal_int();
		declaration = _os_.unmarshal_String("UTF-16LE");
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof Master) {
			Master _o_ = (Master)_o1_;
			if (roleid != _o_.roleid) return false;
			if (!nickname.equals(_o_.nickname)) return false;
			if (level != _o_.level) return false;
			if (school != _o_.school) return false;
			if (shape != _o_.shape) return false;
			if (rank != _o_.rank) return false;
			if (!declaration.equals(_o_.declaration)) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += (int)roleid;
		_h_ += nickname.hashCode();
		_h_ += level;
		_h_ += school;
		_h_ += shape;
		_h_ += rank;
		_h_ += declaration.hashCode();
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(roleid).append(",");
		_sb_.append("T").append(nickname.length()).append(",");
		_sb_.append(level).append(",");
		_sb_.append(school).append(",");
		_sb_.append(shape).append(",");
		_sb_.append(rank).append(",");
		_sb_.append("T").append(declaration.length()).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

}

