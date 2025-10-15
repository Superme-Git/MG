
package fire.pb.ranklist;

import com.locojoy.base.Marshal.Marshal;
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

/** 等排榜
*/
public class LevelRankData implements Marshal {
	public long roleid; // roleid
	public java.lang.String nickname; // 名字
	public int level; // 等级
	public int school; // 职业id
	public int rank; // 排名

	public LevelRankData() {
		nickname = "";
	}

	public LevelRankData(long _roleid_, java.lang.String _nickname_, int _level_, int _school_, int _rank_) {
		this.roleid = _roleid_;
		this.nickname = _nickname_;
		this.level = _level_;
		this.school = _school_;
		this.rank = _rank_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		_os_.marshal(roleid);
		_os_.marshal(nickname, "UTF-16LE");
		_os_.marshal(level);
		_os_.marshal(school);
		_os_.marshal(rank);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		roleid = _os_.unmarshal_long();
		nickname = _os_.unmarshal_String("UTF-16LE");
		level = _os_.unmarshal_int();
		school = _os_.unmarshal_int();
		rank = _os_.unmarshal_int();
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof LevelRankData) {
			LevelRankData _o_ = (LevelRankData)_o1_;
			if (roleid != _o_.roleid) return false;
			if (!nickname.equals(_o_.nickname)) return false;
			if (level != _o_.level) return false;
			if (school != _o_.school) return false;
			if (rank != _o_.rank) return false;
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
		_h_ += rank;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(roleid).append(",");
		_sb_.append("T").append(nickname.length()).append(",");
		_sb_.append(level).append(",");
		_sb_.append(school).append(",");
		_sb_.append(rank).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

}

