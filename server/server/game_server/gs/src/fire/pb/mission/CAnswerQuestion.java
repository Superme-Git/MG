
package fire.pb.mission;

import fire.pb.mission.util.AnswerCommitParam;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __CAnswerQuestion__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class CAnswerQuestion extends __CAnswerQuestion__ {
	@Override
	protected void process() {
		// protocol handle
		final long roleid = gnet.link.Onlines.getInstance().findRoleid( this );
		if ( roleid < 0 )
			return;
		if ( !fire.pb.map.SceneNpcManager.checkDistance( npckey, roleid ) )
			return;
		new PCommitMajorMission(roleid, missionid, new AnswerCommitParam(npckey,answerid ), true ).submit();
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 805448;

	public int getType() {
		return 805448;
	}

	public int missionid; // 任务id
	public long npckey; // npc的key
	public int answerid; // 回答的答案

	public CAnswerQuestion() {
	}

	public CAnswerQuestion(int _missionid_, long _npckey_, int _answerid_) {
		this.missionid = _missionid_;
		this.npckey = _npckey_;
		this.answerid = _answerid_;
	}

	public final boolean _validator_() {
		if (missionid < 1001 || missionid >  999999) return false;
		if (answerid < 0 || answerid > 5) return false;
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(missionid);
		_os_.marshal(npckey);
		_os_.marshal(answerid);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		missionid = _os_.unmarshal_int();
		npckey = _os_.unmarshal_long();
		answerid = _os_.unmarshal_int();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof CAnswerQuestion) {
			CAnswerQuestion _o_ = (CAnswerQuestion)_o1_;
			if (missionid != _o_.missionid) return false;
			if (npckey != _o_.npckey) return false;
			if (answerid != _o_.answerid) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += missionid;
		_h_ += (int)npckey;
		_h_ += answerid;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(missionid).append(",");
		_sb_.append(npckey).append(",");
		_sb_.append(answerid).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(CAnswerQuestion _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = missionid - _o_.missionid;
		if (0 != _c_) return _c_;
		_c_ = Long.signum(npckey - _o_.npckey);
		if (0 != _c_) return _c_;
		_c_ = answerid - _o_.answerid;
		if (0 != _c_) return _c_;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

