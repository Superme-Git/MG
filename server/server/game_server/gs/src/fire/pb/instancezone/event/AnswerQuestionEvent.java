package fire.pb.instancezone.event;

import fire.pb.instancezone.InstanceZone;
import fire.pb.instancezone.Module;
import fire.pb.instancezone.conf.ConfigParamName;
import fire.pb.instancezone.conf.EventConfig;
import fire.pb.instancezone.faction.FactionInstZone;
import fire.pb.mission.Squestions;
import fire.pb.npc.QuestionManager;
import fire.pb.npc.SAskQuestion;
import fire.pb.team.Team;
import fire.pb.team.TeamManager;
import gnet.link.Onlines;

public class AnswerQuestionEvent extends Event {
	private final long roleId;
	private final Squestions question;
	private final int answer;
	private final boolean correct;
	private final long npckey;

	public AnswerQuestionEvent(final long roleId, final Squestions question,
			final int answer, final boolean correct, final long npckey) {
		this.roleId = roleId;
		this.question = question;
		this.answer = answer;
		this.correct = correct;
		this.npckey = npckey;
		getParams().put(ConfigParamName.ROLE_ID, this.roleId);
		getParams().put(ConfigParamName.QUESTION_ID, this.question.getId());
		getParams().put(ConfigParamName.QUESTIONS_ID,
				this.question.getQuestionsid());
		getParams().put(ConfigParamName.ANSWER_ID, this.answer);
		getParams().put(ConfigParamName.ANSWER_RIGHT, this.correct ? 1 : 0);
		getParams().put(ConfigParamName.NPC_KEY, npckey);
		
		Team team = TeamManager.getTeamByRoleId(roleId);
		if (team != null) {
			if (team.isTeamLeader(roleId)) {
				getParams().put(ConfigParamName.ROLE_IDS, team.getNormalMemberIds());
			}
		}
	}

	@Override
	public int getType() {
		return EventConfig.TYPE_ANSWER_QUESTION;
	}

	@Override
	public boolean process(InstanceZone instzone) {
		if (instzone instanceof FactionInstZone) {
			if (!instzone.checkNpcSave(roleId, npckey))
				return false;
			Module.getInstance().cancleNpcTimerSchedule(
					instzone.getLandkey(), npckey);

			SAskQuestion sAskQuestion = new SAskQuestion();
			if (correct) {
				if (!instzone.checkNpcSaveCount(npckey)) {
					Squestions sq = QuestionManager.getInstance()
							.getQuestionLibs().get(question.questionsid)
							.randomQuestion();
					sAskQuestion.questionid = sq.id;
					sAskQuestion.lastresult = 1;
					sAskQuestion.questiontype = SAskQuestion.GUILD_ANSWER;
					sAskQuestion.npckey = npckey;
					sAskQuestion.lasttime = Module.ASK_QUESTION_TIMEOUT;
					Onlines.getInstance().send(roleId, sAskQuestion);
					Module.getInstance().createNpcTimerSchedule(
							instzone.getLandkey(), npckey);
					return true;
				} else {
					sAskQuestion.lastresult = 1;
					sAskQuestion.questionid = -1;
					sAskQuestion.questiontype = SAskQuestion.GUILD_ANSWER;
					sAskQuestion.npckey = npckey;
					sAskQuestion.lasttime = Module.ASK_QUESTION_TIMEOUT;
					Onlines.getInstance().send(roleId, sAskQuestion);

					return super.process(instzone);
				}
			} else {
				sAskQuestion.lastresult = -1;
				sAskQuestion.questionid = -1;
				sAskQuestion.questiontype = SAskQuestion.GUILD_ANSWER;
				sAskQuestion.npckey = npckey;
				sAskQuestion.lasttime = Module.ASK_QUESTION_TIMEOUT;
				instzone.reSetSaveNpcState(npckey);
				Onlines.getInstance().send(roleId, sAskQuestion);
				Module.getInstance().setDynamicNpcState(instzone.getLandkey(), npckey, 0);
				return true;
			}
		} else {
			return super.process(instzone);
		}
	}

	public long getRoleId() {
		return this.roleId;
	}
}
