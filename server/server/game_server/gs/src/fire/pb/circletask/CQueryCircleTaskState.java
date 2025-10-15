
package fire.pb.circletask;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __CQueryCircleTaskState__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class CQueryCircleTaskState extends __CQueryCircleTaskState__ {
	@Override
	protected void process() {
		// protocol handle
		final long roleid = gnet.link.Onlines.getInstance().findRoleid(this);
		if (roleid < 0)
			return;
		final CircleTask sq = new CircleTask(roleid, true);
		SQueryCircleTaskState sendmsg = new SQueryCircleTaskState();
		int state = sq.getCircleTaskState(questid);
		sendmsg.questid = questid;
		sendmsg.state = state;
		gnet.link.Onlines.getInstance().send(roleid, sendmsg);
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 807452;

	public int getType() {
		return 807452;
	}

	public int questid; // 任务id

	public CQueryCircleTaskState() {
	}

	public CQueryCircleTaskState(int _questid_) {
		this.questid = _questid_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(questid);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		questid = _os_.unmarshal_int();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof CQueryCircleTaskState) {
			CQueryCircleTaskState _o_ = (CQueryCircleTaskState)_o1_;
			if (questid != _o_.questid) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += questid;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(questid).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(CQueryCircleTaskState _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = questid - _o_.questid;
		if (0 != _c_) return _c_;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

