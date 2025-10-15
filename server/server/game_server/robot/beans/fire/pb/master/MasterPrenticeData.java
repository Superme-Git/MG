
package fire.pb.master;

import com.locojoy.base.Marshal.Marshal;
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public class MasterPrenticeData implements Marshal {
	public long roleid; // 徒弟的roleid
	public java.lang.String nickname; // 徒弟的名字
	public int level; // 徒弟等级
	public int school; // 徒弟职业id
	public long lastofflinetime; // 上次下线时间
	public int onlinestate; // 在线状态

	public MasterPrenticeData() {
		nickname = "";
	}

	public MasterPrenticeData(long _roleid_, java.lang.String _nickname_, int _level_, int _school_, long _lastofflinetime_, int _onlinestate_) {
		this.roleid = _roleid_;
		this.nickname = _nickname_;
		this.level = _level_;
		this.school = _school_;
		this.lastofflinetime = _lastofflinetime_;
		this.onlinestate = _onlinestate_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		_os_.marshal(roleid);
		_os_.marshal(nickname, "UTF-16LE");
		_os_.marshal(level);
		_os_.marshal(school);
		_os_.marshal(lastofflinetime);
		_os_.marshal(onlinestate);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		roleid = _os_.unmarshal_long();
		nickname = _os_.unmarshal_String("UTF-16LE");
		level = _os_.unmarshal_int();
		school = _os_.unmarshal_int();
		lastofflinetime = _os_.unmarshal_long();
		onlinestate = _os_.unmarshal_int();
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof MasterPrenticeData) {
			MasterPrenticeData _o_ = (MasterPrenticeData)_o1_;
			if (roleid != _o_.roleid) return false;
			if (!nickname.equals(_o_.nickname)) return false;
			if (level != _o_.level) return false;
			if (school != _o_.school) return false;
			if (lastofflinetime != _o_.lastofflinetime) return false;
			if (onlinestate != _o_.onlinestate) return false;
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
		_h_ += (int)lastofflinetime;
		_h_ += onlinestate;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(roleid).append(",");
		_sb_.append("T").append(nickname.length()).append(",");
		_sb_.append(level).append(",");
		_sb_.append(school).append(",");
		_sb_.append(lastofflinetime).append(",");
		_sb_.append(onlinestate).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

}

