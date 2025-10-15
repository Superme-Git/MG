
package fire.pb.battle;

import com.locojoy.base.Marshal.Marshal;
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public class SubResultItem implements Marshal {
	public int subskillid;
	public int subskillstarttime;
	public java.util.LinkedList<fire.pb.battle.DemoResult> resultlist;

	public SubResultItem() {
		resultlist = new java.util.LinkedList<fire.pb.battle.DemoResult>();
	}

	public SubResultItem(int _subskillid_, int _subskillstarttime_, java.util.LinkedList<fire.pb.battle.DemoResult> _resultlist_) {
		this.subskillid = _subskillid_;
		this.subskillstarttime = _subskillstarttime_;
		this.resultlist = _resultlist_;
	}

	public final boolean _validator_() {
		for (fire.pb.battle.DemoResult _v_ : resultlist)
			if (!_v_._validator_()) return false;
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		_os_.marshal(subskillid);
		_os_.marshal(subskillstarttime);
		_os_.compact_uint32(resultlist.size());
		for (fire.pb.battle.DemoResult _v_ : resultlist) {
			_os_.marshal(_v_);
		}
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		subskillid = _os_.unmarshal_int();
		subskillstarttime = _os_.unmarshal_int();
		for (int _size_ = _os_.uncompact_uint32(); _size_ > 0; --_size_) {
			fire.pb.battle.DemoResult _v_ = new fire.pb.battle.DemoResult();
			_v_.unmarshal(_os_);
			resultlist.add(_v_);
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof SubResultItem) {
			SubResultItem _o_ = (SubResultItem)_o1_;
			if (subskillid != _o_.subskillid) return false;
			if (subskillstarttime != _o_.subskillstarttime) return false;
			if (!resultlist.equals(_o_.resultlist)) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += subskillid;
		_h_ += subskillstarttime;
		_h_ += resultlist.hashCode();
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(subskillid).append(",");
		_sb_.append(subskillstarttime).append(",");
		_sb_.append(resultlist).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

}

