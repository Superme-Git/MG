
package fire.msp.team;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __GRefreshTeamHugs__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class GRefreshTeamHugs extends __GRefreshTeamHugs__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 728904;

	public int getType() {
		return 728904;
	}

	public long teamid;
	public java.util.HashMap<Long,Long> hugs; // key=抱的人角色Id，value=被抱的人角色id

	public GRefreshTeamHugs() {
		hugs = new java.util.HashMap<Long,Long>();
	}

	public GRefreshTeamHugs(long _teamid_, java.util.HashMap<Long,Long> _hugs_) {
		this.teamid = _teamid_;
		this.hugs = _hugs_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(teamid);
		_os_.compact_uint32(hugs.size());
		for (java.util.Map.Entry<Long, Long> _e_ : hugs.entrySet()) {
			_os_.marshal(_e_.getKey());
			_os_.marshal(_e_.getValue());
		}
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		teamid = _os_.unmarshal_long();
		for (int size = _os_.uncompact_uint32(); size > 0; --size) {
			long _k_;
			_k_ = _os_.unmarshal_long();
			long _v_;
			_v_ = _os_.unmarshal_long();
			hugs.put(_k_, _v_);
		}
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof GRefreshTeamHugs) {
			GRefreshTeamHugs _o_ = (GRefreshTeamHugs)_o1_;
			if (teamid != _o_.teamid) return false;
			if (!hugs.equals(_o_.hugs)) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += (int)teamid;
		_h_ += hugs.hashCode();
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(teamid).append(",");
		_sb_.append(hugs).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

