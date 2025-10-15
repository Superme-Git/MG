
package fire.pb.ranklist;

import com.locojoy.base.Marshal.Marshal;
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

/** 冰封王座副本排行榜
*/
public class BingFengRankData implements Marshal {
	public int shool; // 职业
	public int rank; // 排名
	public long roleid; // roleid
	public java.lang.String rolename; // 玩家的名字
	public int stage; // 关数
	public int times; // 耗时

	public BingFengRankData() {
		rolename = "";
	}

	public BingFengRankData(int _shool_, int _rank_, long _roleid_, java.lang.String _rolename_, int _stage_, int _times_) {
		this.shool = _shool_;
		this.rank = _rank_;
		this.roleid = _roleid_;
		this.rolename = _rolename_;
		this.stage = _stage_;
		this.times = _times_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		_os_.marshal(shool);
		_os_.marshal(rank);
		_os_.marshal(roleid);
		_os_.marshal(rolename, "UTF-16LE");
		_os_.marshal(stage);
		_os_.marshal(times);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		shool = _os_.unmarshal_int();
		rank = _os_.unmarshal_int();
		roleid = _os_.unmarshal_long();
		rolename = _os_.unmarshal_String("UTF-16LE");
		stage = _os_.unmarshal_int();
		times = _os_.unmarshal_int();
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof BingFengRankData) {
			BingFengRankData _o_ = (BingFengRankData)_o1_;
			if (shool != _o_.shool) return false;
			if (rank != _o_.rank) return false;
			if (roleid != _o_.roleid) return false;
			if (!rolename.equals(_o_.rolename)) return false;
			if (stage != _o_.stage) return false;
			if (times != _o_.times) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += shool;
		_h_ += rank;
		_h_ += (int)roleid;
		_h_ += rolename.hashCode();
		_h_ += stage;
		_h_ += times;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(shool).append(",");
		_sb_.append(rank).append(",");
		_sb_.append(roleid).append(",");
		_sb_.append("T").append(rolename.length()).append(",");
		_sb_.append(stage).append(",");
		_sb_.append(times).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

}

