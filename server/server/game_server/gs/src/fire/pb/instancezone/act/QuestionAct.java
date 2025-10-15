package fire.pb.instancezone.act;

import java.util.Map;

import mkdb.Transaction;
import fire.pb.instancezone.InstanceZone;
import fire.pb.instancezone.Module;
import fire.pb.instancezone.conf.ActionConfig;
import fire.pb.instancezone.conf.ConfigParamName;
import fire.pb.instancezone.faction.FactionInstZone;
import fire.pb.mission.Squestions;
import fire.pb.npc.QuestionLib;
import fire.pb.npc.QuestionManager;
import fire.pb.npc.SAskQuestion;

public class QuestionAct extends ServiceAct {

	public QuestionAct(int actId, Map<String, Object> params) {
		super(actId, params);
	}

	@Override
	public int getType() {
		return ActionConfig.TYPE_ANSWER_QUESTION;
	}

	@Override
	public boolean defaultprocess(InstanceZone instzone) {
		if (instzone instanceof FactionInstZone) {
			Object npcCount = getParams().get(ConfigParamName.COUNT);
			if (npcCount == null)
				return false;

			if (!instzone.getNpcSaveState(roleId, npckey))
				return false;

			instzone.npcSaveSetState(roleId, npckey, (Integer) npcCount);
			Module.getInstance().setDynamicNpcState(instzone.getLandkey(), npckey, 1);
		}

		Object o = getParams().get(ConfigParamName.QUESTIONS_ID);
		if (o == null)
			return false;
		Integer questionsId = (Integer) o;
		QuestionLib lib = QuestionManager.getInstance().getQuestionLibs()
				.get(questionsId);
		if (lib == null)
			return false;
		if (lib.questions.isEmpty())
			return false;

		Object key = getParams().get(ConfigParamName.QUESTION_ID);
		Squestions question = null;
		if (key != null)
			question = lib.questions.get(key);
		else
			question = lib.randomQuestion();
		if (question == null)
			return false;

		SAskQuestion sask = new SAskQuestion();
		sask.lastresult = 0;
		sask.questionid = question.getId();
		sask.questiontype = SAskQuestion.GUILD_ANSWER;
		sask.npckey = npckey;
		sask.xiangguanid = 0;
		sask.lasttime = Module.ASK_QUESTION_TIMEOUT;
		if (Transaction.current() == null)
			gnet.link.Onlines.getInstance().send(roleId, sask);
		else
			mkdb.Procedure.psendWhileCommit(roleId, sask);

		Module.getInstance().createNpcTimerSchedule(instzone.getLandkey(), npckey);
		return true;
	}

}
