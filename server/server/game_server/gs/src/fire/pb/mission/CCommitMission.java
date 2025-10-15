package fire.pb.mission;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __CCommitMission__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class CCommitMission extends __CCommitMission__ {
	@Override
	protected void process() {
		// protocol handle
		final long roleid = gnet.link.Onlines.getInstance().findRoleid(this);
		if (roleid < 0)
			return;
		new TCommitMission(roleid, missionid, npckey, option).handling();
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 805444;

	public int getType() {
		return 805444;
	}

	public int missionid; // 任务id
	public long npckey; // npckey为npc的唯一ID
	public int option; // 选项id

	public CCommitMission() {
	}

	public CCommitMission(int _missionid_, long _npckey_, int _option_) {
		this.missionid = _missionid_;
		this.npckey = _npckey_;
		this.option = _option_;
	}

	public final boolean _validator_() {
		if (missionid < 1001 || missionid >  999999) return false;
		if (npckey < 0) return false;
		if (option < 0) return false;
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(missionid);
		_os_.marshal(npckey);
		_os_.marshal(option);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		missionid = _os_.unmarshal_int();
		npckey = _os_.unmarshal_long();
		option = _os_.unmarshal_int();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof CCommitMission) {
			CCommitMission _o_ = (CCommitMission)_o1_;
			if (missionid != _o_.missionid) return false;
			if (npckey != _o_.npckey) return false;
			if (option != _o_.option) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += missionid;
		_h_ += (int)npckey;
		_h_ += option;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(missionid).append(",");
		_sb_.append(npckey).append(",");
		_sb_.append(option).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(CCommitMission _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = missionid - _o_.missionid;
		if (0 != _c_) return _c_;
		_c_ = Long.signum(npckey - _o_.npckey);
		if (0 != _c_) return _c_;
		_c_ = option - _o_.option;
		if (0 != _c_) return _c_;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}
