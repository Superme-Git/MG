
package fire.pb.npc;

import fire.pb.activity.award.RewardMgr;

import java.util.HashMap;
import java.util.Map;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __CGrabActivityReward__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class CGrabActivityReward extends __CGrabActivityReward__ {
	@Override
	protected void process() {
		// protocol handle
		
		final long roleid = gnet.link.Onlines.getInstance().findRoleid(this);
		if (roleid < 0)
			return;
		mkdb.Procedure grabactivityreward = new mkdb.Procedure()
		{
			@Override
			protected boolean process()
			{
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
						
						if (!fire.pb.util.DateValidate.inTheSameDay(lasttime, now)) //如果不是同一天走人by changhao
						{
							return true;
						}
						
						if (activityquestion.getGrabreward() == 1)
						{
							fire.pb.common.SCommon c = fire.pb.main.ConfigManager.getInstance().getConf(fire.pb.common.SCommon.class).get(236);
							int rewardid = Integer.parseInt(c.getValue());
							
							//添加回答正确的参数
							Map<String, Object> paras = new HashMap<String, Object>(10);
							paras.put(RewardMgr.ANSWER_RIGHT_TIMES, activityquestion.getAnswerrighttimes());
							fire.pb.activity.award.RewardMgr.getInstance().distributeAllAward(roleid, rewardid, paras, fire.log.enums.YYLoggerTuJingEnum.tujing_Value_grab, 0,fire.pb.PAddExpProc.OTHER_QUEST, "答题");
							activityquestion.setGrabreward(2);
							
							fire.pb.talk.MessageMgr.sendMsgNotify(roleid, 160417, null);
														
							SGrabActivityReward msg = new SGrabActivityReward();
							mkdb.Procedure.psendWhileCommit(roleid, msg);						
						}
						else if (activityquestion.getGrabreward() == 2)
						{
							fire.pb.talk.MessageMgr.sendMsgNotify(roleid, 160418, null);	
						}
						else if (activityquestion.getGrabreward() == 3)
						{
							fire.pb.talk.MessageMgr.sendMsgNotify(roleid, 160416, null);								
						}
					}
				}
				
				return true;
			}
		};
		
		grabactivityreward.submit();		
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 795531;

	public int getType() {
		return 795531;
	}


	public CGrabActivityReward() {
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
		if (_o1_ instanceof CGrabActivityReward) {
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

	public int compareTo(CGrabActivityReward _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

