
package fire.pb.mission;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __CMissionDialogEnd__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class CMissionDialogEnd extends __CMissionDialogEnd__ {
	@Override
	protected void process() {
		// protocol handle
		final long roleid = gnet.link.Onlines.getInstance().findRoleid( this );
		if ( roleid < 0 )
			return;
		final xbean.ShowingMission st = xtable.Showingmission.select( roleid );
		if ( st == null )
			return;
		MissionConfig conf = MissionManager.getInstance().getMissionConfig(st.getMissionid());
		if (conf == null)
			return;
		conf.afterNote(roleid, st.getIsleader());
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 805458;

	public int getType() {
		return 805458;
	}

	public int missionid; // 任务id

	public CMissionDialogEnd() {
	}

	public CMissionDialogEnd(int _missionid_) {
		this.missionid = _missionid_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(missionid);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		missionid = _os_.unmarshal_int();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof CMissionDialogEnd) {
			CMissionDialogEnd _o_ = (CMissionDialogEnd)_o1_;
			if (missionid != _o_.missionid) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += missionid;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(missionid).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(CMissionDialogEnd _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = missionid - _o_.missionid;
		if (0 != _c_) return _c_;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

