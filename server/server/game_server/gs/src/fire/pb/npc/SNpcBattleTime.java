
package fire.pb.npc;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __SNpcBattleTime__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class SNpcBattleTime extends __SNpcBattleTime__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 795669;

	public int getType() {
		return 795669;
	}

	public int npcid; // npcid
	public long npckey; // npckey
	public long usetime; // 匹配总时间
	public long lasttime; // 匹配结束时间

	public SNpcBattleTime() {
	}

	public SNpcBattleTime(int _npcid_, long _npckey_, long _usetime_, long _lasttime_) {
		this.npcid = _npcid_;
		this.npckey = _npckey_;
		this.usetime = _usetime_;
		this.lasttime = _lasttime_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(npcid);
		_os_.marshal(npckey);
		_os_.marshal(usetime);
		_os_.marshal(lasttime);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		npcid = _os_.unmarshal_int();
		npckey = _os_.unmarshal_long();
		usetime = _os_.unmarshal_long();
		lasttime = _os_.unmarshal_long();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof SNpcBattleTime) {
			SNpcBattleTime _o_ = (SNpcBattleTime)_o1_;
			if (npcid != _o_.npcid) return false;
			if (npckey != _o_.npckey) return false;
			if (usetime != _o_.usetime) return false;
			if (lasttime != _o_.lasttime) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += npcid;
		_h_ += (int)npckey;
		_h_ += (int)usetime;
		_h_ += (int)lasttime;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(npcid).append(",");
		_sb_.append(npckey).append(",");
		_sb_.append(usetime).append(",");
		_sb_.append(lasttime).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(SNpcBattleTime _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = npcid - _o_.npcid;
		if (0 != _c_) return _c_;
		_c_ = Long.signum(npckey - _o_.npckey);
		if (0 != _c_) return _c_;
		_c_ = Long.signum(usetime - _o_.usetime);
		if (0 != _c_) return _c_;
		_c_ = Long.signum(lasttime - _o_.lasttime);
		if (0 != _c_) return _c_;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

