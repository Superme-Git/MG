
package fire.pb.npc;
import fire.pb.mission.activelist.RoleLiveness;
// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __CRequestActivityAnswerQuestion__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class CRequestActivityAnswerQuestion extends __CRequestActivityAnswerQuestion__ {
	@Override
	protected void process() {
		// protocol handle
		final long roleid = gnet.link.Onlines.getInstance().findRoleid(this);
		if (roleid < 0)
			return;
		mkdb.Procedure requestteammatchlist = new mkdb.Procedure()
		{
			@Override
			protected boolean process()
			{
				fire.pb.mission.activelist.RoleLiveness actrole = fire.pb.mission.activelist.RoleLiveness.getRoleLiveness(roleid, false);
				if (actrole != null)
				{
					int count = actrole.getActiveNum(fire.pb.mission.activelist.RoleLiveness.ANSWER_QUESTION);
				
					fire.pb.activity.ActivityConfNew activity = RoleLiveness.getConfigActivity(fire.pb.mission.activelist.RoleLiveness.ANSWER_QUESTION);
					
					boolean start = fire.pb.activity.answerquestion.ActivityQuestionManager.getInstance().IsStart();
					if (count < activity.maxnum && start == true) //一天次数上限 by changhao
					{
						xbean.Properties prop = xtable.Properties.select(roleid);
						if (prop == null)
						{
							return false;
						}
						
						if (prop.getLevel() < activity.level)
						{
							return false;
						}
						
						xbean.ActivityQuestion activityquestion = xtable.Roleid2activityquestion.get(roleid);
						
						final long now = java.util.Calendar.getInstance().getTimeInMillis();
						if (activityquestion == null) //加入一个新的 by changhao
						{
							activityquestion = xbean.Pod.newActivityQuestion();
							xtable.Roleid2activityquestion.insert(roleid, activityquestion);
							
							fire.pb.activity.answerquestion.ActivityQuestionManager.getInstance().ResetActivityQuestionData(activityquestion, now, true);
						}	
						else
						{
							long lasttime = activityquestion.getActivityquestionstarttime();
							
							if (!fire.pb.util.DateValidate.inTheSameDay(lasttime, now)) //如果不是同一天要清空 by changhao
							{
								fire.pb.activity.answerquestion.ActivityQuestionManager.getInstance().ResetActivityQuestionData(activityquestion, now, true);	
							}
						}
						
						//发送题目 by changhao
						fire.pb.activity.answerquestion.ActivityQuestionManager.getInstance().sendQuestion(roleid, activityquestion, (byte)0);
					}
				}
				
				return true;
			}
		};
		
		requestteammatchlist.submit();		
	}
	
	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 795527;

	public int getType() {
		return 795527;
	}


	public CRequestActivityAnswerQuestion() {
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
		if (_o1_ instanceof CRequestActivityAnswerQuestion) {
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

	public int compareTo(CRequestActivityAnswerQuestion _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

