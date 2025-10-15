
package fire.pb.npc;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __CActivityAnswerQuestionHelp__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}


public class CActivityAnswerQuestionHelp extends __CActivityAnswerQuestionHelp__ {
	@Override
	protected void process() {
		// protocol handle
		
		final long roleid = gnet.link.Onlines.getInstance().findRoleid(this);
		if (roleid < 0)
			return;
		mkdb.Procedure activityanswerquestionhelp = new mkdb.Procedure()
		{
			@Override
			protected boolean process()
			{
				final xbean.ClanInfo factionInfo = fire.pb.clan.ClanUtils.getClanInfoById(roleid, true);
				if (null == factionInfo)
				{
					fire.pb.talk.MessageMgr.sendMsgNotify(roleid, 160420, null);
					return true;
				}	
				
				fire.pb.mission.activelist.RoleLiveness actrole = fire.pb.mission.activelist.RoleLiveness.getRoleLiveness(roleid, false);
				if (actrole != null)
				{
					xbean.ActivityQuestion activityquestion = xtable.Roleid2activityquestion.get(roleid);
					
					final long now = java.util.Calendar.getInstance().getTimeInMillis();
					if (activityquestion == null)
					{
						return true;
					}	
					else
					{
						long lasttime = activityquestion.getActivityquestionstarttime();
						
						if (!fire.pb.util.DateValidate.inTheSameDay(lasttime, now)) //如果不是同一天by changhao
						{
							return true;
						}
						
						if (activityquestion.getHelptimes() < 3)
						{
							activityquestion.setHelptimes(activityquestion.getHelptimes() + 1);
							
							mkdb.Procedure.pexecuteWhileCommit(new PSendActivityAnswerQuestionHelp(roleid, questionid));
							
							SActivityAnswerQuestionHelp msg = new SActivityAnswerQuestionHelp();
							msg.helpnum = activityquestion.getHelptimes();
							mkdb.Procedure.psendWhileCommit(roleid, msg);
						}
						else
						{
							fire.pb.talk.MessageMgr.sendMsgNotify(roleid, 160421, null);	
						}
					}
				}
				
				return true;
			}
		};
		
		activityanswerquestionhelp.submit();		
		
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 795532;

	public int getType() {
		return 795532;
	}

	public int questionid;

	public CActivityAnswerQuestionHelp() {
	}

	public CActivityAnswerQuestionHelp(int _questionid_) {
		this.questionid = _questionid_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(questionid);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		questionid = _os_.unmarshal_int();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof CActivityAnswerQuestionHelp) {
			CActivityAnswerQuestionHelp _o_ = (CActivityAnswerQuestionHelp)_o1_;
			if (questionid != _o_.questionid) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += questionid;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(questionid).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(CActivityAnswerQuestionHelp _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = questionid - _o_.questionid;
		if (0 != _c_) return _c_;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

