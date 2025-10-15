
package fire.pb.mission;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __CReqLandTask__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class CReqLandTask extends __CReqLandTask__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 805468;

	public int getType() {
		return 805468;
	}

	public int taskid; // 副本Id
	public int tasktype; // 战斗难度 1=简单模式  2=普通难度 3=困难模式

	public CReqLandTask() {
	}

	public CReqLandTask(int _taskid_, int _tasktype_) {
		this.taskid = _taskid_;
		this.tasktype = _tasktype_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(taskid);
		_os_.marshal(tasktype);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		taskid = _os_.unmarshal_int();
		tasktype = _os_.unmarshal_int();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof CReqLandTask) {
			CReqLandTask _o_ = (CReqLandTask)_o1_;
			if (taskid != _o_.taskid) return false;
			if (tasktype != _o_.tasktype) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += taskid;
		_h_ += tasktype;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(taskid).append(",");
		_sb_.append(tasktype).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(CReqLandTask _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = taskid - _o_.taskid;
		if (0 != _c_) return _c_;
		_c_ = tasktype - _o_.tasktype;
		if (0 != _c_) return _c_;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

