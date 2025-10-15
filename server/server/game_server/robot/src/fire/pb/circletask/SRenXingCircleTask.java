
package fire.pb.circletask;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __SRenXingCircleTask__ extends mkio.Protocol { }

/** 任性任务相关
*/
// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class SRenXingCircleTask extends __SRenXingCircleTask__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 807450;

	public int getType() {
		return 807450;
	}

	public int serviceid; // 该服务id
	public int questid; // 任务id
	public int renxingtimes; // 已经任性的次数
	public long npckey; // npckey

	public SRenXingCircleTask() {
	}

	public SRenXingCircleTask(int _serviceid_, int _questid_, int _renxingtimes_, long _npckey_) {
		this.serviceid = _serviceid_;
		this.questid = _questid_;
		this.renxingtimes = _renxingtimes_;
		this.npckey = _npckey_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(serviceid);
		_os_.marshal(questid);
		_os_.marshal(renxingtimes);
		_os_.marshal(npckey);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		serviceid = _os_.unmarshal_int();
		questid = _os_.unmarshal_int();
		renxingtimes = _os_.unmarshal_int();
		npckey = _os_.unmarshal_long();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof SRenXingCircleTask) {
			SRenXingCircleTask _o_ = (SRenXingCircleTask)_o1_;
			if (serviceid != _o_.serviceid) return false;
			if (questid != _o_.questid) return false;
			if (renxingtimes != _o_.renxingtimes) return false;
			if (npckey != _o_.npckey) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += serviceid;
		_h_ += questid;
		_h_ += renxingtimes;
		_h_ += (int)npckey;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(serviceid).append(",");
		_sb_.append(questid).append(",");
		_sb_.append(renxingtimes).append(",");
		_sb_.append(npckey).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(SRenXingCircleTask _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = serviceid - _o_.serviceid;
		if (0 != _c_) return _c_;
		_c_ = questid - _o_.questid;
		if (0 != _c_) return _c_;
		_c_ = renxingtimes - _o_.renxingtimes;
		if (0 != _c_) return _c_;
		_c_ = Long.signum(npckey - _o_.npckey);
		if (0 != _c_) return _c_;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

