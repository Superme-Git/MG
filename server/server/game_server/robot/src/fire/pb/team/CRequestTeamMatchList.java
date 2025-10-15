
package fire.pb.team;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __CRequestTeamMatchList__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class CRequestTeamMatchList extends __CRequestTeamMatchList__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 794509;

	public int getType() {
		return 794509;
	}

	public int targetid; // 目标ID by changhao
	public long startteamid; // 起始队伍ID （0的话就从头取）by changhao
	public int num; // 取起始队伍id后面的num个数据  by changhao

	public CRequestTeamMatchList() {
	}

	public CRequestTeamMatchList(int _targetid_, long _startteamid_, int _num_) {
		this.targetid = _targetid_;
		this.startteamid = _startteamid_;
		this.num = _num_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(targetid);
		_os_.marshal(startteamid);
		_os_.marshal(num);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		targetid = _os_.unmarshal_int();
		startteamid = _os_.unmarshal_long();
		num = _os_.unmarshal_int();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof CRequestTeamMatchList) {
			CRequestTeamMatchList _o_ = (CRequestTeamMatchList)_o1_;
			if (targetid != _o_.targetid) return false;
			if (startteamid != _o_.startteamid) return false;
			if (num != _o_.num) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += targetid;
		_h_ += (int)startteamid;
		_h_ += num;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(targetid).append(",");
		_sb_.append(startteamid).append(",");
		_sb_.append(num).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(CRequestTeamMatchList _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = targetid - _o_.targetid;
		if (0 != _c_) return _c_;
		_c_ = Long.signum(startteamid - _o_.startteamid);
		if (0 != _c_) return _c_;
		_c_ = num - _o_.num;
		if (0 != _c_) return _c_;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

