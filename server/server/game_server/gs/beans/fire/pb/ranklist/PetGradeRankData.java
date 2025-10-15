
package fire.pb.ranklist;

import com.locojoy.base.Marshal.Marshal;
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

/** 宠排榜
*/
public class PetGradeRankData implements Marshal {
	public long roleid; // roleid
	public long uniquepetid; // 宠物的唯一id
	public java.lang.String nickname; // 名字
	public java.lang.String petname; // 宠物名字
	public int petgrade; // 宠物评分
	public int rank; // 排名
	public int colour; // 宠物颜色

	public PetGradeRankData() {
		nickname = "";
		petname = "";
	}

	public PetGradeRankData(long _roleid_, long _uniquepetid_, java.lang.String _nickname_, java.lang.String _petname_, int _petgrade_, int _rank_, int _colour_) {
		this.roleid = _roleid_;
		this.uniquepetid = _uniquepetid_;
		this.nickname = _nickname_;
		this.petname = _petname_;
		this.petgrade = _petgrade_;
		this.rank = _rank_;
		this.colour = _colour_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		_os_.marshal(roleid);
		_os_.marshal(uniquepetid);
		_os_.marshal(nickname, "UTF-16LE");
		_os_.marshal(petname, "UTF-16LE");
		_os_.marshal(petgrade);
		_os_.marshal(rank);
		_os_.marshal(colour);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		roleid = _os_.unmarshal_long();
		uniquepetid = _os_.unmarshal_long();
		nickname = _os_.unmarshal_String("UTF-16LE");
		petname = _os_.unmarshal_String("UTF-16LE");
		petgrade = _os_.unmarshal_int();
		rank = _os_.unmarshal_int();
		colour = _os_.unmarshal_int();
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof PetGradeRankData) {
			PetGradeRankData _o_ = (PetGradeRankData)_o1_;
			if (roleid != _o_.roleid) return false;
			if (uniquepetid != _o_.uniquepetid) return false;
			if (!nickname.equals(_o_.nickname)) return false;
			if (!petname.equals(_o_.petname)) return false;
			if (petgrade != _o_.petgrade) return false;
			if (rank != _o_.rank) return false;
			if (colour != _o_.colour) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += (int)roleid;
		_h_ += (int)uniquepetid;
		_h_ += nickname.hashCode();
		_h_ += petname.hashCode();
		_h_ += petgrade;
		_h_ += rank;
		_h_ += colour;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(roleid).append(",");
		_sb_.append(uniquepetid).append(",");
		_sb_.append("T").append(nickname.length()).append(",");
		_sb_.append("T").append(petname.length()).append(",");
		_sb_.append(petgrade).append(",");
		_sb_.append(rank).append(",");
		_sb_.append(colour).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

}

