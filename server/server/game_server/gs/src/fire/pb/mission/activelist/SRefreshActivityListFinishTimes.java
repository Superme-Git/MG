
package fire.pb.mission.activelist;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __SRefreshActivityListFinishTimes__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class SRefreshActivityListFinishTimes extends __SRefreshActivityListFinishTimes__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 805485;

	public int getType() {
		return 805485;
	}

	public java.util.HashMap<Integer,fire.pb.mission.activelist.SimpleActivityInfo> activities; // key为活动id, value为活动信息
	public int activevalue; // 今日活跃度值
	public java.util.HashMap<Integer,Integer> chests; // key为宝箱id, value为是否领取,0未领取,1已领取
	public int recommend; // 当前推荐度
	public java.util.LinkedList<Integer> closeids; // 服务器关闭了这些活动

	public SRefreshActivityListFinishTimes() {
		activities = new java.util.HashMap<Integer,fire.pb.mission.activelist.SimpleActivityInfo>();
		chests = new java.util.HashMap<Integer,Integer>();
		closeids = new java.util.LinkedList<Integer>();
	}

	public SRefreshActivityListFinishTimes(java.util.HashMap<Integer,fire.pb.mission.activelist.SimpleActivityInfo> _activities_, int _activevalue_, java.util.HashMap<Integer,Integer> _chests_, int _recommend_, java.util.LinkedList<Integer> _closeids_) {
		this.activities = _activities_;
		this.activevalue = _activevalue_;
		this.chests = _chests_;
		this.recommend = _recommend_;
		this.closeids = _closeids_;
	}

	public final boolean _validator_() {
		for (java.util.Map.Entry<Integer, fire.pb.mission.activelist.SimpleActivityInfo> _e_ : activities.entrySet()) {
			if (!_e_.getValue()._validator_()) return false;
		}
		if (activevalue < 0) return false;
		if (recommend < 0) return false;
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.compact_uint32(activities.size());
		for (java.util.Map.Entry<Integer, fire.pb.mission.activelist.SimpleActivityInfo> _e_ : activities.entrySet()) {
			_os_.marshal(_e_.getKey());
			_os_.marshal(_e_.getValue());
		}
		_os_.marshal(activevalue);
		_os_.compact_uint32(chests.size());
		for (java.util.Map.Entry<Integer, Integer> _e_ : chests.entrySet()) {
			_os_.marshal(_e_.getKey());
			_os_.marshal(_e_.getValue());
		}
		_os_.marshal(recommend);
		_os_.compact_uint32(closeids.size());
		for (Integer _v_ : closeids) {
			_os_.marshal(_v_);
		}
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		for (int size = _os_.uncompact_uint32(); size > 0; --size) {
			int _k_;
			_k_ = _os_.unmarshal_int();
			fire.pb.mission.activelist.SimpleActivityInfo _v_ = new fire.pb.mission.activelist.SimpleActivityInfo();
			_v_.unmarshal(_os_);
			activities.put(_k_, _v_);
		}
		activevalue = _os_.unmarshal_int();
		for (int size = _os_.uncompact_uint32(); size > 0; --size) {
			int _k_;
			_k_ = _os_.unmarshal_int();
			int _v_;
			_v_ = _os_.unmarshal_int();
			chests.put(_k_, _v_);
		}
		recommend = _os_.unmarshal_int();
		for (int _size_ = _os_.uncompact_uint32(); _size_ > 0; --_size_) {
			int _v_;
			_v_ = _os_.unmarshal_int();
			closeids.add(_v_);
		}
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof SRefreshActivityListFinishTimes) {
			SRefreshActivityListFinishTimes _o_ = (SRefreshActivityListFinishTimes)_o1_;
			if (!activities.equals(_o_.activities)) return false;
			if (activevalue != _o_.activevalue) return false;
			if (!chests.equals(_o_.chests)) return false;
			if (recommend != _o_.recommend) return false;
			if (!closeids.equals(_o_.closeids)) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += activities.hashCode();
		_h_ += activevalue;
		_h_ += chests.hashCode();
		_h_ += recommend;
		_h_ += closeids.hashCode();
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(activities).append(",");
		_sb_.append(activevalue).append(",");
		_sb_.append(chests).append(",");
		_sb_.append(recommend).append(",");
		_sb_.append(closeids).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

