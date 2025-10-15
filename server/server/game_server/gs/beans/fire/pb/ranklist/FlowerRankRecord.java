
package fire.pb.ranklist;

import com.locojoy.base.Marshal.Marshal;
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

/** 鲜花榜 by changhao
*/
public class FlowerRankRecord implements Marshal {
	public int rank; // 排名 by changhao
	public long roleid; // 角色ID by changhao
	public java.lang.String rolename; // 人物名称 by changhao
	public int school; // 职业 by changhao
	public long num; // 数量 by changhao

	public FlowerRankRecord() {
		rolename = "";
	}

	public FlowerRankRecord(int _rank_, long _roleid_, java.lang.String _rolename_, int _school_, long _num_) {
		this.rank = _rank_;
		this.roleid = _roleid_;
		this.rolename = _rolename_;
		this.school = _school_;
		this.num = _num_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		_os_.marshal(rank);
		_os_.marshal(roleid);
		_os_.marshal(rolename, "UTF-16LE");
		_os_.marshal(school);
		_os_.marshal(num);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		rank = _os_.unmarshal_int();
		roleid = _os_.unmarshal_long();
		rolename = _os_.unmarshal_String("UTF-16LE");
		school = _os_.unmarshal_int();
		num = _os_.unmarshal_long();
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof FlowerRankRecord) {
			FlowerRankRecord _o_ = (FlowerRankRecord)_o1_;
			if (rank != _o_.rank) return false;
			if (roleid != _o_.roleid) return false;
			if (!rolename.equals(_o_.rolename)) return false;
			if (school != _o_.school) return false;
			if (num != _o_.num) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += rank;
		_h_ += (int)roleid;
		_h_ += rolename.hashCode();
		_h_ += school;
		_h_ += (int)num;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(rank).append(",");
		_sb_.append(roleid).append(",");
		_sb_.append("T").append(rolename.length()).append(",");
		_sb_.append(school).append(",");
		_sb_.append(num).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

}

