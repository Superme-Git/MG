package fire.pb.mission.instance;

import mkdb.Procedure;
import mkdb.Transaction;
import fire.pb.mission.Squestions;
import fire.pb.npc.CAnswerQuestion;
import fire.pb.npc.QuestionManager;
import fire.pb.npc.SAskQuestion;

public class PAnswerQuestion extends Procedure {
	private final long roleId;
	private final int questionid;
	private final int answerid;
	public PAnswerQuestion(long roleId, CAnswerQuestion protocol)
	{
		this.roleId = roleId;
		this.questionid = protocol.questionid;
		this.answerid = protocol.answerid;
	}
	public PAnswerQuestion(final long roleId, final int questionid, final int answerid) {
		this.roleId = roleId;
		this.questionid = questionid;
		this.answerid = answerid;
	}
	
	@Override
	protected boolean process() throws Exception
	{
		xbean.InstanceInfoCol taskInfo = xtable.Roleinstancetask.select(roleId);
		if (taskInfo == null) {
			fire.pb.talk.MessageMgr.psendMsgNotifyWhileRollback(roleId, 144838, null);
			return false;
		}
		
		xbean.InstanceTaskInfo info = taskInfo.getInstinfo().get(taskInfo.getLastinstanceid());
		if (info == null) {
			fire.pb.talk.MessageMgr.psendMsgNotifyWhileRollback(roleId, 144838, null);
			return false;
		}
		
		final long instanceOnlyId = info.getInstanceonlyid();
		
		xbean.InstanceInfo instInfo = xtable.Instancetask.get(instanceOnlyId);
		if (instInfo == null) {
			return false;
		}
		
		Squestions question = QuestionManager.getInstance().getAllQuestions().get(questionid);
		if (question == null)
			return false;
		
		boolean correct = (answerid == question.getCorrect());
		SAskQuestion sask = new SAskQuestion();
		sask.lastresult = (byte)(correct? 1 : -1);
		sask.questionid = -1;
		if(Transaction.current() == null)
			gnet.link.Onlines.getInstance().send(roleId, sask);
		else
			mkdb.Procedure.psend(roleId, sask);
		
		InstanceManager.getInstance().doNextStep(instInfo, instInfo.getServiceid(), instInfo.getInstanceid(), instanceOnlyId);
		return true;
	}

}
