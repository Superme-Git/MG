
package fire.pb.circletask.anye;

import com.locojoy.base.Marshal.Marshal;
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

/** 暗夜马戏团任务相关
*/
public class AnYeTask implements Marshal , Comparable<AnYeTask>{
	public int pos; // 任务栏位
	public int id; // 任务id
	public int kind; // 任务类型
	public int state; // 任务状态
	public int dstitemid; // 目的道具的id
	public int dstitemnum; // 目的道具的数量
	public long dstnpckey; // 目的npc的key
	public int dstnpcid; // 目的npc的id
	public int legend; // 传说状态 0不能传说 1可以传说 2已传说 3成功 4失败
	public long legendtime; // 传说持续时间
	public long legendend; // 传说结束时间

	public AnYeTask() {
	}

	public AnYeTask(int _pos_, int _id_, int _kind_, int _state_, int _dstitemid_, int _dstitemnum_, long _dstnpckey_, int _dstnpcid_, int _legend_, long _legendtime_, long _legendend_) {
		this.pos = _pos_;
		this.id = _id_;
		this.kind = _kind_;
		this.state = _state_;
		this.dstitemid = _dstitemid_;
		this.dstitemnum = _dstitemnum_;
		this.dstnpckey = _dstnpckey_;
		this.dstnpcid = _dstnpcid_;
		this.legend = _legend_;
		this.legendtime = _legendtime_;
		this.legendend = _legendend_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		_os_.marshal(pos);
		_os_.marshal(id);
		_os_.marshal(kind);
		_os_.marshal(state);
		_os_.marshal(dstitemid);
		_os_.marshal(dstitemnum);
		_os_.marshal(dstnpckey);
		_os_.marshal(dstnpcid);
		_os_.marshal(legend);
		_os_.marshal(legendtime);
		_os_.marshal(legendend);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		pos = _os_.unmarshal_int();
		id = _os_.unmarshal_int();
		kind = _os_.unmarshal_int();
		state = _os_.unmarshal_int();
		dstitemid = _os_.unmarshal_int();
		dstitemnum = _os_.unmarshal_int();
		dstnpckey = _os_.unmarshal_long();
		dstnpcid = _os_.unmarshal_int();
		legend = _os_.unmarshal_int();
		legendtime = _os_.unmarshal_long();
		legendend = _os_.unmarshal_long();
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof AnYeTask) {
			AnYeTask _o_ = (AnYeTask)_o1_;
			if (pos != _o_.pos) return false;
			if (id != _o_.id) return false;
			if (kind != _o_.kind) return false;
			if (state != _o_.state) return false;
			if (dstitemid != _o_.dstitemid) return false;
			if (dstitemnum != _o_.dstitemnum) return false;
			if (dstnpckey != _o_.dstnpckey) return false;
			if (dstnpcid != _o_.dstnpcid) return false;
			if (legend != _o_.legend) return false;
			if (legendtime != _o_.legendtime) return false;
			if (legendend != _o_.legendend) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += pos;
		_h_ += id;
		_h_ += kind;
		_h_ += state;
		_h_ += dstitemid;
		_h_ += dstitemnum;
		_h_ += (int)dstnpckey;
		_h_ += dstnpcid;
		_h_ += legend;
		_h_ += (int)legendtime;
		_h_ += (int)legendend;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(pos).append(",");
		_sb_.append(id).append(",");
		_sb_.append(kind).append(",");
		_sb_.append(state).append(",");
		_sb_.append(dstitemid).append(",");
		_sb_.append(dstitemnum).append(",");
		_sb_.append(dstnpckey).append(",");
		_sb_.append(dstnpcid).append(",");
		_sb_.append(legend).append(",");
		_sb_.append(legendtime).append(",");
		_sb_.append(legendend).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(AnYeTask _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = pos - _o_.pos;
		if (0 != _c_) return _c_;
		_c_ = id - _o_.id;
		if (0 != _c_) return _c_;
		_c_ = kind - _o_.kind;
		if (0 != _c_) return _c_;
		_c_ = state - _o_.state;
		if (0 != _c_) return _c_;
		_c_ = dstitemid - _o_.dstitemid;
		if (0 != _c_) return _c_;
		_c_ = dstitemnum - _o_.dstitemnum;
		if (0 != _c_) return _c_;
		_c_ = Long.signum(dstnpckey - _o_.dstnpckey);
		if (0 != _c_) return _c_;
		_c_ = dstnpcid - _o_.dstnpcid;
		if (0 != _c_) return _c_;
		_c_ = legend - _o_.legend;
		if (0 != _c_) return _c_;
		_c_ = Long.signum(legendtime - _o_.legendtime);
		if (0 != _c_) return _c_;
		_c_ = Long.signum(legendend - _o_.legendend);
		if (0 != _c_) return _c_;
		return _c_;
	}

}

