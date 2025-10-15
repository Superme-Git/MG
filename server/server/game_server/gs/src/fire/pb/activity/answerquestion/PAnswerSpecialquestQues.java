

package fire.pb.activity.answerquestion;

import mkdb.Procedure;
import fire.pb.map.Npc;
import fire.pb.map.SceneNpcManager;
import fire.pb.mission.Squestions;
import fire.pb.npc.QuestionManager;
import fire.pb.npc.SAskQuestion;


public class PAnswerSpecialquestQues extends Procedure {

	private final long roleid;
	private final long npckey;
	private final int xiangguanid;
	private final int questionid;
	
	
	public PAnswerSpecialquestQues(long roleId, long npckey, int xiangguanid, int questionid, int answerid) {

		this.roleid = roleId;
		this.npckey = npckey;
		this.xiangguanid = xiangguanid;
		this.questionid = questionid;
		
	}

	@Override
	protected boolean process() throws Exception {

		SAskQuestion sAskQuestion = new SAskQuestion();
		sAskQuestion.questiontype = SAskQuestion.SPECIALQUEST_ANSWER;
		sAskQuestion.xiangguanid = xiangguanid;
		sAskQuestion.npckey = npckey;
		Npc npc = SceneNpcManager.getNpcByKey(npckey);
		if (npc == null){
			fire.pb.talk.MessageMgr.sendMsgNotify(roleid, 143713, 0, null);
			return false;
		}
		
		sAskQuestion.lastresult = -1;
		int libid = QuestionManager.getInstance().getAllQuestions().get(questionid).questionsid;
		Squestions sq = QuestionManager.getInstance().getQuestionLibs().get(libid).randomQuestion();
		sAskQuestion.questionid = sq.id;
		sAskQuestion.lastresult = -1;
		
		psendWhileCommit(roleid, sAskQuestion);
		return true;
	}

	
}

