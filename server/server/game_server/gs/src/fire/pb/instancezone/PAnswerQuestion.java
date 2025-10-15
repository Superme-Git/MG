package fire.pb.instancezone;

import fire.pb.instancezone.event.AnswerQuestionEvent;
import fire.pb.mission.Squestions;
import fire.pb.npc.CAnswerQuestion;
import fire.pb.npc.QuestionManager;

public class PAnswerQuestion extends mkdb.Procedure
{
	private final long roleId;
	private final long npckey;
	private final int questionid;
	private final int answerid;
	public PAnswerQuestion(long roleId, CAnswerQuestion protocol)
	{
		this.roleId = roleId;
		this.questionid = protocol.questionid;
		this.answerid = protocol.answerid;
		this.npckey = protocol.npckey;
	}
	public PAnswerQuestion(final long roleId, final int questionid, final int answerid) {
		this.roleId = roleId;
		this.questionid = questionid;
		this.answerid = answerid;
		this.npckey = 0;
	}
	
	@Override
	protected boolean process() throws Exception {
		Squestions question = QuestionManager.getInstance().getAllQuestions().get(questionid);
		if (question == null)
			return false;
		InstanceZone instzone = InstanceZoneFactory.getRoleCurInstanceZone(roleId, false);
		if (instzone == null)
			return false;
		boolean correct = (answerid == question.getCorrect());
		AnswerQuestionEvent event = new AnswerQuestionEvent(roleId, question, answerid, correct, npckey);
		return instzone.processEvent(event);
	}
	
}
