package fire.pb.npc;

import fire.pb.clan.ClanUtils;
import fire.pb.main.ConfigManager;
import fire.pb.talk.ChatChannel;
import fire.pb.team.TeamManager;
import mkdb.Procedure;

public class PSendActivityAnswerQuestionHelp extends Procedure {
	private final long roleid;
	private final int questionid;
	public PSendActivityAnswerQuestionHelp( long roleid, int questionid ) {
		this.roleid = roleid;
		this.questionid = questionid;
	}
	
	protected boolean process() {
		
		final xbean.ClanInfo clanInfo = ClanUtils.getClanInfoById(roleid, true);
		if (clanInfo == null) {
			return false;
		}
		
		xbean.Properties prop = xtable.Properties.select(roleid);
		if (prop == null)
		{
			return false;
		}
		
		fire.pb.message.SStringRes msg1 = ConfigManager.getInstance().getConf(fire.pb.message.SStringRes.class).get(298);
		if (msg1 == null)
		{
			TeamManager.logger.debug("PSendActivityAnswerQuestionHelp:找不到字符串 "+roleid);
			return false;						
		}
		
		fire.pb.message.SStringRes msg2 = ConfigManager.getInstance().getConf(fire.pb.message.SStringRes.class).get(299);
		if (msg2 == null)
		{
			TeamManager.logger.debug("PSendActivityAnswerQuestionHelp:找不到字符串 "+roleid);
			return false;						
		}				
		
		String msgstring1 = msg1.msg;
		String msgstring2 = msg2.msg;
		
		fire.pb.mission.SActivityQuestion question = ConfigManager.getInstance().getConf(fire.pb.mission.SActivityQuestion.class).get(questionid);

		msgstring1 = msgstring1.replaceAll("\\$parameter1\\$", question.question);

		Integer q = questionid;
		msgstring1 = msgstring1.replaceAll("\\$parameter2\\$", q.toString());
		
		msgstring1 = msgstring1.replaceAll("\\$parameter3\\$", prop.getRolename());
		
		msgstring1 = msgstring1.replaceAll("\\$parameter4\\$", String.valueOf(roleid));
		
		msgstring1 = msgstring1.replaceAll("\\$parameter5\\$", String.valueOf(213));
		
		final java.util.ArrayList<fire.pb.talk.DisplayInfo> showinfos  = new java.util.ArrayList<fire.pb.talk.DisplayInfo>();
		
		ChatChannel.getInstance().process(roleid, fire.pb.talk.ChannelType.CHANNEL_CLAN, msgstring1, msgstring2, showinfos, 1);
					
		return true;
	}
}
