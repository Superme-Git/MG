
package fire.pb.circletask;





import fire.pb.circletask.CircTaskClass;
import fire.pb.circletask.SpecialQuestState;
import fire.pb.map.Transfer;
import fire.pb.move.SRoleEnterScene;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __CReqGotoPatrol__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class CReqGotoPatrol extends __CReqGotoPatrol__ {
	@Override
	protected void process() {
		final long roleid = gnet.link.Onlines.getInstance().findRoleid(this);	
		if (roleid < 0)
			return;
		for(Integer curType : CircleTaskManager.getInstance().getCircTaskTypes()) {
			final xbean.CircleTaskInfo sqinfo = fire.pb.circletask.CircleTask.selectSpecialQuestInfo(roleid, curType);
			if (null == sqinfo)
				continue;
			if (sqinfo.getQuestclass() == CircTaskClass.CircTask_Patrol) {
				if (sqinfo.getQueststate() == SpecialQuestState.UNDONE)
					Transfer.justGotoRandom(roleid, sqinfo.getDstmapid(),SRoleEnterScene.QUEST);
			}
		}
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 807446;

	public int getType() {
		return 807446;
	}


	public CReqGotoPatrol() {
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof CReqGotoPatrol) {
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(CReqGotoPatrol _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

