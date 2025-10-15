
package fire.pb.friends;

import com.locojoy.base.Marshal.Marshal;
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public class InfoBean implements Marshal {
	public long roleid;
	public java.lang.String name;
	public short rolelevel;
	public byte school;
	public byte online; // 0=不在线 1=在线
	public int shape; // 头像
	public byte camp; // 0中立 1部落 2联盟
	public short relation; // 好友关系 位与0=普通好友 1=结婚2=结交4=师徒
	public long factionid; // 公会id
	public java.lang.String factionname; // 公会名称

	public InfoBean() {
		name = "";
		factionname = "";
	}

	public InfoBean(long _roleid_, java.lang.String _name_, short _rolelevel_, byte _school_, byte _online_, int _shape_, byte _camp_, short _relation_, long _factionid_, java.lang.String _factionname_) {
		this.roleid = _roleid_;
		this.name = _name_;
		this.rolelevel = _rolelevel_;
		this.school = _school_;
		this.online = _online_;
		this.shape = _shape_;
		this.camp = _camp_;
		this.relation = _relation_;
		this.factionid = _factionid_;
		this.factionname = _factionname_;
	}

	public final boolean _validator_() {
		if (roleid < 0) return false;
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		_os_.marshal(roleid);
		_os_.marshal(name, "UTF-16LE");
		_os_.marshal(rolelevel);
		_os_.marshal(school);
		_os_.marshal(online);
		_os_.marshal(shape);
		_os_.marshal(camp);
		_os_.marshal(relation);
		_os_.marshal(factionid);
		_os_.marshal(factionname, "UTF-16LE");
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		roleid = _os_.unmarshal_long();
		name = _os_.unmarshal_String("UTF-16LE");
		rolelevel = _os_.unmarshal_short();
		school = _os_.unmarshal_byte();
		online = _os_.unmarshal_byte();
		shape = _os_.unmarshal_int();
		camp = _os_.unmarshal_byte();
		relation = _os_.unmarshal_short();
		factionid = _os_.unmarshal_long();
		factionname = _os_.unmarshal_String("UTF-16LE");
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof InfoBean) {
			InfoBean _o_ = (InfoBean)_o1_;
			if (roleid != _o_.roleid) return false;
			if (!name.equals(_o_.name)) return false;
			if (rolelevel != _o_.rolelevel) return false;
			if (school != _o_.school) return false;
			if (online != _o_.online) return false;
			if (shape != _o_.shape) return false;
			if (camp != _o_.camp) return false;
			if (relation != _o_.relation) return false;
			if (factionid != _o_.factionid) return false;
			if (!factionname.equals(_o_.factionname)) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += (int)roleid;
		_h_ += name.hashCode();
		_h_ += rolelevel;
		_h_ += (int)school;
		_h_ += (int)online;
		_h_ += shape;
		_h_ += (int)camp;
		_h_ += relation;
		_h_ += (int)factionid;
		_h_ += factionname.hashCode();
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(roleid).append(",");
		_sb_.append("T").append(name.length()).append(",");
		_sb_.append(rolelevel).append(",");
		_sb_.append(school).append(",");
		_sb_.append(online).append(",");
		_sb_.append(shape).append(",");
		_sb_.append(camp).append(",");
		_sb_.append(relation).append(",");
		_sb_.append(factionid).append(",");
		_sb_.append("T").append(factionname.length()).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

}

