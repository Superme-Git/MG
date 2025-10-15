
package fire.pb.mission;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __SDailyTaskStateList__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class SDailyTaskStateList extends __SDailyTaskStateList__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 805520;

	public int getType() {
		return 805520;
	}

	public java.util.LinkedList<fire.pb.mission.DailyTaskState> notifylist;

	public SDailyTaskStateList() {
		notifylist = new java.util.LinkedList<fire.pb.mission.DailyTaskState>();
	}

	public SDailyTaskStateList(java.util.LinkedList<fire.pb.mission.DailyTaskState> _notifylist_) {
		this.notifylist = _notifylist_;
	}

	public final boolean _validator_() {
		for (fire.pb.mission.DailyTaskState _v_ : notifylist)
			if (!_v_._validator_()) return false;
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.compact_uint32(notifylist.size());
		for (fire.pb.mission.DailyTaskState _v_ : notifylist) {
			_os_.marshal(_v_);
		}
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		for (int _size_ = _os_.uncompact_uint32(); _size_ > 0; --_size_) {
			fire.pb.mission.DailyTaskState _v_ = new fire.pb.mission.DailyTaskState();
			_v_.unmarshal(_os_);
			notifylist.add(_v_);
		}
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof SDailyTaskStateList) {
			SDailyTaskStateList _o_ = (SDailyTaskStateList)_o1_;
			if (!notifylist.equals(_o_.notifylist)) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += notifylist.hashCode();
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(notifylist).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

