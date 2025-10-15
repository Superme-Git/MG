
package fire.pb.clan;

import com.locojoy.base.Marshal.Marshal;
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public class RuneInfo implements Marshal {
	public long roleid; // 请求角色roleid
	public java.lang.String rolename; // 角色名
	public long targetroleid; // 目标roleid
	public java.lang.String targetrolename; // 目标角色名
	public int level; // 角色等级
	public int school; // 职业职业
	public int shape; // 角色造型头像
	public int givenum; // 捐符数
	public int acceptnum; // 收符数
	public int actiontype; // 动作类型  0 请求符文    1捐献符文
	public long requesttime; // 请求时间
	public int itemid; // 物品id
	public int itemlevel; // 物品等级

	public RuneInfo() {
		rolename = "";
		targetrolename = "";
	}

	public RuneInfo(long _roleid_, java.lang.String _rolename_, long _targetroleid_, java.lang.String _targetrolename_, int _level_, int _school_, int _shape_, int _givenum_, int _acceptnum_, int _actiontype_, long _requesttime_, int _itemid_, int _itemlevel_) {
		this.roleid = _roleid_;
		this.rolename = _rolename_;
		this.targetroleid = _targetroleid_;
		this.targetrolename = _targetrolename_;
		this.level = _level_;
		this.school = _school_;
		this.shape = _shape_;
		this.givenum = _givenum_;
		this.acceptnum = _acceptnum_;
		this.actiontype = _actiontype_;
		this.requesttime = _requesttime_;
		this.itemid = _itemid_;
		this.itemlevel = _itemlevel_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		_os_.marshal(roleid);
		_os_.marshal(rolename, "UTF-16LE");
		_os_.marshal(targetroleid);
		_os_.marshal(targetrolename, "UTF-16LE");
		_os_.marshal(level);
		_os_.marshal(school);
		_os_.marshal(shape);
		_os_.marshal(givenum);
		_os_.marshal(acceptnum);
		_os_.marshal(actiontype);
		_os_.marshal(requesttime);
		_os_.marshal(itemid);
		_os_.marshal(itemlevel);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		roleid = _os_.unmarshal_long();
		rolename = _os_.unmarshal_String("UTF-16LE");
		targetroleid = _os_.unmarshal_long();
		targetrolename = _os_.unmarshal_String("UTF-16LE");
		level = _os_.unmarshal_int();
		school = _os_.unmarshal_int();
		shape = _os_.unmarshal_int();
		givenum = _os_.unmarshal_int();
		acceptnum = _os_.unmarshal_int();
		actiontype = _os_.unmarshal_int();
		requesttime = _os_.unmarshal_long();
		itemid = _os_.unmarshal_int();
		itemlevel = _os_.unmarshal_int();
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof RuneInfo) {
			RuneInfo _o_ = (RuneInfo)_o1_;
			if (roleid != _o_.roleid) return false;
			if (!rolename.equals(_o_.rolename)) return false;
			if (targetroleid != _o_.targetroleid) return false;
			if (!targetrolename.equals(_o_.targetrolename)) return false;
			if (level != _o_.level) return false;
			if (school != _o_.school) return false;
			if (shape != _o_.shape) return false;
			if (givenum != _o_.givenum) return false;
			if (acceptnum != _o_.acceptnum) return false;
			if (actiontype != _o_.actiontype) return false;
			if (requesttime != _o_.requesttime) return false;
			if (itemid != _o_.itemid) return false;
			if (itemlevel != _o_.itemlevel) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += (int)roleid;
		_h_ += rolename.hashCode();
		_h_ += (int)targetroleid;
		_h_ += targetrolename.hashCode();
		_h_ += level;
		_h_ += school;
		_h_ += shape;
		_h_ += givenum;
		_h_ += acceptnum;
		_h_ += actiontype;
		_h_ += (int)requesttime;
		_h_ += itemid;
		_h_ += itemlevel;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(roleid).append(",");
		_sb_.append("T").append(rolename.length()).append(",");
		_sb_.append(targetroleid).append(",");
		_sb_.append("T").append(targetrolename.length()).append(",");
		_sb_.append(level).append(",");
		_sb_.append(school).append(",");
		_sb_.append(shape).append(",");
		_sb_.append(givenum).append(",");
		_sb_.append(acceptnum).append(",");
		_sb_.append(actiontype).append(",");
		_sb_.append(requesttime).append(",");
		_sb_.append(itemid).append(",");
		_sb_.append(itemlevel).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

}

